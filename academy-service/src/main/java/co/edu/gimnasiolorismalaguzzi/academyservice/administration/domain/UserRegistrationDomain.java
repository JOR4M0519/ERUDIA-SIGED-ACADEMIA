package co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegistrationDomain {
    private UserDomain userDomain;
    private UserDetailDomain userDetailDomain;
    private Integer groupId;
}
