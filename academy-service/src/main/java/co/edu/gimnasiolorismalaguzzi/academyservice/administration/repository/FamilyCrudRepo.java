package co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.Family;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.FamilyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FamilyCrudRepo extends JpaRepository<Family, Integer> {

    @Query(value = "select f.id, f.student_id, f.user_id, f.relationship_id from family f join public.users u on f.student_id = u.id where f.id = :id;", nativeQuery = true)
    List<Family> findRelativesByStudent(@Param("id") Integer id);

    @Query(value = "SELECT * FROM obtener_familias()", nativeQuery = true)
    List<Object[]> getFamilyReports();

    List<Family> findByUser_Id(Integer id);

    List<Family> findByStudent_Id(Integer id);

    List<Family> findByStudent_IdAndUserIdNot(Integer studentId, Integer userId);

    List<Family> findByRelationship_Id(Integer id);

    boolean existsByStudent_IdAndUser_Id(Integer id, Integer id1);

    @Override
    Optional<Family> findById(Integer integer);
}
