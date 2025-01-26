package co.edu.gimnasiolorismalaguzzi.academyservice.administration.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDetailDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;


public interface PersistenceUserDetailPort extends PersistencePort<UserDetailDomain, String> {
    UserDetailDomain saveDetailUser(String uuid,UserDetailDomain user);
}
