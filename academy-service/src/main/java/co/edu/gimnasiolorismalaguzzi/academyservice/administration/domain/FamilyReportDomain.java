package co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FamilyReportDomain {
    private String code;           // Código único de la familia (FA-001)
    private String familyName;     // Nombre de la familia
    private Long totalMembers;     // Cambiado de Integer a Long
    private Long activeStudents;   // Cambiado de Integer a Long
    private List<Integer> studentIds; // Lista de IDs de estudiantes en esta familia
}
