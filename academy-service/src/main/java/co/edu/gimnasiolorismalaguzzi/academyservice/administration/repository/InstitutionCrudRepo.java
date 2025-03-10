package co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.Institution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstitutionCrudRepo extends JpaRepository<Institution, Integer> {

    Institution findByNit(String nit);
}
