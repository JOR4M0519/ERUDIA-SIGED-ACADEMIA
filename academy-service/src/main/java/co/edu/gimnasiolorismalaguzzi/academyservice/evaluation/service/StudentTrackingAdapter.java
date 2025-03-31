package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.UserMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.StudentTracking;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.persistence.PersistenceStudentTrackingPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.StudentTrackingDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.mapper.StudentTrackingMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.repository.StudentTrackingCrudRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class StudentTrackingAdapter implements PersistenceStudentTrackingPort {

    private final StudentTrackingCrudRepo studentTrackingCrudRepo;



    @Autowired
    private StudentTrackingMapper studentTrackingMapper;

    @Autowired
    private UserMapper userMapper;

    public StudentTrackingAdapter(StudentTrackingCrudRepo studentTrackingCrudRepo) {
        this.studentTrackingCrudRepo = studentTrackingCrudRepo;
    }

    @Override
    public List<StudentTrackingDomain> findAll() {
        return studentTrackingMapper.toDomains(this.studentTrackingCrudRepo.findAll());
    }

    @Override
    public StudentTrackingDomain findById(Integer integer) {
        Optional<StudentTracking> studentTracking = this.studentTrackingCrudRepo.findById(integer);
        return studentTracking.map(studentTrackingMapper::toDomain).orElse(null);
    }

    @Override
    public StudentTrackingDomain save(StudentTrackingDomain studentTrackingDomain) {
        StudentTracking studentTracking = studentTrackingMapper.toEntity(studentTrackingDomain);
        StudentTracking savedTracking = this.studentTrackingCrudRepo.save(studentTracking);
        return studentTrackingMapper.toDomain(savedTracking);
    }

    @Override
    public StudentTrackingDomain update(Integer integer, StudentTrackingDomain studentTrackingDomain) {
        try{
            Optional<StudentTracking> existingTracking = studentTrackingCrudRepo.findById(integer);
            if(existingTracking.isPresent()){
                existingTracking.get().setStudent(userMapper.toEntity(studentTrackingDomain.getStudent()));
                existingTracking.get().setProfessor(userMapper.toEntity(studentTrackingDomain.getProfessor()));
                existingTracking.get().setPeriod(studentTrackingMapper.toEntity(studentTrackingDomain).getPeriod());
                existingTracking.get().setTrackingType(studentTrackingMapper.toEntity(studentTrackingDomain).getTrackingType());
                existingTracking.get().setSituation(studentTrackingDomain.getSituation());
                existingTracking.get().setCompromise(studentTrackingDomain.getCompromise());
                existingTracking.get().setFollowUp(studentTrackingDomain.getFollowUp());
                existingTracking.get().setStatus("A");
            }
            return studentTrackingMapper.toDomain(studentTrackingCrudRepo.save(existingTracking.get()));
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Tracking with ID " + integer + "not found!");
        }
    }

    @Override
    public HttpStatus delete(Integer integer) {
        try{
            if(this.studentTrackingCrudRepo.existsById(integer)){
                studentTrackingCrudRepo.updateStatusById("I", integer);
                return HttpStatus.OK;
            } else {
                throw new AppException("Tracking with ID does exist! ", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e){
            throw new AppException("Interal Error! ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<StudentTrackingDomain> getTrackingByStudentId(Integer id) {
        return studentTrackingMapper.toDomains(studentTrackingCrudRepo.findByStudent_Id(id));
    }

    @Override
    public List<StudentTrackingDomain> getTrackingListStudentsCreatedByteacher(Integer teacherId, String status) {
        return studentTrackingMapper.toDomains(studentTrackingCrudRepo.findByProfessor_IdAndStatusNotLike(teacherId,status));
    }
}
