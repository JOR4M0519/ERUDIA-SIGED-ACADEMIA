package co.edu.gimnasiolorismalaguzzi.academyservice.application.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDetailDomain;

import java.util.List;

public interface UserService {
    List<UserDetailDomain> getAllUsers();
    UserDetailDomain getUserById(Integer id);
    UserDetailDomain createUser(UserDetailDomain user);
    UserDetailDomain updateUser(Integer id, UserDetailDomain user);
    void deleteUser(Integer id);
}
