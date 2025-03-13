package co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.FamilyDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.RoleDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserRoleDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.Family;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.Role;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.UserRole;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "roleName", target = "roleName"),
            @Mapping(source = "status", target = "status")
    })
    RoleDomain toDomain(Role role);

    @InheritInverseConfiguration
    Role toEntity(RoleDomain roleDomain);

    List<RoleDomain> toDomains(List<Role> roles);
    List<Role> toEntities(List<RoleDomain> roleDomains);
}
