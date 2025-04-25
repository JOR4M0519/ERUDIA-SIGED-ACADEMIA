package co.edu.gimnasiolorismalaguzzi.academyservice.modules.student;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.GradeSettingDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.EducationalLevelDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.EducationalLevel;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.mapper.EducationalLevelMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.repository.EduLevelCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.EducationalLevelAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence.PersistenceEducationalLevelPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence.PersistenceGroupsPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceGradeSettingPort;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EducationalLevelAdapterTest {

    @Mock private EduLevelCrudRepo eduLevelCrudRepo;
    @Mock private EducationalLevelMapper educationalLevelMapper;
    @Mock private PersistenceGroupsPort groupsPort;
    @Mock private PersistenceGradeSettingPort gradeSettingPort;
    @InjectMocks private EducationalLevelAdapter adapter;

    private EducationalLevel entity;
    private EducationalLevelDomain domain;
    private List<EducationalLevel> entities;
    private List<EducationalLevelDomain> domains;

    @BeforeEach
    void setUp() {
        // inject autowired ports
        ReflectionTestUtils.setField(adapter, "groupsPort", groupsPort);
        ReflectionTestUtils.setField(adapter, "gradeSettingPort", gradeSettingPort);

        entity = EducationalLevel.builder()
                .id(1)
                .levelName("Primaria")
                .status("A")
                .build();
        domain = EducationalLevelDomain.builder()
                .id(1)
                .levelName("Primaria")
                .status("A")
                .build();
        entities = Arrays.asList(entity);
        domains = Arrays.asList(domain);
    }

    @Test
    void findAll_returnsMappedDomains() {
        when(eduLevelCrudRepo.findAll()).thenReturn(entities);
        when(educationalLevelMapper.toDomains(entities)).thenReturn(domains);

        List<EducationalLevelDomain> result = adapter.findAll();

        assertEquals(domains, result);
        verify(eduLevelCrudRepo).findAll();
        verify(educationalLevelMapper).toDomains(entities);
    }

    @Test
    void findAll_emptyList() {
        when(eduLevelCrudRepo.findAll()).thenReturn(Collections.emptyList());
        when(educationalLevelMapper.toDomains(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<EducationalLevelDomain> result = adapter.findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    void findById_present() {
        when(eduLevelCrudRepo.findById(1)).thenReturn(Optional.of(entity));
        when(educationalLevelMapper.toDomain(entity)).thenReturn(domain);

        EducationalLevelDomain result = adapter.findById(1);
        assertEquals(domain, result);
    }

    @Test
    void findById_absent() {
        when(eduLevelCrudRepo.findById(2)).thenReturn(Optional.empty());
        assertNull(adapter.findById(2));
    }

    @Test
    void save_setsStatusAndPersists() {
        EducationalLevelDomain toSave = EducationalLevelDomain.builder().levelName("Secundaria").build();
        EducationalLevel entToSave = EducationalLevel.builder().levelName("Secundaria").status("A").build();
        EducationalLevel savedEnt = EducationalLevel.builder().id(2).levelName("Secundaria").status("A").build();
        EducationalLevelDomain savedDom = EducationalLevelDomain.builder().id(2).levelName("Secundaria").status("A").build();

        when(educationalLevelMapper.toEntity(toSave)).thenReturn(entToSave);
        when(eduLevelCrudRepo.save(entToSave)).thenReturn(savedEnt);
        when(educationalLevelMapper.toDomain(savedEnt)).thenReturn(savedDom);

        EducationalLevelDomain result = adapter.save(toSave);

        assertEquals("A", toSave.getStatus());
        assertEquals(savedDom, result);
        verify(eduLevelCrudRepo).save(entToSave);
    }

    @Test
    void update_present_updatesAndReturns() {
        EducationalLevelDomain updateDom = EducationalLevelDomain.builder().id(1).levelName("Secundaria").status("I").build();
        EducationalLevel existing = EducationalLevel.builder().id(1).levelName("Primaria").status("A").build();
        EducationalLevel updated = EducationalLevel.builder().id(1).levelName("Secundaria").status("I").build();

        when(eduLevelCrudRepo.findById(1)).thenReturn(Optional.of(existing));
        when(eduLevelCrudRepo.save(existing)).thenReturn(updated);
        when(educationalLevelMapper.toDomain(updated)).thenReturn(updateDom);

        EducationalLevelDomain result = adapter.update(1, updateDom);
        assertEquals(updateDom, result);
    }

    @Test
    void update_absent_throwsNoSuchElement() {
        when(eduLevelCrudRepo.findById(99)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> adapter.update(99, domain));
    }

    // --- delete ---

    @Test
    void delete_nonExistent_throwsNotFound() {
        when(eduLevelCrudRepo.existsById(3)).thenReturn(false);
        AppException ex = assertThrows(AppException.class, () -> adapter.delete(3));
        assertEquals(HttpStatus.NOT_FOUND, ex.getCode());
    }

    @Test
    void delete_withAssociations_throwsConflict() {
        when(eduLevelCrudRepo.existsById(1)).thenReturn(true);
        when(groupsPort.findByLevelId(1)).thenReturn(List.of(GroupsDomain.builder().build()));
        when(gradeSettingPort.findByLevelId(1)).thenReturn(List.of(GradeSettingDomain.builder().build()));

        AppException ex = assertThrows(AppException.class, () -> adapter.delete(1));
        assertEquals(HttpStatus.IM_USED, ex.getCode());
    }

    @Test
    void delete_noAssociations_updatesStatusAndReturnsOk() {
        when(eduLevelCrudRepo.existsById(1)).thenReturn(true);
        when(groupsPort.findByLevelId(1)).thenReturn(Collections.emptyList());
        when(gradeSettingPort.findByLevelId(1)).thenReturn(Collections.emptyList());

        HttpStatus status = adapter.delete(1);
        assertEquals(HttpStatus.OK, status);
        verify(eduLevelCrudRepo).updateStatusById("I", 1);
    }

    @Test
    void delete_updateThrows_exceptionMapsToInternalError() {
        when(eduLevelCrudRepo.existsById(1)).thenReturn(true);
        when(groupsPort.findByLevelId(1)).thenReturn(Collections.emptyList());
        when(gradeSettingPort.findByLevelId(1)).thenReturn(Collections.emptyList());
        doThrow(new RuntimeException("fail")).when(eduLevelCrudRepo).updateStatusById(anyString(), anyInt());

        AppException ex = assertThrows(AppException.class, () -> adapter.delete(1));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getCode());
    }
}
