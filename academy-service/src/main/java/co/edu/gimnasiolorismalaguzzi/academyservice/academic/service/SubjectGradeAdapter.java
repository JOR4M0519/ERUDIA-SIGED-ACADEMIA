package co.edu.gimnasiolorismalaguzzi.academyservice.academic.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.AcademicPeriodDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.RecoveryPeriodDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectGradeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.AcademicPeriod;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.RecoveryPeriod;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.Subject;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectGrade;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.RecoveryPeriodMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectGradeMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.RecoveryPeriodCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectGradeCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceAcademicPeriodPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceSubjectGradePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.UserMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence.PersistenceGroupStudentPort;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@PersistenceAdapter
@Slf4j
public class SubjectGradeAdapter implements PersistenceSubjectGradePort {

    private final SubjectGradeCrudRepo subjectGradeCrudRepo;

    private final RecoveryPeriodCrudRepo recoveryPeriodCrudRepo;

    @Autowired
    private SubjectGradeMapper subjectGradeMapper;

    @Autowired
    private RecoveryPeriodMapper recoveryMapper;

    @Autowired
    private PersistenceAcademicPeriodPort academicPeriodPort;

    @Autowired
    private PersistenceGroupStudentPort persistenceGroupStudentPort;


    @Autowired
    private UserMapper userMapper;

    public SubjectGradeAdapter(SubjectGradeCrudRepo subjectGradeCrudRepo, RecoveryPeriodCrudRepo recoveryPeriodCrudRepo) {
        this.subjectGradeCrudRepo = subjectGradeCrudRepo;
        this.recoveryPeriodCrudRepo = recoveryPeriodCrudRepo;
    }

    @Override
    public List<SubjectGradeDomain> findAll() {
        return this.subjectGradeMapper.toDomains(this.subjectGradeCrudRepo.findAll());
    }

    @Override
    public List<SubjectGradeDomain> findBySubjectPeriodStudentId(int subjectId, int periodId, int studentId) {
        return this.subjectGradeMapper.toDomains(subjectGradeCrudRepo.findByStudentIdSubjectIdAndPeriodId(studentId, subjectId, periodId));
    }

    @Transactional
    @Override
    public void editRecoverStudent(int recoveryId, BigDecimal newScore,String status) {
        try{
            SubjectGrade subjectGradeEntity = findRecoveryById(recoveryId).getSubjectGrade();
            subjectGradeEntity.setTotalScore(newScore);
            subjectGradeEntity.setRecovered(status);
            subjectGradeCrudRepo.save(subjectGradeEntity);
        } catch (Exception e) {
            throw new AppException("Error al editar nota de la materia!",HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Transactional
    @Override
    public void deleteRecoverStudent(int recoverId) {
        try{
            RecoveryPeriod recoveryEntity = findRecoveryById(recoverId);
            SubjectGrade subjectGradeEntity = recoveryEntity.getSubjectGrade();

            subjectGradeEntity.setTotalScore(recoveryEntity.getPreviousScore());
            subjectGradeEntity.setRecovered("N");
            subjectGradeCrudRepo.save(subjectGradeEntity);

            recoveryPeriodCrudRepo.delete(recoveryEntity);
        } catch (Exception e) {
            throw new AppException("Error al eliminar nota de la materia!",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<RecoveryPeriodDomain> findRecoveryListSubjects(int subjectId,String year, int levelId) {

        //Listar los periodos del año
        List<AcademicPeriodDomain> academicPeriodDomains = academicPeriodPort.getPeriodsByYear(year);

        //Listar los recovery_Period por los filtros
        List<RecoveryPeriod> recoveryPeriodsEntityList = new ArrayList<>();
        for (AcademicPeriodDomain periodId :academicPeriodDomains) {
            //Agrega la lista de estudiantes que perdieron en esa materia
            recoveryPeriodsEntityList.addAll(recoveryPeriodCrudRepo.
                    findBySubjectGrade_Subject_IdAndSubjectGrade_Period_Id(subjectId, periodId.getId()));
        }

        // Paso 1: Asignar groupsDomain a todos los elementos
        List<RecoveryPeriodDomain> allDomains = this.recoveryMapper.toDomains(recoveryPeriodsEntityList);
        for (RecoveryPeriodDomain domain : allDomains) {
            GroupsDomain groupsDomain = persistenceGroupStudentPort
                    .getGroupsStudentById(domain.getSubjectGrade().getStudent().getId(), "A")
                    .get(0)
                    .getGroup();
            domain.setGroupsDomain(groupsDomain);
        }

        // Paso 2: Filtrar por nivel
        List<RecoveryPeriodDomain> recoveryPeriodDomains = allDomains.stream()
                .filter(domain -> domain.getGroupsDomain().getLevel().getId() == levelId)
                .collect(Collectors.toList());


        /*List<RecoveryPeriodDomain> recoveryPeriodDomains = this.recoveryMapper.toDomains(recoveryPeriodsEntityList);

        for (RecoveryPeriodDomain recoveryStudent: recoveryPeriodDomains){
            GroupsDomain groupsDomain=  persistenceGroupStudentPort.
                    getGroupsStudentById(recoveryStudent.getSubjectGrade().getStudent().getId(),"A").get(0).getGroup();
            int levelIdStudent = groupsDomain.getLevel().getId();
            if(levelId!=levelIdStudent){
                recoveryPeriodDomains.remove(recoveryStudent);
            }
        }*/



        return recoveryPeriodDomains;
    }


    @Override
    public SubjectGradeDomain findById(Integer integer) {
        Optional<SubjectGrade> subjectGradeOptional = this.subjectGradeCrudRepo.findById(integer);
        return subjectGradeOptional.map(subjectGradeMapper::toDomain).orElse(null);
    }

    public RecoveryPeriod findRecoveryById(Integer integer) {
        Optional<RecoveryPeriod> subjectGradeOptional = this.recoveryPeriodCrudRepo.findById(integer);
        return subjectGradeOptional.orElse(null);
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
                existingSubjectGrade.get().setStudent(userMapper.toEntity(entity.getStudent()));
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

    @Transactional
    @Override
    public SubjectGradeDomain saveOrUpdateSubjectGrade(Integer studentId, Integer subjectId, Integer periodId, BigDecimal finalScore) {
        // Buscar si ya existe un registro para este estudiante, materia y periodo
        List<SubjectGradeDomain> existingGrades = findBySubjectPeriodStudentId(subjectId,periodId,studentId);

        if (!existingGrades.isEmpty()) {
            // Si existe, actualizar el registro
            SubjectGrade existingGrade = subjectGradeMapper.toEntity(existingGrades.get(0));
            existingGrade.setTotalScore(finalScore);

            // Guardar y retornar el objeto actualizado
            return subjectGradeMapper.toDomain(subjectGradeCrudRepo.save(existingGrade));
        } else {
            // Si no existe, crear un nuevo registro
            SubjectGrade newGrade = SubjectGrade.builder()
                    .subject(Subject.builder().id(subjectId).build())
                    .student(User.builder().id(studentId).build())
                    .period(AcademicPeriod.builder().id(periodId).build())
                    .totalScore(finalScore)
                    .recovered("I") // Por defecto no es recuperación
                            .build();

            // Guardar y retornar el nuevo objeto
            return subjectGradeMapper.toDomain(subjectGradeCrudRepo.save(newGrade));
        }
    }


}
