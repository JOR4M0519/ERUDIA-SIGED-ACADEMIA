package co.edu.gimnasiolorismalaguzzi.academyservice;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "subject")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "subject_name", nullable = false, length = 50)
    private String subjectName;

    @Size(max = 1)
    @NotNull
    @Column(name = "status", nullable = false, length = 1)
    private String status;

    @OneToMany(mappedBy = "subject")
    private Set<Activity> activities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "subject")
    private Set<RecoveryPeriod> recoveryPeriods = new LinkedHashSet<>();

    @OneToMany(mappedBy = "subject")
    private Set<SubjectDimension> subjectDimensions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idSubject")
    private Set<SubjectKnowledge> subjectKnowledges = new LinkedHashSet<>();

    @OneToMany(mappedBy = "subject")
    private Set<SubjectProfessor> subjectProfessors = new LinkedHashSet<>();

    @OneToMany(mappedBy = "subject")
    private Set<SubjectSchedule> subjectSchedules = new LinkedHashSet<>();

}