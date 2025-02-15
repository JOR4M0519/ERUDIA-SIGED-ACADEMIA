package co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class FamilyDomain {
    private Integer id;
    private UserDomain student;
    private UserDomain user;
    private RelationshipDomain relationship;

}
