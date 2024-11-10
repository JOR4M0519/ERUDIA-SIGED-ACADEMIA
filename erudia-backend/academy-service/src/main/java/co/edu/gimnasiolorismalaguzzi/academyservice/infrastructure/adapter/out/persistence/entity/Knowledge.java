package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "knowledge")
public class Knowledge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 10)
    @Column(name = "name", length = 10)
    private String name;

    @Column(name = "achievement", length = Integer.MAX_VALUE)
    private String achievement;

    @Column(name = "status")
    private String status;

//    @OneToMany(mappedBy = "knowledge")
//    private Set<Activity> activities = new LinkedHashSet<>();
//
//    @OneToMany(mappedBy = "idKnowledge")
//    private Set<SubjectKnowledge> subjectKnowledges = new LinkedHashSet<>();

}