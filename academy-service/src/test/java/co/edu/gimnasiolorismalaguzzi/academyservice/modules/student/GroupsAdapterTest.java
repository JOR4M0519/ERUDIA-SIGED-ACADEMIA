package co.edu.gimnasiolorismalaguzzi.academyservice.modules.student;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.AttendanceReportDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupStudentsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.RepeatingStudentsGroupReportDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.ReportGroupsStatusDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.StudentPromotionDTO;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.Groups;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.mapper.GroupsMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.repository.GroupsCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.GroupsAdapter;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GroupsAdapterTest {

    @Mock
    private GroupsCrudRepo groupsCrudRepo;

    @Mock
    private GroupsMapper groupsMapper;

    @Mock
    private JdbcTemplate jdbcTemplate;

    private GroupsAdapter groupsAdapter;

    private Groups group;
    private GroupsDomain groupDomain;
    private List<Groups> groups;
    private List<GroupsDomain> groupDomains;

    @BeforeEach
    void setUp() {
        // Crear la instancia con el constructor e inyectar manualmente las dependencias
        groupsAdapter = new GroupsAdapter(groupsCrudRepo,jdbcTemplate,groupsMapper);

        // Inicializar entidades
        group = Groups.builder()
                .id(1)
                .groupName("Grupo A")
                .status("A")
                .build();

        // Inicializar dominio
        groupDomain = GroupsDomain.builder()
                .id(1)
                .groupName("Grupo A")
                .status("A")
                .build();

        // Inicializar listas
        groups = Arrays.asList(group);
        groupDomains = Arrays.asList(groupDomain);
    }

    @Test
    void findAll_ShouldReturnAllGroups() {
        // Arrange
        when(groupsCrudRepo.findAll()).thenReturn(groups);
        when(groupsMapper.toDomains(groups)).thenReturn(groupDomains);

        // Act
        List<GroupsDomain> result = groupsAdapter.findAll();

        // Assert
        assertEquals(groupDomains, result);
        verify(groupsCrudRepo).findAll();
        verify(groupsMapper).toDomains(groups);
    }

    @Test
    void findById_WhenGroupExists_ShouldReturnGroup() {
        // Arrange
        Integer id = 1;
        when(groupsCrudRepo.findById(id)).thenReturn(Optional.of(group));
        when(groupsMapper.toDomain(group)).thenReturn(groupDomain);

        // Act
        GroupsDomain result = groupsAdapter.findById(id);

        // Assert
        assertEquals(groupDomain, result);
        verify(groupsCrudRepo).findById(id);
        verify(groupsMapper).toDomain(group);
    }

    @Test
    void findById_WhenGroupDoesNotExist_ShouldReturnNull() {
        // Arrange
        Integer id = 999;
        when(groupsCrudRepo.findById(id)).thenReturn(Optional.empty());

        // Act
        GroupsDomain result = groupsAdapter.findById(id);

        // Assert
        assertNull(result);
        verify(groupsCrudRepo).findById(id);
        verify(groupsMapper, never()).toDomain(any());
    }

    @Test
    void save_ShouldSaveGroup() {
        // Arrange
        GroupsDomain domainToSave = GroupsDomain.builder()
                .groupName("Grupo B")
                .status("A")
                .build();

        Groups entityToSave = Groups.builder()
                .groupName("Grupo B")
                .status("A")
                .build();

        Groups savedEntity = Groups.builder()
                .id(2)
                .groupName("Grupo B")
                .status("A")
                .build();

        GroupsDomain savedDomain = GroupsDomain.builder()
                .id(2)
                .groupName("Grupo B")
                .status("A")
                .build();

        when(groupsMapper.toEntity(domainToSave)).thenReturn(entityToSave);
        when(groupsCrudRepo.save(entityToSave)).thenReturn(savedEntity);
        when(groupsMapper.toDomain(savedEntity)).thenReturn(savedDomain);

        // Act
        GroupsDomain result = groupsAdapter.save(domainToSave);

        // Assert
        assertEquals(savedDomain, result);
        verify(groupsMapper).toEntity(domainToSave);
        verify(groupsCrudRepo).save(entityToSave);
        verify(groupsMapper).toDomain(savedEntity);
    }
/*
    @Test
    void update_WhenGroupExists_ShouldUpdateAndReturnGroup() {
        // Arrange
        Integer id = 1;

        GroupsDomain domainToUpdate = GroupsDomain.builder()
                .id(1)
                .groupName("Grupo A Actualizado")
                .status("A")
                .build();

        Groups existingEntity = Groups.builder()
                .id(1)
                .groupName("Grupo A")
                .status("A")
                .build();

        Groups updatedEntity = Groups.builder()
                .id(1)
                .groupName("Grupo A Actualizado")
                .status("A")
                .build();

        when(groupsCrudRepo.findById(id)).thenReturn(Optional.of(existingEntity));
        when(groupsCrudRepo.save(any(Groups.class))).thenReturn(updatedEntity);
        when(groupsMapper.toDomain(updatedEntity)).thenReturn(domainToUpdate);

        // Act
        GroupsDomain result = groupsAdapter.update(id, domainToUpdate);

        // Assert
        assertEquals(domainToUpdate, result);
        verify(groupsCrudRepo).findById(id);
        verify(groupsCrudRepo).save(any(Groups.class));
        verify(groupsMapper).toDomain(updatedEntity);
    }

    @Test
    void update_WhenGroupDoesNotExist_ShouldThrowEntityNotFoundException() {
        // Arrange
        Integer id = 999;
        GroupsDomain domainToUpdate = GroupsDomain.builder()
                .id(999)
                .groupName("Grupo Inexistente")
                .status("A")
                .build();

        when(groupsCrudRepo.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            groupsAdapter.update(id, domainToUpdate);
        });

        verify(groupsCrudRepo).findById(id);
        verify(groupsCrudRepo, never()).save(any(Groups.class));
    }

    @Test
    void delete_WhenGroupExists_ShouldDeleteAndReturnOkStatus() {
        // Arrange
        Integer id = 1;

        when(groupsCrudRepo.existsById(id)).thenReturn(true);
        doNothing().when(groupsCrudRepo).updateStatusById("I", id);

        // Act
        HttpStatus result = groupsAdapter.delete(id);

        // Assert
        assertEquals(HttpStatus.OK, result);
        verify(groupsCrudRepo).existsById(id);
        verify(groupsCrudRepo).updateStatusById("I", id);
    }

    @Test
    void delete_WhenGroupDoesNotExist_ShouldThrowAppException() {
        // Arrange
        Integer id = 999;

        when(groupsCrudRepo.existsById(id)).thenReturn(false);

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> {
            groupsAdapter.delete(id);
        });

        assertEquals("Group ID doesnt exist", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getCode());
        verify(groupsCrudRepo).existsById(id);
        verify(groupsCrudRepo, never()).updateStatusById(anyString(), anyInt());
    }

    @Test
    void delete_WhenExceptionOccurs_ShouldThrowAppExceptionWithInternalServerError() {
        // Arrange
        Integer id = 1;

        when(groupsCrudRepo.existsById(id)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> {
            groupsAdapter.delete(id);
        });

        assertEquals("INTERN ERROR", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getCode());
        verify(groupsCrudRepo).existsById(id);
    }*/

    @Test
    void findByStatus_ShouldReturnGroupsByStatus() {
        // Arrange
        String status = "A";

        when(groupsCrudRepo.findByStatus(status)).thenReturn(groups);
        when(groupsMapper.toDomains(groups)).thenReturn(groupDomains);

        // Act
        List<GroupsDomain> result = groupsAdapter.findByStatus(status);

        // Assert
        assertEquals(groupDomains, result);
        verify(groupsCrudRepo).findByStatus(status);
        verify(groupsMapper).toDomains(groups);
    }
/*


    @Test
    void getRepeatingStudentsReport_ShouldReturnRepeatingStudentsReport() {
        // Arrange
        Integer academicPeriodId = 1;

        List<RepeatingStudentsGroupReportDomain> reportDomains = Arrays.asList(
                new RepeatingStudentsGroupReportDomain(1, "Grupo A", 5)
        );

        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(reportDomains);

        // Act
        List<RepeatingStudentsGroupReportDomain> result = groupsAdapter.getRepeatingStudentsByGroupReport();

        // Assert
        assertEquals(reportDomains, result);
        verify(jdbcTemplate).query(anyString(), any(Object[].class), any(RowMapper.class));
    }

    @Test
    void getAttendanceReport_ShouldReturnAttendanceReport() {
        // Arrange
        Integer groupId = 1;
        Integer subjectId = 1;
        Integer periodId = 1;

        List<AttendanceReportDomain> reportDomains = Arrays.asList(
                new AttendanceReportDomain(1, "John Doe", 20, 18, 2)
        );

        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(reportDomains);

        // Act
        List<AttendanceReportDomain> result = groupsAdapter.getAttendanceReport(groupId, subjectId, periodId);

        // Assert
        assertEquals(reportDomains, result);
        verify(jdbcTemplate).query(anyString(), any(Object[].class), any(RowMapper.class));
    }
*/

}