package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence;

//import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.repository.UserCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceUserPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDomain;


import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.util.KeycloakProvider;
import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;


@PersistenceAdapter
@Slf4j
public class UserAdapter implements PersistenceUserPort {


    @Override
    public List<UserRepresentation> findAll() {
        return KeycloakProvider.getRealmResource()
                .users()
                .list();
    }

    public List<UserRepresentation> searchUserByUsername(String username) {
        return KeycloakProvider.getRealmResource()
                .users()
                .searchByUsername(username, true);
    }


    @Override
    public String save(UserDomain userDomain) {
        int status = 0;
        UsersResource usersResource = KeycloakProvider.getUserResource();

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(userDomain.getFirstName());
        userRepresentation.setLastName(userDomain.getLastName());
        userRepresentation.setEmail(userDomain.getEmail());
        userRepresentation.setUsername(userDomain.getUsername());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);

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

            return "User created successfully!!";

        } else if (status == 409) {
            log.error("User exist already!");
            return "User exist already!";
        } else {
            log.error("Error creating user, please contact with the administrator.");
            return "Error creating user, please contact with the administrator.";
        }
    }

    @Override
    public void update(String userId, UserDomain userDomain) {
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
    }

    @Override
    public void delete(String userId) {
        KeycloakProvider.getUserResource()
                .get(userId)
                .remove();
    }
}
