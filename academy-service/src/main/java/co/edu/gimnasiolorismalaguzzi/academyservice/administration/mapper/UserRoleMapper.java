package co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserRoleDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.UserRole;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserRoleMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "user", target = "user"),
            @Mapping(source = "role", target = "role")
    })
    UserRoleDomain toDomain(UserRole userRole);

    @InheritInverseConfiguration
    UserRole toEntity(UserRoleDomain userRoleDomain);

    List<UserRoleDomain> toDomains(List<UserRole> userRoles);
    List<UserRole> toEntities(List<UserRoleDomain> userRoleDomains);

}
