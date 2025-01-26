package co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.entity.Dimension;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DimensionCrudRepo extends JpaRepository<Dimension, Integer> {

}
