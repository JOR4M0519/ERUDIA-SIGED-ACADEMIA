package co.edu.gimnasiolorismalaguzzi.academyservice;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
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
    private Boolean status;

    @OneToMany(mappedBy = "knowledge")
    private Set<Activity> activities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idKnowledge")
    private Set<SubjectKnowledge> subjectKnowledges = new LinkedHashSet<>();

}