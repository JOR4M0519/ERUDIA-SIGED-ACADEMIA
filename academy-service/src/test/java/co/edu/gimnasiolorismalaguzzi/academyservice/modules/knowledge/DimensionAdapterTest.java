package co.edu.gimnasiolorismalaguzzi.academyservice.modules.knowledge;

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

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DimensionAdapterTest {

    @Mock
    private DimensionCrudRepo dimensionCrudRepo;

    @Mock
    private DimensionMapper dimensionMapper;

    private DimensionAdapter dimensionAdapter;

    private Dimension dimension;
    private DimensionDomain dimensionDomain;
    private List<Dimension> dimensions;
    private List<DimensionDomain> dimensionDomains;

    @BeforeEach
    void setUp() {
        dimensionAdapter = new DimensionAdapter(dimensionCrudRepo, dimensionMapper);

        // Inicializar entidades para pruebas
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

        dimensions = Arrays.asList(dimension);
        dimensionDomains = Arrays.asList(dimensionDomain);
    }

    @Test
    void findAll_ShouldReturnAllDimensions() {
        // Arrange
        when(dimensionCrudRepo.findAll()).thenReturn(dimensions);
        when(dimensionMapper.toDomains(dimensions)).thenReturn(dimensionDomains);

        // Act
        List<DimensionDomain> result = dimensionAdapter.findAll();

        // Assert
        assertEquals(dimensionDomains, result);
        verify(dimensionCrudRepo).findAll();
        verify(dimensionMapper).toDomains(dimensions);
    }

    @Test
    void findById_WhenDimensionExists_ShouldReturnDimension() {
        // Arrange
        Integer id = 1;
        when(dimensionCrudRepo.findById(id)).thenReturn(Optional.of(dimension));
        when(dimensionMapper.toDomain(dimension)).thenReturn(dimensionDomain);

        // Act
        DimensionDomain result = dimensionAdapter.findById(id);

        // Assert
        assertEquals(dimensionDomain, result);
        verify(dimensionCrudRepo).findById(id);
        verify(dimensionMapper).toDomain(dimension);
    }

    @Test
    void findById_WhenDimensionDoesNotExist_ShouldReturnNull() {
        // Arrange
        Integer id = 999;
        when(dimensionCrudRepo.findById(id)).thenReturn(Optional.empty());

        // Act
        DimensionDomain result = dimensionAdapter.findById(id);

        // Assert
        assertNull(result);
        verify(dimensionCrudRepo).findById(id);
        verify(dimensionMapper, never()).toDomain(any());
    }

    @Test
    void save_ShouldSaveDimension() {
        // Arrange
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

        // Act
        DimensionDomain result = dimensionAdapter.save(domainToSave);

        // Assert
        assertEquals(savedDomain, result);
        verify(dimensionMapper).toEntity(domainToSave);
        verify(dimensionCrudRepo).save(entityToSave);
        verify(dimensionMapper).toDomain(savedEntity);
    }

    @Test
    void update_WhenDimensionExists_ShouldUpdateAndReturnDimension() {
        // Arrange
        Integer id = 1;
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

        when(dimensionCrudRepo.findById(id)).thenReturn(Optional.of(existingEntity));
        when(dimensionCrudRepo.save(any(Dimension.class))).thenReturn(updatedEntity);
        when(dimensionMapper.toDomain(updatedEntity)).thenReturn(updatedDomain);

        // Act
        DimensionDomain result = dimensionAdapter.update(id, domainToUpdate);

        // Assert
        assertEquals(updatedDomain, result);
        verify(dimensionCrudRepo).findById(id);
        verify(dimensionCrudRepo).save(any(Dimension.class));
        verify(dimensionMapper).toDomain(updatedEntity);
    }

    @Test
    void update_WhenDimensionDoesNotExist_ShouldThrowEntityNotFoundException() {
        // Arrange
        Integer id = 999;
        DimensionDomain domainToUpdate = DimensionDomain.builder()
                .id(999)
                .name("Dimensión Actualizada")
                .description("Descripción actualizada")
                .build();

        when(dimensionCrudRepo.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            dimensionAdapter.update(id, domainToUpdate);
        });

        verify(dimensionCrudRepo).findById(id);
        verify(dimensionCrudRepo, never()).save(any(Dimension.class));
    }

}
