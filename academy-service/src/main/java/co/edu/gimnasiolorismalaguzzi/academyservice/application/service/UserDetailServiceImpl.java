package co.edu.gimnasiolorismalaguzzi.academyservice.application.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.adapter.out.persistence.repository.PersistenceUserDetailAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDetailDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailService {

    @Autowired
    private PersistenceUserDetailAdapter userRepository; // Cambiar a usar UserRepository


    @Override
    public List<UserDetailDomain> getAllUsers() {
        return userRepository.findAll(); // Cambiado para usar el método del repositorio
    }

    @Override
    public UserDetailDomain getUserById(Integer id) {
        return userRepository.findById(id); // Cambiado para usar el método del repositorio
    }

    @Override
    public UserDetailDomain createUser(Integer id, UserDetailDomain userDetailDomain) { // Cambiar tipo de parámetro
        return userRepository.saveDetailUser(id,userDetailDomain); // Cambiado para usar el método del repositorio
    }

    @Override
    public UserDetailDomain updateUser(Integer id, UserDetailDomain userDetailDomain) { // Cambiar tipo de parámetro
        return userRepository.update(id, userDetailDomain); // Cambiado para usar el método del repositorio
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.delete(id); // Cambiado para usar el método del repositorio
    }
}
