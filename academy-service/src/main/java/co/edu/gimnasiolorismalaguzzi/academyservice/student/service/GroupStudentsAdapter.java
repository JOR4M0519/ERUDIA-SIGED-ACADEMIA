package co.edu.gimnasiolorismalaguzzi.academyservice.student.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupStudentsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.GroupStudent;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.mapper.GroupStudentsMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.repository.GroupStudentsCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence.PersistenceGroupStudentPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class GroupStudentsAdapter implements PersistenceGroupStudentPort {

    private final GroupStudentsCrudRepo groupStudentsCrudRepo;

    @Autowired
    private GroupStudentsMapper groupStudentsMapper;

    public GroupStudentsAdapter(GroupStudentsCrudRepo groupStudentsCrudRepo) {
        this.groupStudentsCrudRepo = groupStudentsCrudRepo;
    }

    @Override
    public List<GroupStudentsDomain> findAll() {
        return this.groupStudentsMapper.toDomains(groupStudentsCrudRepo.findAll());
    }

    @Override
    public GroupStudentsDomain findById(Integer integer) {
        Optional<GroupStudent> groupStudent = groupStudentsCrudRepo.findById(integer);
        return groupStudent.map(groupStudentsMapper::toDomain).orElse(null);
    }

    @Override
    public List<GroupStudentsDomain> getGroupsStudentById(int id,String status) {
        return groupStudentsMapper.toDomains(groupStudentsCrudRepo.findByStudent_IdAndGroup_Status(id,status));
    }

    @Override
    public GroupStudentsDomain save(GroupStudentsDomain domain) {
        GroupStudent groupStudent = groupStudentsMapper.toEntity(domain);
        GroupStudent savedGroupStudent = this.groupStudentsCrudRepo.save(groupStudent);
        return groupStudentsMapper.toDomain(savedGroupStudent);
    }

    @Override
    public GroupStudentsDomain update(Integer integer, GroupStudentsDomain domain) {
        try{
            Optional<GroupStudent> existingGroupStudent = groupStudentsCrudRepo.findById(integer);
            if(existingGroupStudent.isPresent()){
                existingGroupStudent.get().setGroup(groupStudentsMapper.toEntity(domain).getGroup());
                existingGroupStudent.get().setStudent(groupStudentsMapper.toEntity(domain).getStudent());
            }
            return groupStudentsMapper.toDomain(groupStudentsCrudRepo.save(existingGroupStudent.get()));
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("GroupStudent with id: " + integer + "not found!");
        }
    }

    @Override
    public HttpStatus delete(Integer integer) {
        try{
            if(this.groupStudentsCrudRepo.existsById(integer)){
                groupStudentsCrudRepo.delete(this.groupStudentsCrudRepo.getReferenceById(integer));
                return HttpStatus.OK;
            } else {
                throw new AppException("GroupStudent ID doesnt exist!", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e){
            throw new AppException("Internal Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
