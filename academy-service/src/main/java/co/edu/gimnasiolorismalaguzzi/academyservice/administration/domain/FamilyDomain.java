package co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Data
public class FamilyDomain {
    private Integer id;
    private UserDomain relativeUser;
    private UserDomain user;
    private RelationshipDomain relationship;

}
