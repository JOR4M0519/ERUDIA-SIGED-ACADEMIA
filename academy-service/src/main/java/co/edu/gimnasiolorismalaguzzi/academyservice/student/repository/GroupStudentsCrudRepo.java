package co.edu.gimnasiolorismalaguzzi.academyservice.student.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.GroupStudent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupStudentsCrudRepo extends JpaRepository<GroupStudent, Integer> {

    List<GroupStudent> findByStudent_IdAndGroup_Status(Integer id, String status);

    List<GroupStudent> findByGroup_IdAndGroup_StatusNotLike(Integer id, String status);

    List<GroupStudent> findByGroup_Mentor_Id(Integer mentorId);

    List<GroupStudent> findByGroup_Status(String status);
}
