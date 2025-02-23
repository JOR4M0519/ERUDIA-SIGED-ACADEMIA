package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.ActivityGradeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.ActivityGrade;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.mapper.ActivityGradeMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.repository.ActivityGradeCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.persistence.PersistanceActivityGradePort;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class ActivityGradeAdapter implements PersistanceActivityGradePort {
    List<ActivityGrade> storedGrades = new ArrayList<ActivityGrade>();
    ActivityGrade storedGrade;
    private ActivityGradeCrudRepo activityGradeCrudRepo;

    @Autowired
    private ActivityGradeMapper activityGradeMapper;

    public ActivityGradeAdapter(ActivityGradeCrudRepo activityGradeCrudRepo, ActivityGradeMapper gradeMapper){
        this.activityGradeMapper = gradeMapper;
        this.activityGradeCrudRepo = activityGradeCrudRepo;
    }

    @Override
    public List<ActivityGradeDomain> findAll() {
        return activityGradeMapper.toDomains(activityGradeCrudRepo.findAll());
    }

    @Override
    public ActivityGradeDomain findById(Integer integer) {
        Optional<ActivityGrade> activityGroup = this.activityGradeCrudRepo.findById(integer);
        return activityGroup.map(activityGradeMapper::toDomain).orElse(null);
    }

    @Override
    public ActivityGradeDomain save(ActivityGradeDomain entity) {
        ActivityGrade activityGroup = activityGradeMapper.toEntity(entity);
        ActivityGrade savedActivityGroup = this.activityGradeCrudRepo.save(activityGroup);
        return activityGradeMapper.toDomain(savedActivityGroup);
    }

    @Override
    public ActivityGradeDomain update(Integer integer, ActivityGradeDomain entity) {
        try{
            Optional<ActivityGrade> existingActivityGrade = activityGradeCrudRepo.findById(integer);
            if(existingActivityGrade.isPresent()){
                existingActivityGrade.get().setStudent(entity.getStudent());
                existingActivityGrade.get().setActivity(entity.getActivity());
                existingActivityGrade.get().setScore(entity.getScore());
                existingActivityGrade.get().setComment(entity.getComment());
            }
            return activityGradeMapper.toDomain(activityGradeCrudRepo.save(existingActivityGrade.get()));
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Relation Activity Grade with ID " + integer + " Not found!");
        }
    }

    @Override
    public HttpStatus delete(Integer integer) {
        return null;
    }


    @Override
    public ActivityGradeDomain getGradeByActivityId(Integer id, ActivityGradeDomain activityGradeDomain) {
        return this.activityGradeMapper.toDomain(activityGradeCrudRepo.findByActivity_Id(id));
    }

    @Override
    public List<ActivityGradeDomain> gradeActivity(List<ActivityGradeDomain> activityGradeDomain) {
        for (ActivityGradeDomain gradeDomain : activityGradeDomain) {
            storedGrade = this.activityGradeCrudRepo.save(activityGradeMapper.toEntity(gradeDomain));
            storedGrades.add(storedGrade);
        }
        return activityGradeMapper.toDomains(storedGrades);
    }
}

