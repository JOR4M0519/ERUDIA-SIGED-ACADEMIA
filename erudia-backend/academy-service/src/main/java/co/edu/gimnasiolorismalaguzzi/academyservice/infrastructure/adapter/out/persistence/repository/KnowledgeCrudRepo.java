package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.Knowledge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface KnowledgeCrudRepo extends JpaRepository<Knowledge, Integer> {
    @Transactional
    @Modifying
    @Query("update Knowledge u set u.status = ?1 where u.id = ?2")
    int updateStatusById(String status, Integer id);
}
