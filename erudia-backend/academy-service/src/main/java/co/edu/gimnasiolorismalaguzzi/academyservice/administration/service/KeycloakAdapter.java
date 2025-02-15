package co.edu.gimnasiolorismalaguzzi.academyservice.administration.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceUserKeycloakPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.util.KeycloakProvider;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;


@PersistenceAdapter
@Slf4j
public class KeycloakAdapter implements PersistenceUserKeycloakPort {



    @Override
    public String getToken(String username, String password) {
        try {
            KeycloakProvider keycloakProvider = new KeycloakProvider();
            // Obtén el token de acceso
            String accessToken = keycloakProvider.getToken(username,password);

            return accessToken;
        } catch (Exception e) {
            log.error("Error obtaining token from Keycloak: ", e);
            throw new AppException("Unable to authenticate user.", HttpStatus.UNAUTHORIZED);
        }
    }



    @Override
    public List<UserRepresentation> getAllUsersKeycloak() {
        return KeycloakProvider.getRealmResource()
                .users()
                .list();
    }

    @Override
    public List<UserRepresentation> getUsersByUsername(String username) {
        return KeycloakProvider.getRealmResource()
                .users()
                .searchByUsername(username, true);
    }


    @Override
    public String createUsersKeycloak(UserDomain userDomain) {
        int status = 0;

        UsersResource usersResource = KeycloakProvider.getUserResource();

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
            credentialRepresentation.setValue(userDomain.getPassword());

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
                                .anyMatch(roleName -> roleName.equalsIgnoreCase(role.getName())))
                        .toList();
            }

            realmResource.users().get(userId).roles().realmLevel().add(rolesRepresentation);

            return userId;

        } else if (status == 409) {
            throw new AppException("The email or username already exist", HttpStatus.CONFLICT);
        } else {
            throw new AppException("Error creating user, please contact with the administrator.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
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

        UserResource usersResource = KeycloakProvider.getUserResource().get(userId);
        usersResource.update(user);
        }catch (Exception e){
            throw new AppException("User could not be updated", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteUsersKeycloak(String userId) {
        KeycloakProvider.getUserResource()
                .get(userId)
                .remove();
    }
}
