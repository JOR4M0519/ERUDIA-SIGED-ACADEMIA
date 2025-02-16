package co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupStudentsDomain;

import java.util.List;

public interface PersistenceGroupStudentPort extends PersistencePort<GroupStudentsDomain, Integer> {
    List<GroupStudentsDomain> getGroupsStudentById(int id,String status);
}
