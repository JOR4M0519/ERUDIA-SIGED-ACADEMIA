package co.edu.gimnasiolorismalaguzzi.academyservice.modules.knowledge;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceSubjectDimensionPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.domain.DimensionDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.entity.Dimension;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.mapper.DimensionMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.repository.DimensionCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.service.DimensionAdapter;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DimensionAdapterTest {

    @Mock
    private DimensionCrudRepo dimensionCrudRepo;

    @Mock
    private DimensionMapper dimensionMapper;

    @Mock
    private PersistenceSubjectDimensionPort subjectDimensionPort;

    private DimensionAdapter dimensionAdapter;
    private Dimension dimension;
    private DimensionDomain dimensionDomain;
    private List<Dimension> dimensions;
    private List<DimensionDomain> dimensionDomains;

    @BeforeEach
    void setUp() {
        dimensionAdapter = new DimensionAdapter(dimensionCrudRepo, dimensionMapper, subjectDimensionPort);

        dimension = Dimension.builder()
                .id(1)
                .name("Dimensión Test")
                .description("Descripción de prueba")
                .build();

        dimensionDomain = DimensionDomain.builder()
                .id(1)
                .name("Dimensión Test")
                .description("Descripción de prueba")
                .build();

        dimensions = List.of(dimension);
        dimensionDomains = List.of(dimensionDomain);
    }

    @Test
    void findAll_ShouldReturnAllDimensions() {
        when(dimensionCrudRepo.findAll()).thenReturn(dimensions);
        when(dimensionMapper.toDomains(dimensions)).thenReturn(dimensionDomains);

        List<DimensionDomain> result = dimensionAdapter.findAll();

        assertEquals(dimensionDomains, result);
        verify(dimensionCrudRepo).findAll();
        verify(dimensionMapper).toDomains(dimensions);
    }

    @Test
    void findById_WhenDimensionExists_ShouldReturnDimension() {
        when(dimensionCrudRepo.findById(1)).thenReturn(Optional.of(dimension));
        when(dimensionMapper.toDomain(dimension)).thenReturn(dimensionDomain);

        DimensionDomain result = dimensionAdapter.findById(1);

        assertEquals(dimensionDomain, result);
        verify(dimensionCrudRepo).findById(1);
        verify(dimensionMapper).toDomain(dimension);
    }

    @Test
    void findById_WhenDimensionDoesNotExist_ShouldReturnNull() {
        when(dimensionCrudRepo.findById(999)).thenReturn(Optional.empty());

        DimensionDomain result = dimensionAdapter.findById(999);

        assertNull(result);
        verify(dimensionCrudRepo).findById(999);
        verify(dimensionMapper, never()).toDomain(any());
    }

    @Test
    void save_ShouldSaveDimension() {
        DimensionDomain domainToSave = DimensionDomain.builder()
                .name("Nueva Dimensión")
                .description("Nueva descripción")
                .build();

        Dimension entityToSave = Dimension.builder()
                .name("Nueva Dimensión")
                .description("Nueva descripción")
                .build();

        Dimension savedEntity = Dimension.builder()
                .id(1)
                .name("Nueva Dimensión")
                .description("Nueva descripción")
                .build();

        DimensionDomain savedDomain = DimensionDomain.builder()
                .id(1)
                .name("Nueva Dimensión")
                .description("Nueva descripción")
                .build();

        when(dimensionMapper.toEntity(domainToSave)).thenReturn(entityToSave);
        when(dimensionCrudRepo.save(entityToSave)).thenReturn(savedEntity);
        when(dimensionMapper.toDomain(savedEntity)).thenReturn(savedDomain);

        DimensionDomain result = dimensionAdapter.save(domainToSave);

        assertEquals(savedDomain, result);
        verify(dimensionMapper).toEntity(domainToSave);
        verify(dimensionCrudRepo).save(entityToSave);
        verify(dimensionMapper).toDomain(savedEntity);
    }

    @Test
    void update_WhenDimensionExists_ShouldUpdateAndReturnDimension() {
        DimensionDomain domainToUpdate = DimensionDomain.builder()
                .id(1)
                .name("Dimensión Actualizada")
                .description("Descripción actualizada")
                .build();

        Dimension existingEntity = Dimension.builder()
                .id(1)
                .name("Dimensión Original")
                .description("Descripción original")
                .build();

        Dimension updatedEntity = Dimension.builder()
                .id(1)
                .name("Dimensión Actualizada")
                .description("Descripción actualizada")
                .build();

        DimensionDomain updatedDomain = DimensionDomain.builder()
                .id(1)
                .name("Dimensión Actualizada")
                .description("Descripción actualizada")
                .build();

        when(dimensionCrudRepo.findById(1)).thenReturn(Optional.of(existingEntity));
        when(dimensionCrudRepo.save(existingEntity)).thenReturn(updatedEntity);
        when(dimensionMapper.toDomain(updatedEntity)).thenReturn(updatedDomain);

        DimensionDomain result = dimensionAdapter.update(1, domainToUpdate);

        assertEquals(updatedDomain, result);
        verify(dimensionCrudRepo).findById(1);
        verify(dimensionCrudRepo).save(existingEntity);
        verify(dimensionMapper).toDomain(updatedEntity);
    }

    @Test
    void update_WhenDimensionDoesNotExist_ShouldThrowNoSuchElementException() {
        when(dimensionCrudRepo.findById(999)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> dimensionAdapter.update(999, dimensionDomain));
        verify(dimensionCrudRepo).findById(999);
        verify(dimensionCrudRepo, never()).save(any(Dimension.class));
    }

    // --- delete ---

    @Test
    void delete_WhenDimensionNotExist_ShouldThrowAppExceptionNotFound() {
        // findById returns null
        when(dimensionCrudRepo.findById(2)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> dimensionAdapter.delete(2));
        assertEquals(HttpStatus.NOT_FOUND, ex.getCode());
    }

    @Test
    void delete_WhenDimensionDoesNotExist_ShouldThrowAppExceptionNotFound() {
        // Arrange
        when(dimensionCrudRepo.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        AppException ex = assertThrows(AppException.class, () -> dimensionAdapter.delete(1));
        assertEquals(HttpStatus.NOT_FOUND, ex.getCode());
        assertEquals("La dimension no existe", ex.getMessage());
    }

    @Test
    void delete_WhenNotUsed_ShouldDeleteAndReturnOk() {
        when(dimensionCrudRepo.findById(1)).thenReturn(Optional.of(dimension));
        when(dimensionMapper.toDomain(dimension)).thenReturn(dimensionDomain);
        when(subjectDimensionPort.getAllByDimensionId(1)).thenReturn(Collections.emptyList());

        HttpStatus status = dimensionAdapter.delete(1);
        assertEquals(HttpStatus.OK, status);
        verify(dimensionCrudRepo).deleteById(1);
    }
}
