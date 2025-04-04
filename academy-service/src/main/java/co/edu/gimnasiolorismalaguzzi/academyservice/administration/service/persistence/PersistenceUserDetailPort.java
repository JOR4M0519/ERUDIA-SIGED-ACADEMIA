package co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDetailDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserFamilyRelationDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserRegistrationDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;
import jakarta.transaction.Transactional;

import java.util.List;


public interface PersistenceUserDetailPort extends PersistencePort<UserDetailDomain, Integer> {
    boolean hasStudentRole(Integer userId);

    @Transactional
    UserDetailDomain findByUser_Id(Integer id);

    UserDetailDomain saveDetailUser(String uuid, UserDetailDomain user);

    UserDetailDomain findByUsername(String username);

}
