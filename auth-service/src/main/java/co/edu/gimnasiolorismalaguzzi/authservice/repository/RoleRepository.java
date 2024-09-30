package co.edu.gimnasiolorismalaguzzi.authservice.repository;


import co.edu.gimnasiolorismalaguzzi.authservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(String roleName);

}
