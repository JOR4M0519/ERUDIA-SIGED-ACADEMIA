package co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDomain;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface PersistenceUserKeycloakPort {

    List<UserRepresentation> getAllUsersKeycloak();
    List<UserRepresentation> getUsersByUsername(String username);
    String createUsersKeycloak(UserDomain userDomain);
    void updateUsersKeycloak(String username, UserDomain userDomain);
    void deleteUsersKeycloak(String username);

}
