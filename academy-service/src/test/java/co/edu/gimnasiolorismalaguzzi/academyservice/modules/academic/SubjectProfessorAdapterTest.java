package co.edu.gimnasiolorismalaguzzi.academyservice.modules.academic;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectProfessorDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.Subject;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectProfessor;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectProfessorMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectProfessorCrudRepo;
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

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubjectProfessorAdapterTest {

    @Mock
    private SubjectProfessorCrudRepo subjectProfessorCrudRepo;

    @Mock
    private SubjectProfessorMapper subjectProfessorMapper;

    private SubjectProfessorAdapter subjectProfessorAdapter;

    private Subject subject;
    private User professor;
    private SubjectProfessor subjectProfessor;
    private SubjectProfessorDomain subjectProfessorDomain;
    private List<SubjectProfessor> subjectProfessors;
    private List<SubjectProfessorDomain> subjectProfessorDomains;

    @BeforeEach
    void setUp() {
        // Crear la instancia con el constructor
        subjectProfessorAdapter = new SubjectProfessorAdapter(subjectProfessorCrudRepo, subjectProfessorMapper);

        // Inicializar entidades
        subject = Subject.builder()
                .id(1)
                .subjectName("Mathematics")
                .status("A")
                .build();

        professor = User.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .build();

        subjectProfessor = SubjectProfessor.builder()
                .id(1)
                .subject(subject)
                .professor(professor)
                .build();

        // Inicializar dominio
        subjectProfessorDomain = SubjectProfessorDomain.builder()
                .id(1)
                .subject(subject)
                .professor(professor)
                .build();

        // Inicializar listas
        subjectProfessors = Arrays.asList(subjectProfessor);
        subjectProfessorDomains = Arrays.asList(subjectProfessorDomain);
    }

    @Test
    void findAll_ShouldReturnAllSubjectProfessors() {
        // Arrange
        when(subjectProfessorCrudRepo.findAll()).thenReturn(subjectProfessors);
        when(subjectProfessorMapper.toDomains(subjectProfessors)).thenReturn(subjectProfessorDomains);

        // Act
        List<SubjectProfessorDomain> result = subjectProfessorAdapter.findAll();

        // Assert
        assertEquals(subjectProfessorDomains, result);
        verify(subjectProfessorCrudRepo).findAll();
        verify(subjectProfessorMapper).toDomains(subjectProfessors);
    }

    @Test
    void findBySubjectId_ShouldReturnSubjectProfessors() {
        // Arrange
        Integer subjectId = 1;
        when(subjectProfessorCrudRepo.findBySubjectId(subjectId)).thenReturn(subjectProfessors);
        when(subjectProfessorMapper.toDomains(subjectProfessors)).thenReturn(subjectProfessorDomains);

        // Act
        List<SubjectProfessorDomain> result = subjectProfessorAdapter.findBySubjectId(subjectId);

        // Assert
        assertEquals(subjectProfessorDomains, result);
        verify(subjectProfessorCrudRepo).findBySubjectId(subjectId);
        verify(subjectProfessorMapper).toDomains(subjectProfessors);
    }

    @Test
    void findById_WhenSubjectProfessorExists_ShouldReturnSubjectProfessor() {
        // Arrange
        Integer id = 1;
        when(subjectProfessorCrudRepo.findById(id)).thenReturn(Optional.of(subjectProfessor));
        when(subjectProfessorMapper.toDomain(subjectProfessor)).thenReturn(subjectProfessorDomain);

        // Act
        SubjectProfessorDomain result = subjectProfessorAdapter.findById(id);

        // Assert
        assertEquals(subjectProfessorDomain, result);
        verify(subjectProfessorCrudRepo).findById(id);
        verify(subjectProfessorMapper).toDomain(subjectProfessor);
    }

    @Test
    void findById_WhenSubjectProfessorDoesNotExist_ShouldReturnNull() {
        // Arrange
        Integer id = 999;
        when(subjectProfessorCrudRepo.findById(id)).thenReturn(Optional.empty());

        // Act
        SubjectProfessorDomain result = subjectProfessorAdapter.findById(id);

        // Assert
        assertNull(result);
        verify(subjectProfessorCrudRepo).findById(id);
        verify(subjectProfessorMapper, never()).toDomain(any());
    }

    @Test
    void save_ShouldSaveSubjectProfessor() {
        // Arrange
        SubjectProfessorDomain domainToSave = SubjectProfessorDomain.builder()
                .subject(subject)
                .professor(professor)
                .build();

        SubjectProfessor entityToSave = SubjectProfessor.builder()
                .subject(subject)
                .professor(professor)
                .build();

        SubjectProfessor savedEntity = SubjectProfessor.builder()
                .id(1)
                .subject(subject)
                .professor(professor)
                .build();

        SubjectProfessorDomain savedDomain = SubjectProfessorDomain.builder()
                .id(1)
                .subject(subject)
                .professor(professor)
                .build();

        when(subjectProfessorMapper.toEntity(any(SubjectProfessorDomain.class))).thenReturn(entityToSave);
        when(subjectProfessorCrudRepo.save(entityToSave)).thenReturn(savedEntity);
        when(subjectProfessorMapper.toDomain(savedEntity)).thenReturn(savedDomain);

        // Act
        SubjectProfessorDomain result = subjectProfessorAdapter.save(domainToSave);

        // Assert
        assertEquals(savedDomain, result);
        verify(subjectProfessorMapper).toEntity(domainToSave);
        verify(subjectProfessorCrudRepo).save(entityToSave);
        verify(subjectProfessorMapper).toDomain(savedEntity);
    }

    @Test
    void update_WhenSubjectProfessorExists_ShouldUpdateAndReturnSubjectProfessor() {
        // Arrange
        Integer id = 1;
        SubjectProfessorDomain domainToUpdate = SubjectProfessorDomain.builder()
                .id(1)
                .subject(subject)
                .professor(professor)
                .build();

        SubjectProfessor existingEntity = SubjectProfessor.builder()
                .id(1)
                .subject(subject)
                .professor(professor)
                .build();

        SubjectProfessor updatedEntity = SubjectProfessor.builder()
                .id(1)
                .subject(subject)
                .professor(professor)
                .build();

        when(subjectProfessorCrudRepo.findById(id)).thenReturn(Optional.of(existingEntity));
        when(subjectProfessorCrudRepo.save(any(SubjectProfessor.class))).thenReturn(updatedEntity);
        when(subjectProfessorMapper.toDomain(updatedEntity)).thenReturn(domainToUpdate);

        // Act
        SubjectProfessorDomain result = subjectProfessorAdapter.update(id, domainToUpdate);

        // Assert
        assertEquals(domainToUpdate, result);
        verify(subjectProfessorCrudRepo).findById(id);
        verify(subjectProfessorCrudRepo).save(any(SubjectProfessor.class));
        verify(subjectProfessorMapper).toDomain(updatedEntity);
    }

    @Test
    void update_WhenSubjectProfessorDoesNotExist_ShouldThrowEntityNotFoundException() {
        // Arrange
        Integer id = 999;
        SubjectProfessorDomain domainToUpdate = SubjectProfessorDomain.builder()
                .id(999)
                .subject(subject)
                .professor(professor)
                .build();

        when(subjectProfessorCrudRepo.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            subjectProfessorAdapter.update(id, domainToUpdate);
        });

        verify(subjectProfessorCrudRepo).findById(id);
        verify(subjectProfessorCrudRepo, never()).save(any(SubjectProfessor.class));
    }

    @Test
    void delete_WhenSubjectProfessorExists_ShouldDeleteAndReturnOk() {
        // Arrange
        Integer id = 1;
        when(subjectProfessorCrudRepo.existsById(id)).thenReturn(true);
        when(subjectProfessorCrudRepo.getReferenceById(id)).thenReturn(subjectProfessor);
        doNothing().when(subjectProfessorCrudRepo).delete(subjectProfessor);

        // Act
        HttpStatus result = subjectProfessorAdapter.delete(id);

        // Assert
        assertEquals(HttpStatus.OK, result);
        verify(subjectProfessorCrudRepo).existsById(id);
        verify(subjectProfessorCrudRepo).getReferenceById(id);
        verify(subjectProfessorCrudRepo).delete(subjectProfessor);
    }

    @Test
    void delete_WhenExceptionOccurs_ShouldThrowInternalServerError() {
        // Arrange
        Integer id = 1;
        when(subjectProfessorCrudRepo.existsById(id)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> {
            subjectProfessorAdapter.delete(id);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getCode());
        assertEquals("INTERN ERROR", exception.getMessage());
        verify(subjectProfessorCrudRepo).existsById(id);
    }
}
