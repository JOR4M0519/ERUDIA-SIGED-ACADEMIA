package co.edu.gimnasiolorismalaguzzi.academyservice.academic.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectGradeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectGrade;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectGradeMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectGradeCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class SubjectGradeAdapter implements PersistenceSubjectGradePort{

    private final SubjectGradeCrudRepo subjectGradeCrudRepo;

    @Autowired
    SubjectGradeMapper subjectGradeMapper;
    @Qualifier("loadBalancedRestTemplateInitializerDeprecated")
    @Autowired
    private SmartInitializingSingleton loadBalancedRestTemplateInitializerDeprecated;

    public SubjectGradeAdapter(SubjectGradeCrudRepo subjectGradeCrudRepo) {
        this.subjectGradeCrudRepo = subjectGradeCrudRepo;
    }

    @Override
    public List<SubjectGradeDomain> findAll() {
        return this.subjectGradeMapper.toDomains(this.subjectGradeCrudRepo.findAll());
    }

    @Override
    public SubjectGradeDomain findById(Integer integer) {
        Optional<SubjectGrade> subjectGradeOptional = this.subjectGradeCrudRepo.findById(integer);
        return subjectGradeOptional.map(subjectGradeMapper::toDomain).orElse(null);
    }

    @Override
    public SubjectGradeDomain save(SubjectGradeDomain entity) {
        SubjectGrade subjectGrade = subjectGradeMapper.toEntity(entity);
        SubjectGrade savedSubjectGrade = this.subjectGradeCrudRepo.save(subjectGrade);
        return subjectGradeMapper.toDomain(savedSubjectGrade);
    }

    @Override
    public SubjectGradeDomain update(Integer integer, SubjectGradeDomain entity) {
        try{
            Optional<SubjectGrade> existingSubjectGrade = subjectGradeCrudRepo.findById(integer);
            if(existingSubjectGrade.isPresent()){
                existingSubjectGrade.get().setRecovered(entity.getRecovered());
                existingSubjectGrade.get().setSubject(entity.getSubject());
                existingSubjectGrade.get().setStudent(entity.getStudent());
                existingSubjectGrade.get().setTotalScore(entity.getTotalScore());
                existingSubjectGrade.get().setPeriod(entity.getPeriod());
            }
            return subjectGradeMapper.toDomain(subjectGradeCrudRepo.save(existingSubjectGrade.get()));
        } catch (Exception e) {
            throw new EntityNotFoundException("SubjectGrade with id " + integer + "not found!");
        }
    }

    @Override
    public HttpStatus delete(Integer integer) {
        //No hay necesidad de borrar una nota
        return HttpStatus.I_AM_A_TEAPOT;
    }

    @Override
    public void recoverStudent(int idStudent, int idSubject, int idPeriod, BigDecimal newScore) {
        subjectGradeCrudRepo.recoverStudent(newScore,idSubject,idStudent, idPeriod);
    }
}
