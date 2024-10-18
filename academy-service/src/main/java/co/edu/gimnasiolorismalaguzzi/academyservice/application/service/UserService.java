package co.edu.gimnasiolorismalaguzzi.academyservice.application.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDomain;

import java.util.List;

public interface UserService {
    List<UserDomain> getAllUsers();
    UserDomain getUserById(Integer id);
    UserDomain createUser(Integer id,UserDomain user);
    UserDomain updateUser(Integer id, UserDomain user);
    void deleteUser(Integer id);
}
