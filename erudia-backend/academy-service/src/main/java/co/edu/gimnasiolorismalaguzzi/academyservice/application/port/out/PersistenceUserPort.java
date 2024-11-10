package co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDomain;
import org.keycloak.representations.idm.UserRepresentation;
import java.util.List;

public interface PersistenceUserPort  {

    List<UserDomain> findAll();
    UserDomain searchUserByUuid(String uuid);
    String save(UserDomain userDomain);
    UserDomain update(String uuid, UserDomain userDomain);
    void delete(String uuid);


}
