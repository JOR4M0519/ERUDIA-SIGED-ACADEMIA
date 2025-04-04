package co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectKnowledgeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectKnowledge;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceSubjectKnowledgePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.domain.KnowledgeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.entity.Knowledge;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.mapper.KnowledgeMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.repository.KnowledgeCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.service.persistence.PersistenceKnowledgePort;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class KnowledgeAdapter implements PersistenceKnowledgePort {

    private final KnowledgeCrudRepo knowledgeCrudRepo;

    @Autowired
    private PersistenceSubjectKnowledgePort subjectKnowledgePort;

    private KnowledgeMapper knowledgeMapper;

    public KnowledgeAdapter(KnowledgeCrudRepo knowledgeCrudRepo, KnowledgeMapper knowledgeMapper) {
        this.knowledgeCrudRepo = knowledgeCrudRepo;
        this.knowledgeMapper = knowledgeMapper;
    }

    @Override
    public List<KnowledgeDomain> findAll() {
        return knowledgeMapper.toDomains(knowledgeCrudRepo.findAll());
    }

    @Override
    public KnowledgeDomain findById(Integer integer) {
        Optional<Knowledge> knowledgeOptional = this.knowledgeCrudRepo.findById(integer);
        return knowledgeOptional.map(knowledgeMapper::toDomain).orElse(null);
    }

    @Override
    public KnowledgeDomain save(KnowledgeDomain entity) {
        Knowledge knowledge = knowledgeMapper.toEntity(entity);
        Knowledge savedKnowledge = this.knowledgeCrudRepo.save(knowledge);
        return knowledgeMapper.toDomain(savedKnowledge);
    }

    @Override
    public KnowledgeDomain update(Integer integer, KnowledgeDomain entity) {
        try {
            Optional<Knowledge> existingKnowledge = knowledgeCrudRepo.findById(integer);
            if (existingKnowledge.isPresent()) {
                existingKnowledge.get().setName(entity.getName());
                existingKnowledge.get().setStatus(entity.getStatus());
            }
            return knowledgeMapper.toDomain(knowledgeCrudRepo.save(existingKnowledge.get()));
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Knowledge with ID " + integer + "not found!");
        }
    }

    public KnowledgeDomain updateStatusById(Integer integer, KnowledgeDomain entity) {
        try {
            Optional<Knowledge> existingKnowledge = knowledgeCrudRepo.findById(integer);
            if (existingKnowledge.isPresent()) {
                existingKnowledge.get().setName(entity.getName());
                existingKnowledge.get().setStatus(entity.getStatus());
            }
            return knowledgeMapper.toDomain(knowledgeCrudRepo.save(existingKnowledge.get()));
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Knowledge with ID " + integer + "not found!");
        }
    }

    @Override
    public HttpStatus delete(Integer id) {

        KnowledgeDomain knowledge = findById(id);

        // Verificar si existe el saber
        if (knowledge.equals(null)) {
            throw new AppException("El saber no existe", HttpStatus.NOT_FOUND);
        }

        // Verificar si el saber está siendo utilizado en logros
        boolean usedInAchievements = !subjectKnowledgePort.getAllSubjectKnowledgeByKnowledgeId(id).isEmpty();

        // Si está siendo utilizado, lanzar excepción
        if (usedInAchievements) {
            throw new AppException(
                    "No es posible eliminar el saber porque está siendo utilizado en logros o evaluaciones",
                    HttpStatus.CONFLICT);
        }

        knowledge.setStatus("I");
        // Si no está siendo utilizado, actualizar el estado a inactivo
        updateStatusById(id, knowledge);
        return HttpStatus.OK;
    }

}
