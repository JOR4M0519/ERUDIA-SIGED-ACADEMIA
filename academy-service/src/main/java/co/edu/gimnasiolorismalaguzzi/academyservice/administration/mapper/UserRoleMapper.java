package co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserRoleDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.UserRole;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserRoleMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "user.id", target = "userId"),
            @Mapping(source = "role", target = "role")
    })
    UserRoleDomain toDomain(UserRole userRole);

    @Mappings({
            @Mapping(source = "userId", target = "user", qualifiedByName = "userIdToUser"),
            @Mapping(source = "role", target = "role")
    })
    UserRole toEntity(UserRoleDomain userRoleDomain);

    List<UserRoleDomain> toDomains(List<UserRole> userRoles);
    List<UserRole> toEntities(List<UserRoleDomain> userRoleDomains);

    @Named("userIdToUser")
    default User userIdToUser(Integer userId) {
        if (userId == null) {
            return null;
        }
        return User.builder().id(userId).build();
    }

}
