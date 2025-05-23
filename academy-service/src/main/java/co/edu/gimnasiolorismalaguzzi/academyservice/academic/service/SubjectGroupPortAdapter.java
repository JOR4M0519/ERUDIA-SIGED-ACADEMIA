package co.edu.gimnasiolorismalaguzzi.academyservice.academic.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectScheduleDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectGroup;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectGroupMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectGroupCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceSubjectGroupPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceSubjectSchedulePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupStudentsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence.PersistenceGroupStudentPort;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@PersistenceAdapter
public class SubjectGroupPortAdapter implements PersistenceSubjectGroupPort {

    private final PersistenceSubjectSchedulePort subjectSchedulePort;
    private final SubjectGroupMapper subjectGroupMapper;

    private final PersistenceGroupStudentPort groupStudentPort;

    @Autowired
    private SubjectGroupCrudRepo subjectGroupCrudRepo;


    public SubjectGroupPortAdapter(PersistenceSubjectSchedulePort subjectSchedulePort, SubjectGroupMapper subjectGroupMapper, PersistenceGroupStudentPort groupStudentPort) {
        this.subjectSchedulePort = subjectSchedulePort;
        this.subjectGroupMapper = subjectGroupMapper;
        this.groupStudentPort = groupStudentPort;
    }

    @Override
    public List<SubjectGroupDomain> findAll() {
        return this.subjectGroupMapper.toDomains(this.subjectGroupCrudRepo.findAll());
    }

    @Override
    public List<SubjectGroupDomain> findAllBySubjectProfessor(Integer subjectProfessorId) {
        return this.subjectGroupMapper.toDomains(subjectGroupCrudRepo.findBySubjectProfessor_Id(subjectProfessorId));
    }

    @Override
    public List<SubjectGroupDomain> getAllSubjectGroupsByStudentId(Integer studentId, String year) {
        return this.subjectGroupMapper.toDomains(this.subjectGroupCrudRepo.findSubjectGroupsByStudentIdAndAcademicYear(studentId,year));
    }

    @Override
    public List<SubjectGroupDomain> getAllSubjectGroupsByStudentIdPeriodId(Integer studentId, Integer periodId) {
        return this.subjectGroupMapper.toDomains(this.subjectGroupCrudRepo.findSubjectGroupsByStudentIdAndPeriodId(studentId,periodId));
    }

    @Override
    public List<SubjectGroupDomain> getAllSubjectGroupsByStudentIdByPeriod(Integer studentId, Integer periodId) {
        return this.subjectGroupMapper.toDomains(this.subjectGroupCrudRepo.findSubjectGroupsByStudentIdAndPeriodId(studentId,periodId));
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
    public List<SubjectGroupDomain> getAllSubjectGRoupsByPeriodAndLevel(Integer periodId, Integer levelId) {
        List<SubjectGroup> subjectGroupEntity = subjectGroupCrudRepo.
                findByAcademicPeriod_IdAndGroups_StatusAndGroups_Level_Id(periodId, "A", levelId);
        return this.subjectGroupMapper.toDomains(subjectGroupEntity);
    }

    @Override
    public List<GroupStudentsDomain> getGroupsStudentsByPeriodIdAndSubjectProfessorId(Integer periodId, Integer subjectId) {
        List<SubjectGroupDomain> subjectGroupDomainList = subjectGroupMapper.toDomains(
                subjectGroupCrudRepo.findByAcademicPeriod_IdAndSubjectProfessor_Subject_Id(periodId,subjectId)
        );

        String STATUS_NOT_LIKE = "I";

        return new ArrayList<>(groupStudentPort.getGroupsStudentByGroupId(
                subjectGroupDomainList.getFirst().getGroups().getId(), STATUS_NOT_LIKE));
    }

    @Override
    public List<GroupStudentsDomain> getGroupsStudentsByPeriodIdAndSubjectProfessorIdAndGroupId(
            Integer periodId, Integer subjectId,Integer groupId) {
        SubjectGroupDomain subjectGroupDomainList = subjectGroupMapper.toDomain(
                subjectGroupCrudRepo.findByAcademicPeriod_IdAndSubjectProfessor_IdAndGroups_Id(periodId,subjectId,groupId)
        );

        String STATUS_NOT_LIKE = "I";

        return new ArrayList<>(groupStudentPort.getGroupsStudentByGroupId(
                subjectGroupDomainList.getGroups().getId(), STATUS_NOT_LIKE));
    }


    @Override
    public List<SubjectGroupDomain> getSubjectsByGroupIdAndPeriodId(Integer groupId, Integer periodId) {
        return this.subjectGroupMapper.toDomains(subjectGroupCrudRepo.findByGroups_IdAndAcademicPeriod_Id(groupId,periodId));
    }


    @Override
    public SubjectGroupDomain findById(Integer integer) {
        Optional<SubjectGroup> subjectGradeDomain = subjectGroupCrudRepo.findById(integer);
        return subjectGradeDomain.map(subjectGroupMapper::toDomain).orElse(null);
    }

    @Transactional
    @Override
    public SubjectGroupDomain save(SubjectGroupDomain domain) {
        try {
            // Save the Subject Group
            SubjectGroup subjectGroup = subjectGroupMapper.toEntity(domain);
            SubjectGroup savedSubjectGroup = subjectGroupCrudRepo.save(subjectGroup);

            // Create a minimal SubjectSchedule with just the SubjectGroup foreign key
            SubjectScheduleDomain subjectScheduleDomain = SubjectScheduleDomain.builder()
                    .subjectGroup(subjectGroupMapper.toDomain(savedSubjectGroup))
                    .dayOfWeek("")
                    .status("A")
                    .build();

            // Save the schedule record
            subjectSchedulePort.save(subjectScheduleDomain);

            return this.subjectGroupMapper.toDomain(savedSubjectGroup);
        } catch (Exception e) {
            log.error("Error saving SubjectGroup and SubjectSchedule: {}", e.getMessage(), e);
            // The @Transactional annotation will automatically rollback if an exception occurs
            throw new AppException("Error creating subject group and schedule record: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
