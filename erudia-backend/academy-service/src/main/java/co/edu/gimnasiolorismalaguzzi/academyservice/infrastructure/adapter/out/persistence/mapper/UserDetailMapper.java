package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.mapper;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.UserDetail;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDetailDomain;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDetailMapper {

/*    @Mapping(source = "user", target = "user")
    @Mapping(source = "idType", target = "idType")
    UserDetailDomain toDomain(UserDetail userDetail);

    @Mapping(source = "user", target = "user")
    @Mapping(source = "idType", target = "idType")
    UserDetail toEntity(UserDetailDomain userDetailDomain);*/
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "user", target = "user"),
            @Mapping(source = "firstName", target = "firstName"),
            @Mapping(source = "middleName", target = "middleName"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "secondLastName", target = "secondLastName"),
            @Mapping(source = "address", target = "address"),
            @Mapping(source = "phoneNumber", target = "phoneNumber"),
            @Mapping(source = "dateOfBirth", target = "dateOfBirth"),
            @Mapping(source = "dni", target = "dni"),
            @Mapping(source = "idType", target = "idType"),
            @Mapping(source = "neighborhood", target = "neighborhood"),
            @Mapping(source = "city", target = "city"),
            @Mapping(source = "positionJob", target = "positionJob")
    })
    UserDetailDomain toDomain(UserDetail userDetail);

    @InheritInverseConfiguration
    UserDetail toEntity(UserDetailDomain userDetailDomain);

    List<UserDetailDomain> toDomains(List<UserDetail> userDetails);
    List<UserDetail> toEntities(Iterable<UserDetailDomain> userDetailDomains);


}