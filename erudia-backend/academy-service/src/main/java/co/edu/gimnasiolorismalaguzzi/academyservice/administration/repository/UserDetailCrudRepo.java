package co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailCrudRepo extends JpaRepository<UserDetail, Integer> {
    UserDetail findByUser_Id(Integer id);

    UserDetail findByUser_Uuid(String uuid);
}
