package co.edu.gimnasiolorismalaguzzi.academyservice.student.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.GroupStudent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupStudentsCrudRepo extends JpaRepository<GroupStudent, Integer> {
}
