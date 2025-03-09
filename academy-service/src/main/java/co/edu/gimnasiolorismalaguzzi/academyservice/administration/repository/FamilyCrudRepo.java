package co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.FamilyDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FamilyCrudRepo extends JpaRepository<Family, Integer> {
    @Query(value = "select f.id, f.student_id, f.user_id, f.relationship_id from family f join public.users u on f.student_id = u.id where f.id = :id;", nativeQuery = true)
    List<Family> findRelativesByStudent(@Param("id") Integer id);

    List<Family> findByUser_Id(Integer id);

    List<Family> findByStudent_Id(Integer id);

    List<Family> findByStudent_IdAndUserIdNot(Integer studentId, Integer userId);
}
