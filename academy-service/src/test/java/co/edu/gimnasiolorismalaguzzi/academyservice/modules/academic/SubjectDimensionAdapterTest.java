package co.edu.gimnasiolorismalaguzzi.academyservice.modules.academic;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectDimensionDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.Subject;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectDimension;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectDimensionMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectDimensionCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.SubjectDimensionAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.entity.Dimension;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubjectDimensionAdapterTest {

    @Mock
    private SubjectDimensionCrudRepo subjectDimensionCrudRepo;

    @Mock
    private SubjectDimensionMapper subjectDimensionMapper;

    private SubjectDimensionAdapter subjectDimensionAdapter;

    private Subject subject;
    private Dimension dimension;
    private SubjectDimension subjectDimension;
    private SubjectDimensionDomain subjectDimensionDomain;
    private List<SubjectDimension> subjectDimensions;
    private List<SubjectDimensionDomain> subjectDimensionDomains;

    @BeforeEach
    void setUp() {
        // Crear la instancia con el constructor
        subjectDimensionAdapter = new SubjectDimensionAdapter(subjectDimensionCrudRepo);

        // Inyectar manualmente el mapper usando ReflectionTestUtils
        ReflectionTestUtils.setField(subjectDimensionAdapter, "subjectDimensionMapper", subjectDimensionMapper);

        // Inicializar entidades
        subject = Subject.builder()
                .id(1)
                .subjectName("Mathematics")
                .status("A")
                .build();

        dimension = Dimension.builder()
                .id(1)
                .name("Algebra")
                .build();

        subjectDimension = SubjectDimension.builder()
                .id(1)
                .subject(subject)
                .dimension(dimension)
                .build();

        // Inicializar dominio
        subjectDimensionDomain = SubjectDimensionDomain.builder()
                .id(1)
                .subject(subject)
                .dimension(dimension)
                .build();

        // Inicializar listas
        subjectDimensions = Arrays.asList(subjectDimension);
        subjectDimensionDomains = Arrays.asList(subjectDimensionDomain);
    }

    @Test
    void findAll_ShouldReturnAllSubjectDimensions() {
        // Arrange
        when(subjectDimensionCrudRepo.findAll()).thenReturn(subjectDimensions);
        when(subjectDimensionMapper.toDomains(subjectDimensions)).thenReturn(subjectDimensionDomains);

        // Act
        List<SubjectDimensionDomain> result = subjectDimensionAdapter.findAll();

        // Assert
        assertEquals(subjectDimensionDomains, result);
        verify(subjectDimensionCrudRepo).findAll();
        verify(subjectDimensionMapper).toDomains(subjectDimensions);
    }

    @Test
    void findById_WhenSubjectDimensionExists_ShouldReturnSubjectDimension() {
        // Arrange
        Integer id = 1;
        when(subjectDimensionCrudRepo.findById(id)).thenReturn(Optional.of(subjectDimension));
        when(subjectDimensionMapper.toDomain(subjectDimension)).thenReturn(subjectDimensionDomain);

        // Act
        SubjectDimensionDomain result = subjectDimensionAdapter.findById(id);

        // Assert
        assertEquals(subjectDimensionDomain, result);
        verify(subjectDimensionCrudRepo).findById(id);
        verify(subjectDimensionMapper).toDomain(subjectDimension);
    }

    @Test
    void findById_WhenSubjectDimensionDoesNotExist_ShouldReturnNull() {
        // Arrange
        Integer id = 999;
        when(subjectDimensionCrudRepo.findById(id)).thenReturn(Optional.empty());

        // Act
        SubjectDimensionDomain result = subjectDimensionAdapter.findById(id);

        // Assert
        assertNull(result);
        verify(subjectDimensionCrudRepo).findById(id);
        verify(subjectDimensionMapper, never()).toDomain(any());
    }

    @Test
    void save_ShouldSaveSubjectDimension() {
        // Arrange
        SubjectDimensionDomain domainToSave = SubjectDimensionDomain.builder()
                .subject(subject)
                .dimension(dimension)
                .build();

        SubjectDimension entityToSave = SubjectDimension.builder()
                .subject(subject)
                .dimension(dimension)
                .build();

        SubjectDimension savedEntity = SubjectDimension.builder()
                .id(1)
                .subject(subject)
                .dimension(dimension)
                .build();

        SubjectDimensionDomain savedDomain = SubjectDimensionDomain.builder()
                .id(1)
                .subject(subject)
                .dimension(dimension)
                .build();

        when(subjectDimensionMapper.toEntity(domainToSave)).thenReturn(entityToSave);
        when(subjectDimensionCrudRepo.save(entityToSave)).thenReturn(savedEntity);
        when(subjectDimensionMapper.toDomain(savedEntity)).thenReturn(savedDomain);

        // Act
        SubjectDimensionDomain result = subjectDimensionAdapter.save(domainToSave);

        // Assert
        assertEquals(savedDomain, result);
        verify(subjectDimensionMapper).toEntity(domainToSave);
        verify(subjectDimensionCrudRepo).save(entityToSave);
        verify(subjectDimensionMapper).toDomain(savedEntity);
    }

    @Test
    void update_WhenSubjectDimensionExists_ShouldUpdateAndReturnSubjectDimension() {
        // Arrange
        Integer id = 1;

        Dimension newDimension = Dimension.builder()
                .id(2)
                .name("Geometry")
                .build();

        SubjectDimensionDomain domainToUpdate = SubjectDimensionDomain.builder()
                .id(1)
                .subject(subject)
                .dimension(newDimension)
                .build();

        SubjectDimension existingEntity = SubjectDimension.builder()
                .id(1)
                .subject(subject)
                .dimension(dimension)
                .build();

        SubjectDimension updatedEntity = SubjectDimension.builder()
                .id(1)
                .subject(subject)
                .dimension(newDimension)
                .build();

        when(subjectDimensionCrudRepo.findById(id)).thenReturn(Optional.of(existingEntity));
        when(subjectDimensionCrudRepo.save(any(SubjectDimension.class))).thenReturn(updatedEntity);
        when(subjectDimensionMapper.toDomain(updatedEntity)).thenReturn(domainToUpdate);

        // Act
        SubjectDimensionDomain result = subjectDimensionAdapter.update(id, domainToUpdate);

        // Assert
        assertEquals(domainToUpdate, result);
        verify(subjectDimensionCrudRepo).findById(id);
        verify(subjectDimensionCrudRepo).save(any(SubjectDimension.class));
        verify(subjectDimensionMapper).toDomain(updatedEntity);
    }

    @Test
    void update_WhenSubjectDimensionDoesNotExist_ShouldThrowEntityNotFoundException() {
        // Arrange
        Integer id = 999;
        SubjectDimensionDomain domainToUpdate = SubjectDimensionDomain.builder()
                .id(999)
                .subject(subject)
                .dimension(dimension)
                .build();

        when(subjectDimensionCrudRepo.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            subjectDimensionAdapter.update(id, domainToUpdate);
        });

        verify(subjectDimensionCrudRepo).findById(id);
        verify(subjectDimensionCrudRepo, never()).save(any(SubjectDimension.class));
    }

    @Test
    void delete_ShouldReturnTeapotStatus() {
        // Act
        HttpStatus result = subjectDimensionAdapter.delete(1);

        // Assert
        assertEquals(HttpStatus.I_AM_A_TEAPOT, result);
        verifyNoInteractions(subjectDimensionCrudRepo); // Verifica que no se interactúa con el repositorio
    }
}
