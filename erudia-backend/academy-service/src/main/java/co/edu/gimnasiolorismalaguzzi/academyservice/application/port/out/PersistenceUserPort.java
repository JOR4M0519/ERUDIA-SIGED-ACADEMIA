package co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDomain;
import org.keycloak.representations.idm.UserRepresentation;
import java.util.List;

public interface PersistenceUserPort  {

    List<UserRepresentation> findAll();
    List<UserRepresentation> searchUserByUsername(String username);
    String save(UserDomain userDomain);
    void update(String userId, UserDomain userDomain);
    void delete(String userId);


}
