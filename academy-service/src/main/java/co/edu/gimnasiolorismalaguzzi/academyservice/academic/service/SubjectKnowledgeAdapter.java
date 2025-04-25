package co.edu.gimnasiolorismalaguzzi.academyservice.academic.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceSubjectKnowledgePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectKnowledgeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectKnowledge;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectKnowledgeMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectKnowledgeCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.service.persistence.PersistenceAchievementGroups;
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
      return null;
    }

    @Override
    public HttpStatus delete(Integer integer) {

        return null;

    }

    @Override
    public List<SubjectKnowledgeDomain> getAllKnowledgesBySubjectIdByPeriodId(Integer subjectId,Integer periodId) {
        return null;    }

    @Override
    public List<SubjectKnowledgeDomain> getAllSubjectKnowledgeByKnowledgeId(Integer knowledgeId) {
        return null;    }

    @Override
    public List<SubjectKnowledgeDomain> getAllSubjectKnowledgeBySubjectIdAndGroupId(Integer subjectId,Integer groupId) {
        return null;    }

    @Override
    public List<SubjectKnowledgeDomain> getAllSubjectKnowledgeBySubjectId(Integer subjectId) {
        return null;    }
}
