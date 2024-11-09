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
    private PersistenceUserDetailPort userDetailPort;


    @Override
    public List<UserDetailDomain> getAllUsers() {
        return userDetailPort.findAll();
    }

    @Override
    public UserDetailDomain getUserById(String uuid) {
        return userDetailPort.findById(uuid);
    }

    @Override
    public UserDetailDomain createUser(String uuid, UserDetailDomain userDetailDomain) {
        return userDetailPort.saveDetailUser(uuid,userDetailDomain);
    }

    @Override
    public UserDetailDomain updateUser(String uuid, UserDetailDomain userDetailDomain) {
        return userDetailPort.update(uuid, userDetailDomain);
    }

    @Override
    public void deleteUser(String uuid) {
        userDetailPort.delete(uuid);
    }
}
