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
    public UserDetailDomain getUserById(Integer id) {
        return userDetailPort.findById(id);
    }

    @Override
    public UserDetailDomain createUser(Integer id, UserDetailDomain userDetailDomain) {
        return userDetailPort.saveDetailUser(id,userDetailDomain);
    }

    @Override
    public UserDetailDomain updateUser(Integer id, UserDetailDomain userDetailDomain) {
        return userDetailPort.update(id, userDetailDomain);
    }

    @Override
    public void deleteUser(Integer id) {
        userDetailPort.delete(id);
    }
}
