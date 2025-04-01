package co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceSubjectDimensionPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.domain.DimensionDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.domain.KnowledgeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.entity.Dimension;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.mapper.DimensionMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.repository.DimensionCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.service.persistence.PersistenceDimensionPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class DimensionAdapter implements PersistenceDimensionPort {


    private final DimensionCrudRepo dimensionCrudRepo;

    @Autowired
    private final DimensionMapper dimensionMapper;

    @Autowired
    private final PersistenceSubjectDimensionPort subjectDimensionPort;

    public DimensionAdapter(DimensionCrudRepo dimensionCrudRepo, DimensionMapper dimensionMapper, PersistenceSubjectDimensionPort subjectDimensionPort) {
        this.dimensionCrudRepo = dimensionCrudRepo;
        this.dimensionMapper = dimensionMapper;
        this.subjectDimensionPort = subjectDimensionPort;
    }

    @Override
    public List<DimensionDomain> findAll() {
        return this.dimensionMapper.toDomains(this.dimensionCrudRepo.findAll());
    }

    @Override
    public DimensionDomain findById(Integer integer) {
        Optional<Dimension> dimensionOptional = this.dimensionCrudRepo.findById(integer);
        return dimensionOptional.map(dimensionMapper::toDomain).orElse(null);
    }

    @Override
    public DimensionDomain save(DimensionDomain dimensionDomain) {
        Dimension dimension = dimensionMapper.toEntity(dimensionDomain);
        Dimension savedDimension = this.dimensionCrudRepo.save(dimension);
        return dimensionMapper.toDomain(savedDimension);
    }

    @Override
    public DimensionDomain update(Integer integer, DimensionDomain entity) {
        try{
            Optional<Dimension> existingDimension = dimensionCrudRepo.findById(integer);
            if(existingDimension.isPresent()){
                existingDimension.get().setName(entity.getName());
                existingDimension.get().setDescription(entity.getDescription());
            }
            return dimensionMapper.toDomain(dimensionCrudRepo.save(existingDimension.get()));
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Dimension with ID " + integer + " Not found!");
        }
    }

    @Override
    public HttpStatus delete(Integer id) {

        DimensionDomain dimension = findById(id);

        // Verificar si existe la dimension
        if (dimension.equals(null)) {
            throw new AppException("La dimension no existe", HttpStatus.NOT_FOUND);
        }

        // Verificar si el saber está siendo utilizado
        boolean usedInSubjectDimension = !subjectDimensionPort.getAllByDimensionId(id).isEmpty();

        // Si está siendo utilizado, lanzar excepción
        if (usedInSubjectDimension) {
            throw new AppException(
                    "No es posible eliminar el saber porque está siendo utilizado en logros o evaluaciones",
                    HttpStatus.CONFLICT);
        }

        dimensionCrudRepo.deleteById(id);
        return HttpStatus.OK;
    }

}
