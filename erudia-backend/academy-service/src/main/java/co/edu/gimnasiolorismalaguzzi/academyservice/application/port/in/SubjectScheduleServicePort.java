package co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectScheduleDomain;

import java.util.List;

public interface SubjectScheduleServicePort {
    List<SubjectScheduleDomain> getAllSubjectSchedules();
    SubjectScheduleDomain getScheduleById(Integer id);
    SubjectScheduleDomain createSubjectSchedule(SubjectScheduleDomain subjectScheduleDomain);
    SubjectScheduleDomain updateSubjectSchedule(Integer id, SubjectScheduleDomain subjectScheduleDomain);
    void deleteSubjectSchedule(Integer id);
}
