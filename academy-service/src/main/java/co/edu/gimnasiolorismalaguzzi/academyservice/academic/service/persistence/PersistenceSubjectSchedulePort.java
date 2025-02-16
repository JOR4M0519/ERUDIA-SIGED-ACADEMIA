package co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectScheduleDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;

import java.util.List;

public interface PersistenceSubjectSchedulePort extends PersistencePort<SubjectScheduleDomain, Integer> {
    List<SubjectScheduleDomain> getScheduleByGroupStudentId(Integer id);
}
