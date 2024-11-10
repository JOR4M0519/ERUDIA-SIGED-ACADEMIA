package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.Dimension;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DimensionCrudRepo extends JpaRepository<Dimension, Integer> {

}
