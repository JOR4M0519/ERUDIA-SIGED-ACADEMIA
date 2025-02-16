package co.edu.gimnasiolorismalaguzzi.academyservice.academic.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectProfessorDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectProfessor;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceSubjectPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.Subject;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectCrudRepo;
//import co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository.UserCrudRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@PersistenceAdapter
@Slf4j
public class SubjectAdapter implements PersistenceSubjectPort {

    private final SubjectCrudRepo subjectCrudRepo; // Repositorio JPA
    private final SubjectProfessorAdapter subjectProfessorAdapter;

    @Autowired
    private SubjectMapper subjectMapper;

    public SubjectAdapter(SubjectCrudRepo subjectCrudRepo, SubjectProfessorAdapter subjectProfessorAdapter) {
        this.subjectCrudRepo = subjectCrudRepo;
        this.subjectProfessorAdapter = subjectProfessorAdapter;
    }

    @Override
    public List<SubjectDomain> findAll() {
        List<SubjectDomain> subjectDomainList = this.subjectMapper.toDomains(this.subjectCrudRepo.findAll());

        for(SubjectDomain subjectDomain : subjectDomainList) {
            List<SubjectProfessorDomain> professorDomains = subjectProfessorAdapter.findBySubjectId(subjectDomain.getId());

            List<User> professors = professorDomains.stream()
                    .map(SubjectProfessorDomain::getProfessor)
                    .toList();
            subjectDomain.setProfessor(professors);
        }
        return subjectDomainList;
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
        SubjectDomain savedSubjectDomain = subjectMapper.toDomain(savedSubject);

        if (subjectDomain.getProfessor() != null && !subjectDomain.getProfessor().isEmpty()) {
            for (User professor : subjectDomain.getProfessor()) {
                SubjectProfessorDomain professorDomain = new SubjectProfessorDomain();
                professorDomain.setSubject(savedSubject);
                professorDomain.setProfessor(professor);
                subjectProfessorAdapter.save(professorDomain);
            }
        } else {
            log.error("No se proporcionaron profesores para la asignatura: {}", savedSubjectDomain.getSubjectName());
        }

        return savedSubjectDomain;
    }

    @Override
    public SubjectDomain update(Integer id, SubjectDomain subjectDomain) {
        try {
            Optional<Subject> existingSubjectOptional = subjectCrudRepo.findById(id);
            if (!existingSubjectOptional.isPresent()) {
                throw new EntityNotFoundException("Subject with ID " + id + " not found");
            }

            Subject existingSubject = existingSubjectOptional.get();

            existingSubject.setSubjectName(subjectDomain.getSubjectName());
            existingSubject.setStatus(subjectDomain.getStatus());

            if (subjectDomain.getProfessor() != null) {
                Set<Integer> newProfessorIds = subjectDomain.getProfessor().stream()
                        .map(User::getId)
                        .collect(Collectors.toSet());

                List<SubjectProfessorDomain> existingRelations = subjectProfessorAdapter.findBySubjectId(id);

                existingRelations.stream()
                        .filter(relation -> !newProfessorIds.contains(relation.getProfessor().getId()))
                        .forEach(relation -> subjectProfessorAdapter.delete(relation.getId()));

                newProfessorIds.forEach(professorId -> {
                    if (existingRelations.stream().noneMatch(r -> r.getProfessor().getId().equals(professorId))) {
                        SubjectProfessorDomain newRelation = new SubjectProfessorDomain();
                        newRelation.setSubject(existingSubject);
                        newRelation.setProfessor(User.builder().id(professorId).build());
                        subjectProfessorAdapter.save(newRelation);
                    }
                });
            }

            Subject updatedSubject = subjectCrudRepo.save(existingSubject);
            SubjectDomain updatedSubjectDomain = subjectMapper.toDomain(updatedSubject);

            List<SubjectProfessorDomain> updatedRelations = subjectProfessorAdapter.findBySubjectId(id);
            Set<SubjectProfessor> subjectProfessors = updatedRelations.stream()
                    .map(spDomain -> {
                        SubjectProfessor sp = new SubjectProfessor();
                        sp.setId(spDomain.getId());
                        sp.setSubject(updatedSubject);
                        sp.setProfessor(spDomain.getProfessor());
                        return sp;
                    })
                    .collect(Collectors.toSet());

            updatedSubject.setSubjectProfessors(subjectProfessors);

            return updatedSubjectDomain;

        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Subject with ID " + id + " not found");
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
                throw new AppException("Subject doesnt exist", HttpStatus.NOT_FOUND);
            }
        }catch(Exception e){
            throw new AppException("INTERN ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
