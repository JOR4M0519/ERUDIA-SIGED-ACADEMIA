package co.edu.gimnasiolorismalaguzzi.academyservice.administration.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceIdTypePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.IdTypeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.IdType;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.IdTypeMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository.IdTypeCrudRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class IdTypeAdapter implements PersistenceIdTypePort {

    private final IdTypeCrudRepo crudRepo;

    @Autowired
    private IdTypeMapper mapper;

    public IdTypeAdapter(IdTypeCrudRepo crudRepo) {
        this.crudRepo = crudRepo;
    }

    @Override
    public List<IdTypeDomain> findAll() {
        return mapper.toDomains(this.crudRepo.findAll());
    }

    @Override
    public IdTypeDomain findById(Integer integer) {
        Optional<IdType> idType = this.crudRepo.findById(integer);
        return idType.map(mapper::toDomain).orElse(null);
    }

    @Override
    public IdTypeDomain save(IdTypeDomain idTypeDomain) {
        IdType idType = mapper.toEntity(idTypeDomain);
        IdType savedIdType = this.crudRepo.save(idType);
        return mapper.toDomain(savedIdType);
    }

    @Override
    public IdTypeDomain update(Integer integer, IdTypeDomain idTypeDomain) {
        try{
            Optional<IdType> existingIdType = crudRepo.findById(integer);
            existingIdType.ifPresent(idType -> idType.setName(idTypeDomain.getName()));
            return mapper.toDomain(crudRepo.save(existingIdType.get()));
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Id Type with id: " + integer + "not found!");
        }
    }

    @Override
    public HttpStatus delete(Integer integer) {
        //no es necesario
        return HttpStatus.CONFLICT;
    }
}
