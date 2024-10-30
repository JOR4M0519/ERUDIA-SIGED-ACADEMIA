package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailCrudRepo extends JpaRepository<UserDetail, Integer> {
    UserDetail findByUser_Id(Integer id);

    // Puedes agregar métodos personalizados aquí si es necesario
}
