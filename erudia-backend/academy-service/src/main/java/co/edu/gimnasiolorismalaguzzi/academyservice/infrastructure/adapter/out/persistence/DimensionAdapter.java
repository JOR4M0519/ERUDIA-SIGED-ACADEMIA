package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceDimensionPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.DimensionDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.Dimension;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.mapper.DimensionMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.repository.DimensionCrudRepo;
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

    public DimensionAdapter(DimensionCrudRepo dimensionCrudRepo, DimensionMapper dimensionMapper) {
        this.dimensionCrudRepo = dimensionCrudRepo;
        this.dimensionMapper = dimensionMapper;
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
    public HttpStatus delete(Integer integer) {
        //No creo que sea necesario borrar una dimension :v
        return null;
    }
}
