package co.edu.gimnasiolorismalaguzzi.academyservice.modules.student;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.EducationalLevelDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.EducationalLevel;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.mapper.EducationalLevelMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.repository.EduLevelCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.EducationalLevelAdapter;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EducationalLevelAdapterTest {

    @Mock
    private EduLevelCrudRepo eduLevelCrudRepo;

    @Mock
    private EducationalLevelMapper educationalLevelMapper;

    private EducationalLevelAdapter educationalLevelAdapter;

    private EducationalLevel educationalLevel;
    private EducationalLevelDomain educationalLevelDomain;
    private List<EducationalLevel> educationalLevels;
    private List<EducationalLevelDomain> educationalLevelDomains;

    @BeforeEach
    void setUp() {
        // Crear la instancia con el constructor e inyectar manualmente el mapper
        educationalLevelAdapter = new EducationalLevelAdapter(eduLevelCrudRepo,educationalLevelMapper);

        // Inicializar entidades
        educationalLevel = EducationalLevel.builder()
                .id(1)
                .levelName("Primaria")
                .status("A")
                .build();

        // Inicializar dominio
        educationalLevelDomain = EducationalLevelDomain.builder()
                .id(1)
                .levelName("Primaria")
                .status("A")
                .build();

        // Inicializar listas
        educationalLevels = Arrays.asList(educationalLevel);
        educationalLevelDomains = Arrays.asList(educationalLevelDomain);
    }

    @Test
    void findAll_ShouldReturnAllEducationalLevels() {
        // Arrange
        when(eduLevelCrudRepo.findAll()).thenReturn(educationalLevels);
        when(educationalLevelMapper.toDomains(educationalLevels)).thenReturn(educationalLevelDomains);

        // Act
        List<EducationalLevelDomain> result = educationalLevelAdapter.findAll();

        // Assert
        assertEquals(educationalLevelDomains, result);
        verify(eduLevelCrudRepo).findAll();
        verify(educationalLevelMapper).toDomains(educationalLevels);
    }

    @Test
    void findById_WhenEducationalLevelExists_ShouldReturnEducationalLevel() {
        // Arrange
        Integer id = 1;
        when(eduLevelCrudRepo.findById(id)).thenReturn(Optional.of(educationalLevel));
        when(educationalLevelMapper.toDomain(educationalLevel)).thenReturn(educationalLevelDomain);

        // Act
        EducationalLevelDomain result = educationalLevelAdapter.findById(id);

        // Assert
        assertEquals(educationalLevelDomain, result);
        verify(eduLevelCrudRepo).findById(id);
        verify(educationalLevelMapper).toDomain(educationalLevel);
    }

    @Test
    void findById_WhenEducationalLevelDoesNotExist_ShouldReturnNull() {
        // Arrange
        Integer id = 999;
        when(eduLevelCrudRepo.findById(id)).thenReturn(Optional.empty());

        // Act
        EducationalLevelDomain result = educationalLevelAdapter.findById(id);

        // Assert
        assertNull(result);
        verify(eduLevelCrudRepo).findById(id);
        verify(educationalLevelMapper, never()).toDomain(any());
    }

    @Test
    void save_ShouldSaveEducationalLevel() {
        // Arrange
        EducationalLevelDomain domainToSave = EducationalLevelDomain.builder()
                .levelName("Secundaria")
                .build();

        EducationalLevel entityToSave = EducationalLevel.builder()
                .levelName("Secundaria")
                .status("A")
                .build();

        EducationalLevel savedEntity = EducationalLevel.builder()
                .id(2)
                .levelName("Secundaria")
                .status("A")
                .build();

        EducationalLevelDomain savedDomain = EducationalLevelDomain.builder()
                .id(2)
                .levelName("Secundaria")
                .status("A")
                .build();

        when(educationalLevelMapper.toEntity(domainToSave)).thenReturn(entityToSave);
        when(eduLevelCrudRepo.save(entityToSave)).thenReturn(savedEntity);
        when(educationalLevelMapper.toDomain(savedEntity)).thenReturn(savedDomain);

        // Act
        EducationalLevelDomain result = educationalLevelAdapter.save(domainToSave);

        // Assert
        assertEquals(savedDomain, result);
        assertEquals("A", domainToSave.getStatus()); // Verificar que se estableció el status a "A"
        verify(educationalLevelMapper).toEntity(domainToSave);
        verify(eduLevelCrudRepo).save(entityToSave);
        verify(educationalLevelMapper).toDomain(savedEntity);
    }

    @Test
    void update_WhenEducationalLevelExists_ShouldUpdateAndReturnEducationalLevel() {
        // Arrange
        Integer id = 1;

        EducationalLevelDomain domainToUpdate = EducationalLevelDomain.builder()
                .id(1)
                .levelName("Bachillerato")
                .status("A")
                .build();

        EducationalLevel existingEntity = EducationalLevel.builder()
                .id(1)
                .levelName("Primaria")
                .status("A")
                .build();

        EducationalLevel updatedEntity = EducationalLevel.builder()
                .id(1)
                .levelName("Bachillerato")
                .status("A")
                .build();

        when(eduLevelCrudRepo.findById(id)).thenReturn(Optional.of(existingEntity));
        when(eduLevelCrudRepo.save(any(EducationalLevel.class))).thenReturn(updatedEntity);
        when(educationalLevelMapper.toDomain(updatedEntity)).thenReturn(domainToUpdate);

        // Act
        EducationalLevelDomain result = educationalLevelAdapter.update(id, domainToUpdate);

        // Assert
        assertEquals(domainToUpdate, result);
        verify(eduLevelCrudRepo).findById(id);
        verify(eduLevelCrudRepo).save(any(EducationalLevel.class));
        verify(educationalLevelMapper).toDomain(updatedEntity);
    }

    @Test
    void update_WhenEducationalLevelDoesNotExist_ShouldThrowNoSuchElementException() {
        // Arrange
        Integer id = 999;
        EducationalLevelDomain domainToUpdate = EducationalLevelDomain.builder()
                .id(999)
                .levelName("Bachillerato")
                .status("A")
                .build();

        when(eduLevelCrudRepo.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            educationalLevelAdapter.update(id, domainToUpdate);
        });

        verify(eduLevelCrudRepo).findById(id);
        verify(eduLevelCrudRepo, never()).save(any(EducationalLevel.class));
    }

    @Test
    void delete_WhenEducationalLevelDoesNotExist_ShouldThrowAppException() {
        // Arrange
        Integer id = 999;

        when(eduLevelCrudRepo.existsById(id)).thenReturn(false);

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> {
            educationalLevelAdapter.delete(id);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getCode());
        verify(eduLevelCrudRepo).existsById(id);
        verify(eduLevelCrudRepo, never()).updateStatusById(anyString(), anyInt());
    }

    @Test
    void delete_WhenExceptionOccurs_ShouldThrowAppExceptionWithInternalServerError() {
        // Arrange
        Integer id = 1;

        when(eduLevelCrudRepo.existsById(id)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> {
            educationalLevelAdapter.delete(id);
        });

        assertEquals("INTERN ERROR", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getCode());
        verify(eduLevelCrudRepo).existsById(id);
    }
}
