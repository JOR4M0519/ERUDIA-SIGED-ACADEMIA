package co.edu.gimnasiolorismalaguzzi.academyservice.administration.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface UserServicePort {

    //Keycloak Methods

    List<UserRepresentation> getAllUsersKeycloak();
    List<UserRepresentation> getUsersByUsername(String username);
    String createUsersKeycloak(UserDomain uuid);
    void updateUsersKeycloak(String uuid, UserDomain userDomain);
    void deleteUsersKeycloak(String uuid);

    public String getToken(String username, String password);


    //User DB Methods

    List<UserDomain> getAllUsers();
    UserDomain getUserByUuid(String uuid);
    void createUser(UserDomain user);
    UserDomain updateUser(String uuid, UserDomain user);
    void deleteUser(String uuid);
}
