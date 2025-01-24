package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.ActivityGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.ActivityGroup;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.mapper.ActivityGroupMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.repository.ActivityGroupCrudRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class ActivityGroupAdapter implements PersistenceActivityGroupPort {

    private ActivityGroupCrudRepo crudRepo;

    @Autowired
    private ActivityGroupMapper mapper;

    public ActivityGroupAdapter(ActivityGroupCrudRepo activityGroupCrudRepo, ActivityGroupMapper activityGroupMapper){
        this.crudRepo = activityGroupCrudRepo;
        this.mapper = activityGroupMapper;
    }

    @Override
    public List<ActivityGroupDomain> findAll() {
        return mapper.toDomains(crudRepo.findAll());
    }

    @Override
    public ActivityGroupDomain findById(Integer integer) {
        Optional<ActivityGroup> activityGroup = this.crudRepo.findById(integer);
        return activityGroup.map(mapper::toDomain).orElse(null);
    }

    @Override
    public ActivityGroupDomain save(ActivityGroupDomain entity) {
        ActivityGroup activityGroup = mapper.toEntity(entity);
        ActivityGroup savedActivityGroup = this.crudRepo.save(activityGroup);
        return mapper.toDomain(savedActivityGroup);
    }

    @Override
    public ActivityGroupDomain update(Integer integer, ActivityGroupDomain entity) {
        try{
            Optional<ActivityGroup> existingActivityGroup = crudRepo.findById(integer);
            if(existingActivityGroup.isPresent()){
                existingActivityGroup.get().setActivity(entity.getActivityDomain());
                existingActivityGroup.get().setGroup(entity.getGroup());
                existingActivityGroup.get().setDue(entity.getDue());
            }
            return mapper.toDomain(crudRepo.save(existingActivityGroup.get()));
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Relation Activity Group with ID " + integer + " Not found!");
        }
    }

    @Override
    public HttpStatus delete(Integer integer) {
        return null;
    }
}
