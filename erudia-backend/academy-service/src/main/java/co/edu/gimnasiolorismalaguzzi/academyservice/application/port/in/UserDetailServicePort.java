package co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDetailDomain;

import java.util.List;

public interface UserDetailServicePort {
    List<UserDetailDomain> getAllUsers();
    UserDetailDomain getUserById(String uuid);
    UserDetailDomain createUser(String uuid,UserDetailDomain user);
    UserDetailDomain updateUser(String uuid, UserDetailDomain user);
    void deleteUser(String uuid);
}
