package co.edu.gimnasiolorismalaguzzi.academyservice.administration.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.UserServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceUserKeycloakPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceUserPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.UseCase;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class UserServiceImpl implements UserServicePort {

    @Autowired
    private final PersistenceUserPort userPort;

    @Autowired
    private final PersistenceUserKeycloakPort userKeycloakPort;


    @Override
    public List<UserRepresentation> getAllUsersKeycloak() {
        return userKeycloakPort.getAllUsersKeycloak();
    }

    @Override
    public List<UserRepresentation> getUsersByUsername(String username) {
        return userKeycloakPort.getUsersByUsername(username);
    }

    @Override
    public String createUsersKeycloak(UserDomain userDomain) {
        String uuid = userKeycloakPort.createUsersKeycloak(userDomain);
        return uuid;
    }

    @Override
    public void updateUsersKeycloak(String username, UserDomain userDomain) {
        userKeycloakPort.updateUsersKeycloak(username,userDomain);
    }

    @Override
    public void deleteUsersKeycloak(String username) {
        userKeycloakPort.deleteUsersKeycloak(username);
    }

    @Override
    public String getToken(String username, String password) {
        return userKeycloakPort.getToken(username,password);
    }


    @Override
    public List<UserDomain> getAllUsers() {
        return userPort.findAll();
    }

    @Override
    public UserDomain getUserByUuid(String uuid) {
        return userPort.searchUserByUuid(uuid);
    }

    @Override
    public void createUser(UserDomain userDomain) {
        userPort.save(userDomain);
    }

    @Override
    public UserDomain updateUser(String uuid, UserDomain user) {
        return userPort.update(uuid,user);
    }

    @Override
    public void deleteUser(String uuid) {
        userPort.delete(uuid);
    }
}
