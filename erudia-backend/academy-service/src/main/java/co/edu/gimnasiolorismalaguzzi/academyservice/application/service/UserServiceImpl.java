package co.edu.gimnasiolorismalaguzzi.academyservice.application.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.UserServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceUserPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.UseCase;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDomain;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UseCase
public class UserServiceImpl implements UserServicePort {

    private final PersistenceUserPort userRepository; // Cambiar a usar UserRepository

    @Autowired
    public UserServiceImpl(PersistenceUserPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDomain> getAllUsers() {
        return userRepository.findAll(); // Cambiado para usar el método del repositorio
    }

    @Override
    public UserDomain getUserById(Integer id) {
        return userRepository.findById(id); // Cambiado para usar el método del repositorio
    }

    @Override
    public UserDomain createUser(Integer id, UserDomain UserDomain) { // Cambiar tipo de parámetro
        return userRepository.save(id,UserDomain); // Cambiado para usar el método del repositorio
    }

    @Override
    public UserDomain updateUser(Integer id, UserDomain UserDomain) { // Cambiar tipo de parámetro
        return userRepository.update(id, UserDomain); // Cambiado para usar el método del repositorio
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.delete(id); // Cambiado para usar el método del repositorio
    }
}
