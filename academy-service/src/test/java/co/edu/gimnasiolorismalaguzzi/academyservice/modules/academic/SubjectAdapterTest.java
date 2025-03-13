package co.edu.gimnasiolorismalaguzzi.academyservice.modules.academic;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectProfessorDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.Subject;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.SubjectAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.SubjectProfessorAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubjectAdapterTest {

    @Mock
    private SubjectCrudRepo subjectCrudRepo;

    @Mock
    private SubjectMapper subjectMapper;

    @Mock
    private SubjectProfessorAdapter subjectProfessorAdapter;

    private SubjectAdapter subjectAdapter;

    private Subject subject;
    private SubjectDomain subjectDomain;
    private List<Subject> subjects;
    private List<SubjectDomain> subjectDomains;
    private User professor;
    private List<User> professors;
    private SubjectProfessorDomain subjectProfessorDomain;
    private List<SubjectProfessorDomain> subjectProfessorDomains;

    @BeforeEach
    void setUp() {
        // Crear la instancia con el constructor
        subjectAdapter = new SubjectAdapter(subjectCrudRepo, subjectProfessorAdapter);

        // Inyectar manualmente el mapper usando ReflectionTestUtils
        ReflectionTestUtils.setField(subjectAdapter, "subjectMapper", subjectMapper);

        // Inicializar entidad profesor
        professor = User.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .build();
        professors = Arrays.asList(professor);

        // Inicializar entidad
        subject = Subject.builder()
                .id(1)
                .subjectName("Mathematics")
                .status("A")
                .build();

        // Inicializar dominio
        subjectDomain = SubjectDomain.builder()
                .id(1)
                .subjectName("Mathematics")
                .status("A")
                .professor(professors)
                .build();

        // Inicializar listas
        subjects = Arrays.asList(subject);
        subjectDomains = Arrays.asList(subjectDomain);

        // Inicializar SubjectProfessorDomain
        subjectProfessorDomain = SubjectProfessorDomain.builder()
                .id(1)
                .subject(subject)
                .professor(professor)
                .build();
        subjectProfessorDomains = Arrays.asList(subjectProfessorDomain);
    }

    @Test
    void findAll_ShouldReturnAllSubjects() {
        // Arrange
        when(subjectCrudRepo.findAll()).thenReturn(subjects);
        when(subjectMapper.toDomains(subjects)).thenReturn(subjectDomains);
        when(subjectProfessorAdapter.findBySubjectId(anyInt())).thenReturn(subjectProfessorDomains);

        // Act
        List<SubjectDomain> result = subjectAdapter.findAll();

        // Assert
        assertEquals(subjectDomains, result);
        verify(subjectCrudRepo).findAll();
        verify(subjectMapper).toDomains(subjects);
        verify(subjectProfessorAdapter).findBySubjectId(1);
    }

    @Test
    void findById_WhenSubjectExists_ShouldReturnSubject() {
        // Arrange
        Integer id = 1;
        when(subjectCrudRepo.findById(id)).thenReturn(Optional.of(subject));
        when(subjectMapper.toDomain(subject)).thenReturn(subjectDomain);

        // Act
        SubjectDomain result = subjectAdapter.findById(id);

        // Assert
        assertEquals(subjectDomain, result);
        verify(subjectCrudRepo).findById(id);
        verify(subjectMapper).toDomain(subject);
    }

    @Test
    void findById_WhenSubjectDoesNotExist_ShouldReturnNull() {
        // Arrange
        Integer id = 999;
        when(subjectCrudRepo.findById(id)).thenReturn(Optional.empty());

        // Act
        SubjectDomain result = subjectAdapter.findById(id);

        // Assert
        assertNull(result);
        verify(subjectCrudRepo).findById(id);
        verify(subjectMapper, never()).toDomain(any());
    }

    @Test
    void save_ShouldSetStatusAndSaveSubject() {
        // Arrange
        SubjectDomain domainToSave = SubjectDomain.builder()
                .subjectName("Physics")
                .professor(professors)
                .build();

        Subject entityToSave = Subject.builder()
                .subjectName("Physics")
                .status("A")
                .build();

        Subject savedEntity = Subject.builder()
                .id(2)
                .subjectName("Physics")
                .status("A")
                .build();

        SubjectDomain savedDomain = SubjectDomain.builder()
                .id(2)
                .subjectName("Physics")
                .status("A")
                .build();

        when(subjectMapper.toEntity(any(SubjectDomain.class))).thenReturn(entityToSave);
        when(subjectCrudRepo.save(entityToSave)).thenReturn(savedEntity);
        when(subjectMapper.toDomain(savedEntity)).thenReturn(savedDomain);

        // Act
        SubjectDomain result = subjectAdapter.save(domainToSave);

        // Assert
        assertEquals("A", domainToSave.getStatus());
        assertEquals(savedDomain, result);
        verify(subjectMapper).toEntity(domainToSave);
        verify(subjectCrudRepo).save(entityToSave);
        verify(subjectMapper).toDomain(savedEntity);
        verify(subjectProfessorAdapter).save(any(SubjectProfessorDomain.class));
    }

    @Test
    void update_WhenSubjectDoesNotExist_ShouldThrowEntityNotFoundException() {
        // Arrange
        Integer id = 999;
        SubjectDomain domainToUpdate = SubjectDomain.builder()
                .id(999)
                .subjectName("Non-existent Subject")
                .status("A")
                .build();

        when(subjectCrudRepo.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            subjectAdapter.update(id, domainToUpdate);
        });

        verify(subjectCrudRepo).findById(id);
        verify(subjectCrudRepo, never()).save(any(Subject.class));
    }

    @Test
    void delete_WhenSubjectExists_ShouldUpdateStatusAndReturnOk() {
        // Arrange
        Integer id = 1;
        when(subjectCrudRepo.existsById(id)).thenReturn(true);
        when(subjectCrudRepo.updateStatusById("I", id)).thenReturn(1);

        // Act
        HttpStatus result = subjectAdapter.delete(id);

        // Assert
        assertEquals(HttpStatus.OK, result);
        verify(subjectCrudRepo).existsById(id);
        verify(subjectCrudRepo).updateStatusById("I", id);
    }


    @Test
    void delete_WhenExceptionOccurs_ShouldThrowInternalServerError() {
        // Arrange
        Integer id = 1;
        when(subjectCrudRepo.existsById(id)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> {
            subjectAdapter.delete(id);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getCode());
        assertEquals("INTERN ERROR", exception.getMessage());
        verify(subjectCrudRepo).existsById(id);
    }
}
