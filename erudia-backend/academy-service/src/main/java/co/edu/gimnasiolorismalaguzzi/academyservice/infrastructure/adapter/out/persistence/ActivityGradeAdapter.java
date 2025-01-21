package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistanceActivityGradePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.ActivityGradeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.ActivityGrade;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.mapper.ActivityGradeMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.repository.ActivityGradeCurdRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class ActivityGradeAdapter implements PersistanceActivityGradePort {

    private ActivityGradeCurdRepo crudRepo;

    @Autowired
    private ActivityGradeMapper mapper;

    public ActivityGradeAdapter(ActivityGradeCurdRepo activityGradeCurdRepo, ActivityGradeMapper mapper){
        this.mapper = mapper;
        this.crudRepo = activityGradeCurdRepo;
    }

    @Override
    public List<ActivityGradeDomain> findAll() {
        return mapper.toDomains(crudRepo.findAll());
    }

    @Override
    public ActivityGradeDomain findById(Integer integer) {
        Optional<ActivityGrade> activityGroup = this.crudRepo.findById(integer);
        return activityGroup.map(mapper::toDomain).orElse(null);
    }

    @Override
    public ActivityGradeDomain save(ActivityGradeDomain entity) {
        ActivityGrade activityGroup = mapper.toEntity(entity);
        ActivityGrade savedActivityGroup = this.crudRepo.save(activityGroup);
        return mapper.toDomain(savedActivityGroup);
    }

    @Override
    public ActivityGradeDomain update(Integer integer, ActivityGradeDomain entity) {
        try{
            Optional<ActivityGrade> existingActivityGrade = crudRepo.findById(integer);
            if(existingActivityGrade.isPresent()){
                existingActivityGrade.get().setStudent(entity.getStudent());
                existingActivityGrade.get().setActivity(entity.getActivity());
                existingActivityGrade.get().setScore(entity.getScore());
                existingActivityGrade.get().setComment(entity.getComment());
            }
            return mapper.toDomain(crudRepo.save(existingActivityGrade.get()));
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Relation Activity Grade with ID " + integer + " Not found!");
        }
    }

    @Override
    public HttpStatus delete(Integer integer) {
        return null;
    }
}
