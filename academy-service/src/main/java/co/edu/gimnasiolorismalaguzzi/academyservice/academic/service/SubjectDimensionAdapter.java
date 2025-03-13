package co.edu.gimnasiolorismalaguzzi.academyservice.academic.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectDimensionDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectGradeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectDimension;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectGrade;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectDimensionMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectDimensionCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectGradeCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceSubjectDimensionPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class SubjectDimensionAdapter implements PersistenceSubjectDimensionPort {

    private final SubjectDimensionCrudRepo subjectDimensionCrudRepo;

    @Autowired
    SubjectDimensionMapper subjectDimensionMapper;

    public SubjectDimensionAdapter(SubjectDimensionCrudRepo subjectDimensionCrudRepo) {
        this.subjectDimensionCrudRepo = subjectDimensionCrudRepo;
    }
    @Override
    public List<SubjectDimensionDomain> findAll() {
        return this.subjectDimensionMapper.toDomains(this.subjectDimensionCrudRepo.findAll());
    }

    @Override
    public SubjectDimensionDomain findById(Integer integer) {
        Optional<SubjectDimension> subjectGradeOptional = this.subjectDimensionCrudRepo.findById(integer);
        return subjectGradeOptional.map(subjectDimensionMapper::toDomain).orElse(null);
    }

    @Override
    public SubjectDimensionDomain save(SubjectDimensionDomain entity) {
        SubjectDimension subjectGrade = subjectDimensionMapper.toEntity(entity);
        SubjectDimension savedSubjectGrade = this.subjectDimensionCrudRepo.save(subjectGrade);
        return subjectDimensionMapper.toDomain(savedSubjectGrade);
    }

    @Override
    public SubjectDimensionDomain update(Integer integer, SubjectDimensionDomain entity) {
        try{
            Optional<SubjectDimension> existingSubjectGrade = subjectDimensionCrudRepo.findById(integer);
            if(existingSubjectGrade.isPresent()){
                existingSubjectGrade.get().setSubject(entity.getSubject());
                existingSubjectGrade.get().setDimension(entity.getDimension());
            }
            return subjectDimensionMapper.toDomain(subjectDimensionCrudRepo.save(existingSubjectGrade.get()));
        } catch (Exception e) {
            throw new EntityNotFoundException("SubjectGrade with id " + integer + "not found!");
        }
    }

    @Override
    public HttpStatus delete(Integer integer) {
        //No hay necesidad de borrar una nota
        return HttpStatus.I_AM_A_TEAPOT;
    }



}
