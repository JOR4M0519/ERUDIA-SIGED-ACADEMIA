package co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;

import java.util.List;

public interface PersistenceSubjectGroupPort extends PersistencePort<SubjectGroupDomain, Integer> {
    List<SubjectGroupDomain> getAllSubjectGroupsByStudentsGroupsId(Integer id);
}
