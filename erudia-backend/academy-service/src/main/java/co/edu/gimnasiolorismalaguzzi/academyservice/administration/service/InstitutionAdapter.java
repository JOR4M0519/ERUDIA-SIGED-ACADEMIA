package co.edu.gimnasiolorismalaguzzi.academyservice.administration.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.InstitutionDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.Institution;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.InstitutionMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository.InstitutionCrudRepo;
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

            if (existingInstitution.isPresent()){
                existingInstitution.get().setName(entity.getName());
                existingInstitution.get().setNit(entity.getNit());
                existingInstitution.get().setAddress(entity.getAddress());
                existingInstitution.get().setPhoneNumber(entity.getPhoneNumber());
                existingInstitution.get().setEmail(entity.getEmail());
                existingInstitution.get().setCity(entity.getCity());
                existingInstitution.get().setDepartment(entity.getDepartment());
                existingInstitution.get().setCountry(entity.getCountry());
                existingInstitution.get().setPostalCode(entity.getPostalCode());
                existingInstitution.get().setLegalRepresentative(entity.getLegalRepresentative());
                existingInstitution.get().setIncorporationDate(entity.getIncorporationDate());
            }

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
