package co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDetailDomain;

import java.util.List;

public interface UserDetailServicePort {
    List<UserDetailDomain> getAllUsers();
    UserDetailDomain getUserById(String uuid);
    UserDetailDomain createUser(String uuid,UserDetailDomain user);
    UserDetailDomain updateUser(String uuid, UserDetailDomain user);
    void deleteUser(String uuid);
}
