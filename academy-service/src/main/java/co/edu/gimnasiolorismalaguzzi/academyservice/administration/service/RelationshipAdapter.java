package co.edu.gimnasiolorismalaguzzi.academyservice.administration.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.RelationshipDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.Relationship;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.RelationshipMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository.RelationshipCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceFamilyPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceRelationshipPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.domain.DimensionDomain;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class RelationshipAdapter implements PersistenceRelationshipPort {

    @Autowired
    private final RelationshipCrudRepo relationshipCrudRepo;

    @Autowired
    private final RelationshipMapper relationshipMapper;

    @Autowired
    private final PersistenceFamilyPort persistenceFamilyPort;

    public RelationshipAdapter(RelationshipCrudRepo relationshipCrudRepo, RelationshipMapper relationshipMapper, PersistenceFamilyPort persistenceFamilyPort) {
        this.relationshipCrudRepo = relationshipCrudRepo;
        this.relationshipMapper = relationshipMapper;
        this.persistenceFamilyPort = persistenceFamilyPort;
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

        RelationshipDomain relationship = findById(integer);

        // Verificar si existe la dimension
        if (relationship.equals(null)) {
            throw new AppException("La dimension no existe", HttpStatus.NOT_FOUND);
        }

        // Verificar si el saber está siendo utilizado
        boolean usedInFamilyRelations = !persistenceFamilyPort.findAllByRelationType(integer).isEmpty();

        // Si está siendo utilizado, lanzar excepción
        if (usedInFamilyRelations) {
            throw new AppException(
                    "No es posible eliminar el saber porque está siendo utilizado en logros o evaluaciones",
                    HttpStatus.CONFLICT);
        }

        relationshipCrudRepo.deleteById(integer);
        return HttpStatus.OK;
    }
}
