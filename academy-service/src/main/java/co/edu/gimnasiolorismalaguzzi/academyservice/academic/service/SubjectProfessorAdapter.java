package co.edu.gimnasiolorismalaguzzi.academyservice.academic.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectProfessorDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectProfessor;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectProfessorMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectProfessorCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceSubjectGroupPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceSubjectProfessorPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class SubjectProfessorAdapter implements PersistenceSubjectProfessorPort {

    private SubjectProfessorCrudRepo professorCrudRepo;

    @Autowired
    private SubjectProfessorMapper professorMapper;

    @Autowired
    private PersistenceSubjectGroupPort subjectGroupPortAdapter;

    public SubjectProfessorAdapter(SubjectProfessorCrudRepo subjectProfessorCrudRepo, SubjectProfessorMapper subjectProfessorMapper){
        this.professorCrudRepo = subjectProfessorCrudRepo;
        this.professorMapper = subjectProfessorMapper;
    }

    @Override
    public List<SubjectProfessorDomain> findAll() {
        return this.professorMapper.toDomains(this.professorCrudRepo.findAll());
    }

    @Override
    public List<SubjectProfessorDomain> findBySubjectId (Integer subjectId){
        List<SubjectProfessor>professors = professorCrudRepo.findBySubjectId(subjectId);
        return professorMapper.toDomains(professors);
    }

    @Override
    public SubjectProfessorDomain findById(Integer integer) {
        Optional<SubjectProfessor> subjectProfessor = this.professorCrudRepo.findById(integer);
        return subjectProfessor.map(professorMapper::toDomain).orElse(null);
    }

    @Override
    public SubjectProfessorDomain save(SubjectProfessorDomain entity) {
        SubjectProfessor subjectProfessor = professorMapper.toEntity(entity);
        SubjectProfessor savedSubjectProfessor = this.professorCrudRepo.save(subjectProfessor);
        return professorMapper.toDomain(savedSubjectProfessor);
    }

    @Override
    public SubjectProfessorDomain update(Integer integer, SubjectProfessorDomain entity) {
        try {
            Optional<SubjectProfessor> existingSubjectProfessor = professorCrudRepo.findById(integer);
            if(existingSubjectProfessor.isPresent()){
                existingSubjectProfessor.get().setProfessor(existingSubjectProfessor.get().getProfessor());
                existingSubjectProfessor.get().setSubject(existingSubjectProfessor.get().getSubject());
            }
            return professorMapper.toDomain(professorCrudRepo.save(existingSubjectProfessor.get()));
        } catch (NoSuchElementException e){
            throw new NoSuchElementException("Subject Professor with ID " + integer + "Not found!");
        }
    }

    @Override
    public HttpStatus delete(Integer id) {

        SubjectProfessorDomain professorDomain = findById(id);

        // Verificar si existe la dimension
        if (professorDomain.equals(null)) {
            throw new AppException("La dimension no existe", HttpStatus.NOT_FOUND);
        }

        // Verificar si el saber está siendo utilizado
        boolean usedInSubjectGroup = !subjectGroupPortAdapter.findAllBySubjectProfessor(id).isEmpty();

        // Si está siendo utilizado, lanzar excepción
        if (usedInSubjectGroup) {
            throw new AppException(
                    "No es posible eliminar la asignación del profesor porque está siendo utilizado en las asignaciones de los groups",
                    HttpStatus.CONFLICT);
        }

        professorCrudRepo.deleteById(id);
        return HttpStatus.OK;
    }

}
