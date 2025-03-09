package co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupStudentsDomain;

import java.util.List;

public interface PersistenceGroupStudentPort extends PersistencePort<GroupStudentsDomain, Integer> {
    List<GroupStudentsDomain> getGroupsStudentById(int id,String status);

    List<GroupStudentsDomain> getGroupsStudentByGroupId(Integer groupId, String statusNotLike);

    List<GroupStudentsDomain> getListByMentorIdByYear(Integer mentorId, Integer year);

    List<GroupStudentsDomain> getGroupListByStatus(String status);
}
