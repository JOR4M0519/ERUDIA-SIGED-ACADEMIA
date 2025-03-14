package co.edu.gimnasiolorismalaguzzi.academyservice.academic.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectProfessorDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectGroup;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectProfessor;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectGroupMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectProfessorMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectGroupCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectProfessorCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceSubjectGroupPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.UserMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@Slf4j
@PersistenceAdapter
public class SubjectGroupPortAdapter implements PersistenceSubjectGroupPort {

    private final SubjectGroupMapper subjectGroupMapper;




    @Autowired
    private SubjectGroupCrudRepo subjectGroupCrudRepo;


    public SubjectGroupPortAdapter(SubjectGroupMapper subjectGroupMapper) {
        this.subjectGroupMapper = subjectGroupMapper;
    }

    @Override
    public List<SubjectGroupDomain> findAll() {
        return this.subjectGroupMapper.toDomains(this.subjectGroupCrudRepo.findAll());
    }


    @Override
    public List<SubjectGroupDomain> getAllSubjectGroupsByStudentId(Integer studentId, String year) {
        return this.subjectGroupMapper.toDomains(this.subjectGroupCrudRepo.findSubjectGroupsByStudentIdAndAcademicYear(studentId,year));
    }

    /**
     * Obtiene todas las materias de un profesor con base en el id
     * @param id
     * @param year
     * @return lista de materias
     */

    @Override
    public List<SubjectGroupDomain> getAllSubjectByTeacher(Integer id, Integer year) {
        List<SubjectGroup> subjectGroupDomains = this.subjectGroupCrudRepo.getAllSubjectByTeacher(id,year);
        return this.subjectGroupMapper.toDomains(subjectGroupDomains);
    }


    /**
     * Obtinee la lsita de estudiantes de una materia
     * @param groupId
     * @param subjectId
     * @param teacherId
     * @param periodId
     * @return
     */
    @Override
    public List<SubjectGroupDomain> getStudentListByGroupTeacherPeriod(Integer groupId,Integer subjectId,Integer teacherId,Integer periodId) {
        List<SubjectGroup> subjectGroupDomains = subjectGroupCrudRepo.findByGroups_IdAndSubjectProfessor_Subject_IdAndSubjectProfessor_Professor_IdAndAcademicPeriod_Id(groupId,subjectId,teacherId,periodId);
        return this.subjectGroupMapper.toDomains(subjectGroupDomains);
    }

    @Override
    public SubjectGroupDomain findById(Integer integer) {
        Optional<SubjectGroup> subjectGradeDomain = subjectGroupCrudRepo.findById(integer);
        return subjectGradeDomain.map(subjectGroupMapper::toDomain).orElse(null);
    }

    @Override
    public SubjectGroupDomain save(SubjectGroupDomain domain) {
        SubjectGroup subjectGroup = subjectGroupMapper.toEntity(domain);
        SubjectGroup savedSubjectGroup = subjectGroupCrudRepo.save(subjectGroup);
        return this.subjectGroupMapper.toDomain(savedSubjectGroup);
    }

    @Override
    public SubjectGroupDomain update(Integer integer, SubjectGroupDomain domain) {
        try{
            Optional<SubjectGroup> existingSubjectGroupDomain = subjectGroupCrudRepo.findById(integer);
            if(existingSubjectGroupDomain.isPresent()){
                existingSubjectGroupDomain.get().setSubjectProfessor(subjectGroupMapper.toEntity(domain).getSubjectProfessor());
                existingSubjectGroupDomain.get().setGroups(subjectGroupMapper.toEntity(domain).getGroups());
            }
            return subjectGroupMapper.toDomain(subjectGroupCrudRepo.save(existingSubjectGroupDomain.get()));
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Subject Group with id " + integer + " not found");
        }
    }

    @Override
    public HttpStatus delete(Integer integer) {
        try{
            if(this.subjectGroupCrudRepo.existsById(integer)){
                subjectGroupCrudRepo.delete(subjectGroupCrudRepo.getReferenceById(integer));
                return HttpStatus.OK;
            } else {
                throw new AppException("Period ID doesnt exist!", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e){
            throw new AppException("Intern Error!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
