package co.edu.gimnasiolorismalaguzzi.gatewayservice.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegistrationDomain {
    private UserDomain userDomain;
    private UserDetailDomain userDetailDomain;
    private Integer groupId;
}
