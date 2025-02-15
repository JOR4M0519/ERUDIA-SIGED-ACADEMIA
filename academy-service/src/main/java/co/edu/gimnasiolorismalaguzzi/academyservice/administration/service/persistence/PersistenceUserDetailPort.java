package co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDetailDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;


public interface PersistenceUserDetailPort extends PersistencePort<UserDetailDomain, Integer> {
    UserDetailDomain saveDetailUser(String uuid,UserDetailDomain user);

    UserDetailDomain findByUsername(String username);
}
