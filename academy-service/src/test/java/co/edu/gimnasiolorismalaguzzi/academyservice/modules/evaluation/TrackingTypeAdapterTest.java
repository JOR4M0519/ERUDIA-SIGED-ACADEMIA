package co.edu.gimnasiolorismalaguzzi.academyservice.modules.evaluation;

import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.TrackingTypeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.TrackingType;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.mapper.TrackingTypeMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.repository.TrackingTypeCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.TrackingTypeAdapter;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrackingTypeAdapterTest {

    @Mock
    private TrackingTypeCrudRepo trackingTypeCrudRepo;

    @Mock
    private TrackingTypeMapper trackingTypeMapper;

    @InjectMocks
    private TrackingTypeAdapter trackingTypeAdapter;

    private TrackingType trackingType1;
    private TrackingType trackingType2;
    private TrackingTypeDomain trackingTypeDomain1;
    private TrackingTypeDomain trackingTypeDomain2;
    private List<TrackingType> trackingTypeList;
    private List<TrackingTypeDomain> trackingTypeDomainList;

    @BeforeEach
    void setUp() {
        // Aseguramos que el mapper sea inyectado correctamente
        ReflectionTestUtils.setField(trackingTypeAdapter, "trackingTypeMapper", trackingTypeMapper);

        // Configurar datos de prueba
        trackingType1 = new TrackingType();
        trackingType1.setId(1);
        trackingType1.setType("Académico");

        trackingType2 = new TrackingType();
        trackingType2.setId(2);
        trackingType2.setType("Comportamental");

        trackingTypeDomain1 = TrackingTypeDomain.builder()
                .id(1)
                .type("Académico")
                .build();

        trackingTypeDomain2 = TrackingTypeDomain.builder()
                .id(2)
                .type("Comportamental")
                .build();

        trackingTypeList = Arrays.asList(trackingType1, trackingType2);
        trackingTypeDomainList = Arrays.asList(trackingTypeDomain1, trackingTypeDomain2);
    }

    @Test
    void findAll_ShouldReturnAllTrackingTypes() {
        // Arrange
        when(trackingTypeCrudRepo.findAll()).thenReturn(trackingTypeList);
        when(trackingTypeMapper.toDomains(trackingTypeList)).thenReturn(trackingTypeDomainList);

        // Act
        List<TrackingTypeDomain> result = trackingTypeAdapter.findAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals(trackingTypeDomainList, result);
        verify(trackingTypeCrudRepo).findAll();
        verify(trackingTypeMapper).toDomains(trackingTypeList);
    }

    @Test
    void findAll_WhenNoTrackingTypes_ShouldReturnEmptyList() {
        // Arrange
        when(trackingTypeCrudRepo.findAll()).thenReturn(List.of());
        when(trackingTypeMapper.toDomains(List.of())).thenReturn(List.of());

        // Act
        List<TrackingTypeDomain> result = trackingTypeAdapter.findAll();

        // Assert
        assertTrue(result.isEmpty());
        verify(trackingTypeCrudRepo).findAll();
        verify(trackingTypeMapper).toDomains(List.of());
    }

    @Test
    void findAll_WhenRepositoryThrowsException_ShouldPropagateException() {
        // Arrange
        when(trackingTypeCrudRepo.findAll()).thenThrow(new DataAccessException("Database connection error") {});

        // Act & Assert
        assertThrows(DataAccessException.class, () -> trackingTypeAdapter.findAll());
        verify(trackingTypeCrudRepo).findAll();
        verify(trackingTypeMapper, never()).toDomains(any());
    }

    @Test
    void findById_WhenTrackingTypeExists_ShouldReturnTrackingType() {
        // Arrange
        Integer id = 1;
        when(trackingTypeCrudRepo.findById(id)).thenReturn(Optional.of(trackingType1));
        when(trackingTypeMapper.toDomain(trackingType1)).thenReturn(trackingTypeDomain1);

        // Act
        TrackingTypeDomain result = trackingTypeAdapter.findById(id);

        // Assert
        assertNotNull(result);
        assertEquals(trackingTypeDomain1, result);
        verify(trackingTypeCrudRepo).findById(id);
        verify(trackingTypeMapper).toDomain(trackingType1);
    }

    @Test
    void findById_WhenTrackingTypeDoesNotExist_ShouldReturnNull() {
        // Arrange
        Integer id = 999;
        when(trackingTypeCrudRepo.findById(id)).thenReturn(Optional.empty());

        // Act
        TrackingTypeDomain result = trackingTypeAdapter.findById(id);

        // Assert
        assertNull(result);
        verify(trackingTypeCrudRepo).findById(id);
        verify(trackingTypeMapper, never()).toDomain(any());
    }

    @Test
    void findById_WithNullId_ShouldHandleNullParameter() {
        // Arrange
        when(trackingTypeCrudRepo.findById(null)).thenReturn(Optional.empty());

        // Act
        TrackingTypeDomain result = trackingTypeAdapter.findById(null);

        // Assert
        assertNull(result);
        verify(trackingTypeCrudRepo).findById(null);
        verify(trackingTypeMapper, never()).toDomain(any());
    }

    @Test
    void findById_WhenRepositoryThrowsException_ShouldPropagateException() {
        // Arrange
        Integer id = 1;
        when(trackingTypeCrudRepo.findById(id)).thenThrow(new DataAccessException("Database error") {});

        // Act & Assert
        assertThrows(DataAccessException.class, () -> trackingTypeAdapter.findById(id));
        verify(trackingTypeCrudRepo).findById(id);
        verify(trackingTypeMapper, never()).toDomain(any());
    }

    @Test
    void save_ShouldSaveAndReturnTrackingType() {
        // Arrange
        TrackingTypeDomain domainToSave = TrackingTypeDomain.builder()
                .type("Nuevo Tipo")
                .build();

        TrackingType entityToSave = new TrackingType();
        entityToSave.setType("Nuevo Tipo");

        TrackingType savedEntity = new TrackingType();
        savedEntity.setId(3);
        savedEntity.setType("Nuevo Tipo");

        TrackingTypeDomain savedDomain = TrackingTypeDomain.builder()
                .id(3)
                .type("Nuevo Tipo")
                .build();

        when(trackingTypeMapper.toEntity(domainToSave)).thenReturn(entityToSave);
        when(trackingTypeCrudRepo.save(entityToSave)).thenReturn(savedEntity);
        when(trackingTypeMapper.toDomain(savedEntity)).thenReturn(savedDomain);

        // Act
        TrackingTypeDomain result = trackingTypeAdapter.save(domainToSave);

        // Assert
        assertNotNull(result);
        assertEquals(savedDomain, result);
        verify(trackingTypeMapper).toEntity(domainToSave);
        verify(trackingTypeCrudRepo).save(entityToSave);
        verify(trackingTypeMapper).toDomain(savedEntity);
    }

    @Test
    void save_WithNullDomain_ShouldHandleNullParameter() {
        // Arrange
        when(trackingTypeMapper.toEntity(null)).thenReturn(null);
        when(trackingTypeCrudRepo.save(null)).thenThrow(
                new IllegalArgumentException("Entity to save cannot be null"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> trackingTypeAdapter.save(null));
        verify(trackingTypeMapper).toEntity(null);
        verify(trackingTypeCrudRepo).save(null);
    }

    @Test
    void save_WhenRepositoryThrowsException_ShouldPropagateException() {
        // Arrange
        TrackingTypeDomain domainToSave = TrackingTypeDomain.builder()
                .type("Nuevo Tipo")
                .build();

        TrackingType entityToSave = new TrackingType();
        entityToSave.setType("Nuevo Tipo");

        when(trackingTypeMapper.toEntity(domainToSave)).thenReturn(entityToSave);
        when(trackingTypeCrudRepo.save(entityToSave)).thenThrow(
                new DataIntegrityViolationException("Unique constraint violation"));

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> trackingTypeAdapter.save(domainToSave));
        verify(trackingTypeMapper).toEntity(domainToSave);
        verify(trackingTypeCrudRepo).save(entityToSave);
    }

    @Test
    void save_WithExistingId_ShouldUpdateExistingRecord() {
        // Arrange
        TrackingTypeDomain domainToSave = TrackingTypeDomain.builder()
                .id(1)  // Existing ID
                .type("Tipo Actualizado")
                .build();

        TrackingType entityToSave = new TrackingType();
        entityToSave.setId(1);
        entityToSave.setType("Tipo Actualizado");

        TrackingType savedEntity = new TrackingType();
        savedEntity.setId(1);
        savedEntity.setType("Tipo Actualizado");

        TrackingTypeDomain savedDomain = TrackingTypeDomain.builder()
                .id(1)
                .type("Tipo Actualizado")
                .build();

        when(trackingTypeMapper.toEntity(domainToSave)).thenReturn(entityToSave);
        when(trackingTypeCrudRepo.save(entityToSave)).thenReturn(savedEntity);
        when(trackingTypeMapper.toDomain(savedEntity)).thenReturn(savedDomain);

        // Act
        TrackingTypeDomain result = trackingTypeAdapter.save(domainToSave);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Tipo Actualizado", result.getType());
        verify(trackingTypeMapper).toEntity(domainToSave);
        verify(trackingTypeCrudRepo).save(entityToSave);
        verify(trackingTypeMapper).toDomain(savedEntity);
    }

    @Test
    void update_WhenTrackingTypeExists_ShouldUpdateAndReturnTrackingType() {
        // Arrange
        Integer id = 1;
        TrackingTypeDomain domainToUpdate = TrackingTypeDomain.builder()
                .type("Tipo Actualizado")
                .build();

        TrackingType entityToUpdate = new TrackingType();
        entityToUpdate.setId(1);
        entityToUpdate.setType("Tipo Actualizado");

        TrackingType existingEntity = new TrackingType();
        existingEntity.setId(1);
        existingEntity.setType("Académico");

        // Simulamos que la entidad actualizada mantiene el mismo ID
        when(trackingTypeMapper.toEntity(domainToUpdate)).thenReturn(entityToUpdate);
        when(trackingTypeCrudRepo.findById(id)).thenReturn(Optional.of(existingEntity));
        when(trackingTypeCrudRepo.save(any(TrackingType.class))).thenAnswer(invocation -> {
            TrackingType saved = invocation.getArgument(0);
            saved.setType(entityToUpdate.getType());
            return saved;
        });
        when(trackingTypeMapper.toDomain(any(TrackingType.class))).thenReturn(domainToUpdate);

        // Act
        TrackingTypeDomain result = trackingTypeAdapter.update(id, domainToUpdate);

        // Assert
        assertNotNull(result);
        assertEquals(domainToUpdate, result);
        verify(trackingTypeCrudRepo).findById(id);
        verify(trackingTypeCrudRepo).save(any(TrackingType.class));
    }

    @Test
    void update_WhenTrackingTypeDoesNotExist_ShouldThrowEntityNotFoundException() {
        // Arrange
        Integer id = 999;
        TrackingTypeDomain domainToUpdate = TrackingTypeDomain.builder()
                .type("Tipo Actualizado")
                .build();

        when(trackingTypeCrudRepo.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            trackingTypeAdapter.update(id, domainToUpdate);
        });

        verify(trackingTypeCrudRepo).findById(id);
        verify(trackingTypeCrudRepo, never()).save(any());
    }

    @Test
    void update_WhenExceptionOccurs_ShouldThrowEntityNotFoundException() {
        // Arrange
        Integer id = 1;
        TrackingTypeDomain domainToUpdate = TrackingTypeDomain.builder()
                .type("Tipo Actualizado")
                .build();

        when(trackingTypeCrudRepo.findById(id)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            trackingTypeAdapter.update(id, domainToUpdate);
        });

        verify(trackingTypeCrudRepo).findById(id);
        verify(trackingTypeCrudRepo, never()).save(any());
    }

    @Test
    void update_WithNullId_ShouldThrowEntityNotFoundException() {
        // Arrange
        TrackingTypeDomain domainToUpdate = TrackingTypeDomain.builder()
                .type("Tipo Actualizado")
                .build();

        when(trackingTypeCrudRepo.findById(null)).thenThrow(new IllegalArgumentException("ID cannot be null"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> trackingTypeAdapter.update(null, domainToUpdate));
        verify(trackingTypeCrudRepo).findById(null);
        verify(trackingTypeCrudRepo, never()).save(any());
    }

    @Test
    void update_WithNullDomain_ShouldThrowEntityNotFoundException() {
        // Arrange
        Integer id = 1;

        when(trackingTypeCrudRepo.findById(id)).thenReturn(Optional.of(trackingType1));
        when(trackingTypeMapper.toEntity(null)).thenThrow(new NullPointerException("Domain cannot be null"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> trackingTypeAdapter.update(id, null));
        verify(trackingTypeCrudRepo).findById(id);
    }

    @Test
    void delete_ShouldReturnTeapotStatus() {
        // Arrange
        Integer id = 1;

        // Act
        HttpStatus result = trackingTypeAdapter.delete(id);

        // Assert
        assertEquals(HttpStatus.I_AM_A_TEAPOT, result);
    }

    @Test
    void delete_WithNullId_ShouldStillReturnTeapotStatus() {
        // Act
        HttpStatus result = trackingTypeAdapter.delete(null);

        // Assert
        assertEquals(HttpStatus.I_AM_A_TEAPOT, result);
    }

    @Test
    void delete_ShouldNotCallRepositoryDeleteMethod() {
        // Arrange
        Integer id = 1;

        // Act
        HttpStatus result = trackingTypeAdapter.delete(id);

        // Assert
        assertEquals(HttpStatus.I_AM_A_TEAPOT, result);
        verify(trackingTypeCrudRepo, never()).deleteById(anyInt());
    }
}