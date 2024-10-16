package co.edu.gimnasiolorismalaguzzi.academyservice.application.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.adapter.out.persistence.repository.PersistenceUserDetailAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDetailDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final PersistenceUserDetailAdapter userRepository; // Cambiar a usar UserRepository

    @Autowired
    public UserServiceImpl(PersistenceUserDetailAdapter userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDetailDomain> getAllUsers() {
        return userRepository.findAllUsers(); // Cambiado para usar el método del repositorio
    }

    @Override
    public UserDetailDomain getUserById(Integer id) {
        return userRepository.findUserById(id); // Cambiado para usar el método del repositorio
    }

    @Override
    public UserDetailDomain createUser(UserDetailDomain userDetailDomain) { // Cambiar tipo de parámetro
        return userRepository.saveUser(userDetailDomain); // Cambiado para usar el método del repositorio
    }

    @Override
    public UserDetailDomain updateUser(Integer id, UserDetailDomain userDetailDomain) { // Cambiar tipo de parámetro
        return userRepository.updateUser(id, userDetailDomain); // Cambiado para usar el método del repositorio
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteUser(id); // Cambiado para usar el método del repositorio
    }
}
