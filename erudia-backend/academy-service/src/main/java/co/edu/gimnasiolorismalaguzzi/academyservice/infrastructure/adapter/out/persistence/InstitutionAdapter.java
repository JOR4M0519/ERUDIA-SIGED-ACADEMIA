package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceInstitutionPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.InstitutionDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.Institution;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.mapper.InstitutionMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.repository.InstitutionCrudRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class InstitutionAdapter implements PersistenceInstitutionPort {

    private final InstitutionCrudRepo institutionCrudRepo; // Repositorio JPA

    @Autowired
    private InstitutionMapper institutionMapper;

    public InstitutionAdapter(InstitutionCrudRepo institutionCrudRepo) {
        this.institutionCrudRepo = institutionCrudRepo;
    }

    @Override
    public List<InstitutionDomain> findAll() {
        return this.institutionMapper.toDomains(this.institutionCrudRepo.findAll());
    }

    @Override
    public InstitutionDomain findById(Integer id) {
        Optional<Institution> institutionOptional = this.institutionCrudRepo.findById(id);
        return institutionOptional.map(institutionMapper::toDomain).orElse(null);
    }

    @Override
    public InstitutionDomain save(InstitutionDomain institutionDomain) {
        Institution Institution = institutionMapper.toEntity(institutionDomain);
        Institution savedInstitution = this.institutionCrudRepo.save(Institution);
        return institutionMapper.toDomain(savedInstitution);
    }

    @Override
    public InstitutionDomain update(Integer id, InstitutionDomain entity) {

        try{
            Optional<Institution> existingInstitution = institutionCrudRepo.findById(id);

            if (existingInstitution.get().getName() != null) existingInstitution.get().setName(entity.getName());

            return institutionMapper.toDomain(institutionCrudRepo.save(existingInstitution.get()));

        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Institution with ID " + id + " not found");
        }

    }

    @Override
    public HttpStatus delete(Integer integer) {
        return null;
    }
}