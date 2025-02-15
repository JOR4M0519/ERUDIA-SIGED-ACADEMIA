package co.edu.gimnasiolorismalaguzzi.academyservice.student.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupStudentDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.GroupStudent;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.mapper.GroupStudentMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.repository.GroupStudentCrudRepo;
//import co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository.UserCrudRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class GroupStudentAdapter implements PersistenceGroupStudentPort {

    private final GroupStudentCrudRepo groupStudentCrudRepo; // Repositorio JPA

    @Autowired
    private GroupStudentMapper groupStudentMapper;

    public GroupStudentAdapter(GroupStudentCrudRepo groupStudentCrudRepo) {
        this.groupStudentCrudRepo = groupStudentCrudRepo;
    }

    @Override
    public List<GroupStudentDomain> findAll() {
        return this.groupStudentMapper.toDomains(this.groupStudentCrudRepo.findAll());
    }

    @Override
    public GroupStudentDomain findById(Integer id) {
        Optional<GroupStudent> groupStudentOptional = this.groupStudentCrudRepo.findById(id);
        return groupStudentOptional.map(groupStudentMapper::toDomain).orElse(null);
    }

    @Override
    public GroupStudentDomain save(GroupStudentDomain groupStudentDomain) {
        GroupStudent groupStudent = groupStudentMapper.toEntity(groupStudentDomain);
        GroupStudent savedGroup = this.groupStudentCrudRepo.save(groupStudent);
        return groupStudentMapper.toDomain(savedGroup);
    }

    @Override
    public GroupStudentDomain update(Integer id, GroupStudentDomain entity) {
        try{
            Optional<GroupStudent> existingGroup = groupStudentCrudRepo.findById(id);
            if(existingGroup.isPresent()){
                existingGroup.get().setLevel(entity.getLevel());
                existingGroup.get().setGroupCode(entity.getGroupCode());
                existingGroup.get().setGroupName(entity.getGroupName());
                existingGroup.get().setProfessor(entity.getProfessor());
                existingGroup.get().setStatus(entity.getStatus());
            }
            return groupStudentMapper.toDomain(groupStudentCrudRepo.save(existingGroup.get()));
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Group with id: " + id + "not found!");
        }
    }

    @Override
    public HttpStatus delete(Integer id) {
        try {
            if(this.groupStudentCrudRepo.existsById(id)){
                groupStudentCrudRepo.updateStatusById("I",id); //I de inactivo
                return HttpStatus.OK;
            } else {
                throw new AppException("Group ID doesnt exist!", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e){
            throw new AppException("Internal Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
