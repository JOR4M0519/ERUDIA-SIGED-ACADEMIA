package co.edu.gimnasiolorismalaguzzi.academyservice.application.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.UserDetailServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceUserDetailPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.UseCase;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDetailDomain;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UseCase
public class UserDetailServiceImpl implements UserDetailServicePort {

    @Autowired
    private PersistenceUserDetailPort userDetailPort; // Cambiar a usar UserRepository


    @Override
    public List<UserDetailDomain> getAllUsers() {
        return userDetailPort.findAll(); // Cambiado para usar el método del repositorio
    }

    @Override
    public UserDetailDomain getUserById(Integer id) {
        return userDetailPort.findById(id); // Cambiado para usar el método del repositorio
    }

    @Override
    public UserDetailDomain createUser(Integer id, UserDetailDomain userDetailDomain) { // Cambiar tipo de parámetro
        return userDetailPort.saveDetailUser(id,userDetailDomain); // Cambiado para usar el método del repositorio
    }

    @Override
    public UserDetailDomain updateUser(Integer id, UserDetailDomain userDetailDomain) { // Cambiar tipo de parámetro
        return userDetailPort.update(id, userDetailDomain); // Cambiado para usar el método del repositorio
    }

    @Override
    public void deleteUser(Integer id) {
        userDetailPort.delete(id); // Cambiado para usar el método del repositorio
    }
}
