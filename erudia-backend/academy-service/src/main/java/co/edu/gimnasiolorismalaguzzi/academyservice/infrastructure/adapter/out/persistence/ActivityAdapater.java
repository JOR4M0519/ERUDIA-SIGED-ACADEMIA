package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceActivityPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.ActivityDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.Activity;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.EducationalLevel;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.mapper.ActivityMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.repository.ActivityCrudRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;


@PersistenceAdapter
@Slf4j
public class ActivityAdapater implements PersistenceActivityPort {

    private final ActivityCrudRepo activityCrudRepo;

    @Autowired
    private final ActivityMapper activityMapper;

    public ActivityAdapater(ActivityCrudRepo activityCrudRepo, ActivityMapper activityMapper) {
        this.activityCrudRepo = activityCrudRepo;
        this.activityMapper = activityMapper;
    }

    @Override
    public List<ActivityDomain> findAll() {
        return this.activityMapper.toDomains(this.activityCrudRepo.findAll());
    }

    @Override
    public ActivityDomain findById(Integer integer) {
        Optional<Activity> activityOptional = this.activityCrudRepo.findById(integer);
        return activityOptional.map(activityMapper::toDomain).orElse(null);
    }

    @Override
    public ActivityDomain save(ActivityDomain entity) {
        entity.setActivityName(entity.getActivityName());
        entity.setStatus("A");

        Activity activity = activityMapper.toEntity(entity);
        Activity savedActivity = this.activityCrudRepo.save(activity);

        return activityMapper.toDomain(savedActivity);
    }

    @Override
    public ActivityDomain update(Integer integer, ActivityDomain entity) {
        try{
            Optional<Activity> existingActivity = activityCrudRepo.findById(integer);

            if (existingActivity.isPresent()) existingActivity.get().setActivityName(entity.getActivityName());

            return activityMapper.toDomain(activityCrudRepo.save(existingActivity.get()));

        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException("UserDetail with ID " + integer + " not found");
        }
    }

    @Override
    public HttpStatus delete(Integer integer) {
        try{
            if (this.activityCrudRepo.existsById(integer)) {
                activityCrudRepo.updateStatusById("I",integer);
                return HttpStatus.OK;
            } else {
                throw new AppException("Activity ID doesnt exist", HttpStatus.NOT_FOUND);
            }
        }catch(Exception e){
            throw new AppException("INTERN ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
