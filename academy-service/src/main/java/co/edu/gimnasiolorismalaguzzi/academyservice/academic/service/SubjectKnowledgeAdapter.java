package co.edu.gimnasiolorismalaguzzi.academyservice.academic.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceSubjectKnowledgePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectKnowledgeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectKnowledge;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectKnowledgeMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectKnowledgeCrudRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class SubjectKnowledgeAdapter implements PersistenceSubjectKnowledgePort {

    private SubjectKnowledgeCrudRepo crudRepo;

    @Autowired
    private SubjectKnowledgeMapper mapper;

    public SubjectKnowledgeAdapter(SubjectKnowledgeCrudRepo subjectKnowledgeCrudRepo, SubjectKnowledgeMapper subjectKnowledgeMapper){
        this.crudRepo = subjectKnowledgeCrudRepo;
        this.mapper = subjectKnowledgeMapper;
    }

    @Override
    public List<SubjectKnowledgeDomain> findAll() {
        return this.mapper.toDomains(this.crudRepo.findAll());
    }

    @Override
    public SubjectKnowledgeDomain findById(Integer integer) {
        Optional<SubjectKnowledge> subjectKnowledge = this.crudRepo.findById(integer);
        return subjectKnowledge.map(mapper::toDomain).orElse(null);
    }

    @Override
    public SubjectKnowledgeDomain save(SubjectKnowledgeDomain entity) {
        SubjectKnowledge subjectKnowledge = mapper.toEntity(entity);
        SubjectKnowledge savedSubjectKnowledge = this.crudRepo.save(subjectKnowledge);
        return mapper.toDomain(savedSubjectKnowledge);
    }

    @Override
    public SubjectKnowledgeDomain update(Integer integer, SubjectKnowledgeDomain entity) {
        try{
            Optional<SubjectKnowledge> existingSubjectKnowledge = crudRepo.findById(integer);
            if(existingSubjectKnowledge.isPresent()){
                existingSubjectKnowledge.get().setIdSubject(entity.getIdSubject());
                existingSubjectKnowledge.get().setIdKnowledge(entity.getIdKnowledge());
            }
            return mapper.toDomain(crudRepo.save(existingSubjectKnowledge.get()));
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("UserDetail with ID " + integer + " not found");
        }
    }

    @Override
    public HttpStatus delete(Integer integer) {
        try {
            if(this.crudRepo.existsById(integer)){
                crudRepo.delete(this.crudRepo.getReferenceById(integer));
                return HttpStatus.OK;
            } else {
                throw new AppException("Relation within Subject and Knowledge ID doesnt exist", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e){
            throw new AppException("INTERN ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
