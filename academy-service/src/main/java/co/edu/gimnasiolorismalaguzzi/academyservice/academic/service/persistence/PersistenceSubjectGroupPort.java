package co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectProfessorDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupStudentsDomain;

import java.util.List;

public interface PersistenceSubjectGroupPort extends PersistencePort<SubjectGroupDomain, Integer> {
    List<SubjectGroupDomain> findAllBySubjectProfessor(Integer subjectProfessorId);

    List<SubjectGroupDomain> getAllSubjectGroupsByStudentId(Integer studentId, String year);
    List<SubjectGroupDomain> getAllSubjectGroupsByStudentIdPeriodId(Integer studentId, Integer periodId);

    List<SubjectGroupDomain> getAllSubjectGroupsByStudentIdByPeriod(Integer studentId, Integer periodId);

    List<SubjectGroupDomain> getAllSubjectByTeacher(Integer id, Integer year);

    List<SubjectGroupDomain> getStudentListByGroupTeacherPeriod(Integer groupId,Integer subjectId, Integer teacherId,Integer periodId);

    List<SubjectGroupDomain> getAllSubjectGRoupsByPeriodAndLevel(Integer periodId, Integer levelId);

    List<GroupStudentsDomain> getGroupsStudentsByPeriodIdAndSubjectProfessorIdAndGroupId(Integer periodId, Integer subjectId, Integer groupId);

    List<SubjectGroupDomain> getSubjectsByGroupIdAndPeriodId(Integer groupId, Integer periodId);

    List<GroupStudentsDomain> getGroupsStudentsByPeriodIdAndSubjectProfessorId(Integer periodId, Integer subjectId);
}
