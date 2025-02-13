package co.edu.gimnasiolorismalaguzzi.academyservice.academic.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectDimensionDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectDimension;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectDimensionMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectDimensionCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceSubjectDimension;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.mapper.DimensionMapper;
import com.netflix.discovery.converters.Auto;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class SubjectDimensionAdapter implements PersistenceSubjectDimension {

    private final SubjectDimensionCrudRepo subjectDimensionCrudRepo;

    @Autowired
    private final SubjectDimensionMapper subeSubjectDimensionMapper;

    @Autowired
    private final DimensionMapper dimensionMapper;

    @Autowired
    private final SubjectMapper subjectMapper;
    @Autowired
    private SubjectDimensionMapper subjectDimensionMapper;

    public SubjectDimensionAdapter(SubjectDimensionCrudRepo subjectDimensionCrudRepo, SubjectDimensionMapper subeSubjectDimensionMapper, DimensionMapper dimensionMapper, SubjectMapper subjectMapper) {
        this.subjectDimensionCrudRepo = subjectDimensionCrudRepo;
        this.subeSubjectDimensionMapper = subeSubjectDimensionMapper;
        this.dimensionMapper = dimensionMapper;
        this.subjectMapper = subjectMapper;
    }

    @Override
    public List<SubjectDimensionDomain> findAll() {
        return this.subeSubjectDimensionMapper.toDomains(this.subjectDimensionCrudRepo.findAll());
    }

    @Override
    public SubjectDimensionDomain findById(Integer integer) {
        Optional<SubjectDimension> subjectDimensionOptional = this.subjectDimensionCrudRepo.findById(integer);
        return subjectDimensionOptional.map(subeSubjectDimensionMapper::toDomain).orElse(null);
    }

    @Override
    public SubjectDimensionDomain save(SubjectDimensionDomain domain) {
        SubjectDimension subjectDimension = subeSubjectDimensionMapper.toEntity(domain);
        SubjectDimension savedSubjecTDimension = this.subjectDimensionCrudRepo.save(subjectDimension);
        return subeSubjectDimensionMapper.toDomain(savedSubjecTDimension);
    }

    @Override
    public SubjectDimensionDomain update(Integer integer, SubjectDimensionDomain subjectDimensionDomain) {
        try{
            Optional<SubjectDimension> existingSubjectDimension = subjectDimensionCrudRepo.findById(integer);
            if (existingSubjectDimension.isPresent()){
                existingSubjectDimension.get().setDimension(dimensionMapper.toEntity(subjectDimensionDomain.getDimension()));
                existingSubjectDimension.get().setSubject(subjectMapper.toEntity(subjectDimensionDomain.getSubject()));
                return subjectDimensionMapper.toDomain(subjectDimensionCrudRepo.save(existingSubjectDimension.get()));
            }
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("SubjectDimension with ID " + integer + " not found!");
        }
        return null;
    }

    @Override
    public HttpStatus delete(Integer integer) {
        try{
            if(this.subjectDimensionCrudRepo.existsById(integer)){
                this.subjectDimensionCrudRepo.deleteById(integer);
                return HttpStatus.OK;
            } else {
                throw new AppException("Subject with that dimension doesnt exist", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e){
            throw new AppException("INTERN ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
