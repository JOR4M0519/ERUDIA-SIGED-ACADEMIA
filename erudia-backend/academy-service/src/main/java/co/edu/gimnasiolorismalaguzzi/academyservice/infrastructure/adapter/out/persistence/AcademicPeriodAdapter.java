package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceAcademicPeriodPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.AcademicPeriodDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.AcademicPeriod;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.EducationalLevel;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.mapper.AcademicPeriodMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.repository.AcademicPeriodCrudRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class AcademicPeriodAdapter implements PersistenceAcademicPeriodPort {

    private final AcademicPeriodCrudRepo academicPeriodCrudRepo;

    @Autowired
    private AcademicPeriodMapper academicPeriodMapper;
    @Autowired
    private InetUtils inetUtils;

    public AcademicPeriodAdapter(AcademicPeriodCrudRepo academicPeriodCrudRepo) {
        this.academicPeriodCrudRepo = academicPeriodCrudRepo;
    }


    @Override
    public List<AcademicPeriodDomain> findAll() {
        return this.academicPeriodMapper.toDomains(this.academicPeriodCrudRepo.findAll());
    }

    @Override
    public AcademicPeriodDomain findById(Integer integer) {
        Optional<AcademicPeriod> academicPeriodOptional = this.academicPeriodCrudRepo.findById(integer);
        return academicPeriodOptional.map(academicPeriodMapper::toDomain).orElse(null);
    }

    @Override
    public AcademicPeriodDomain save(AcademicPeriodDomain entity) {
        entity.setStatus("A");
        AcademicPeriod academicPeriod = academicPeriodMapper.toEntity(entity);
        AcademicPeriod savedAcademicPeriod = this.academicPeriodCrudRepo.save(academicPeriod);
        return academicPeriodMapper.toDomain(savedAcademicPeriod);
    }

    @Override
    public AcademicPeriodDomain update(Integer integer, AcademicPeriodDomain entity) {
        try {
            Optional<AcademicPeriod> existingPeriod = academicPeriodCrudRepo.findById(integer);
            if(existingPeriod.isPresent()){
                existingPeriod.get().setStartDate(entity.getStartDate());
                existingPeriod.get().setEndDate(entity.getEndDate());
                existingPeriod.get().setName(entity.getName());
                existingPeriod.get().setStatus(entity.getStatus());
            }
            return academicPeriodMapper.toDomain(academicPeriodCrudRepo.save(existingPeriod.get()));
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Period with id " + integer + " not found");
        }
    }

    @Override
    public HttpStatus delete(Integer integer) {
        try {
            if(this.academicPeriodCrudRepo.existsById(integer)){
                academicPeriodCrudRepo.updateStatusById("I", integer);
                return HttpStatus.OK;
            } else {
                throw new AppException("Period ID doesnt exist!", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e){
            throw new AppException("Intern Error!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
