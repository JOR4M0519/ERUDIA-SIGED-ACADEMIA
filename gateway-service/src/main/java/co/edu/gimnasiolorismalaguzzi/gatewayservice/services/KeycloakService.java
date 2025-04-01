package co.edu.gimnasiolorismalaguzzi.gatewayservice.services;

import co.edu.gimnasiolorismalaguzzi.gatewayservice.domain.UserDetailDomain;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.domain.UserRegistrationDomain;
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

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class KeycloakService {


    private KeycloakProvider keycloakProvider;

    @Autowired
    private UserService userService;

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
                keycloakProvider = new KeycloakProvider();
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
                keycloakProvider = new KeycloakProvider();
                return keycloakProvider.getTokens(username, password); // Obtiene los tokens
            } catch (Exception e) {
                log.error("Error obtaining tokens from Keycloak: ", e);
                throw new AppException("Unable to authenticate user.", HttpStatus.UNAUTHORIZED);
            }
        }).subscribeOn(Schedulers.boundedElastic()); // Ejecuta en un hilo seguro para bloqueos
    }

    // Actualizar el método refreshToken para que use directamente el refresh token
    public String refreshToken(String refreshToken) {
        keycloakProvider = new KeycloakProvider();
        return keycloakProvider.refreshToken(refreshToken);
    }

    public List<String> getUserGroupsByUsername(String username) {
        keycloakProvider = new KeycloakProvider();
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
        keycloakProvider = new KeycloakProvider();
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
        keycloakProvider = new KeycloakProvider();
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
    public UserDetailDomain registerByGroupinStudentUser(UserRegistrationDomain registrationDomain) {
        keycloakProvider = new KeycloakProvider();

        // Paso 1: Crear usuario en Keycloak
        String keycloakUserId = null;
        UserDetailDomain result = null;

        try {
            // Primero, creamos el usuario en Keycloak
            keycloakUserId = createUserInKeycloak(registrationDomain);

            // Si se creó exitosamente, actualizamos el UUID en el objeto de dominio
            registrationDomain.getUserDomain().setUuid(keycloakUserId);

            // Paso 2: Registrar en Academy Service
            log.info("Usuario creado en Keycloak con ID: {}. Registrando en Academy Service...", keycloakUserId);
            result = userService.createStudentUser(registrationDomain)
                    .timeout(java.time.Duration.ofSeconds(30))  // Agregamos un timeout para evitar bloqueos indefinidos
                    .block();  // Bloqueamos para esperar la respuesta

            log.info("Usuario registrado exitosamente en ambos sistemas");
            return result;

        } catch (Exception e) {
            log.error("Error al registrar usuario: {}", e.getMessage(), e);

            // Si se creó el usuario en Keycloak pero falló en Academy,
            // debemos hacer rollback eliminando el usuario de Keycloak
            if (keycloakUserId != null) {
                try {
                    log.warn("Realizando rollback: eliminando usuario {} de Keycloak", keycloakUserId);
                    deleteUsersKeycloak(keycloakUserId);
                    log.info("Rollback completado exitosamente");
                } catch (Exception rollbackEx) {
                    log.error("Error al realizar rollback en Keycloak: {}", rollbackEx.getMessage(), rollbackEx);
                    throw new AppException("Error en el registro y fallo en el rollback. Contacte al administrador.",
                            HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            // Propagamos la excepción original con un mensaje más descriptivo
            if (e instanceof AppException) {
                throw (AppException) e;
            } else {
                throw new AppException("Error al registrar el usuario: " + e.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
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

            RealmResource realmResource = KeycloakProvider.getRealmResource();

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

            realmResource.users().get(userId).roles().realmLevel().add(rolesRepresentation);

            registrationDomain.getUserDomain().setUuid(userId);

            userService.createStudentUser(registrationDomain);

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

        // 1️⃣ Obtener roles efectivos del usuario (directos + heredados)
        List<RoleRepresentation> effectiveRoles = userResource.roles().realmLevel().listEffective();
        List<String> roles = new ArrayList<>(effectiveRoles.stream().map(RoleRepresentation::getName).collect(Collectors.toList()));

        // 2️⃣ Obtener los grupos del usuario
        List<GroupRepresentation> userGroups = userResource.groups();

        // 3️⃣ Obtener roles efectivos de los grupos
        for (GroupRepresentation group : userGroups) {
            List<RoleRepresentation> groupEffectiveRoles = keycloakProvider.getRealmResource()
                    .groups()
                    .group(group.getId())
                    .roles()
                    .realmLevel()
                    .listEffective(); // 🔥 Cambiamos de listAll() a listEffective()

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
        keycloakProvider = new KeycloakProvider();
        List<UserRepresentation> users = keycloakProvider.getRealmResource()
                .users()
                .searchByUsername(username, true); // true -> búsqueda exacta

        if (users.isEmpty()) {
            throw new AppException("User not found", HttpStatus.NOT_FOUND);
        }
        return users.get(0); // Retorna el primer usuario encontrado
    }

}
