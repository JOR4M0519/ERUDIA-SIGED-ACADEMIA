package co.edu.gimnasiolorismalaguzzi.gatewayservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRegistrationDomain {
    private UserDomain userDomain;
    private UserDetailDomain userDetailDomain;
    private Integer groupId;
}
