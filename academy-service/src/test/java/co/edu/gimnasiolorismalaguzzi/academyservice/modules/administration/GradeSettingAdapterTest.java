package co.edu.gimnasiolorismalaguzzi.academyservice.modules.administration;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.GradeSettingDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.GradeSetting;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.GradeSettingMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository.GradeSettingsCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.GradeSettingAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.AcademicPeriod;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.AcademicPeriodCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GradeSettingAdapterTest {

    @Mock private GradeSettingsCrudRepo crudRepo;
    @Mock private AcademicPeriodCrudRepo academicPeriodCrudRepo;
    @Mock private GradeSettingMapper mapper;
    @InjectMocks private GradeSettingAdapter adapter;

    @BeforeEach
    void setUp() {
        // ensure mapper field is injected since @InjectMocks uses constructor injection first
        ReflectionTestUtils.setField(adapter, "mapper", mapper);
    }

    // --- findAll ---

    @Test
    void findAll_returnsMappedDomains() {
        List<GradeSetting> entities = List.of(new GradeSetting());
        List<GradeSettingDomain> domains = List.of(GradeSettingDomain.builder().build());
        when(crudRepo.findAll()).thenReturn(entities);
        when(mapper.toDomains(entities)).thenReturn(domains);

        List<GradeSettingDomain> result = adapter.findAll();

        assertEquals(domains, result);
        verify(crudRepo).findAll();
        verify(mapper).toDomains(entities);
    }

    @Test
    void findAll_emptyList() {
        when(crudRepo.findAll()).thenReturn(Collections.emptyList());
        when(mapper.toDomains(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<GradeSettingDomain> result = adapter.findAll();

        assertTrue(result.isEmpty());
        verify(crudRepo).findAll();
        verify(mapper).toDomains(Collections.emptyList());
    }

    // --- findByLevelId ---

    @Test
    void findByLevelId_returnsMapped() {
        List<GradeSetting> entities = List.of(new GradeSetting());
        List<GradeSettingDomain> domains = List.of(GradeSettingDomain.builder().build());
        when(crudRepo.findByLevelId(42)).thenReturn(entities);
        when(mapper.toDomains(entities)).thenReturn(domains);

        List<GradeSettingDomain> result = adapter.findByLevelId(42);

        assertEquals(domains, result);
        verify(crudRepo).findByLevelId(42);
        verify(mapper).toDomains(entities);
    }

    @Test
    void findByLevelId_emptyList() {
        when(crudRepo.findByLevelId(100)).thenReturn(Collections.emptyList());
        when(mapper.toDomains(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<GradeSettingDomain> result = adapter.findByLevelId(100);

        assertTrue(result.isEmpty());
        verify(crudRepo).findByLevelId(100);
        verify(mapper).toDomains(Collections.emptyList());
    }

    // --- findById ---

    @Test
    void findById_present() {
        GradeSetting entity = new GradeSetting();
        GradeSettingDomain domain = GradeSettingDomain.builder().build();
        when(crudRepo.findById(7)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        GradeSettingDomain result = adapter.findById(7);

        assertEquals(domain, result);
        verify(crudRepo).findById(7);
        verify(mapper).toDomain(entity);
    }

    @Test
    void findById_absent() {
        when(crudRepo.findById(7)).thenReturn(Optional.empty());

        GradeSettingDomain result = adapter.findById(7);

        assertNull(result);
        verify(crudRepo).findById(7);
        verify(mapper, never()).toDomain(any());
    }

    // --- save ---

    @Test
    void save_persistsEntity() {
        GradeSettingDomain input = GradeSettingDomain.builder()
                .levelId(1).name("S1").description("D").build();
        GradeSetting toSave = new GradeSetting();
        GradeSetting saved = new GradeSetting();
        GradeSettingDomain out = GradeSettingDomain.builder().build();

        when(mapper.toEntity(input)).thenReturn(toSave);
        when(crudRepo.save(toSave)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(out);

        GradeSettingDomain result = adapter.save(input);

        assertEquals(out, result);
        verify(mapper).toEntity(input);
        verify(crudRepo).save(toSave);
        verify(mapper).toDomain(saved);
    }

    // --- update ---

    @Test
    void update_present() {
        GradeSettingDomain updateDto = GradeSettingDomain.builder()
                .levelId(2).name("New").description("X").minimumGrade(10).maximumGrade(20).passGrade(15).build();
        GradeSetting existing = new GradeSetting();
        existing.setLevelId(1);
        when(crudRepo.findById(9)).thenReturn(Optional.of(existing));
        when(crudRepo.save(existing)).thenReturn(existing);
        when(mapper.toDomain(existing)).thenReturn(updateDto);

        GradeSettingDomain result = adapter.update(9, updateDto);

        assertEquals(updateDto, result);
        assertEquals(2, existing.getLevelId());
        assertEquals("New", existing.getName());
        verify(crudRepo).findById(9);
        verify(crudRepo).save(existing);
        verify(mapper).toDomain(existing);
    }

    @Test
    void update_absent_throwsNoSuchElement() {
        when(crudRepo.findById(99)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
                adapter.update(99, GradeSettingDomain.builder().build()));
        verify(crudRepo).findById(99);
    }

    // --- delete ---

    @Test
    void delete_noAssociatedPeriods_returnsOk() {
        when(academicPeriodCrudRepo.findBySetting_Id(5)).thenReturn(Collections.emptyList());

        HttpStatus status = adapter.delete(5);

        assertEquals(HttpStatus.OK, status);
        verify(academicPeriodCrudRepo).findBySetting_Id(5);
        verify(crudRepo).deleteById(5);
    }

    @Test
    void delete_withAssociatedPeriods_throwsConflict() {
        AcademicPeriod ap = new AcademicPeriod();
        when(academicPeriodCrudRepo.findBySetting_Id(5)).thenReturn(List.of(ap));

        AppException ex = assertThrows(AppException.class, () ->
                adapter.delete(5));

        assertEquals(HttpStatus.IM_USED, ex.getCode());
        assertTrue(ex.getMessage().contains("No es posible eliminarlo"));
        verify(academicPeriodCrudRepo).findBySetting_Id(5);
        verify(crudRepo, never()).deleteById(any());
    }

    @Test
    void delete_nonExistingSetting_throwsEntityNotFound() {
        when(academicPeriodCrudRepo.findBySetting_Id(7)).thenReturn(Collections.emptyList());
        doThrow(new EntityNotFoundException("internal")).when(crudRepo).deleteById(7);

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                adapter.delete(7)
        );
        assertEquals("Setting with ID 7 not found!", ex.getMessage());
        verify(academicPeriodCrudRepo).findBySetting_Id(7);
        verify(crudRepo).deleteById(7);
    }
}
