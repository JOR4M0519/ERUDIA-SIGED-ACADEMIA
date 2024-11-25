package co.edu.gimnasiolorismalaguzzi.academyservice.application.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.SubjectScheduleServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceSubjectSchedulePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.UseCase;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.SubjectScheduleDomain;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UseCase
public class SubjectScheduleServiceImpl implements SubjectScheduleServicePort {

    private PersistenceSubjectSchedulePort subjectSchedulePort;

    @Autowired
    public SubjectScheduleServiceImpl(PersistenceSubjectSchedulePort persistenceSubjectSchedulePort){
        this.subjectSchedulePort = persistenceSubjectSchedulePort;
    }

    @Override
    public List<SubjectScheduleDomain> getAllSubjectSchedules() {
        return subjectSchedulePort.findAll();
    }

    @Override
    public SubjectScheduleDomain getScheduleById(Integer id) {
        return subjectSchedulePort.findById(id);
    }

    @Override
    public SubjectScheduleDomain createSubjectSchedule(SubjectScheduleDomain subjectScheduleDomain) {
        return subjectSchedulePort.save(subjectScheduleDomain);
    }

    @Override
    public SubjectScheduleDomain updateSubjectSchedule(Integer id, SubjectScheduleDomain subjectScheduleDomain) {
        return subjectSchedulePort.update(id, subjectScheduleDomain);
    }

    @Override
    public void deleteSubjectSchedule(Integer id) {
        subjectSchedulePort.delete(id);
    }
}
