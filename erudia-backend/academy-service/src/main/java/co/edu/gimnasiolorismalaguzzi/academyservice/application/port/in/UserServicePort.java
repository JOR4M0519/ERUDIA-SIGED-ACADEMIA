package co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDomain;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface UserServicePort {
    List<UserRepresentation> getAllUsers();
    List<UserRepresentation> getUsersByUsername(String id);
    String createUser(UserDomain user);
    void updateUser(String id, UserDomain user);
    void deleteUser(String id);
}
