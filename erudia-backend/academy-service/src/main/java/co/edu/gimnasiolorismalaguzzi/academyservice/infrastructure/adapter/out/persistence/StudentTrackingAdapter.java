package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceStudentTrackingPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.StudentTrackingDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.StudentTracking;
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

    private final StudentTrackingCrudRepo crudRepo;

    @Autowired
    private StudentTrackingMapper mapper;

    public StudentTrackingAdapter(StudentTrackingCrudRepo crudRepo) {
        this.crudRepo = crudRepo;
    }

    @Override
    public List<StudentTrackingDomain> findAll() {
        return mapper.toDomains(this.crudRepo.findAll());
    }

    @Override
    public StudentTrackingDomain findById(Integer integer) {
        Optional<StudentTracking> studentTracking = this.crudRepo.findById(integer);
        return studentTracking.map(mapper::toDomain).orElse(null);
    }

    @Override
    public StudentTrackingDomain save(StudentTrackingDomain studentTrackingDomain) {
        StudentTracking studentTracking = mapper.toEntity(studentTrackingDomain);
        StudentTracking savedTracking = this.crudRepo.save(studentTracking);
        return mapper.toDomain(savedTracking);
    }

    @Override
    public StudentTrackingDomain update(Integer integer, StudentTrackingDomain studentTrackingDomain) {
        try{
            Optional<StudentTracking> existingTracking = crudRepo.findById(integer);
            if(existingTracking.isPresent()){
                existingTracking.get().setStudent(studentTrackingDomain.getStudent());
                existingTracking.get().setProfessor(studentTrackingDomain.getProfessor());
                existingTracking.get().setSituation(studentTrackingDomain.getSituation());
                existingTracking.get().setCompromise(studentTrackingDomain.getCompromise());
                existingTracking.get().setFollowUp(studentTrackingDomain.getFollowUp());
                existingTracking.get().setStatus("A");
            }
            return mapper.toDomain(crudRepo.save(existingTracking.get()));
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Tracking with ID " + integer + "not found!");
        }
    }

    @Override
    public HttpStatus delete(Integer integer) {
        try{
            if(this.crudRepo.existsById(integer)){
                crudRepo.updateStatusById("I", integer);
                return HttpStatus.OK;
            } else {
                throw new AppException("Tracking with ID does exist! ", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e){
            throw new AppException("Interal Error! ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
