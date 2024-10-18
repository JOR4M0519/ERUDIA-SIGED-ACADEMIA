package co.edu.gimnasiolorismalaguzzi.academyservice.application.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.adapter.out.persistence.repository.PersistenceUserAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.adapter.out.persistence.repository.PersistenceUserDetailAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final PersistenceUserAdapter userRepository; // Cambiar a usar UserRepository

    @Autowired
    public UserServiceImpl(PersistenceUserAdapter userRepository) {
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
