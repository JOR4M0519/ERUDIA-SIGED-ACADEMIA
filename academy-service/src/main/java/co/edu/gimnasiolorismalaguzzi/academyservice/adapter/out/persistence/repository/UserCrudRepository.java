package co.edu.gimnasiolorismalaguzzi.academyservice.adapter.out.persistence.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.adapter.out.persistence.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.adapter.out.persistence.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserCrudRepository extends JpaRepository<User, Integer> {

    // Puedes agregar métodos personalizados aquí si es necesario
    @Transactional
    @Modifying
    @Query("update User u set u.status = ?1 where u.id = ?2")
    int updateStatusById(String status, Integer id);
}
