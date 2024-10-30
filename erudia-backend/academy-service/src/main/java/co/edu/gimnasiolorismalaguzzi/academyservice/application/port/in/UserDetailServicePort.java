package co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDetailDomain;

import java.util.List;

public interface UserDetailServicePort {
    List<UserDetailDomain> getAllUsers();
    UserDetailDomain getUserById(Integer id);
    UserDetailDomain createUser(Integer id,UserDetailDomain user);
    UserDetailDomain updateUser(Integer id, UserDetailDomain user);
    void deleteUser(Integer id);
}
