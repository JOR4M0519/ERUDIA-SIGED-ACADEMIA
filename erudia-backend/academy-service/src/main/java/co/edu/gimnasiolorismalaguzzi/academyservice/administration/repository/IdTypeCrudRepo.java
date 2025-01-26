package co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.IdType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdTypeCrudRepo extends JpaRepository<IdType, Integer> {
}
