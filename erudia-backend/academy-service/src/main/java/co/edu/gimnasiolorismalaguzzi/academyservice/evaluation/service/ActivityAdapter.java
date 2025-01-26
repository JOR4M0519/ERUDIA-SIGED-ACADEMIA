package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.ActivityDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.Activity;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.mapper.ActivityMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.repository.ActivityCrudRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;


@PersistenceAdapter
@Slf4j
public class ActivityAdapter implements PersistenceActivityPort {

    private final ActivityCrudRepo activityCrudRepo;

    @Autowired
    private final ActivityMapper activityMapper;

    public ActivityAdapter(ActivityCrudRepo activityCrudRepo, ActivityMapper activityMapper) {
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
        entity.setStatus("A");
        Activity activity = activityMapper.toEntity(entity);
        Activity savedActivity = this.activityCrudRepo.save(activity);

        return activityMapper.toDomain(savedActivity);
    }

    @Override
    public ActivityDomain update(Integer integer, ActivityDomain entity) {
        try{
            Optional<Activity> existingActivity = activityCrudRepo.findById(integer);

            if (existingActivity.isPresent()){
                existingActivity.get().setActivityName(entity.getActivityName());
                existingActivity.get().setDescription(entity.getDescription());
                existingActivity.get().setSubject(entity.getSubject());
                existingActivity.get().setPeriod(entity.getPeriod());
                existingActivity.get().setKnowledge(entity.getKnowledge());
                existingActivity.get().setStatus(entity.getStatus());
            }

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
