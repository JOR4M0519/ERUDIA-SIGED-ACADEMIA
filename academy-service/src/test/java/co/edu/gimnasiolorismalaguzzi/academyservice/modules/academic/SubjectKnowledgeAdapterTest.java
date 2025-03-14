package co.edu.gimnasiolorismalaguzzi.academyservice.modules.academic;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectKnowledgeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.Subject;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectKnowledge;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectKnowledgeMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectKnowledgeCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.SubjectKnowledgeAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.entity.Knowledge;
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
public class SubjectKnowledgeAdapterTest {

    @Mock
    private SubjectKnowledgeCrudRepo subjectKnowledgeCrudRepo;

    @Mock
    private SubjectKnowledgeMapper subjectKnowledgeMapper;

    private SubjectKnowledgeAdapter subjectKnowledgeAdapter;

    private SubjectKnowledge subjectKnowledge;
    private SubjectKnowledgeDomain subjectKnowledgeDomain;
    private List<SubjectKnowledge> subjectKnowledges;
    private List<SubjectKnowledgeDomain> subjectKnowledgeDomains;
    private Subject subject;
    private Knowledge knowledge;

    @BeforeEach
    void setUp() {
        // Crear la instancia con el constructor
        subjectKnowledgeAdapter = new SubjectKnowledgeAdapter(subjectKnowledgeCrudRepo, subjectKnowledgeMapper);

        // Inicializar entidades
        subject = Subject.builder()
                .id(1)
                .subjectName("Mathematics")
                .status("A")
                .build();

        knowledge = Knowledge.builder()
                .id(1)
                .name("Algebra")
                .build();

        subjectKnowledge = SubjectKnowledge.builder()
                .id(1)
                .idSubject(subject)
                .idKnowledge(knowledge)
                .build();

        // Inicializar dominio
        subjectKnowledgeDomain = SubjectKnowledgeDomain.builder()
                .id(1)
                .idSubject(subject)
                .idKnowledge(knowledge)
                .build();

        // Inicializar listas
        subjectKnowledges = Arrays.asList(subjectKnowledge);
        subjectKnowledgeDomains = Arrays.asList(subjectKnowledgeDomain);
    }

    @Test
    void findAll_ShouldReturnAllSubjectKnowledges() {
        // Arrange
        when(subjectKnowledgeCrudRepo.findAll()).thenReturn(subjectKnowledges);
        when(subjectKnowledgeMapper.toDomains(subjectKnowledges)).thenReturn(subjectKnowledgeDomains);

        // Act
        List<SubjectKnowledgeDomain> result = subjectKnowledgeAdapter.findAll();

        // Assert
        assertEquals(subjectKnowledgeDomains, result);
        verify(subjectKnowledgeCrudRepo).findAll();
        verify(subjectKnowledgeMapper).toDomains(subjectKnowledges);
    }

    @Test
    void findById_WhenSubjectKnowledgeExists_ShouldReturnSubjectKnowledge() {
        // Arrange
        Integer id = 1;
        when(subjectKnowledgeCrudRepo.findById(id)).thenReturn(Optional.of(subjectKnowledge));
        when(subjectKnowledgeMapper.toDomain(subjectKnowledge)).thenReturn(subjectKnowledgeDomain);

        // Act
        SubjectKnowledgeDomain result = subjectKnowledgeAdapter.findById(id);

        // Assert
        assertEquals(subjectKnowledgeDomain, result);
        verify(subjectKnowledgeCrudRepo).findById(id);
        verify(subjectKnowledgeMapper).toDomain(subjectKnowledge);
    }

    @Test
    void findById_WhenSubjectKnowledgeDoesNotExist_ShouldReturnNull() {
        // Arrange
        Integer id = 999;
        when(subjectKnowledgeCrudRepo.findById(id)).thenReturn(Optional.empty());

        // Act
        SubjectKnowledgeDomain result = subjectKnowledgeAdapter.findById(id);

        // Assert
        assertNull(result);
        verify(subjectKnowledgeCrudRepo).findById(id);
        verify(subjectKnowledgeMapper, never()).toDomain(any());
    }

    @Test
    void save_ShouldSaveSubjectKnowledge() {
        // Arrange
        SubjectKnowledgeDomain domainToSave = SubjectKnowledgeDomain.builder()
                .idSubject(subject)
                .idKnowledge(knowledge)
                .build();

        SubjectKnowledge entityToSave = SubjectKnowledge.builder()
                .idSubject(subject)
                .idKnowledge(knowledge)
                .build();

        SubjectKnowledge savedEntity = SubjectKnowledge.builder()
                .id(1)
                .idSubject(subject)
                .idKnowledge(knowledge)
                .build();

        SubjectKnowledgeDomain savedDomain = SubjectKnowledgeDomain.builder()
                .id(1)
                .idSubject(subject)
                .idKnowledge(knowledge)
                .build();

        when(subjectKnowledgeMapper.toEntity(domainToSave)).thenReturn(entityToSave);
        when(subjectKnowledgeCrudRepo.save(entityToSave)).thenReturn(savedEntity);
        when(subjectKnowledgeMapper.toDomain(savedEntity)).thenReturn(savedDomain);

        // Act
        SubjectKnowledgeDomain result = subjectKnowledgeAdapter.save(domainToSave);

        // Assert
        assertEquals(savedDomain, result);
        verify(subjectKnowledgeMapper).toEntity(domainToSave);
        verify(subjectKnowledgeCrudRepo).save(entityToSave);
        verify(subjectKnowledgeMapper).toDomain(savedEntity);
    }

    @Test
    void update_WhenSubjectKnowledgeExists_ShouldUpdateAndReturnSubjectKnowledge() {
        // Arrange
        Integer id = 1;

        // Crear nuevos objetos para la actualización
        Subject newSubject = Subject.builder()
                .id(2)
                .subjectName("Physics")
                .status("A")
                .build();

        Knowledge newKnowledge = Knowledge.builder()
                .id(2)
                .name("Mechanics")
                .build();

        SubjectKnowledgeDomain domainToUpdate = SubjectKnowledgeDomain.builder()
                .id(1)
                .idSubject(newSubject)
                .idKnowledge(newKnowledge)
                .build();

        SubjectKnowledge existingEntity = SubjectKnowledge.builder()
                .id(1)
                .idSubject(subject)
                .idKnowledge(knowledge)
                .build();

        SubjectKnowledge updatedEntity = SubjectKnowledge.builder()
                .id(1)
                .idSubject(newSubject)
                .idKnowledge(newKnowledge)
                .build();

        when(subjectKnowledgeCrudRepo.findById(id)).thenReturn(Optional.of(existingEntity));
        when(subjectKnowledgeCrudRepo.save(any(SubjectKnowledge.class))).thenReturn(updatedEntity);
        when(subjectKnowledgeMapper.toDomain(updatedEntity)).thenReturn(domainToUpdate);

        // Act
        SubjectKnowledgeDomain result = subjectKnowledgeAdapter.update(id, domainToUpdate);

        // Assert
        assertEquals(domainToUpdate, result);
        verify(subjectKnowledgeCrudRepo).findById(id);
        verify(subjectKnowledgeCrudRepo).save(any(SubjectKnowledge.class));
        verify(subjectKnowledgeMapper).toDomain(updatedEntity);
    }

    @Test
    void update_WhenSubjectKnowledgeDoesNotExist_ShouldThrowEntityNotFoundException() {
        // Arrange
        Integer id = 999;
        SubjectKnowledgeDomain domainToUpdate = SubjectKnowledgeDomain.builder()
                .id(999)
                .idSubject(subject)
                .idKnowledge(knowledge)
                .build();

        when(subjectKnowledgeCrudRepo.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            subjectKnowledgeAdapter.update(id, domainToUpdate);
        });

        verify(subjectKnowledgeCrudRepo).findById(id);
        verify(subjectKnowledgeCrudRepo, never()).save(any(SubjectKnowledge.class));
    }

    @Test
    void delete_WhenSubjectKnowledgeExists_ShouldDeleteAndReturnOkStatus() {
        // Arrange
        Integer id = 1;

        when(subjectKnowledgeCrudRepo.existsById(id)).thenReturn(true);
        when(subjectKnowledgeCrudRepo.getReferenceById(id)).thenReturn(subjectKnowledge);
        doNothing().when(subjectKnowledgeCrudRepo).delete(subjectKnowledge);

        // Act
        HttpStatus result = subjectKnowledgeAdapter.delete(id);

        // Assert
        assertEquals(HttpStatus.OK, result);
        verify(subjectKnowledgeCrudRepo).existsById(id);
        verify(subjectKnowledgeCrudRepo).getReferenceById(id);
        verify(subjectKnowledgeCrudRepo).delete(subjectKnowledge);
    }

    @Test
    void delete_WhenSubjectKnowledgeDoesNotExist_ShouldThrowAppException() {
        // Arrange
        Integer id = 999;

        when(subjectKnowledgeCrudRepo.existsById(id)).thenReturn(false);

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> {
            subjectKnowledgeAdapter.delete(id);
        });


        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getCode());
        verify(subjectKnowledgeCrudRepo).existsById(id);
        verify(subjectKnowledgeCrudRepo, never()).getReferenceById(any());
        verify(subjectKnowledgeCrudRepo, never()).delete(any());
    }

    @Test
    void delete_WhenExceptionOccurs_ShouldThrowAppExceptionWithInternalServerError() {
        // Arrange
        Integer id = 1;

        when(subjectKnowledgeCrudRepo.existsById(id)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> {
            subjectKnowledgeAdapter.delete(id);
        });

        assertEquals("INTERN ERROR", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getCode());
        verify(subjectKnowledgeCrudRepo).existsById(id);
    }

    @Test
    void getAllKnowledgesBySubjectIdByPeriodId_ShouldReturnMatchingSubjectKnowledges() {
        // Arrange
        Integer subjectId = 1;
        Integer periodId = 1;

        when(subjectKnowledgeCrudRepo.findKnowledgesBySubjectId(subjectId, periodId))
                .thenReturn(subjectKnowledges);
        when(subjectKnowledgeMapper.toDomains(subjectKnowledges)).thenReturn(subjectKnowledgeDomains);

        // Act
        List<SubjectKnowledgeDomain> result = subjectKnowledgeAdapter.getAllKnowledgesBySubjectIdByPeriodId(subjectId, periodId);

        // Assert
        assertEquals(subjectKnowledgeDomains, result);
        verify(subjectKnowledgeCrudRepo).findKnowledgesBySubjectId(subjectId, periodId);
        verify(subjectKnowledgeMapper).toDomains(subjectKnowledges);
    }
}
