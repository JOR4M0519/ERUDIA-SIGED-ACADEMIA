package co.edu.gimnasiolorismalaguzzi.academyservice.student.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.Groups;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.mapper.GroupsMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.repository.GroupsCrudRepo;
//import co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository.UserCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence.PersistenceGroupsPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class GroupsAdapter implements PersistenceGroupsPort {

    private final GroupsCrudRepo groupsCrudRepo; // Repositorio JPA

    @Autowired
    private GroupsMapper groupsMapper;

    public GroupsAdapter(GroupsCrudRepo groupsCrudRepo) {
        this.groupsCrudRepo = groupsCrudRepo;
    }

    @Override
    public List<GroupsDomain> findAll() {
        return this.groupsMapper.toDomains(this.groupsCrudRepo.findAll());
    }

    @Override
    public GroupsDomain findById(Integer id) {
        Optional<Groups> groupStudentOptional = this.groupsCrudRepo.findById(id);
        return groupStudentOptional.map(groupsMapper::toDomain).orElse(null);
    }

    @Override
    public GroupsDomain save(GroupsDomain groupsDomain) {
        Groups groups = groupsMapper.toEntity(groupsDomain);
        Groups savedGroup = this.groupsCrudRepo.save(groups);
        return groupsMapper.toDomain(savedGroup);
    }

    @Override
    public GroupsDomain update(Integer id, GroupsDomain domain) {
        try{
            Optional<Groups> existingGroup = groupsCrudRepo.findById(id);
            if(existingGroup.isPresent()){
                existingGroup.get().setLevel(groupsMapper.toEntity(domain).getLevel());
                existingGroup.get().setGroupCode(domain.getGroupCode());
                existingGroup.get().setGroupName(domain.getGroupName());
                existingGroup.get().setMentor(groupsMapper.toEntity(domain).getMentor());
                existingGroup.get().setStatus(domain.getStatus());
            }
            return groupsMapper.toDomain(groupsCrudRepo.save(existingGroup.get()));
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Group with id: " + id + "not found!");
        }
    }

    @Override
    public HttpStatus delete(Integer id) {
        try {
            if(this.groupsCrudRepo.existsById(id)){
                groupsCrudRepo.updateStatusById("I",id); //I de inactivo
                return HttpStatus.OK;
            } else {
                throw new AppException("Group ID doesnt exist!", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e){
            throw new AppException("Internal Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
