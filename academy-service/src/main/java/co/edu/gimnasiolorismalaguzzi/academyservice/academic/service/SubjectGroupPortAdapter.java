package co.edu.gimnasiolorismalaguzzi.academyservice.academic.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectProfessorDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectGroup;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectProfessor;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectGroupMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectProfessorMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectGroupCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectProfessorCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceSubjectGroupPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@Slf4j
@PersistenceAdapter
public class SubjectGroupPortAdapter implements PersistenceSubjectGroupPort {

    private final SubjectGroupMapper subjectGroupMapper;

    private final SubjectProfessorMapper subjectProfessorMapper;


    @Autowired
    private SubjectGroupCrudRepo subjectGroupCrudRepo;
    @Autowired
    private SubjectProfessorCrudRepo subjectProfessorCrudRepo;


    public SubjectGroupPortAdapter(SubjectGroupMapper subjectGroupMapper, SubjectProfessorMapper subjectProfessorMapper) {
        this.subjectGroupMapper = subjectGroupMapper;
        this.subjectProfessorMapper = subjectProfessorMapper;
    }

    @Override
    public List<SubjectGroupDomain> findAll() {
        return this.subjectGroupMapper.toDomains(this.subjectGroupCrudRepo.findAll());
    }


    @Override
    public List<SubjectGroupDomain> getAllSubjectGroupsByStudentsGroupsId(Integer id) {
        return this.subjectGroupMapper.toDomains(this.subjectGroupCrudRepo.findByGroups_Id(id));
    }

    @Override
    public List<SubjectProfessorDomain> getAllSubjectByTeacher(Integer id, Integer year) {
        List<SubjectProfessor> subjectProfessorList = this.subjectProfessorCrudRepo.getAllSubjectByTeacher(id,year);
        return this.subjectProfessorMapper.toDomains(subjectProfessorList);
    }

    @Override
    public SubjectGroupDomain findById(Integer integer) {
        Optional<SubjectGroup> subjectGradeDomain = subjectGroupCrudRepo.findById(integer);
        return subjectGradeDomain.map(subjectGroupMapper::toDomain).orElse(null);
    }

    @Override
    public SubjectGroupDomain save(SubjectGroupDomain domain) {
        SubjectGroup subjectGroup = subjectGroupMapper.toEntity(domain);
        SubjectGroup savedSubjectGroup = subjectGroupCrudRepo.save(subjectGroup);
        return this.subjectGroupMapper.toDomain(savedSubjectGroup);
    }

    @Override
    public SubjectGroupDomain update(Integer integer, SubjectGroupDomain domain) {
        try{
            Optional<SubjectGroup> existingSubjectGroupDomain = subjectGroupCrudRepo.findById(integer);
            if(existingSubjectGroupDomain.isPresent()){
                existingSubjectGroupDomain.get().setSubject(subjectGroupMapper.toEntity(domain).getSubject());
                existingSubjectGroupDomain.get().setGroups(subjectGroupMapper.toEntity(domain).getGroups());
            }
            return subjectGroupMapper.toDomain(subjectGroupCrudRepo.save(existingSubjectGroupDomain.get()));
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Subject Group with id " + integer + " not found");
        }
    }

    @Override
    public HttpStatus delete(Integer integer) {
        try{
            if(this.subjectGroupCrudRepo.existsById(integer)){
                subjectGroupCrudRepo.delete(subjectGroupCrudRepo.getReferenceById(integer));
                return HttpStatus.OK;
            } else {
                throw new AppException("Period ID doesnt exist!", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e){
            throw new AppException("Intern Error!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
