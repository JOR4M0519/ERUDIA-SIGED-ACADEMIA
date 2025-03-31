package co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDetailDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserRegistrationDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserRoleDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PersistenceUserRolePort extends PersistencePort<UserRoleDomain,Integer> {

    List<UserDomain> getStudents();

    List<UserDomain> getAdministrativeUsers();
}
