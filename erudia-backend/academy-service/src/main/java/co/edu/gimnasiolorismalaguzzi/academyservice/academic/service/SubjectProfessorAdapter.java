package co.edu.gimnasiolorismalaguzzi.academyservice.academic.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectProfessorDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectProfessor;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectProfessorMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectProfessorCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class SubjectProfessorAdapter implements PersistenceSubjectProfessorPort {

    private SubjectProfessorCrudRepo crudRepo;

    @Autowired
    private SubjectProfessorMapper mapper;

    public SubjectProfessorAdapter(SubjectProfessorCrudRepo subjectProfessorCrudRepo, SubjectProfessorMapper subjectProfessorMapper){
        this.crudRepo = subjectProfessorCrudRepo;
        this.mapper = subjectProfessorMapper;
    }

    @Override
    public List<SubjectProfessorDomain> findAll() {
        return this.mapper.toDomains(this.crudRepo.findAll());
    }

    @Override
    public List<SubjectProfessorDomain> findBySubjectId (Integer subjectId){
        List<SubjectProfessor>professors = crudRepo.findBySubjectId(subjectId);
        return mapper.toDomains(professors);
    }

    @Override
    public SubjectProfessorDomain findById(Integer integer) {
        Optional<SubjectProfessor> subjectProfessor = this.crudRepo.findById(integer);
        return subjectProfessor.map(mapper::toDomain).orElse(null);
    }

    @Override
    public SubjectProfessorDomain save(SubjectProfessorDomain entity) {
        SubjectProfessor subjectProfessor = mapper.toEntity(entity);
        SubjectProfessor savedSubjectProfessor = this.crudRepo.save(subjectProfessor);
        return mapper.toDomain(savedSubjectProfessor);
    }

    @Override
    public SubjectProfessorDomain update(Integer integer, SubjectProfessorDomain entity) {
        try {
            Optional<SubjectProfessor> existingSubjectProfessor = crudRepo.findById(integer);
            if(existingSubjectProfessor.isPresent()){
                existingSubjectProfessor.get().setProfessor(existingSubjectProfessor.get().getProfessor());
                existingSubjectProfessor.get().setSubject(existingSubjectProfessor.get().getSubject());
            }
            return mapper.toDomain(crudRepo.save(existingSubjectProfessor.get()));
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Subject Professor with ID " + integer + "Not found!");
        }
    }

    @Override
    public HttpStatus delete(Integer integer) {
        try {
            if(this.crudRepo.existsById(integer)){
                crudRepo.delete(this.crudRepo.getReferenceById(integer));
                return HttpStatus.OK;
            } else {
                throw new AppException("Relation within Professor and Subject ID doesnt exist", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            throw new AppException("INTERN ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
