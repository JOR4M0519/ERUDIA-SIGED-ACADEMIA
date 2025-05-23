package co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.AcademicPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AcademicPeriodCrudRepo extends JpaRepository<AcademicPeriod, Integer> {
    @Transactional
    @Modifying
    @Query("update AcademicPeriod a set a.status = ?1 where a.id = ?2")
    int updateStatusById(String status, Integer id);

    @Transactional
    @Modifying
    @Query(value = "SELECT * FROM get_academic_periods(:year)", nativeQuery = true)
    List<AcademicPeriod> getActivePeriodsByYear(@Param("year") String year);

    @Transactional
    @Modifying
    @Query(value = "SELECT * FROM get_all_academic_periods(:year)", nativeQuery = true)
    List<AcademicPeriod> getPeriodsByYear(@Param("year") String year);

    List<AcademicPeriod> findByStatus(String status);

    List<AcademicPeriod> findBySetting_Id(Integer id);

    @Query(value = "SELECT * FROM get_academic_years_with_percentages()", nativeQuery = true)
    List<Object[]> getAcademicYearsWithPercentages();

    @Query(value = "SELECT verify_year_percentages(:year)", nativeQuery = true)
    Boolean verifyYearPercentages(@Param("year") Integer year);
}
