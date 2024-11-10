package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.mapper;


import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDomain;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "username", target = "username"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "password", target = "password"),
            @Mapping(source = "firstName", target = "firstName"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "uuid", target = "uuid"),
            //@Mapping(source = "createdAt", target = "createdAt"),
           // @Mapping(source = "lastLogin", target = "lastLogin"),
            //@Mapping(source = "attemptedFailedLogin", target = "attemptedFailedLogin"),
            @Mapping(source = "status", target = "status")
    })
    UserDomain toDomain(User user);

    @InheritInverseConfiguration
    User toEntity(UserDomain userDomain);

    List<UserDomain> toDomains(List<User> users);
    List<User> toEntities(List<UserDomain> userDomains);
}
