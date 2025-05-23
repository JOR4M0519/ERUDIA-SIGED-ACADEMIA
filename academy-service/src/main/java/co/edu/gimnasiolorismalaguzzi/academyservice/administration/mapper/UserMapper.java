package co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper;


import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserRoleMapper.class})
public interface UserMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "username", target = "username"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "password", target = "password"),
            @Mapping(source = "firstName", target = "firstName"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "uuid", target = "uuid"),
            @Mapping(source = "userRoles",target = "roles"),
            //@Mapping(source = "createdAt", target = "createdAt"),
            // @Mapping(source = "lastLogin", target = "lastLogin"),
            //@Mapping(source = "attemptedFailedLogin", target = "attemptedFailedLogin"),
            @Mapping(source = "status", target = "status"),
            @Mapping(source = "promotionStatus", target = "promotionStatus")
    })
    UserDomain toDomain(User user);

    @InheritInverseConfiguration
    User toEntity(UserDomain userDomain);

    List<UserDomain> toDomains(List<User> users);
    List<User> toEntities(List<UserDomain> userDomains);
}
