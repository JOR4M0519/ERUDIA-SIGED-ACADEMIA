package co.edu.gimnasiolorismalaguzzi.academyservice.modules.academic;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.AcademicPeriodDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.AcademicPeriod;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.AcademicPeriodMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.AcademicPeriodCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.AcademicPeriodAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class AcademicPeriodAdapterTest {

    @Mock
    private AcademicPeriodCrudRepo academicPeriodCrudRepo;

    @Mock
    private AcademicPeriodMapper academicPeriodMapper;

    private AcademicPeriodAdapter academicPeriodAdapter;

    private AcademicPeriod academicPeriod;
    private AcademicPeriodDomain academicPeriodDomain;
    private List<AcademicPeriod> academicPeriods;
    private List<AcademicPeriodDomain> academicPeriodDomains;

    @BeforeEach
    void setUp() {
        // Crear la instancia con el constructor que solo acepta el repositorio
        academicPeriodAdapter = new AcademicPeriodAdapter(academicPeriodCrudRepo);

        // Inyectar manualmente el mapper usando ReflectionTestUtils
        ReflectionTestUtils.setField(academicPeriodAdapter, "academicPeriodMapper", academicPeriodMapper);

        // Inicializar entidad
        academicPeriod = AcademicPeriod.builder()
                .id(1)
                .name("2023-01")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 6, 30))
                .status("A")
                .build();

        // Inicializar dominio
        academicPeriodDomain = AcademicPeriodDomain.builder()
                .id(1)
                .name("2023-01")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 6, 30))
                .status("A")
                .build();

        // Inicializar listas
        academicPeriods = Arrays.asList(academicPeriod);
        academicPeriodDomains = Arrays.asList(academicPeriodDomain);
    }


    @Test
    void findAll_ShouldReturnAllPeriods() {
        // Arrange
        when(academicPeriodCrudRepo.findAll()).thenReturn(academicPeriods);
        when(academicPeriodMapper.toDomains(academicPeriods)).thenReturn(academicPeriodDomains);

        // Act
        List<AcademicPeriodDomain> result = academicPeriodAdapter.findAll();

        // Assert
        assertEquals(academicPeriodDomains, result);
        verify(academicPeriodCrudRepo).findAll();
        verify(academicPeriodMapper).toDomains(academicPeriods);
    }

    @Test
    void getAllPeriodsByStatus_ShouldReturnFilteredPeriods() {
        // Arrange
        String status = "A";
        when(academicPeriodCrudRepo.findByStatus(status)).thenReturn(academicPeriods);
        when(academicPeriodMapper.toDomains(academicPeriods)).thenReturn(academicPeriodDomains);

        // Act
        List<AcademicPeriodDomain> result = academicPeriodAdapter.getAllPeriodsByStatus(status);

        // Assert
        assertEquals(academicPeriodDomains, result);
        verify(academicPeriodCrudRepo).findByStatus(status);
        verify(academicPeriodMapper).toDomains(academicPeriods);
    }

    @Test
    void findById_WhenPeriodExists_ShouldReturnPeriod() {
        // Arrange
        Integer id = 1;
        when(academicPeriodCrudRepo.findById(id)).thenReturn(Optional.of(academicPeriod));
        when(academicPeriodMapper.toDomain(academicPeriod)).thenReturn(academicPeriodDomain);

        // Act
        AcademicPeriodDomain result = academicPeriodAdapter.findById(id);

        // Assert
        assertEquals(academicPeriodDomain, result);
        verify(academicPeriodCrudRepo).findById(id);
        verify(academicPeriodMapper).toDomain(academicPeriod);
    }

    @Test
    void findById_WhenPeriodDoesNotExist_ShouldReturnNull() {
        // Arrange
        Integer id = 999;
        when(academicPeriodCrudRepo.findById(id)).thenReturn(Optional.empty());

        // Act
        AcademicPeriodDomain result = academicPeriodAdapter.findById(id);

        // Assert
        assertNull(result);
        verify(academicPeriodCrudRepo).findById(id);
        verify(academicPeriodMapper, never()).toDomain(any());
    }

    @Test
    void save_ShouldSetStatusAndSavePeriod() {
        // Arrange
        AcademicPeriodDomain domainToSave = AcademicPeriodDomain.builder()
                .name("2023-02")
                .startDate(LocalDate.of(2023, 7, 1))
                .endDate(LocalDate.of(2023, 12, 31))
                .build();

        AcademicPeriod entityToSave = AcademicPeriod.builder()
                .name("2023-02")
                .startDate(LocalDate.of(2023, 7, 1))
                .endDate(LocalDate.of(2023, 12, 31))
                .status("A")
                .build();

        AcademicPeriod savedEntity = AcademicPeriod.builder()
                .id(2)
                .name("2023-02")
                .startDate(LocalDate.of(2023, 7, 1))
                .endDate(LocalDate.of(2023, 12, 31))
                .status("A")
                .build();

        AcademicPeriodDomain savedDomain = AcademicPeriodDomain.builder()
                .id(2)
                .name("2023-02")
                .startDate(LocalDate.of(2023, 7, 1))
                .endDate(LocalDate.of(2023, 12, 31))
                .status("A")
                .build();

        when(academicPeriodMapper.toEntity(any(AcademicPeriodDomain.class))).thenReturn(entityToSave);
        when(academicPeriodCrudRepo.save(entityToSave)).thenReturn(savedEntity);
        when(academicPeriodMapper.toDomain(savedEntity)).thenReturn(savedDomain);

        // Act
        AcademicPeriodDomain result = academicPeriodAdapter.save(domainToSave);

        // Assert
        assertEquals("A", domainToSave.getStatus());
        assertEquals(savedDomain, result);
        verify(academicPeriodMapper).toEntity(domainToSave);
        verify(academicPeriodCrudRepo).save(entityToSave);
        verify(academicPeriodMapper).toDomain(savedEntity);
    }

    @Test
    void update_WhenPeriodExists_ShouldUpdateAndReturnPeriod() {
        // Arrange
        Integer id = 1;
        AcademicPeriodDomain domainToUpdate = AcademicPeriodDomain.builder()
                .id(1)
                .name("2023-01-Updated")
                .startDate(LocalDate.of(2023, 2, 1))
                .endDate(LocalDate.of(2023, 7, 31))
                .status("A")
                .build();

        AcademicPeriod existingEntity = AcademicPeriod.builder()
                .id(1)
                .name("2023-01")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 6, 30))
                .status("A")
                .build();

        AcademicPeriod updatedEntity = AcademicPeriod.builder()
                .id(1)
                .name("2023-01-Updated")
                .startDate(LocalDate.of(2023, 2, 1))
                .endDate(LocalDate.of(2023, 7, 31))
                .status("A")
                .build();

        when(academicPeriodCrudRepo.findById(id)).thenReturn(Optional.of(existingEntity));
        when(academicPeriodCrudRepo.save(any(AcademicPeriod.class))).thenReturn(updatedEntity);
        when(academicPeriodMapper.toDomain(updatedEntity)).thenReturn(domainToUpdate);

        // Act
        AcademicPeriodDomain result = academicPeriodAdapter.update(id, domainToUpdate);

        // Assert
        assertEquals(domainToUpdate, result);
        verify(academicPeriodCrudRepo).findById(id);
        verify(academicPeriodCrudRepo).save(any(AcademicPeriod.class));
        verify(academicPeriodMapper).toDomain(updatedEntity);
    }


    @Test
    void getPeriodsByYear_ShouldReturnFilteredPeriods() {
        // Arrange
        String year = "2023";
        when(academicPeriodCrudRepo.getPeriodsByYear(year)).thenReturn(academicPeriods);
        when(academicPeriodMapper.toDomains(academicPeriods)).thenReturn(academicPeriodDomains);

        // Act
        List<AcademicPeriodDomain> result = academicPeriodAdapter.getPeriodsByYear(year);

        // Assert
        assertEquals(academicPeriodDomains, result);
        verify(academicPeriodCrudRepo).getPeriodsByYear(year);
        verify(academicPeriodMapper).toDomains(academicPeriods);
    }

    @Test
    void delete_WhenPeriodExists_ShouldUpdateStatusAndReturnOk() {
        // Arrange
        Integer id = 1;
        when(academicPeriodCrudRepo.existsById(id)).thenReturn(true);
        when(academicPeriodCrudRepo.updateStatusById("I", id)).thenReturn(1);

        // Act
        HttpStatus result = academicPeriodAdapter.delete(id);

        // Assert
        assertEquals(HttpStatus.OK, result);
        verify(academicPeriodCrudRepo).existsById(id);
        verify(academicPeriodCrudRepo).updateStatusById("I", id);
    }


    @Test
    void delete_WhenExceptionOccurs_ShouldThrowInternalServerError() {
        // Arrange
        Integer id = 1;
        when(academicPeriodCrudRepo.existsById(id)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> {
            academicPeriodAdapter.delete(id);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getCode());
        assertEquals("Intern Error!", exception.getMessage());
        verify(academicPeriodCrudRepo).existsById(id);
    }
}
