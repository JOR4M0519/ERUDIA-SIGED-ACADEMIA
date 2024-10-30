package co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDomain;

import java.util.List;

public interface UserServicePort {
    List<UserDomain> getAllUsers();
    UserDomain getUserById(Integer id);
    UserDomain createUser(Integer id,UserDomain user);
    UserDomain updateUser(Integer id, UserDomain user);
    void deleteUser(Integer id);
}
