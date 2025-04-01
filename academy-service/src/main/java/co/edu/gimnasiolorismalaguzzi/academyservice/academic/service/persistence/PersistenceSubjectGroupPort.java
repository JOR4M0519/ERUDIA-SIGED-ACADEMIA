package co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectProfessorDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;

import java.util.List;

public interface PersistenceSubjectGroupPort extends PersistencePort<SubjectGroupDomain, Integer> {
    List<SubjectGroupDomain> getAllSubjectGroupsByStudentId(Integer studentId, String year);

    List<SubjectGroupDomain> getAllSubjectByTeacher(Integer id, Integer year);

    List<SubjectGroupDomain> getStudentListByGroupTeacherPeriod(Integer groupId,Integer subjectId, Integer teacherId,Integer periodId);

    List<SubjectGroupDomain> getAllSubjectGRoupsByPeriodAndLevel(Integer periodId, Integer levelId);

    List<SubjectGroupDomain> getSubjectsByGroupIdAndPeriodId(Integer groupId, Integer periodId);
}
