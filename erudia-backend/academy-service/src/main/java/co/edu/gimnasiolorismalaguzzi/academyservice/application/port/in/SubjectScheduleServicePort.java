package co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.SubjectScheduleDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.SubjectSchedule;

import java.util.List;

public interface SubjectScheduleServicePort {
    List<SubjectScheduleDomain> getAllSubjectSchedules();
    SubjectScheduleDomain getScheduleById(Integer id);
    SubjectScheduleDomain createSubjectSchedule(SubjectScheduleDomain subjectScheduleDomain);
    SubjectScheduleDomain updateSubjectSchedule(Integer id, SubjectScheduleDomain subjectScheduleDomain);
    void deleteSubjectSchedule(Integer id);
}
