package co.edu.gimnasiolorismalaguzzi.gatewayservice.services;

import co.edu.gimnasiolorismalaguzzi.gatewayservice.domain.*;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.util.KeycloakProvider;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.GroupsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.keycloak.representations.idm.UserRepresentation;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class KeycloakService {

    protected KeycloakProvider createProvider() {
        return new KeycloakProvider();
    }

    private final KeycloakProvider keycloakProvider;
    private final UserService userService;

    @Autowired
    public KeycloakService(KeycloakProvider keycloakProvider, UserService userService) {
        this.keycloakProvider = keycloakProvider;
        this.userService = userService;
    }

    /*public String getToken(String username, String password) {
        try {
            keycloakProvider = new KeycloakProvider();
            // Obtén el token de acceso
            String accessToken = keycloakProvider.getToken(username,password);

            return accessToken;
        } catch (Exception e) {
            log.error("Error obtaining token from Keycloak: ", e);
            throw new AppException("Unable to authenticate user.", HttpStatus.UNAUTHORIZED);
        }
    }*/


    public Mono<String> getToken(String username, String password) {
        return Mono.fromCallable(() -> {
            try {

                return keycloakProvider.getToken(username, password); //  Obtiene el token
            } catch (Exception e) {
                log.error("Error obtaining token from Keycloak: ", e);
                throw new AppException("Unable to authenticate user.", HttpStatus.UNAUTHORIZED);
            }
        }).subscribeOn(Schedulers.boundedElastic()); //  Ejecuta en un hilo seguro para bloqueos
    }

    public Mono<Map<String, String>> getTokens(String username, String password) {
        return Mono.fromCallable(() -> {
            try {
                return keycloakProvider.getTokens(username, password); // Obtiene los tokens
            } catch (Exception e) {
                log.error("Error obtaining tokens from Keycloak: ", e);
                throw new AppException("Unable to authenticate user.", HttpStatus.UNAUTHORIZED);
            }
        }).subscribeOn(Schedulers.boundedElastic()); // Ejecuta en un hilo seguro para bloqueos
    }

    // Actualizar el método refreshToken para que use directamente el refresh token
    public String refreshToken(String refreshToken) {
        return keycloakProvider.refreshToken(refreshToken);
    }

    public List<String> getUserGroupsByUsername(String username) {
        UsersResource usersResource = keycloakProvider.getUserResource();

        //  Buscar usuario por username
        List<UserRepresentation> users = usersResource.searchByUsername(username, true);

        if (users.isEmpty()) {
            throw new AppException("User not found: " + username, HttpStatus.NOT_FOUND);
        }

        String userId = users.get(0).getId(); // Obtener el ID del usuario

        //  Obtener los grupos del usuario
        UserResource userResource = usersResource.get(userId);
        List<GroupRepresentation> userGroups = userResource.groups();

        //  Extraer los nombres de los grupos como roles
        return userGroups.stream()
                .map(GroupRepresentation::getName) // Extraer solo los nombres de los grupos
                .collect(Collectors.toList());
    }


    public List<Map<String, String>> getUsersByGroup(String groupId) {
        UsersResource usersResource = keycloakProvider.getUserResource();

        try {
            return usersResource.list().stream()
                    .filter(user -> {
                        try {
                            // Obtener la lista de grupos del usuario
                            UserResource userResource = usersResource.get(user.getId());
                            List<GroupRepresentation> userGroups = userResource.groups();

                            // Verificar si el usuario pertenece al grupo por ID
                            return userGroups.stream().anyMatch(group -> group.getId().equals(groupId));
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    })
                    .map(user -> {
                        // Obtener los grupos del usuario
                        UserResource userResource = usersResource.get(user.getId());
                        List<GroupRepresentation> userGroups = userResource.groups();

                        // Buscar el grupo específico y obtener su nombre (rol)
                        String roleName = userGroups.stream()
                                .filter(group -> group.getId().equals(groupId))
                                .map(GroupRepresentation::getName)
                                .findFirst()
                                .orElse("Desconocido");

                        // Convertir el nombre con la primera letra en mayúscula
                        String formattedName = capitalize(user.getFirstName()) + " " + capitalize(user.getLastName());

                        return Map.of(
                                "id", user.getId(),
                                "name", formattedName,
                                "role", capitalize(roleName) // Asegurar que el rol también tenga formato
                        );
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error obtaining users from Keycloak: ", e);
            throw new AppException("Unable to fetch users.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Método auxiliar para convertir la primera letra en mayúscula.
     */
    private String capitalize(String text) {
        if (text == null || text.isEmpty()) return "";
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }


    public List<GroupRepresentation> getGroups() {
        try {
            List<GroupRepresentation> groupList = keycloakProvider.getRealmResource().groups().groups();
            return groupList;
        } catch (Exception e) {
            log.error("Error obtaining token from Keycloak: ", e);
            throw new AppException("Unable to authenticate user.", HttpStatus.UNAUTHORIZED);
        }
    }


    /**
     * Registra un estudiante tanto en Keycloak como en el Academy Service
     * con manejo de transacción y rollback en caso de error
     */

    public Mono<UserDetailDomain> registerByGroupinStudentUser(UserRegistrationDomain registrationDomain) {

        return Mono.fromCallable(() -> {
                    // Create user in Keycloak (this is blocking but we run it in a separate thread)
                    String keycloakUserId = createUserInKeycloak(registrationDomain);
                    registrationDomain.getUserDomain().setUuid(keycloakUserId);
                    return keycloakUserId;
                })
                .subscribeOn(Schedulers.boundedElastic()) // Run blocking code on a separate thread pool
                .flatMap(keycloakUserId -> {
                    // Register in Academy Service
                    log.info("Usuario creado en Keycloak con ID: {}. Registrando en Academy Service...", keycloakUserId);
                    return userService.createStudentUser(registrationDomain)
                            .timeout(java.time.Duration.ofSeconds(30))
                            .doOnSuccess(result -> log.info("Usuario registrado exitosamente en ambos sistemas"))
                            .onErrorResume(e -> {
                                // If Academy Service registration fails, rollback Keycloak user
                                log.error("Error al registrar usuario en Academy Service: {}", e.getMessage(), e);
                                return Mono.fromCallable(() -> {
                                            try {
                                                log.warn("Realizando rollback: eliminando usuario {} de Keycloak", keycloakUserId);
                                                deleteUsersKeycloak(keycloakUserId);
                                                log.info("Rollback completado exitosamente");
                                            } catch (Exception rollbackEx) {
                                                log.error("Error al realizar rollback en Keycloak: {}", rollbackEx.getMessage(), rollbackEx);
                                                throw new AppException("Error en el registro y fallo en el rollback",
                                                        HttpStatus.INTERNAL_SERVER_ERROR);
                                            }
                                            return e;  // Return the exception to handle it in the next flatMap
                                        }).subscribeOn(Schedulers.boundedElastic())
                                        .flatMap(ex -> Mono.error(ex instanceof AppException ? (AppException)ex :
                                                new AppException("Error al registrar el usuario: " + ex.getMessage(),
                                                        HttpStatus.INTERNAL_SERVER_ERROR)));
                            });
                });
    }
    // El método original puede quedar como está o delegarse al nuevo método
/*    public String createUsersKeycloak(UserRegistrationDomain registrationDomain) {
        try {
            keycloakProvider = new KeycloakProvider();
            String userId = createUserInKeycloak(registrationDomain);
            registrationDomain.getUserDomain().setUuid(userId);

            // Intentamos crear el usuario en Academy Service pero no bloqueamos el flujo
            // ni hacemos rollback si falla (para mantener compatibilidad con código existente)
            try {
                userService.createStudentUser(registrationDomain)
                        .timeout(java.time.Duration.ofSeconds(15))
                        .subscribe(
                                result -> log.info("Usuario registrado exitosamente en Academy Service: {}", result),
                                error -> log.error("Error al registrar en Academy Service: {}", error.getMessage())
                        );
            } catch (Exception e) {
                log.warn("No se pudo registrar el usuario en Academy Service: {}", e.getMessage());
            }

            return userId;
        } catch (Exception e) {
            log.error("Error en createUsersKeycloak: {}", e.getMessage(), e);
            if (e instanceof AppException) {
                throw (AppException) e;
            }
            throw new AppException("Error al crear usuario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

    public String createUserInKeycloak(UserRegistrationDomain registrationDomain) {
        UserDomain userDomain = registrationDomain.getUserDomain();
        int status = 0;

        UsersResource usersResource = keycloakProvider.getUserResource();

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(userDomain.getFirstName());
        userRepresentation.setLastName(userDomain.getLastName());
        userRepresentation.setEmail(userDomain.getEmail());
        userRepresentation.setUsername(userDomain.getUsername());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);

        //Guarda usuario en Keycloak
        Response response = usersResource.create(userRepresentation);

        status = response.getStatus();

        if (status == 201) {

            String path = response.getLocation().getPath();
            String userId = path.substring(path.lastIndexOf("/") + 1);


            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setTemporary(false);
            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);

            //Se usa el numero de documento como contraseña inicial
            credentialRepresentation.setValue(registrationDomain.getUserDetailDomain().getDni());

            usersResource.get(userId).resetPassword(credentialRepresentation);

            RealmResource realmResource = keycloakProvider.getRealmResource();

            List<RoleRepresentation> rolesRepresentation = null;

            if (userDomain.getRoles() == null || userDomain.getRoles().isEmpty()) {
                rolesRepresentation = List.of(realmResource.roles().get("user").toRepresentation());
            } else {
                rolesRepresentation = realmResource.roles()
                        .list()
                        .stream()
                        .filter(role -> userDomain.getRoles()
                                .stream()
                                .anyMatch(roleName -> roleName.getRole().getRoleName().equalsIgnoreCase(role.getName())))
                        .toList();
            }

            // Añadir usuario a grupos basados en sus roles
            if (userDomain.getRoles() != null && !userDomain.getRoles().isEmpty()) {
                GroupsResource groupsResource = realmResource.groups();
                List<GroupRepresentation> allGroups = groupsResource.groups();

                for (UserRoleDomain roleObj : userDomain.getRoles()) {
                    String roleName = roleObj.getRole().getRoleName();

                    // Buscar grupo por nombre
                    Optional<GroupRepresentation> groupOpt = allGroups.stream()
                            .filter(group -> group.getName().equalsIgnoreCase(roleName))
                            .findFirst();

                    if (groupOpt.isPresent()) {
                        GroupRepresentation group = groupOpt.get();
                        // Añadir usuario al grupo
                        realmResource.users().get(userId).joinGroup(group.getId());
                        log.info("Usuario {} añadido al grupo {}", userId, group.getName());
                    } else {
                        log.warn("No se encontró un grupo con nombre '{}' para el usuario {}", roleName, userId);
                    }
                }
            }

            realmResource.users().get(userId).roles().realmLevel().add(rolesRepresentation);

            registrationDomain.getUserDomain().setUuid(userId);

            return userId;

        } else if (status == 409) {
            throw new AppException("The email or username already exist", HttpStatus.CONFLICT);
        } else {
            throw new AppException("Error creating user, please contact with the administrator.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public void updateUsersKeycloak(String userId, UserDomain userDomain) {

        try{


            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setTemporary(false);
            credentialRepresentation.setType(OAuth2Constants.PASSWORD);
            credentialRepresentation.setValue(userDomain.getPassword());

            UserRepresentation user = new UserRepresentation();
            user.setUsername(userDomain.getUsername());
            user.setFirstName(userDomain.getFirstName());
            user.setLastName(userDomain.getLastName());
            user.setEmail(userDomain.getEmail());
            user.setEnabled(true);
            user.setEmailVerified(true);
            user.setCredentials(Collections.singletonList(credentialRepresentation));

            UserResource usersResource = keycloakProvider.getUserResource().get(userId);
            usersResource.update(user);
        }catch (Exception e){
            throw new AppException("User could not be updated", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public void deleteUsersKeycloak(String userId) {
        keycloakProvider.getUserResource()
                .get(userId)
                .remove();
    }


    public List<String> getUserRolesByUsername(String username) {
        UserRepresentation user = getUsersByUsername(username);
        String userId = user.getId(); // Obtenemos el ID del usuario

        UsersResource usersResource = keycloakProvider.getUserResource();
        UserResource userResource = usersResource.get(userId);

        // 1️ Obtener roles efectivos del usuario (directos + heredados)
        List<RoleRepresentation> effectiveRoles = userResource.roles().realmLevel().listEffective();
        List<String> roles = new ArrayList<>(effectiveRoles.stream().map(RoleRepresentation::getName).collect(Collectors.toList()));

        // 2️ Obtener los grupos del usuario
        List<GroupRepresentation> userGroups = userResource.groups();

        // 3️ Obtener roles efectivos de los grupos
        for (GroupRepresentation group : userGroups) {
            List<RoleRepresentation> groupEffectiveRoles = keycloakProvider.getRealmResource()
                    .groups()
                    .group(group.getId())
                    .roles()
                    .realmLevel()
                    .listEffective(); //  Cambiamos de listAll() a listEffective()

            roles.addAll(groupEffectiveRoles.stream().map(RoleRepresentation::getName).collect(Collectors.toList()));
        }

        return roles;
    }

/*    public List<String> getGroupByUser(String username) {
        try {

            String uuid = getUsersByUsername(username).get(0).getId();

            List<String> groupsUser = new ArrayList<>();
            keycloakProvider.getRealmResource().users().get(username).groups().stream().map(group ->
                    groupsUser.add(group.getName())
            );

            return groupsUser;
        } catch (Exception e) {
            log.error("Error obtaining token from Keycloak: ", e);
            throw new AppException("Unable to authenticate user.", HttpStatus.UNAUTHORIZED);
        }
    }*/

    public UserRepresentation getUsersByUsername(String username) {
        List<UserRepresentation> users = keycloakProvider.getRealmResource()
                .users()
                .searchByUsername(username, true); // true -> búsqueda exacta

        if (users.isEmpty()) {
            throw new AppException("User not found", HttpStatus.NOT_FOUND);
        }
        return users.get(0); // Retorna el primer usuario encontrado
    }

    public Mono<UserDetailDomain> registerUser(UserRegistrationDomain registrationDomain) {

        // Verificar y establecer un valor por defecto para dateOfBirth si es nulo
        if (registrationDomain.getUserDetailDomain() != null &&
                registrationDomain.getUserDetailDomain().getDateOfBirth() == null) {
            // Usar una fecha por defecto (por ejemplo, la fecha actual)
            registrationDomain.getUserDetailDomain().setDateOfBirth(LocalDate.now());
            log.info("Se estableció una fecha de nacimiento por defecto ya que era nula");
        }

        return Mono.fromCallable(() -> {
                    // Create user in Keycloak (this is blocking but we run it in a separate thread)
                    String keycloakUserId = createUserInKeycloak(registrationDomain);
                    registrationDomain.getUserDomain().setUuid(keycloakUserId);
                    return keycloakUserId;
                })
                .subscribeOn(Schedulers.boundedElastic()) // Run blocking code on a separate thread pool
                .flatMap(keycloakUserId -> {
                    // Register in Academy Service as administrative user
                    log.info("Usuario administrativo creado en Keycloak con ID: {}. Registrando en Academy Service...", keycloakUserId);
                    return userService.createGeneralUser(registrationDomain)
                            .timeout(java.time.Duration.ofSeconds(30))
                            .doOnSuccess(result -> log.info("Usuario administrativo registrado exitosamente en ambos sistemas"))
                            .onErrorResume(e -> {
                                // If Academy Service registration fails, rollback Keycloak user
                                log.error("Error al registrar usuario administrativo en Academy Service: {}", e.getMessage(), e);
                                return Mono.fromCallable(() -> {
                                            try {
                                                log.warn("Realizando rollback: eliminando usuario {} de Keycloak", keycloakUserId);
                                                deleteUsersKeycloak(keycloakUserId);
                                                log.info("Rollback completado exitosamente");
                                            } catch (Exception rollbackEx) {
                                                log.error("Error al realizar rollback en Keycloak: {}", rollbackEx.getMessage(), rollbackEx);
                                                throw new AppException("Error en el registro y fallo en el rollback",
                                                        HttpStatus.INTERNAL_SERVER_ERROR);
                                            }
                                            return e;  // Return the exception to handle it in the next flatMap
                                        }).subscribeOn(Schedulers.boundedElastic())
                                        .flatMap(ex -> Mono.error(ex instanceof AppException ? (AppException)ex :
                                                new AppException("Error al registrar el usuario administrativo: " + ex.getMessage(),
                                                        HttpStatus.INTERNAL_SERVER_ERROR)));
                            });
                });
    }
    public HttpStatus updatePassword(String username, Login login) {
        try {

            // Buscar el usuario por su nombre de usuario
            UserRepresentation user = getUsersByUsername(username);
            if (user == null) {
                log.error("Usuario no encontrado: {}", username);
                return HttpStatus.NOT_FOUND;
            }

            // Validar contraseña actual antes de permitir el cambio
            if (login.getLastPassword() != null && !login.getLastPassword().isEmpty()) {
                try {
                    // Intentamos autenticar con las credenciales actuales
                    keycloakProvider.getToken(username, login.getLastPassword());
                    log.debug("Credenciales actuales verificadas correctamente para usuario: {}", username);
                } catch (AppException e) {
                    // Si la autenticación falla, la contraseña anterior no es correcta
                    log.error("Contraseña anterior incorrecta para usuario: {}", username);
                    throw new AppException("La contraseña anterior no es correcta", HttpStatus.UNAUTHORIZED);
                }
            } else {
                log.warn("No se proporcionó la contraseña anterior para el usuario: {}", username);
                throw new AppException("Se requiere la contraseña anterior para actualizar la contraseña", HttpStatus.BAD_REQUEST);
            }

            // Obtener el ID del usuario
            String userId = user.getId();
            log.debug("Actualizando contraseña para usuario con ID: {}", userId);

            // Crear representación de credenciales para la nueva contraseña
            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setTemporary(false);
            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
            credentialRepresentation.setValue(login.getPassword());

            // Obtener el recurso de usuario y actualizar la contraseña
            UserResource userResource = keycloakProvider.getUserResource().get(userId);
            userResource.resetPassword(credentialRepresentation);

            log.info("Contraseña actualizada exitosamente para el usuario: {}", username);
            return HttpStatus.OK;
        } catch (AppException e) {
            log.error("Error al actualizar la contraseña - AppException: {}", e.getMessage(), e);
            throw e; // Re-lanzar AppException con su código de estado
        } catch (Exception e) {
            log.error("Error al actualizar la contraseña: {}", e.getMessage(), e);
            throw new AppException("Error al actualizar la contraseña: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
