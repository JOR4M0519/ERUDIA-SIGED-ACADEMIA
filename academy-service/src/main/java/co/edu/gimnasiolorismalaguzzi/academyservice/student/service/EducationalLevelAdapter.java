package co.edu.gimnasiolorismalaguzzi.academyservice.student.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.EducationalLevelDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.EducationalLevel;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.mapper.EducationalLevelMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.repository.EduLevelCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence.PersistenceEducationalLevelPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class EducationalLevelAdapter implements PersistenceEducationalLevelPort {

    private final EduLevelCrudRepo EduLevelCrudRepo; // Repositorio JPA

    @Autowired
    private EducationalLevelMapper EducationalLevelMapper;

    public EducationalLevelAdapter(EduLevelCrudRepo EduLevelCrudRepo) {
        this.EduLevelCrudRepo = EduLevelCrudRepo;
    }

    @Override
    public List<EducationalLevelDomain> findAll() {
        return this.EducationalLevelMapper.toDomains(this.EduLevelCrudRepo.findAll());
    }

    @Override
    public EducationalLevelDomain findById(Integer id) {
        Optional<EducationalLevel> EducationalLevelOptional = this.EduLevelCrudRepo.findById(id);
        return EducationalLevelOptional.map(EducationalLevelMapper::toDomain).orElse(null);
    }

    @Override
    public EducationalLevelDomain save(EducationalLevelDomain EducationalLevelDomain) {

        EducationalLevelDomain.setLevelName(EducationalLevelDomain.getLevelName());
        EducationalLevelDomain.setStatus("A");

        EducationalLevel EducationalLevel = EducationalLevelMapper.toEntity(EducationalLevelDomain);
        EducationalLevel savedEducationalLevel = this.EduLevelCrudRepo.save(EducationalLevel);
        return EducationalLevelMapper.toDomain(savedEducationalLevel);
    }

    @Override
    public EducationalLevelDomain update(Integer id, EducationalLevelDomain entity) {

        try{
            Optional<EducationalLevel> existingEducationalLevel = EduLevelCrudRepo.findById(id);

            if(existingEducationalLevel.isPresent()){
                existingEducationalLevel.get().setLevelName(entity.getLevelName());
                existingEducationalLevel.get().setStatus(entity.getStatus());
            }

            return EducationalLevelMapper.toDomain(EduLevelCrudRepo.save(existingEducationalLevel.get()));

        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException("UserDetail with ID " + id + " not found");
        }
    }

    @Override
    public HttpStatus delete(Integer integer) {

        //Check if there are  some register in other table
        //table
         try{
            if (this.EduLevelCrudRepo.existsById(integer)) {
                EduLevelCrudRepo.updateStatusById("I",integer);
                return HttpStatus.OK;
            } else {
                throw new AppException("User ID doesnt exist", HttpStatus.NOT_FOUND);
            }
        }catch(Exception e){
            throw new AppException("INTERN ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
