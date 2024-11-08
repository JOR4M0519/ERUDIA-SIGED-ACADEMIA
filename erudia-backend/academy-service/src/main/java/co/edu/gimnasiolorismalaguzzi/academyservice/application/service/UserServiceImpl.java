package co.edu.gimnasiolorismalaguzzi.academyservice.application.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.UserServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceUserPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.UseCase;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDomain;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UseCase
public class UserServiceImpl implements UserServicePort {

    private final PersistenceUserPort userRepository;

    @Autowired
    public UserServiceImpl(PersistenceUserPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserRepresentation> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<UserRepresentation> getUsersByUsername(String id) {
        return userRepository.searchUserByUsername(id);
    }

    @Override
    public String createUser(UserDomain UserDomain) {
        return userRepository.save(UserDomain);
    }

    @Override
    public void updateUser(String id, UserDomain UserDomain) {
        userRepository.update(id, UserDomain);
    }

    @Override
    public void deleteUser(String id) {
        userRepository.delete(id);
    }
}
