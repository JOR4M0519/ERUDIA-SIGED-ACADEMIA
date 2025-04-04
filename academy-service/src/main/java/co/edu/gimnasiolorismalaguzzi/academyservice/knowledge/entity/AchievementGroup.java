package co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.entity;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.AcademicPeriod;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectKnowledge;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.Groups;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "achievement_groups")
public class AchievementGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "subject_knowledge_id", nullable = false)
    private SubjectKnowledge subjectKnowledge;

    @NotNull
    @Column(name = "achievement", nullable = false, length = Integer.MAX_VALUE)
    private String achievement;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private Groups group;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "period_id", nullable = false)
    private AcademicPeriod period;

}