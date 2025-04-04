package co.edu.gimnasiolorismalaguzzi.academyservice.academic.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceSubjectSchedulePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectScheduleDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectSchedule;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectScheduleMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectScheduleCrudRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class SubjectScheduleAdapter implements PersistenceSubjectSchedulePort {

    private final SubjectScheduleCrudRepo subjectScheduleCrudRepo;

    @Autowired
    private final SubjectScheduleMapper subjectScheduleMapper;

    public SubjectScheduleAdapter(SubjectScheduleCrudRepo subjectScheduleCrudRepo, SubjectScheduleMapper subjectScheduleMapper) {
        this.subjectScheduleCrudRepo = subjectScheduleCrudRepo;
        this.subjectScheduleMapper = subjectScheduleMapper;
    }


    @Override
    public List<SubjectScheduleDomain> findAll() {
        return this.subjectScheduleMapper.toDomains(subjectScheduleCrudRepo.findAll());
    }

    @Override
    public SubjectScheduleDomain findById(Integer integer) {
        Optional<SubjectSchedule> subjectSchedule = this.subjectScheduleCrudRepo.findById(integer);
        return subjectSchedule.map(subjectScheduleMapper::toDomain).orElse(null);
    }

    @Override
    public SubjectScheduleDomain save(SubjectScheduleDomain subjectScheduleDomain) {
        SubjectSchedule subjectSchedule = subjectScheduleMapper.toEntity(subjectScheduleDomain);
        SubjectSchedule savedSchedule = this.subjectScheduleCrudRepo.save(subjectSchedule);
        return this.subjectScheduleMapper.toDomain(savedSchedule);
    }

    @Override
    public SubjectScheduleDomain update(Integer integer, SubjectScheduleDomain subjectScheduleDomain) {
        try{
            Optional<SubjectSchedule> existingSchedule = subjectScheduleCrudRepo.findById(integer);
            if(existingSchedule.isPresent()){
                existingSchedule.get().setSubjectGroup(subjectScheduleMapper.toEntity(subjectScheduleDomain).getSubjectGroup());
                existingSchedule.get().setDayOfWeek(subjectScheduleDomain.getDayOfWeek());
                existingSchedule.get().setStartTime(subjectScheduleDomain.getStartTime());
                existingSchedule.get().setEndTime(subjectScheduleDomain.getEndTime());
                existingSchedule.get().setStatus("A");
            }
            return subjectScheduleMapper.toDomain(subjectScheduleCrudRepo.save(existingSchedule.get()));
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Subject Schedule with id " + integer + " Not found!");
        }
    }

    @Override
    public HttpStatus delete(Integer integer) {
        try{
            if(this.subjectScheduleCrudRepo.existsById(integer)){
                subjectScheduleCrudRepo.updateStatusById("I", integer);
                return HttpStatus.OK;
            } else {
                throw new AppException("Subject schedule doest exist!", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e){
            throw new AppException("Internal Error! ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<SubjectScheduleDomain> getScheduleByGroupStudentId(Integer id) {
        return this.subjectScheduleMapper.toDomains(subjectScheduleCrudRepo.findBySubjectGroup_Groups_Id(id));
    }
}
