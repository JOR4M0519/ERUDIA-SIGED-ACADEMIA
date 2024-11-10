package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceSubjectPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceUserPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.SubjectDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.Subject;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.UserDetail;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.mapper.SubjectMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.repository.SubjectCrudRepo;
//import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.repository.UserCrudRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class SubjectAdapter implements PersistenceSubjectPort {

    private final SubjectCrudRepo subjectCrudRepo; // Repositorio JPA

    @Autowired
    private SubjectMapper subjectMapper;

    public SubjectAdapter(SubjectCrudRepo subjectCrudRepo) {
        this.subjectCrudRepo = subjectCrudRepo;
    }

    @Override
    public List<SubjectDomain> findAll() {
        return this.subjectMapper.toDomains(this.subjectCrudRepo.findAll());
    }

    @Override
    public SubjectDomain findById(Integer id) {
        Optional<Subject> subjectOptional = this.subjectCrudRepo.findById(id);
        return subjectOptional.map(subjectMapper::toDomain).orElse(null);
    }

    @Override
    public SubjectDomain save(SubjectDomain subjectDomain) {

        subjectDomain.setSubjectName(subjectDomain.getSubjectName());
        subjectDomain.setStatus("A");

        Subject subject = subjectMapper.toEntity(subjectDomain);
        Subject savedSubject = this.subjectCrudRepo.save(subject);
        return subjectMapper.toDomain(savedSubject);
    }

    @Override
    public SubjectDomain update(Integer id, SubjectDomain subjectDomain) {

        try{
            Optional<Subject> existingSubject = subjectCrudRepo.findById(id);

            if (existingSubject.isPresent()){
                existingSubject.get().setSubjectName(subjectDomain.getSubjectName());
                existingSubject.get().setStatus(subjectDomain.getStatus());
            }

            return subjectMapper.toDomain(subjectCrudRepo.save(existingSubject.get()));

        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException("UserDetail with ID " + id + " not found");
        }

    }

    @Override
    public HttpStatus delete(Integer integer) {

        //Check if there are  some register in other table
        //table
         try{
            if (this.subjectCrudRepo.existsById(integer)) {
                subjectCrudRepo.updateStatusById("I",integer);
                return HttpStatus.OK;
            } else {
                throw new AppException("User ID doesnt exist", HttpStatus.NOT_FOUND);
            }
        }catch(Exception e){
            throw new AppException("INTERN ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
