package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.ActivityGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.ActivityGroup;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.mapper.ActivityGroupMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.repository.ActivityGroupCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.persistence.PersistenceActivityGroupPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class ActivityGroupAdapter implements PersistenceActivityGroupPort {

    private ActivityGroupCrudRepo activityGroupCrudRepo;

    @Autowired
    private ActivityGroupMapper activityGroupMapper;

    public ActivityGroupAdapter(ActivityGroupCrudRepo activityGroupCrudRepo, ActivityGroupMapper activityGroupMapper){
        this.activityGroupCrudRepo = activityGroupCrudRepo;
        this.activityGroupMapper = activityGroupMapper;
    }

    @Override
    public List<ActivityGroupDomain> findAll() {
        return activityGroupMapper.toDomains(activityGroupCrudRepo.findAll());
    }

    @Override
    public ActivityGroupDomain findById(Integer integer) {
        Optional<ActivityGroup> activityGroup = this.activityGroupCrudRepo.findById(integer);
        return activityGroup.map(activityGroupMapper::toDomain).orElse(null);
    }

    @Override
    public ActivityGroupDomain save(ActivityGroupDomain entity) {
        ActivityGroup activityGroup = activityGroupMapper.toEntity(entity);
        ActivityGroup savedActivityGroup = this.activityGroupCrudRepo.save(activityGroup);
        return activityGroupMapper.toDomain(savedActivityGroup);
    }

    @Override
    public ActivityGroupDomain update(Integer integer, ActivityGroupDomain domain) {
        try{
            Optional<ActivityGroup> existingActivityGroup = activityGroupCrudRepo.findById(integer);
            if(existingActivityGroup.isPresent()){
                existingActivityGroup.get().setActivity(activityGroupMapper.toEntity(domain).getActivity());
                existingActivityGroup.get().setGroup(activityGroupMapper.toEntity(domain).getGroup());
                existingActivityGroup.get().setStartDate(activityGroupMapper.toEntity(domain).getStartDate());
                existingActivityGroup.get().setEndDate(activityGroupMapper.toEntity(domain).getEndDate());
            }
            return activityGroupMapper.toDomain(activityGroupCrudRepo.save(existingActivityGroup.get()));
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Relation Activity Group with ID " + integer + " Not found!");
        }
    }

    @Override
    public HttpStatus delete(Integer integer) {
        return null;
    }

    @Override
    public List<ActivityGroupDomain> findActivitiesByGroupId(Integer id, String status) {
        return activityGroupMapper.toDomains(activityGroupCrudRepo.findByGroup_IdAndActivity_StatusNotLike(id, status));
    }
}
