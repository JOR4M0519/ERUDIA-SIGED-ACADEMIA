package co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.Family;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.FamilyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FamilyCrudRepo extends JpaRepository<Family, Integer> {


    @Query(value = "SELECT * FROM obtener_familias()", nativeQuery = true)
    List<Object[]> getFamilyReports();

    List<Family> findByUser_Id(Integer id);

    List<Family> findByStudent_Id(Integer id);

    List<Family> findByStudent_IdAndUserIdNot(Integer studentId, Integer userId);
}
