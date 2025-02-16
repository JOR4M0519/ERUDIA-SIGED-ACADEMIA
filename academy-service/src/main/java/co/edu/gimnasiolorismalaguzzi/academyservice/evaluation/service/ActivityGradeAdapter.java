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

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class ActivityGradeAdapter implements PersistanceActivityGradePort {

    private ActivityGradeCrudRepo activityGradeCrudRepo;

    @Autowired
    private ActivityGradeMapper gradeMapper;

    public ActivityGradeAdapter(ActivityGradeCrudRepo activityGradeCrudRepo, ActivityGradeMapper gradeMapper){
        this.gradeMapper = gradeMapper;
        this.activityGradeCrudRepo = activityGradeCrudRepo;
    }

    @Override
    public List<ActivityGradeDomain> findAll() {
        return gradeMapper.toDomains(activityGradeCrudRepo.findAll());
    }

    @Override
    public List<ActivityGradeDomain> getAllActivity_ByPeriodUser(Integer periodId, Integer userId) {
        return gradeMapper.toDomains(activityGradeCrudRepo.findByActivity_Activity_Period_IdAndStudent_Id(periodId,userId));
    }

    @Override
    public List<ActivityGradeDomain> getAllActivity_ByPeriod_Student_Subject(Integer subjectId, Integer periodId, Integer userId) {
        return gradeMapper.toDomains(
                activityGradeCrudRepo.
                        findByActivity_Activity_Subject_IdAndActivity_Activity_Period_IdAndStudent_Id(
                                subjectId,periodId,userId
                        ));
    }
    @Override
    public ActivityGradeDomain findById(Integer integer) {
        Optional<ActivityGrade> activityGroup = this.activityGradeCrudRepo.findById(integer);
        return activityGroup.map(gradeMapper::toDomain).orElse(null);
    }

    @Override
    public ActivityGradeDomain save(ActivityGradeDomain entity) {
        ActivityGrade activityGroup = gradeMapper.toEntity(entity);
        ActivityGrade savedActivityGroup = this.activityGradeCrudRepo.save(activityGroup);
        return gradeMapper.toDomain(savedActivityGroup);
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
            return gradeMapper.toDomain(activityGradeCrudRepo.save(existingActivityGrade.get()));
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Relation Activity Grade with ID " + integer + " Not found!");
        }
    }

    @Override
    public HttpStatus delete(Integer integer) {
        return null;
    }


}
