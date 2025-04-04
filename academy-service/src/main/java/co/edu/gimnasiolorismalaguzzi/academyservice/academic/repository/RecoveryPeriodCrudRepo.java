package co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.RecoveryPeriod;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectGrade;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RecoveryPeriodCrudRepo extends JpaRepository<RecoveryPeriod, Integer> {

    List<RecoveryPeriod> findBySubjectGrade_Subject_IdAndSubjectGrade_Period_Id(Integer id, Integer id1);
}
