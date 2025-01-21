package co.edu.gimnasiolorismalaguzzi.academyservice.student.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupStudentDomain;

import java.util.List;

public interface GroupStudentServicePort {
    List<GroupStudentDomain> getAllGroups();
    GroupStudentDomain getGroupById(Integer id);
    GroupStudentDomain createGroup(GroupStudentDomain groupStudentDomain);
    GroupStudentDomain updateGroup(Integer id, GroupStudentDomain groupStudentDomain);
    void deleteGroup(Integer id);
}
