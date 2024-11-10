package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceKnowledgePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.KnowledgeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.Knowledge;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.mapper.KnowledgeMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.repository.KnowledgeCrudRepo;
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
    private KnowledgeMapper knowledgeMapper;

    public KnowledgeAdapter(KnowledgeCrudRepo knowledgeCrudRepo) {
        this.knowledgeCrudRepo = knowledgeCrudRepo;
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
        try{
            Optional<Knowledge> existingKnowledge = knowledgeCrudRepo.findById(integer);
            if(existingKnowledge.isPresent()) {
                existingKnowledge.get().setName(entity.getName());
                existingKnowledge.get().setAchievement(entity.getAchievement());
                existingKnowledge.get().setStatus(entity.getStatus());
            }
            return knowledgeMapper.toDomain(knowledgeCrudRepo.save(existingKnowledge.get()));
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Knowledge with ID " + integer + "not found!");
        }
    }

    @Override
    public HttpStatus delete(Integer integer) {
        try{
            if(this.knowledgeCrudRepo.existsById(integer)){
                knowledgeCrudRepo.updateStatusById("I", integer);
                return HttpStatus.OK;
            } else {
                throw new AppException("Knowledge with ID does exist! ", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e){
            throw new AppException("Interal Error! ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
