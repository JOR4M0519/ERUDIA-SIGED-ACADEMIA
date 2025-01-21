package co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface PersistenceUserKeycloakPort {

    String getToken(String username, String password);

    List<UserRepresentation> getAllUsersKeycloak();
    List<UserRepresentation> getUsersByUsername(String username);
    String createUsersKeycloak(UserDomain userDomain);
    void updateUsersKeycloak(String username, UserDomain userDomain);
    void deleteUsersKeycloak(String username);

}
