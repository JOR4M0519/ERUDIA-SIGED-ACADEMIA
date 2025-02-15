package co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectGrade;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface SubjectGradeCrudRepo extends JpaRepository<SubjectGrade, Integer> {
    @Transactional
    @Modifying
    @Query(value = "call recover_student(:idStudent, :idSubject, :idPeriod, :newScore)", nativeQuery = true)
    void recoverStudent(
            @Param("newScore") BigDecimal recoveredGrade,
            @Param("idSubject") Integer subject,
            @Param("idStudent") Integer studentId,
            @Param("idPeriod") Integer periodId
    );
}
