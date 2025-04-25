package co.edu.gimnasiolorismalaguzzi.academyservice.modules.academic;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectScheduleDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectGroup;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectSchedule;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectScheduleMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectScheduleCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.SubjectScheduleAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubjectScheduleAdapterTest {

    @Mock
    private SubjectScheduleCrudRepo crudRepo;
    @Mock
    private SubjectScheduleMapper mapper;

    private SubjectScheduleAdapter adapter;

    private SubjectSchedule        entity;
    private SubjectScheduleDomain  domain;
    private List<SubjectSchedule>        entities;
    private List<SubjectScheduleDomain>  domains;

    @BeforeEach
    void setUp() {
        adapter = new SubjectScheduleAdapter(crudRepo, mapper);

        SubjectGroup sg = new SubjectGroup();
        sg.setId(1);

        entity = SubjectSchedule.builder()
                .id(1)
                .subjectGroup(sg)
                .dayOfWeek("MONDAY")
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(9, 0))
                .status("A")
                .build();

        domain = SubjectScheduleDomain.builder()
                .id(1)
                .subjectGroup(null)   // el mapper de SubjectGroup lo omitimos en estos tests
                .dayOfWeek("MONDAY")
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(9, 0))
                .status("A")
                .build();

        entities = Arrays.asList(entity);
        domains  = Arrays.asList(domain);
    }

    // ---------- findAll ----------

    @Test
    void findAll_ShouldReturnAllSubjectSchedules() {
        when(crudRepo.findAll()).thenReturn(entities);
        when(mapper.toDomains(entities)).thenReturn(domains);

        List<SubjectScheduleDomain> result = adapter.findAll();

        assertEquals(domains, result);
        verify(crudRepo).findAll();
        verify(mapper).toDomains(entities);
    }

    @Test
    void findAll_WhenRepoThrows_ShouldPropagate() {
        when(crudRepo.findAll()).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> adapter.findAll());
        assertEquals("DB error", ex.getMessage());
    }

    // ---------- findById ----------

    @Test
    void findById_WhenExists_ShouldReturnDomain() {
        when(crudRepo.findById(1)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        SubjectScheduleDomain result = adapter.findById(1);

        assertEquals(domain, result);
        verify(crudRepo).findById(1);
        verify(mapper).toDomain(entity);
    }

    @Test
    void findById_WhenNotExists_ShouldReturnNull() {
        when(crudRepo.findById(999)).thenReturn(Optional.empty());

        SubjectScheduleDomain result = adapter.findById(999);
        assertNull(result);
        verify(crudRepo).findById(999);
        verify(mapper, never()).toDomain(any());
    }

    // ---------- save ----------

    @Test
    void save_ShouldPersistAndReturnDomain() {
        SubjectScheduleDomain dto = SubjectScheduleDomain.builder()
                .subjectGroup(null)
                .dayOfWeek("TUESDAY")
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .status("A")
                .build();

        SubjectSchedule toSave = SubjectSchedule.builder()
                .dayOfWeek("TUESDAY")
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .status("A")
                .build();

        SubjectSchedule saved = SubjectSchedule.builder()
                .id(2)
                .dayOfWeek("TUESDAY")
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .status("A")
                .build();

        SubjectScheduleDomain savedDto = SubjectScheduleDomain.builder()
                .id(2)
                .dayOfWeek("TUESDAY")
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .status("A")
                .build();

        when(mapper.toEntity(dto)).thenReturn(toSave);
        when(crudRepo.save(toSave)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(savedDto);

        SubjectScheduleDomain result = adapter.save(dto);

        assertEquals(savedDto, result);
        verify(mapper).toEntity(dto);
        verify(crudRepo).save(toSave);
        verify(mapper).toDomain(saved);
    }

    @Test
    void save_WhenRepoThrows_ShouldPropagate() {
        SubjectScheduleDomain dto = domain;
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(crudRepo.save(entity)).thenThrow(new RuntimeException("Save failed"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> adapter.save(dto));
        assertEquals("Save failed", ex.getMessage());
    }

    // ---------- update ----------

    @Test
    void update_WhenExists_ShouldModifyAndReturnDomain() {
        SubjectScheduleDomain dto = SubjectScheduleDomain.builder()
                .id(1)
                .subjectGroup(null)
                .dayOfWeek("WEDNESDAY")
                .startTime(LocalTime.of(12, 0))
                .endTime(LocalTime.of(13, 0))
                .status("A")
                .build();

        // Stub para evitar NPE en mapper.toEntity(...)
        when(mapper.toEntity(dto)).thenReturn(entity);

        SubjectSchedule existing = entity;
        SubjectSchedule updated  = SubjectSchedule.builder().id(1).build();

        when(crudRepo.findById(1)).thenReturn(Optional.of(existing));
        when(crudRepo.save(existing)).thenReturn(updated);
        when(mapper.toDomain(updated)).thenReturn(dto);

        SubjectScheduleDomain result = adapter.update(1, dto);

        assertEquals(dto, result);
        verify(crudRepo).findById(1);
        verify(crudRepo).save(existing);
        verify(mapper).toDomain(updated);
    }

    @Test
    void update_WhenNotExists_ShouldThrowNoSuchElement() {
        SubjectScheduleDomain dto = domain;
        when(crudRepo.findById(999)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> adapter.update(999, dto));
        verify(crudRepo).findById(999);
        verify(crudRepo, never()).save(any());
    }

    @Test
    void update_WhenSaveFails_ShouldPropagate() {
        SubjectScheduleDomain dto = domain;
        when(mapper.toEntity(dto)).thenReturn(entity);

        SubjectSchedule existing = entity;
        when(crudRepo.findById(1)).thenReturn(Optional.of(existing));
        when(crudRepo.save(existing)).thenThrow(new RuntimeException("Update failed"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> adapter.update(1, dto));
        assertEquals("Update failed", ex.getMessage());
    }

    // ---------- delete ----------

    @Test
    void delete_WhenExists_ShouldMarkInactiveAndReturnOk() {
        when(crudRepo.existsById(1)).thenReturn(true);
        // updateStatusById devuelve un int, no es void
        when(crudRepo.updateStatusById("I", 1)).thenReturn(1);

        HttpStatus result = adapter.delete(1);

        assertEquals(HttpStatus.OK, result);
        verify(crudRepo).updateStatusById("I", 1);
    }

    @Test
    void delete_WhenNotExists_ShouldThrowInternalError() {
        when(crudRepo.existsById(999)).thenReturn(false);

        AppException ex = assertThrows(AppException.class, () -> adapter.delete(999));
        // Como el throw va dentro del try, acaba en INTERNAL_SERVER_ERROR
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getCode());
        assertTrue(ex.getMessage().contains("Internal Error"));
    }

    @Test
    void delete_WhenUpdateFails_ShouldThrowInternalError() {
        when(crudRepo.existsById(1)).thenReturn(true);
        when(crudRepo.updateStatusById("I", 1)).thenThrow(new RuntimeException("DB gone"));

        AppException ex = assertThrows(AppException.class, () -> adapter.delete(1));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getCode());
        assertTrue(ex.getMessage().contains("Internal Error"));
    }

    // ---------- getScheduleByGroupStudentId ----------

    @Test
    void getScheduleByGroupStudentId_ShouldReturnList() {
        when(crudRepo.findBySubjectGroup_Groups_Id(5)).thenReturn(entities);
        when(mapper.toDomains(entities)).thenReturn(domains);

        List<SubjectScheduleDomain> result = adapter.getScheduleByGroupStudentId(5);

        assertEquals(domains, result);
        verify(crudRepo).findBySubjectGroup_Groups_Id(5);
        verify(mapper).toDomains(entities);
    }

    @Test
    void getScheduleByGroupStudentId_WhenEmpty_ShouldReturnEmptyList() {
        when(crudRepo.findBySubjectGroup_Groups_Id(5)).thenReturn(Collections.emptyList());
        when(mapper.toDomains(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<SubjectScheduleDomain> result = adapter.getScheduleByGroupStudentId(5);

        assertTrue(result.isEmpty());
    }
}
