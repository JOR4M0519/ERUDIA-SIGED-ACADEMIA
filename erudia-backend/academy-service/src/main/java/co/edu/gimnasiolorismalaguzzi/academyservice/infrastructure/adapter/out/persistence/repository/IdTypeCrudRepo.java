package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.IdType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdTypeCrudRepo extends JpaRepository<IdType, Integer> {
}
