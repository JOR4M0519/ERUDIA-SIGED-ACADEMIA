package co.edu.gimnasiolorismalaguzzi.academyservice.modules.student;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.AttendanceReportDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.RepeatingStudentsGroupReportDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.ReportGroupsStatusDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.Groups;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.mapper.GroupsMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.repository.GroupsCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.GroupsAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupsAdapterTest {

    @Mock private GroupsCrudRepo crudRepo;
    @Mock private GroupsMapper mapper;
    @Mock private JdbcTemplate jdbcTemplate;
    @InjectMocks private GroupsAdapter adapter;

    private Groups entity;
    private GroupsDomain domain;
    private List<Groups> entities;
    private List<GroupsDomain> domains;

    @BeforeEach
    void setUp() {
        entity = Groups.builder().id(1).groupName("G1").status("A").build();
        domain = GroupsDomain.builder().id(1).groupName("G1").status("A").build();
        entities = Arrays.asList(entity);
        domains = Arrays.asList(domain);
    }

    @Test
    void findAll_returnsMapped() {
        when(crudRepo.findAll()).thenReturn(entities);
        when(mapper.toDomains(entities)).thenReturn(domains);

        List<GroupsDomain> result = adapter.findAll();
        assertEquals(domains, result);
        verify(crudRepo).findAll();
        verify(mapper).toDomains(entities);
    }

    @Test
    void findAll_emptyList() {
        when(crudRepo.findAll()).thenReturn(Collections.emptyList());
        when(mapper.toDomains(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<GroupsDomain> result = adapter.findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    void findById_present() {
        when(crudRepo.findById(1)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        GroupsDomain result = adapter.findById(1);
        assertEquals(domain, result);
    }

    @Test
    void findById_absent() {
        when(crudRepo.findById(2)).thenReturn(Optional.empty());
        GroupsDomain result = adapter.findById(2);
        assertNull(result);
    }

    @Test
    void findByLevelId_returnsMapped() {
        when(crudRepo.findByLevel_Id(5)).thenReturn(entities);
        when(mapper.toDomains(entities)).thenReturn(domains);

        List<GroupsDomain> result = adapter.findByLevelId(5);
        assertEquals(domains, result);
        verify(crudRepo).findByLevel_Id(5);
    }

    @Test
    void findByLevelId_emptyList() {
        when(crudRepo.findByLevel_Id(6)).thenReturn(Collections.emptyList());
        when(mapper.toDomains(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<GroupsDomain> result = adapter.findByLevelId(6);
        assertTrue(result.isEmpty());
    }

    @Test
    void save_persistsEntity() {
        GroupsDomain toSave = GroupsDomain.builder().groupName("X").status("A").build();
        Groups entToSave = Groups.builder().groupName("X").status("A").build();
        Groups saved = Groups.builder().id(2).groupName("X").status("A").build();
        GroupsDomain out = GroupsDomain.builder().id(2).groupName("X").status("A").build();

        when(mapper.toEntity(toSave)).thenReturn(entToSave);
        when(crudRepo.save(entToSave)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(out);

        GroupsDomain result = adapter.save(toSave);
        assertEquals(out, result);
        verify(crudRepo).save(entToSave);
    }


    @Test
    void update_absent_throwsNoSuchElement() {
        when(crudRepo.findById(9)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> adapter.update(9, domain));
    }

    @Test
    void delete_existing_deletesAndReturnsOk() {
        when(crudRepo.existsById(1)).thenReturn(true);
        HttpStatus status = adapter.delete(1);
        assertEquals(HttpStatus.OK, status);
        verify(crudRepo).deleteById(1);
    }

    @Test
    void delete_nonexistent_throwsNotFound() {
        when(crudRepo.existsById(2)).thenReturn(false);
        AppException ex = assertThrows(AppException.class, () -> adapter.delete(2));
        assertEquals(HttpStatus.CONFLICT, ex.getCode());
    }

    @Test
    void delete_deleteThrowsException_updatesStatusAndThrowsConflict() {
        when(crudRepo.existsById(3)).thenReturn(true);
        doThrow(new RuntimeException("fail")).when(crudRepo).deleteById(3);
        AppException ex = assertThrows(AppException.class, () -> adapter.delete(3));
        assertEquals(HttpStatus.CONFLICT, ex.getCode());
        verify(crudRepo).updateStatusById("I", 3);
    }

    @Test
    void getAcademicLevelReport_mapsRows() {
        Object[] row = {1, "L1", "G1", "S1", 10L};
        when(crudRepo.getAcademicLevelReport()).thenReturn(Arrays.<Object[]>asList(row));

        List<ReportGroupsStatusDomain> res = adapter.getAcademicLevelReport();
        assertEquals(1, res.size());
        ReportGroupsStatusDomain r = res.get(0);
        assertEquals(1, r.getGroupId());
        assertEquals("L1", r.getLevelName());
    }

    @Test
    void getAttendanceReport_returnsMapped() {
        AttendanceReportDomain rep = new AttendanceReportDomain(1, "L", "G", "Sec", 5L, 4L, 1L, 0L, null);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(List.of(rep));

        List<AttendanceReportDomain> res = adapter.getAttendanceReport();
        assertEquals(1, res.size());
    }

    @Test
    void getRepeatingStudentsByGroupReport_returnsMapped() {
        RepeatingStudentsGroupReportDomain rep = new RepeatingStudentsGroupReportDomain(1, "G1", "L1", 2L);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(List.of(rep));

        List<RepeatingStudentsGroupReportDomain> res = adapter.getRepeatingStudentsByGroupReport();
        assertEquals(1, res.size());
    }

    @Test
    void getGroupsByLevelAndStatus_valid_returnsMapped() {
        when(crudRepo.findByLevelIdAndStatus(10, "A")).thenReturn(entities);
        when(mapper.toDomains(entities)).thenReturn(domains);

        List<GroupsDomain> res = adapter.getGroupsByLevelAndStatus("10", "A");
        assertEquals(domains, res);
    }

    @Test
    void getGroupsByLevelAndStatus_invalidNumber_throwsBadRequest() {
        AppException ex = assertThrows(AppException.class, () -> adapter.getGroupsByLevelAndStatus("x", "A"));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getCode());
    }

    @Test
    void getGroupsByLevelAndStatus_repoError_throwsInternalServerError() {
        when(crudRepo.findByLevelIdAndStatus(1, "A")).thenThrow(new RuntimeException("err"));
        AppException ex = assertThrows(AppException.class, () -> adapter.getGroupsByLevelAndStatus("1", "A"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getCode());
    }
}
