package co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain;

import lombok.Data;

@Data
public class UserRegistrationDomain {
    private UserDomain userDomain;
    private UserDetailDomain userDetailDomain;
    private Integer groupId;
}
