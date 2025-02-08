package co.edu.gimnasiolorismalaguzzi.academyservice.administration.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.RelationshipDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.Relationship;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.RelationshipMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository.RelationshipCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class RelationshipAdapter implements PersistenceRelationshipPort{

    @Autowired
    private final RelationshipCrudRepo relationshipCrudRepo;

    @Autowired
    private final RelationshipMapper relationshipMapper;

    public RelationshipAdapter(RelationshipCrudRepo relationshipCrudRepo, RelationshipMapper relationshipMapper) {
        this.relationshipCrudRepo = relationshipCrudRepo;
        this.relationshipMapper = relationshipMapper;
    }

    @Override
    public List<RelationshipDomain> findAll() {
        return this.relationshipMapper.toDomains(this.relationshipCrudRepo.findAll());
    }

    @Override
    public RelationshipDomain findById(Integer integer) {
        Optional<Relationship> relationshipOptional = this.relationshipCrudRepo.findById(integer);
        return relationshipOptional.map(relationshipMapper::toDomain).orElse(null);
    }

    @Override
    public RelationshipDomain save(RelationshipDomain entity) {
        Relationship relationship = relationshipMapper.toEntity(entity);
        Relationship savedRelationship = this.relationshipCrudRepo.save(relationship);
        return relationshipMapper.toDomain(savedRelationship);
    }

    @Override
    public RelationshipDomain update(Integer integer, RelationshipDomain entity) {
        try{
            Optional<Relationship> existingRelationship = relationshipCrudRepo.findById(integer);
            existingRelationship.ifPresent(relationship -> relationship.setRelationshipType(entity.getRelationshipType()));
            return relationshipMapper.toDomain(relationshipCrudRepo.save(existingRelationship.get()));
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("RelationshipType with ID " + integer + " not found!");
        }
    }

    @Override
    public HttpStatus delete(Integer integer) {
        return null;
    }
}
