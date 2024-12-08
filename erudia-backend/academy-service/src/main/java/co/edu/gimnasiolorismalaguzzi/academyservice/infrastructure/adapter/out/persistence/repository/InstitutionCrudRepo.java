package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.Institution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstitutionCrudRepo extends JpaRepository<Institution, Integer> {

}
