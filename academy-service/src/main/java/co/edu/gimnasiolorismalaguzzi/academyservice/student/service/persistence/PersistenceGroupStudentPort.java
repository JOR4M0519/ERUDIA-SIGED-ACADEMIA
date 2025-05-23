package co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupStudentsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.StudentPromotionDTO;

import java.util.List;

public interface PersistenceGroupStudentPort extends PersistencePort<GroupStudentsDomain, Integer> {
    List<GroupStudentsDomain> findByStudentId(Integer studentId);

    List<GroupStudentsDomain> getGroupsStudentById(int id, String status);

    List<GroupStudentsDomain> getGroupsStudentByGroupId(Integer groupId, String statusNotLike);

    List<GroupStudentsDomain> getListByMentorIdByYear(Integer mentorId, Integer year);

    List<GroupStudentsDomain> getGroupListByStatus(String status);

    List<GroupStudentsDomain> promoteStudents(StudentPromotionDTO promotionDTO);
}
