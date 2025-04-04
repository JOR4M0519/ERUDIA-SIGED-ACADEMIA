package co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFamilyRelationDomain implements Serializable {
    private UserDetailDomain userDetail;
    private List<FamilyDomain> familyRelations;
    private boolean isStudent;

}
