package co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;

import java.util.List;

public interface PersistenceUserPort  {

    List<UserDomain> findAll();
    UserDomain searchUserByUuid(String uuid);
    String save(UserDomain userDomain);
    UserDomain update(String uuid, UserDomain userDomain);
    void delete(String uuid);


}
