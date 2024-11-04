package co.edu.gimnasiolorismalaguzzi.academyservice.application.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.GroupStudentServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceGroupStudentPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.UseCase;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.GroupStudentDomain;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UseCase
public class GroupStudentServiceImpl implements GroupStudentServicePort {

    @Autowired
    private PersistenceGroupStudentPort groupStudentPort;

    @Override
    public List<GroupStudentDomain> getAllGroups() {
        return groupStudentPort.findAll();
    }

    @Override
    public GroupStudentDomain getGroupById(Integer id) {
        return groupStudentPort.findById(id);
    }

    @Override
    public GroupStudentDomain createGroup(GroupStudentDomain groupStudentDomain) {
        return groupStudentPort.save(groupStudentDomain);
    }

    @Override
    public GroupStudentDomain updateGroup(Integer id, GroupStudentDomain groupStudentDomain) {
        return groupStudentPort.update(id,groupStudentDomain);
    }

    @Override
    public void deleteGroup(Integer id) {
        groupStudentPort.delete(id);
    }
}
