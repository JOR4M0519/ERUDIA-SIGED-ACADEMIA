package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.EducationalLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface EduLevelCrudRepo extends JpaRepository<EducationalLevel, Integer> {
    @Transactional
    @Modifying
    @Query("update EducationalLevel u set u.status = ?1 where u.id = ?2")
    int updateStatusById(String status, Integer id);


    @Override
    Optional<EducationalLevel> findById(Integer integer);
}
