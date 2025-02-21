package co.edu.gimnasiolorismalaguzzi.gatewayservice.services;

import co.edu.gimnasiolorismalaguzzi.gatewayservice.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.util.KeycloakProvider;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.GroupsResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.keycloak.representations.idm.UserRepresentation;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class KeycloakService {


    private KeycloakProvider keycloakProvider;

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
                return keycloakProvider.getToken(username, password); // 🔥 Obtiene el token
            } catch (Exception e) {
                log.error("Error obtaining token from Keycloak: ", e);
                throw new AppException("Unable to authenticate user.", HttpStatus.UNAUTHORIZED);
            }
        }).subscribeOn(Schedulers.boundedElastic()); // 🔥 Ejecuta en un hilo seguro para bloqueos
    }




    public List<String> getUserGroupsByUsername(String username) {
        keycloakProvider = new KeycloakProvider();
        UsersResource usersResource = keycloakProvider.getUserResource();

        // 🔥 Buscar usuario por username
        List<UserRepresentation> users = usersResource.searchByUsername(username, true);

        if (users.isEmpty()) {
            throw new AppException("User not found: " + username, HttpStatus.NOT_FOUND);
        }

        String userId = users.get(0).getId(); // Obtener el ID del usuario

        // 🔥 Obtener los grupos del usuario
        UserResource userResource = usersResource.get(userId);
        List<GroupRepresentation> userGroups = userResource.groups();

        // 🔥 Extraer los nombres de los grupos como roles
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


    public List<String> getUserRolesByUsername(String username) {
        UserRepresentation user = getUsersByUsername(username);
        String userId = user.getId(); // Obtenemos el ID del usuario

        keycloakProvider = new KeycloakProvider();
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
