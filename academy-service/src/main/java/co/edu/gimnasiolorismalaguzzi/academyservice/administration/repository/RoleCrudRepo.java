package co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleCrudRepo  extends JpaRepository<Role,Integer> {
}
