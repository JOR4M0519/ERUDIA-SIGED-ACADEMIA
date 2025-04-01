package co.edu.gimnasiolorismalaguzzi.academyservice.administration.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.AcademicPeriod;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.AcademicPeriodCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.GradeSettingDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.GradeSetting;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.GradeSettingMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository.GradeSettingsCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceGradeSettingPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class GradeSettingAdapter implements PersistenceGradeSettingPort {

    private final GradeSettingsCrudRepo crudRepo;
    private final AcademicPeriodCrudRepo academicPeriodCrudRepo;

    @Autowired
    private GradeSettingMapper mapper;

    public GradeSettingAdapter(GradeSettingsCrudRepo crudRepo, AcademicPeriodCrudRepo academicPeriodCrudRepo) {
        this.crudRepo = crudRepo;
        this.academicPeriodCrudRepo = academicPeriodCrudRepo;
    }

    @Override
    public List<GradeSettingDomain> findAll() {
        return this.mapper.toDomains(this.crudRepo.findAll());
    }

    @Override
    public List<GradeSettingDomain> findByLevelId(Integer levelId) {
        return this.mapper.toDomains(crudRepo.findByLevelId(levelId));
    }

    @Override
    public GradeSettingDomain findById(Integer integer) {
        Optional<GradeSetting> gradeSetting = this.crudRepo.findById(integer);
        return gradeSetting.map(mapper::toDomain).orElse(null);
    }

    @Override
    public GradeSettingDomain save(GradeSettingDomain entity) {
        GradeSetting gradeSetting = mapper.toEntity(entity);
        GradeSetting savedSetting = this.crudRepo.save(gradeSetting);
        return mapper.toDomain(savedSetting);
    }

    @Override
    public GradeSettingDomain update(Integer integer, GradeSettingDomain entity) {
        try{
            Optional<GradeSetting> existingSetting = crudRepo.findById(integer);
            if(existingSetting.isPresent()){
                existingSetting.get().setLevelId(entity.getLevelId());
                existingSetting.get().setName(entity.getName());
                existingSetting.get().setDescription(entity.getDescription());
                existingSetting.get().setMaximumGrade(entity.getMaximumGrade());
                existingSetting.get().setMinimumGrade(entity.getMinimumGrade());
                existingSetting.get().setPassGrade(entity.getPassGrade());
            }

            return mapper.toDomain(crudRepo.save(existingSetting.get()));
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Setting with ID " + integer + "Not found!");
        }
    }

    @Override
    public HttpStatus delete(Integer integer) {
        List<AcademicPeriod> academicPeriodList = academicPeriodCrudRepo.findBySetting_Id(integer);

        try {
            if(academicPeriodList.isEmpty()) {
                crudRepo.deleteById(integer);
                return HttpStatus.OK;
            } else {
                throw new AppException(
                        "No es posible eliminarlo porque se encuentra asociado a un periodo académico",
                        HttpStatus.IM_USED);
            }
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Setting with ID " + integer + " not found!");
        }
    }

}
