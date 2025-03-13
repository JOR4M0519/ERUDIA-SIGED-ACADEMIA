package co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDetailDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserRegistrationDomain;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PersistenceUserPort  {

    List<UserDomain> findAll();
    UserDomain searchUserByUuid(String uuid);
    String save(UserDomain userDomain);
    UserDomain update(String uuid, UserDomain userDomain);
    void delete(String uuid);

    @Transactional
    void updatePromotionStatus(Integer userId, String promotionStatus);

    @Transactional
    void updateBulkPromotionStatus(List<UserDomain> users);

    @Transactional
    UserDetailDomain registerByGroupinStudentUser(UserRegistrationDomain registrationDomain);
}
