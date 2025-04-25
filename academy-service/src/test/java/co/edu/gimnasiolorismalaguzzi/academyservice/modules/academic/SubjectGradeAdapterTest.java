package co.edu.gimnasiolorismalaguzzi.academyservice.modules.academic;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.RecoveryPeriodDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectGradeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.RecoveryPeriod;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.Subject;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectGrade;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.AcademicPeriod;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.RecoveryPeriodMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectGradeMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.RecoveryPeriodCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectGradeCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.SubjectGradeAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceAcademicPeriodPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.UserMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence.PersistenceGroupStudentPort;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubjectGradeAdapterTest {

    @Mock private SubjectGradeCrudRepo subjectGradeCrudRepo;
    @Mock private RecoveryPeriodCrudRepo recoveryPeriodCrudRepo;
    @Mock private SubjectGradeMapper subjectGradeMapper;
    @Mock private RecoveryPeriodMapper recoveryMapper;
    @Mock private PersistenceAcademicPeriodPort academicPeriodPort;
    @Mock private PersistenceGroupStudentPort persistenceGroupStudentPort;
    @Mock private UserMapper userMapper;

    private SubjectGradeAdapter adapter;

    private Subject subject;
    private User student;
    private UserDomain student2;
    private AcademicPeriod period;
    private SubjectGrade subjectGrade;
    private SubjectGradeDomain subjectGradeDomain;
    private List<SubjectGrade> subjectGrades;
    private List<SubjectGradeDomain> subjectGradeDomains;

    @BeforeEach
    void setUp() {
        adapter = new SubjectGradeAdapter(subjectGradeCrudRepo, recoveryPeriodCrudRepo);
        // Inyectar todos los mappers y puertos
        ReflectionTestUtils.setField(adapter, "subjectGradeMapper", subjectGradeMapper);
        ReflectionTestUtils.setField(adapter, "recoveryMapper", recoveryMapper);
        ReflectionTestUtils.setField(adapter, "academicPeriodPort", academicPeriodPort);
        ReflectionTestUtils.setField(adapter, "persistenceGroupStudentPort", persistenceGroupStudentPort);
        ReflectionTestUtils.setField(adapter, "userMapper", userMapper);

        subject = Subject.builder().id(1).subjectName("Math").status("A").build();
        student = User.builder().id(1).firstName("Jane").lastName("Doe").build();
        student2 = UserDomain.builder().id(1).firstName("Jane").lastName("Doe").build();

        period = AcademicPeriod.builder().id(1).name("P1").build();

        subjectGrade = new SubjectGrade();
        subjectGrade.setId(1);
        subjectGrade.setSubject(subject);
        subjectGrade.setStudent(student);
        subjectGrade.setPeriod(period);
        subjectGrade.setTotalScore(new BigDecimal("4.5"));
        subjectGrade.setRecovered("N");

        subjectGradeDomain = SubjectGradeDomain.builder()
                .id(1)
                .subject(subject)
                .student(student2)
                .period(period)
                .totalScore(new BigDecimal("4.5"))
                .recovered("N")
                .build();

        subjectGrades = Collections.singletonList(subjectGrade);
        subjectGradeDomains = Collections.singletonList(subjectGradeDomain);
    }

    //-------------- findAll --------------

    @Test @DisplayName("findAll retorna todos los registros")
    void findAll_success() {
        when(subjectGradeCrudRepo.findAll()).thenReturn(subjectGrades);
        when(subjectGradeMapper.toDomains(subjectGrades)).thenReturn(subjectGradeDomains);

        List<SubjectGradeDomain> res = adapter.findAll();

        assertEquals(subjectGradeDomains, res);
        verify(subjectGradeCrudRepo).findAll();
    }

    @Test @DisplayName("findAll propaga RuntimeException si el repo falla")
    void findAll_failure() {
        when(subjectGradeCrudRepo.findAll()).thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> adapter.findAll());
    }

    //------ findBySubjectPeriodStudentId ------

    @Test @DisplayName("findBySubjectPeriodStudentId retorna lista cuando hay datos")
    void findBySubjectPeriodStudentId_success() {
        when(subjectGradeCrudRepo.findByStudentIdSubjectIdAndPeriodId(1, 1, 1))
                .thenReturn(subjectGrades);
        when(subjectGradeMapper.toDomains(subjectGrades)).thenReturn(subjectGradeDomains);

        List<SubjectGradeDomain> res = adapter.findBySubjectPeriodStudentId(1,1,1);
        assertEquals(subjectGradeDomains, res);
        verify(subjectGradeCrudRepo).findByStudentIdSubjectIdAndPeriodId(1,1,1);
    }

    @Test @DisplayName("findBySubjectPeriodStudentId retorna vacío si no hay datos")
    void findBySubjectPeriodStudentId_empty() {
        when(subjectGradeCrudRepo.findByStudentIdSubjectIdAndPeriodId(2,2,2))
                .thenReturn(Collections.emptyList());
        when(subjectGradeMapper.toDomains(Collections.emptyList()))
                .thenReturn(Collections.emptyList());

        List<SubjectGradeDomain> res = adapter.findBySubjectPeriodStudentId(2,2,2);
        assertTrue(res.isEmpty());
    }

    //------------- editRecoverStudent -------------

    @Test @DisplayName("editRecoverStudent actualiza nota y recovered correctamente")
    void editRecoverStudent_success() {
        int recId = 5;
        BigDecimal newScore = new BigDecimal("3.2");
        String status = "Y";

        RecoveryPeriod rec = new RecoveryPeriod();
        rec.setId(recId);
        rec.setSubjectGrade(subjectGrade);
        rec.setPreviousScore(new BigDecimal("2.0"));

        when(recoveryPeriodCrudRepo.findById(recId)).thenReturn(Optional.of(rec));
        when(subjectGradeCrudRepo.save(subjectGrade)).thenReturn(subjectGrade);

        adapter.editRecoverStudent(recId, newScore, status);

        assertEquals(newScore, subjectGrade.getTotalScore());
        assertEquals(status, subjectGrade.getRecovered());
        verify(subjectGradeCrudRepo).save(subjectGrade);
    }

    @Test @DisplayName("editRecoverStudent lanza AppException si recovery no existe")
    void editRecoverStudent_notFound() {
        when(recoveryPeriodCrudRepo.findById(9)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class,
                () -> adapter.editRecoverStudent(9, BigDecimal.ONE, "Y"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getCode());
        assertEquals("Error al editar nota de la materia!", ex.getMessage());
    }

    @Test @DisplayName("editRecoverStudent lanza AppException si save falla")
    void editRecoverStudent_saveFailure() {
        int recId = 6;
        RecoveryPeriod rec = new RecoveryPeriod();
        rec.setId(recId);
        rec.setSubjectGrade(subjectGrade);
        rec.setPreviousScore(new BigDecimal("2.0"));

        when(recoveryPeriodCrudRepo.findById(recId)).thenReturn(Optional.of(rec));
        doThrow(new RuntimeException("DB")).when(subjectGradeCrudRepo).save(subjectGrade);

        AppException ex = assertThrows(AppException.class,
                () -> adapter.editRecoverStudent(recId, BigDecimal.TEN, "Z"));
        assertEquals("Error al editar nota de la materia!", ex.getMessage());
    }

    //----------- deleteRecoverStudent -----------

    @Test @DisplayName("deleteRecoverStudent restaura nota y elimina recovery correctamente")
    void deleteRecoverStudent_success() {
        int recId = 7;
        RecoveryPeriod rec = new RecoveryPeriod();
        rec.setId(recId);
        rec.setSubjectGrade(subjectGrade);
        rec.setPreviousScore(new BigDecimal("1.5"));

        when(recoveryPeriodCrudRepo.findById(recId)).thenReturn(Optional.of(rec));

        adapter.deleteRecoverStudent(recId);

        assertEquals(new BigDecimal("1.5"), subjectGrade.getTotalScore());
        assertEquals("N", subjectGrade.getRecovered());
        verify(subjectGradeCrudRepo).save(subjectGrade);
        verify(recoveryPeriodCrudRepo).delete(rec);
    }

    @Test @DisplayName("deleteRecoverStudent lanza AppException si recovery no existe")
    void deleteRecoverStudent_notFound() {
        when(recoveryPeriodCrudRepo.findById(8)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class,
                () -> adapter.deleteRecoverStudent(8));
        assertEquals("Error al eliminar nota de la materia!", ex.getMessage());
    }

    @Test @DisplayName("deleteRecoverStudent lanza AppException si delete falla")
    void deleteRecoverStudent_failure() {
        int recId = 9;
        RecoveryPeriod rec = new RecoveryPeriod();
        rec.setId(recId);
        rec.setSubjectGrade(subjectGrade);
        rec.setPreviousScore(new BigDecimal("3.0"));

        when(recoveryPeriodCrudRepo.findById(recId)).thenReturn(Optional.of(rec));
        doThrow(new RuntimeException("DB")).when(recoveryPeriodCrudRepo).delete(rec);

        AppException ex = assertThrows(AppException.class,
                () -> adapter.deleteRecoverStudent(recId));
        assertEquals("Error al eliminar nota de la materia!", ex.getMessage());
    }

    //------ findRecoveryListSubjects (rama vacía) ------

    @Test @DisplayName("findRecoveryListSubjects retorna vacío si no hay períodos")
    void findRecoveryListSubjects_empty() {
        when(academicPeriodPort.getPeriodsByYear("2023")).thenReturn(Collections.emptyList());

        List<RecoveryPeriodDomain> res = adapter.findRecoveryListSubjects(1, "2023", 1);
        assertTrue(res.isEmpty());
    }

    //-------------- saveOrUpdateSubjectGrade ------------

    @Test @DisplayName("saveOrUpdateSubjectGrade actualiza un registro existente")
    void saveOrUpdateSubjectGrade_update() {
        BigDecimal newScore = new BigDecimal("5.0");
        // stub branch existente
        when(adapter.findBySubjectPeriodStudentId(1,1,1))
                .thenReturn(subjectGradeDomains);
        // mapper de domain a entity
        when(subjectGradeMapper.toEntity(subjectGradeDomain)).thenReturn(subjectGrade);
        // simula guardado
        when(subjectGradeCrudRepo.save(subjectGrade)).thenReturn(subjectGrade);
        when(subjectGradeMapper.toDomain(subjectGrade)).thenReturn(subjectGradeDomain);

        SubjectGradeDomain res = adapter.saveOrUpdateSubjectGrade(1,1,1,newScore);
        assertEquals(subjectGradeDomain, res);
        verify(subjectGradeCrudRepo).save(subjectGrade);
    }

    @Test @DisplayName("saveOrUpdateSubjectGrade crea nuevo registro cuando no existe")
    void saveOrUpdateSubjectGrade_create() {
        BigDecimal score = new BigDecimal("6.0");
        when(adapter.findBySubjectPeriodStudentId(2,2,2)).thenReturn(Collections.emptyList());

        SubjectGrade newEntity = SubjectGrade.builder()
                .subject(Subject.builder().id(2).build())
                .student(User.builder().id(2).build())
                .period(AcademicPeriod.builder().id(2).build())
                .totalScore(score)
                .recovered("I")
                .build();

        when(subjectGradeCrudRepo.save(any(SubjectGrade.class))).thenReturn(newEntity);
        when(subjectGradeMapper.toDomain(newEntity)).thenReturn(
                SubjectGradeDomain.builder()
                        .id(null)
                        .subject(newEntity.getSubject())
                        .student(UserDomain.builder().id(2).build())
                        .period(newEntity.getPeriod())
                        .totalScore(score)
                        .recovered("I")
                        .build()
        );

        SubjectGradeDomain res = adapter.saveOrUpdateSubjectGrade(2,2,2, score);
        assertEquals(score, res.getTotalScore());
        assertEquals("I", res.getRecovered());
        verify(subjectGradeCrudRepo).save(any(SubjectGrade.class));
    }

    //----------- tests existentes -----------

    @Test @DisplayName("findById retorna dominio cuando existe")
    void findById_WhenSubjectGradeExists_ShouldReturnSubjectGrade() {
        when(subjectGradeCrudRepo.findById(1)).thenReturn(Optional.of(subjectGrade));
        when(subjectGradeMapper.toDomain(subjectGrade)).thenReturn(subjectGradeDomain);

        SubjectGradeDomain result = adapter.findById(1);

        assertEquals(subjectGradeDomain, result);
    }

    @Test @DisplayName("findById retorna null cuando no existe")
    void findById_WhenSubjectGradeDoesNotExist_ShouldReturnNull() {
        when(subjectGradeCrudRepo.findById(999)).thenReturn(Optional.empty());
        SubjectGradeDomain result = adapter.findById(999);
        assertNull(result);
    }

    @Test @DisplayName("save guarda correctamente")
    void save_ShouldSaveSubjectGrade() {
        SubjectGradeDomain dom = subjectGradeDomain;
        when(subjectGradeMapper.toEntity(dom)).thenReturn(subjectGrade);
        when(subjectGradeCrudRepo.save(subjectGrade)).thenReturn(subjectGrade);
        when(subjectGradeMapper.toDomain(subjectGrade)).thenReturn(dom);

        SubjectGradeDomain res = adapter.save(dom);
        assertEquals(dom, res);
    }

    @Test @DisplayName("update lanza EntityNotFoundException si no existe")
    void update_WhenSubjectGradeDoesNotExist_ShouldThrowEntityNotFoundException() {
        when(subjectGradeCrudRepo.findById(999)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> adapter.update(999, subjectGradeDomain));
    }

    @Test @DisplayName("update actualiza correctamente")
    void update_WhenSubjectGradeExists_ShouldUpdateAndReturnSubjectGrade() {
        SubjectGradeDomain dom = subjectGradeDomain;
        SubjectGrade existing = subjectGrade;
        when(subjectGradeCrudRepo.findById(1)).thenReturn(Optional.of(existing));
        when(userMapper.toEntity(student2)).thenReturn(student);
        when(subjectGradeCrudRepo.save(existing)).thenReturn(existing);
        when(subjectGradeMapper.toDomain(existing)).thenReturn(dom);

        SubjectGradeDomain res = adapter.update(1, dom);
        assertEquals(dom, res);
    }

    @Test @DisplayName("update lanza EntityNotFoundException si save falla")
    void update_saveFails() {
        SubjectGradeDomain dom = subjectGradeDomain;
        SubjectGrade existing = subjectGrade;
        when(subjectGradeCrudRepo.findById(1)).thenReturn(Optional.of(existing));
        when(userMapper.toEntity(student2)).thenReturn(student);
        doThrow(new RuntimeException("DB")).when(subjectGradeCrudRepo).save(existing);

        assertThrows(EntityNotFoundException.class, () -> adapter.update(1, dom));
    }

    @Test @DisplayName("delete siempre devuelve I_AM_A_TEAPOT")
    void delete_ShouldReturnTeapotStatus() {
        assertEquals(HttpStatus.I_AM_A_TEAPOT, adapter.delete(1));
    }

    @Test @DisplayName("recoverStudent invoca repositorio")
    void recoverStudent_ShouldCallRepositoryMethod() {
        BigDecimal score = new BigDecimal("7.0");
        doNothing().when(subjectGradeCrudRepo)
                .recoverStudent(score, 1,1,1);

        adapter.recoverStudent(1,1,1,score);
        verify(subjectGradeCrudRepo).recoverStudent(score,1,1,1);
    }
}
