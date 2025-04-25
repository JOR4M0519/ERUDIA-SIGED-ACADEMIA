package co.edu.gimnasiolorismalaguzzi.academyservice.modules.academic;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.AcademicPeriodDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.AcademicYearPercentageDTO;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.AcademicPeriod;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.AcademicPeriodMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.AcademicPeriodCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.AcademicPeriodAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.GradeSettingDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.GradeSetting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
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
        List<AcademicPeriodDomain> result = academicPeriodAdapter.getActivePeriodsByYear(year);

        // Assert
        assertEquals(academicPeriodDomains, result);
        verify(academicPeriodCrudRepo).getPeriodsByYear(year);
        verify(academicPeriodMapper).toDomains(academicPeriods);
    }


    @Test
    @DisplayName("delete debe lanzar NOT_FOUND si el periodo no existe")
    void delete_WhenPeriodDoesNotExist_ShouldThrowNotFound() {
        // Arrange
        Integer id = 1;
        when(academicPeriodCrudRepo.existsById(id)).thenReturn(false);

        // Act & Assert
        AppException ex = assertThrows(AppException.class, () -> academicPeriodAdapter.delete(id));

        assertEquals(HttpStatus.NOT_FOUND, ex.getCode());
        assertEquals("El periodo no existe", ex.getMessage());
        verify(academicPeriodCrudRepo).existsById(id);
        verify(academicPeriodCrudRepo, never()).deleteById(any());
    }

    @Test
    @DisplayName("delete elimina periodo existente y devuelve OK")
    void delete_WhenPeriodExists_ShouldDeleteAndReturnOk() {
        // Arrange
        Integer id = 1;
        when(academicPeriodCrudRepo.existsById(id)).thenReturn(true);

        // Act
        HttpStatus result = academicPeriodAdapter.delete(id);

        // Then
        assertEquals(HttpStatus.OK, result);
        verify(academicPeriodCrudRepo).existsById(id);
        verify(academicPeriodCrudRepo).deleteById(id);
    }

    @Test
    @DisplayName("delete lanza INTERNAL_SERVER_ERROR cuando falla deleteById")
    void delete_WhenExceptionOccurs_ShouldThrowInternalServerError() {
        // Arrange
        Integer id = 1;
        when(academicPeriodCrudRepo.existsById(id)).thenReturn(true);
        doThrow(new RuntimeException("DB error"))
                .when(academicPeriodCrudRepo).deleteById(id);

        // Act & Assert
        AppException ex = assertThrows(AppException.class, () -> academicPeriodAdapter.delete(id));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getCode());
        assertEquals("Intern Error!", ex.getMessage());
        verify(academicPeriodCrudRepo).existsById(id);
        verify(academicPeriodCrudRepo).deleteById(id);
    }
    @Test
    void getAcademicYearsWithPercentages_ShouldReturnYearsWithPercentages() {
        // Arrange
        List<Object[]> mockResults = new ArrayList<>();
        mockResults.add(new Object[]{2023, 100});
        mockResults.add(new Object[]{2024, 80});

        when(academicPeriodCrudRepo.getAcademicYearsWithPercentages()).thenReturn(mockResults);

        // Act
        List<AcademicYearPercentageDTO> result = academicPeriodAdapter.getAcademicYearsWithPercentages();

        // Assert
        assertEquals(2, result.size());
        assertEquals(2023, result.get(0).getYear());
        assertEquals(100, result.get(0).getTotalPercentage());
        assertTrue(result.get(0).getIsComplete());
        assertEquals(2024, result.get(1).getYear());
        assertEquals(80, result.get(1).getTotalPercentage());
        assertFalse(result.get(1).getIsComplete());

        verify(academicPeriodCrudRepo).getAcademicYearsWithPercentages();
    }

    @Test
    void verifyYearPercentages_ShouldReturnRepositoryResult() {
        // Arrange
        Integer year = 2023;
        when(academicPeriodCrudRepo.verifyYearPercentages(year)).thenReturn(true);

        // Act
        Boolean result = academicPeriodAdapter.verifyYearPercentages(year);

        // Assert
        assertTrue(result);
        verify(academicPeriodCrudRepo).verifyYearPercentages(year);
    }

    @Test
    void update_WhenChangingToActiveWithIncompletePercentage_ShouldThrowException() {
        // Arrange
        Integer id = 1;
        AcademicPeriodDomain domainToUpdate = AcademicPeriodDomain.builder()
                .id(1)
                .name("2023-01")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 6, 30))
                .status("A")
                .build();

        AcademicPeriod existingEntity = AcademicPeriod.builder()
                .id(1)
                .name("2023-01")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 6, 30))
                .status("I")
                .build();

        when(academicPeriodCrudRepo.findById(id)).thenReturn(Optional.of(existingEntity));
        when(academicPeriodCrudRepo.verifyYearPercentages(2023)).thenReturn(false);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            academicPeriodAdapter.update(id, domainToUpdate);
        });

        assertTrue(exception.getMessage().contains("no suman exactamente 100%"));
        verify(academicPeriodCrudRepo).findById(id);
        verify(academicPeriodCrudRepo).verifyYearPercentages(2023);
    }

    @Test
    void getPeriodsBySettingsAndYear_ShouldReturnFilteredPeriods() {
        // Arrange
        Integer settingId = 1;
        String year = "2023";

        GradeSetting gradeSetting = new GradeSetting();
        gradeSetting.setId(settingId);

        AcademicPeriod period1 = AcademicPeriod.builder()
                .id(1)
                .name("2023-01")
                .setting(gradeSetting)
                .build();

        AcademicPeriod period2 = AcademicPeriod.builder()
                .id(2)
                .name("2023-02")
                .setting(gradeSetting)
                .build();

        List<AcademicPeriod> periods = Arrays.asList(period1, period2);

        GradeSettingDomain gradeSettingDomain =GradeSettingDomain.builder().build();
        gradeSettingDomain.setId(settingId);

        AcademicPeriodDomain periodDomain1 = AcademicPeriodDomain.builder()
                .id(1)
                .name("2023-01")
                .gradeSetting(gradeSettingDomain)
                .build();

        AcademicPeriodDomain periodDomain2 = AcademicPeriodDomain.builder()
                .id(2)
                .name("2023-02")
                .gradeSetting(gradeSettingDomain)
                .build();

        List<AcademicPeriodDomain> periodDomains = Arrays.asList(periodDomain1, periodDomain2);

        when(academicPeriodCrudRepo.getPeriodsByYear(year)).thenReturn(periods);
        when(academicPeriodMapper.toDomains(periods)).thenReturn(periodDomains);

        // Act
        List<AcademicPeriodDomain> result = academicPeriodAdapter.getPeriodsBySettingsAndYear(settingId, year);

        // Assert
        assertEquals(2, result.size());
        verify(academicPeriodCrudRepo).getPeriodsByYear(year);
        verify(academicPeriodMapper).toDomains(periods);
    }


}
