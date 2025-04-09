package co.edu.gimnasiolorismalaguzzi.academyservice.academic.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.AcademicYearPercentageDTO;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceAcademicPeriodPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.AcademicPeriodDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.AcademicPeriod;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.AcademicPeriodMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.AcademicPeriodCrudRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@PersistenceAdapter
@Slf4j
public class AcademicPeriodAdapter implements PersistenceAcademicPeriodPort {

    private final AcademicPeriodCrudRepo academicPeriodCrudRepo;

    @Autowired
    private AcademicPeriodMapper academicPeriodMapper;

    public AcademicPeriodAdapter(AcademicPeriodCrudRepo academicPeriodCrudRepo) {
        this.academicPeriodCrudRepo = academicPeriodCrudRepo;
    }


    @Override
    public List<AcademicPeriodDomain> findAll() {
        return this.academicPeriodMapper.toDomains(this.academicPeriodCrudRepo.findAll());
    }

    @Override
    public List<AcademicPeriodDomain> getAllPeriodsByStatus(String status) {
        return this.academicPeriodMapper.toDomains(academicPeriodCrudRepo.findByStatus(status));
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
                AcademicPeriod period = existingPeriod.get();

                // Verificar si está cambiando el estado a Activo (A)
                if ("A".equals(entity.getStatus()) && !"A".equals(period.getStatus())) {
                    // Extraer el año de la fecha de inicio
                    int year = LocalDate.parse(entity.getStartDate().toString()).getYear();

                    // Verificar si el año tiene un porcentaje total del 100%
                    Boolean isComplete = academicPeriodCrudRepo.verifyYearPercentages(year);

                    // Si no está completo, lanzar una excepción
                    if (Boolean.FALSE.equals(isComplete)) {
                        throw new IllegalStateException("No se puede activar el periodo porque los periodos del año " +
                                year + " no suman exactamente 100%");
                    }
                }

                // Actualizar los campos
                period.setStartDate(entity.getStartDate());
                period.setEndDate(entity.getEndDate());
                period.setName(entity.getName());
                period.setStatus(entity.getStatus());
                period.setPercentage(entity.getPercentage());

                return academicPeriodMapper.toDomain(academicPeriodCrudRepo.save(period));
            } else {
                throw new EntityNotFoundException("Period with id " + integer + " not found");
            }
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error updating academic period: " + e.getMessage(), e);
        }
    }

    @Override
    public List<AcademicPeriodDomain> getActivePeriodsByYear(String year){
        List<AcademicPeriodDomain> academicPeriodDomain = this.academicPeriodMapper.toDomains(academicPeriodCrudRepo.getActivePeriodsByYear(year));
        return academicPeriodDomain;
    }

    @Override
    public List<AcademicPeriodDomain> getPeriodsByYear(String year){
        List<AcademicPeriodDomain> academicPeriodDomain = this.academicPeriodMapper.toDomains(academicPeriodCrudRepo.getPeriodsByYear(year));
        return academicPeriodDomain;
    }

    @Override
    public List<AcademicPeriodDomain> getPeriodsBySettingsAndYear(Integer settingId, String year) {
        List<AcademicPeriodDomain> academicPeriodDomain = this.academicPeriodMapper.toDomains(academicPeriodCrudRepo.getPeriodsByYear(year));

        return academicPeriodDomain.stream()
                .filter(period -> period.getGradeSetting().getId().equals(settingId))
                .collect(Collectors.toList());
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

    // Agregar esto a AcademicPeriodAdapter.java
    @Override
    public List<AcademicYearPercentageDTO> getAcademicYearsWithPercentages() {
        List<Object[]> results = academicPeriodCrudRepo.getAcademicYearsWithPercentages();
        List<AcademicYearPercentageDTO> dtos = new ArrayList<>();

        for (Object[] result : results) {
            Integer year = ((Number) result[0]).intValue();
            Integer percentage = ((Number) result[1]).intValue();
            Boolean isComplete = percentage == 100;

            dtos.add(new AcademicYearPercentageDTO(year, percentage, isComplete));
        }

        return dtos;
    }

    @Override
    public Boolean verifyYearPercentages(Integer year) {
        return academicPeriodCrudRepo.verifyYearPercentages(year);
    }


}
