package co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity;

import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.Activity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subject")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "subject_name", nullable = false, length = 50)
    private String subjectName;

    @Size(max = 1)
    @Column(name = "status",  nullable = false, length = 1)
    private String status;

//    @OneToMany(mappedBy = "subject")
//    private Set<Activity> activities = new LinkedHashSet<>();

//    @OneToMany(mappedBy = "subject")
//    private Set<RecoveryPeriod> recoveryPeriods = new LinkedHashSet<>();

    @OneToMany(mappedBy = "subject")
    private Set<SubjectDimension> subjectDimensions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idSubject")
    private Set<SubjectKnowledge> subjectKnowledges = new LinkedHashSet<>();

    @OneToMany(mappedBy = "subject")
    private Set<SubjectProfessor> subjectProfessors = new LinkedHashSet<>();

//    @OneToMany(mappedBy = "subject")
//    private Set<SubjectSchedule> subjectSchedules = new LinkedHashSet<>();

}