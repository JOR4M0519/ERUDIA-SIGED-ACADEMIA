package co.edu.gimnasiolorismalaguzzi.academyservice.student.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.UseCase;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence.GroupServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence.PersistenceGroupsPort;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UseCase
public class GroupsServiceAdapter implements GroupServicePort {

    @Autowired
    private PersistenceGroupsPort groupStudentPort;

    @Override
    public List<GroupsDomain> getAllGroups() {
        return groupStudentPort.findAll();
    }

    @Override
    public GroupsDomain getGroupById(Integer id) {
        return groupStudentPort.findById(id);
    }

    @Override
    public GroupsDomain createGroup(GroupsDomain groupsDomain) {
        return groupStudentPort.save(groupsDomain);
    }

    @Override
    public GroupsDomain updateGroup(Integer id, GroupsDomain groupsDomain) {
        return groupStudentPort.update(id, groupsDomain);
    }

    @Override
    public void deleteGroup(Integer id) {
        groupStudentPort.delete(id);
    }
}
