package co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelationshipCrudRepo extends JpaRepository<Relationship, Integer> {
}
