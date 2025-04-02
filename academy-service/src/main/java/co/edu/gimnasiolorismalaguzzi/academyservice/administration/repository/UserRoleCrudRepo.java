package co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
public interface UserRoleCrudRepo extends JpaRepository<UserRole,Integer> {

    @Query("SELECT DISTINCT ur.user FROM UserRole ur WHERE ur.role.roleName IN ('administrador', 'profesor', 'admin')")
    List<User> findAllAdministrativeUsers();

    @Query("SELECT DISTINCT ur.user FROM UserRole ur WHERE ur.role.roleName = 'estudiante'")
    List<User> findAllStudents();

    List<UserRole> findByUserId(Integer userId);

    long deleteByUser_Id(Integer id);
}
