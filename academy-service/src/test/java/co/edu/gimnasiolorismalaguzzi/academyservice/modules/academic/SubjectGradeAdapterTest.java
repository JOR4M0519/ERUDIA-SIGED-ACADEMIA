package co.edu.gimnasiolorismalaguzzi.academyservice.modules.academic;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectGradeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.AcademicPeriod;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.Subject;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectGrade;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectGradeMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectGradeCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.SubjectGradeAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubjectGradeAdapterTest {

    @Mock
    private SubjectGradeCrudRepo subjectGradeCrudRepo;

    @Mock
    private SubjectGradeMapper subjectGradeMapper;

    private SubjectGradeAdapter subjectGradeAdapter;

    private Subject subject;
    private User student;
    private AcademicPeriod period;
    private SubjectGrade subjectGrade;
    private SubjectGradeDomain subjectGradeDomain;
    private List<SubjectGrade> subjectGrades;
    private List<SubjectGradeDomain> subjectGradeDomains;

    @BeforeEach
    void setUp() {
        // Crear la instancia con el constructor
        subjectGradeAdapter = new SubjectGradeAdapter(subjectGradeCrudRepo);

        // Inyectar manualmente el mapper usando ReflectionTestUtils
        ReflectionTestUtils.setField(subjectGradeAdapter, "subjectGradeMapper", subjectGradeMapper);

        // Inicializar entidades
        subject = Subject.builder()
                .id(1)
                .subjectName("Mathematics")
                .status("A")
                .build();

        student = User.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .build();

        period = new AcademicPeriod();
        period.setId(1);
        period.setName("First Period 2023");

        subjectGrade = new SubjectGrade();
        subjectGrade.setId(1);
        subjectGrade.setSubject(subject);
        subjectGrade.setStudent(student);
        subjectGrade.setPeriod(period);
        subjectGrade.setTotalScore(new BigDecimal("4.5"));
        subjectGrade.setRecovered("N");

        // Inicializar dominio
        subjectGradeDomain = SubjectGradeDomain.builder()
                .id(1)
                .subject(subject)
                .student(student)
                .period(period)
                .totalScore(new BigDecimal("4.5"))
                .recovered("N")
                .build();

        // Inicializar listas
        subjectGrades = Arrays.asList(subjectGrade);
        subjectGradeDomains = Arrays.asList(subjectGradeDomain);
    }

    @Test
    void findAll_ShouldReturnAllSubjectGrades() {
        // Arrange
        when(subjectGradeCrudRepo.findAll()).thenReturn(subjectGrades);
        when(subjectGradeMapper.toDomains(subjectGrades)).thenReturn(subjectGradeDomains);

        // Act
        List<SubjectGradeDomain> result = subjectGradeAdapter.findAll();

        // Assert
        assertEquals(subjectGradeDomains, result);
        verify(subjectGradeCrudRepo).findAll();
        verify(subjectGradeMapper).toDomains(subjectGrades);
    }

    @Test
    void findBySubjectPeriodStudentId_ShouldReturnMatchingSubjectGrades() {
        // Arrange
        int subjectId = 1;
        int periodId = 1;
        int studentId = 1;

        when(subjectGradeCrudRepo.findByStudent_IdAndPeriod_IdAndSubject_Id(studentId, periodId, subjectId))
                .thenReturn(subjectGrades);
        when(subjectGradeMapper.toDomains(subjectGrades)).thenReturn(subjectGradeDomains);

        // Act
        List<SubjectGradeDomain> result = subjectGradeAdapter.findBySubjectPeriodStudentId(subjectId, periodId, studentId);

        // Assert
        assertEquals(subjectGradeDomains, result);
        verify(subjectGradeCrudRepo).findByStudent_IdAndPeriod_IdAndSubject_Id(studentId, periodId, subjectId);
        verify(subjectGradeMapper).toDomains(subjectGrades);
    }

    @Test
    void findById_WhenSubjectGradeExists_ShouldReturnSubjectGrade() {
        // Arrange
        Integer id = 1;
        when(subjectGradeCrudRepo.findById(id)).thenReturn(Optional.of(subjectGrade));
        when(subjectGradeMapper.toDomain(subjectGrade)).thenReturn(subjectGradeDomain);

        // Act
        SubjectGradeDomain result = subjectGradeAdapter.findById(id);

        // Assert
        assertEquals(subjectGradeDomain, result);
        verify(subjectGradeCrudRepo).findById(id);
        verify(subjectGradeMapper).toDomain(subjectGrade);
    }

    @Test
    void findById_WhenSubjectGradeDoesNotExist_ShouldReturnNull() {
        // Arrange
        Integer id = 999;
        when(subjectGradeCrudRepo.findById(id)).thenReturn(Optional.empty());

        // Act
        SubjectGradeDomain result = subjectGradeAdapter.findById(id);

        // Assert
        assertNull(result);
        verify(subjectGradeCrudRepo).findById(id);
        verify(subjectGradeMapper, never()).toDomain(any());
    }

    @Test
    void save_ShouldSaveSubjectGrade() {
        // Arrange
        SubjectGradeDomain domainToSave = SubjectGradeDomain.builder()
                .subject(subject)
                .student(student)
                .period(period)
                .totalScore(new BigDecimal("4.5"))
                .recovered("N")
                .build();

        SubjectGrade entityToSave = new SubjectGrade();
        entityToSave.setSubject(subject);
        entityToSave.setStudent(student);
        entityToSave.setPeriod(period);
        entityToSave.setTotalScore(new BigDecimal("4.5"));
        entityToSave.setRecovered("N");

        SubjectGrade savedEntity = new SubjectGrade();
        savedEntity.setId(1);
        savedEntity.setSubject(subject);
        savedEntity.setStudent(student);
        savedEntity.setPeriod(period);
        savedEntity.setTotalScore(new BigDecimal("4.5"));
        savedEntity.setRecovered("N");

        SubjectGradeDomain savedDomain = SubjectGradeDomain.builder()
                .id(1)
                .subject(subject)
                .student(student)
                .period(period)
                .totalScore(new BigDecimal("4.5"))
                .recovered("N")
                .build();

        when(subjectGradeMapper.toEntity(domainToSave)).thenReturn(entityToSave);
        when(subjectGradeCrudRepo.save(entityToSave)).thenReturn(savedEntity);
        when(subjectGradeMapper.toDomain(savedEntity)).thenReturn(savedDomain);

        // Act
        SubjectGradeDomain result = subjectGradeAdapter.save(domainToSave);

        // Assert
        assertEquals(savedDomain, result);
        verify(subjectGradeMapper).toEntity(domainToSave);
        verify(subjectGradeCrudRepo).save(entityToSave);
        verify(subjectGradeMapper).toDomain(savedEntity);
    }

    @Test
    void update_WhenSubjectGradeExists_ShouldUpdateAndReturnSubjectGrade() {
        // Arrange
        Integer id = 1;

        SubjectGradeDomain domainToUpdate = SubjectGradeDomain.builder()
                .id(1)
                .subject(subject)
                .student(student)
                .period(period)
                .totalScore(new BigDecimal("4.8"))
                .recovered("Y")
                .build();

        SubjectGrade existingEntity = new SubjectGrade();
        existingEntity.setId(1);
        existingEntity.setSubject(subject);
        existingEntity.setStudent(student);
        existingEntity.setPeriod(period);
        existingEntity.setTotalScore(new BigDecimal("4.5"));
        existingEntity.setRecovered("N");

        SubjectGrade updatedEntity = new SubjectGrade();
        updatedEntity.setId(1);
        updatedEntity.setSubject(subject);
        updatedEntity.setStudent(student);
        updatedEntity.setPeriod(period);
        updatedEntity.setTotalScore(new BigDecimal("4.8"));
        updatedEntity.setRecovered("Y");

        when(subjectGradeCrudRepo.findById(id)).thenReturn(Optional.of(existingEntity));
        when(subjectGradeCrudRepo.save(any(SubjectGrade.class))).thenReturn(updatedEntity);
        when(subjectGradeMapper.toDomain(updatedEntity)).thenReturn(domainToUpdate);

        // Act
        SubjectGradeDomain result = subjectGradeAdapter.update(id, domainToUpdate);

        // Assert
        assertEquals(domainToUpdate, result);
        verify(subjectGradeCrudRepo).findById(id);
        verify(subjectGradeCrudRepo).save(any(SubjectGrade.class));
        verify(subjectGradeMapper).toDomain(updatedEntity);
    }

    @Test
    void update_WhenSubjectGradeDoesNotExist_ShouldThrowEntityNotFoundException() {
        // Arrange
        Integer id = 999;
        SubjectGradeDomain domainToUpdate = SubjectGradeDomain.builder()
                .id(999)
                .subject(subject)
                .student(student)
                .period(period)
                .totalScore(new BigDecimal("4.8"))
                .recovered("Y")
                .build();

        when(subjectGradeCrudRepo.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            subjectGradeAdapter.update(id, domainToUpdate);
        });

        verify(subjectGradeCrudRepo).findById(id);
        verify(subjectGradeCrudRepo, never()).save(any(SubjectGrade.class));
    }

    @Test
    void delete_ShouldReturnTeapotStatus() {
        // Act
        HttpStatus result = subjectGradeAdapter.delete(1);

        // Assert
        assertEquals(HttpStatus.I_AM_A_TEAPOT, result);
        verifyNoInteractions(subjectGradeCrudRepo); // Verifica que no se interactúa con el repositorio
    }

    @Test
    void recoverStudent_ShouldCallRepositoryMethod() {
        // Arrange
        int studentId = 1;
        int subjectId = 1;
        int periodId = 1;
        BigDecimal newScore = new BigDecimal("4.8");

        doNothing().when(subjectGradeCrudRepo).recoverStudent(newScore, subjectId, studentId, periodId);

        // Act
        subjectGradeAdapter.recoverStudent(studentId, subjectId, periodId, newScore);

        // Assert
        verify(subjectGradeCrudRepo).recoverStudent(newScore, subjectId, studentId, periodId);
    }
}
