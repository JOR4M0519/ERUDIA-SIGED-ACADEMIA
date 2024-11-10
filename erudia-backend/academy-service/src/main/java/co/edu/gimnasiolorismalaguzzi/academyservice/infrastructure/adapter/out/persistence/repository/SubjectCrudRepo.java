package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SubjectCrudRepo extends JpaRepository<Subject, Integer> {
    @Transactional
    @Modifying
    @Query("update Subject u set u.status = ?1 where u.id = ?2")
    int updateStatusById(String status, Integer id);

    @Override
    Optional<Subject> findById(Integer integer);


}
