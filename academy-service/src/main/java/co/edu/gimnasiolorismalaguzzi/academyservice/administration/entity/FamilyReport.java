package co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FamilyReport {
    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "total_members")
    private Long totalMembers;     // Cambiado de Integer a Long
    @Column(name = "active_students")
    private Long activeStudents;   // Cambiado de Integer a Long
    @ElementCollection
    @CollectionTable(
            name = "family_group_students",
            joinColumns = @JoinColumn(name = "family_code")
    )
    @Column(name = "student_id")
    private List<Integer> studentIds;
}
