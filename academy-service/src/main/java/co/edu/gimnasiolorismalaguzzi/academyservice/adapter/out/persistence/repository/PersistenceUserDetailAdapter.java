package co.edu.gimnasiolorismalaguzzi.academyservice.adapter.out.persistence.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDetailDomain;


import java.util.List;

public interface PersistenceUserDetailAdapter {
    List<UserDetailDomain> findAllUsers();
    UserDetailDomain findUserById(Integer id);
    UserDetailDomain saveUser(UserDetailDomain user);
    UserDetailDomain updateUser(Integer id, UserDetailDomain user);
    void deleteUser(Integer id);
}
