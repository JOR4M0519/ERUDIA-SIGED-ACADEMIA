package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.AcademicPeriod;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.Subject;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.entity.AchievementGroup;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.entity.Knowledge;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "activity")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "activity_name", nullable = false, length = 50)
    private String activityName;

    @NotNull
    @Column(name = "description", nullable = false, length = Integer.MAX_VALUE)
    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "achievement_groups_id", nullable = false)
    private AchievementGroup achievementGroup;

    @Size(max = 1)
    @NotNull
    @Column(name = "status", nullable = false, length = 1)
    private String status;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ActivityGroup> activityGroups = new LinkedHashSet<>();


}