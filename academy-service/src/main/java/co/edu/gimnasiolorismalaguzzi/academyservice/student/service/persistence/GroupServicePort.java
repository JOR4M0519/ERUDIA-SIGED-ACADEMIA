package co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;

import java.util.List;

public interface GroupServicePort {
    List<GroupsDomain> getAllGroups();
    GroupsDomain getGroupById(Integer id);
    GroupsDomain createGroup(GroupsDomain groupsDomain);
    GroupsDomain updateGroup(Integer id, GroupsDomain groupsDomain);
    void deleteGroup(Integer id);
}
