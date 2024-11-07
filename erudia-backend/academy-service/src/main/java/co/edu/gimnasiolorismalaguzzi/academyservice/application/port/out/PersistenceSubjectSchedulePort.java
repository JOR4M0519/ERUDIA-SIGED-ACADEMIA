package co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.SubjectScheduleDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.SubjectSchedule;

public interface PersistenceSubjectSchedulePort extends PersistencePort<SubjectScheduleDomain, Integer>{
}
