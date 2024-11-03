package co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDetailDomain;


public interface PersistenceUserDetailPort extends PersistencePort<UserDetailDomain, Integer> {
    UserDetailDomain saveDetailUser(Integer id,UserDetailDomain user);
}
