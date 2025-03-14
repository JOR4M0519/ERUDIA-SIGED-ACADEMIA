package co.edu.gimnasiolorismalaguzzi.academyservice.modules.student;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectSchedule;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectScheduleCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.AttendanceDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.Attendance;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.mapper.AttendanceMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.repository.AttendanceCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.AttendanceAdapter;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AttendanceAdapterTest {

    @Mock
    private AttendanceCrudRepo attendanceCrudRepo;

    @Mock
    private AttendanceMapper attendanceMapper;

    @Mock
    private SubjectScheduleCrudRepo subjectScheduleCrudRepo;

    private AttendanceAdapter attendanceAdapter;

    private Attendance attendance;
    private AttendanceDomain attendanceDomain;
    private List<Attendance> attendances;
    private List<AttendanceDomain> attendanceDomains;
    private SubjectSchedule subjectSchedule;
    private User user;

    @BeforeEach
    void setUp() {
        // Crear la instancia con el constructor e inyectar manualmente subjectScheduleCrudRepo
        attendanceAdapter = new AttendanceAdapter(attendanceCrudRepo, attendanceMapper, subjectScheduleCrudRepo);

        // Inicializar entidades
        user = User.builder()
                .id(1)
                .firstName("John Doe")
                .build();

        subjectSchedule = SubjectSchedule.builder()
                .id(1)
                .dayOfWeek("MONDAY")
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(9, 0))
                .build();

        attendance = Attendance.builder()
                .id(1)
                .schedule(subjectSchedule)
                .student(user)
                .attendanceDate(LocalDate.now())
                .status("P") // Presente
                .build();

        // Inicializar dominio
        attendanceDomain = AttendanceDomain.builder()
                .id(1)
                . schedule(subjectSchedule)
                .student(user)
                .attendanceDate(LocalDate.now())
                .status("P")
                .build();

        // Inicializar listas
        attendances = Arrays.asList(attendance);
        attendanceDomains = Arrays.asList(attendanceDomain);
    }

    @Test
    void findAll_ShouldReturnAllAttendances() {
        // Arrange
        when(attendanceCrudRepo.findAll()).thenReturn(attendances);
        when(attendanceMapper.toDomains(attendances)).thenReturn(attendanceDomains);

        // Act
        List<AttendanceDomain> result = attendanceAdapter.findAll();

        // Assert
        assertEquals(attendanceDomains, result);
        verify(attendanceCrudRepo).findAll();
        verify(attendanceMapper).toDomains(attendances);
    }

    @Test
    void getHistoricalAttendance_ShouldReturnHistoricalAttendance() {
        // Arrange
        Integer groupId = 1;
        Integer subjectId = 1;
        Integer periodId = 1;

        when(attendanceCrudRepo.getHistoricalAttendance(groupId, subjectId, periodId))
                .thenReturn(attendances);
        when(attendanceMapper.toDomains(attendances)).thenReturn(attendanceDomains);

        // Act
        List<AttendanceDomain> result = attendanceAdapter.getHistoricalAttendance(groupId, subjectId, periodId);

        // Assert
        assertEquals(attendanceDomains, result);
        verify(attendanceCrudRepo).getHistoricalAttendance(groupId, subjectId, periodId);
        verify(attendanceMapper).toDomains(attendances);
    }

    @Test
    void findById_WhenAttendanceExists_ShouldReturnAttendance() {
        // Arrange
        Integer id = 1;
        when(attendanceCrudRepo.findById(id)).thenReturn(Optional.of(attendance));
        when(attendanceMapper.toDomain(attendance)).thenReturn(attendanceDomain);

        // Act
        AttendanceDomain result = attendanceAdapter.findById(id);

        // Assert
        assertEquals(attendanceDomain, result);
        verify(attendanceCrudRepo).findById(id);
        verify(attendanceMapper).toDomain(attendance);
    }

    @Test
    void findById_WhenAttendanceDoesNotExist_ShouldReturnNull() {
        // Arrange
        Integer id = 999;
        when(attendanceCrudRepo.findById(id)).thenReturn(Optional.empty());

        // Act
        AttendanceDomain result = attendanceAdapter.findById(id);

        // Assert
        assertNull(result);
        verify(attendanceCrudRepo).findById(id);
        verify(attendanceMapper, never()).toDomain(any());
    }

    @Test
    void save_ShouldSaveAttendance() {
        // Arrange
        AttendanceDomain domainToSave = AttendanceDomain.builder()
                .schedule(subjectSchedule)
                .student(user)
                .attendanceDate(LocalDate.now())
                .status("P")
                .build();

        Attendance entityToSave = Attendance.builder()
                .schedule(subjectSchedule)
                .student(user)
                .attendanceDate(LocalDate.now())
                .status("P")
                .build();

        Attendance savedEntity = Attendance.builder()
                .id(1)
                .schedule(subjectSchedule)
                .student(user)
                .attendanceDate(LocalDate.now())
                .status("P")
                .build();

        AttendanceDomain savedDomain = AttendanceDomain.builder()
                .id(1)
                .schedule(subjectSchedule)
                .student(user)
                .attendanceDate(LocalDate.now())
                .status("P")
                .build();

        when(attendanceMapper.toEntity(domainToSave)).thenReturn(entityToSave);
        when(attendanceCrudRepo.save(entityToSave)).thenReturn(savedEntity);
        when(attendanceMapper.toDomain(savedEntity)).thenReturn(savedDomain);

        // Act
        AttendanceDomain result = attendanceAdapter.save(domainToSave);

        // Assert
        assertEquals(savedDomain, result);
        verify(attendanceMapper).toEntity(domainToSave);
        verify(attendanceCrudRepo).save(entityToSave);
        verify(attendanceMapper).toDomain(savedEntity);
    }

    @Test
    void update_WhenAttendanceExists_ShouldUpdateAndReturnAttendance() {
        // Arrange
        Integer id = 1;

        AttendanceDomain domainToUpdate = AttendanceDomain.builder()
                .id(1)
                .schedule(subjectSchedule)
                .student(user)
                .attendanceDate(LocalDate.now())
                .status("A") // Actualizado a Ausente
                .build();

        Attendance existingEntity = Attendance.builder()
                .id(1)
                .schedule(subjectSchedule)
                .student(user)
                .attendanceDate(LocalDate.now())
                .status("P")
                .build();

        Attendance updatedEntity = Attendance.builder()
                .id(1)
                .schedule(subjectSchedule)
                .student(user)
                .attendanceDate(LocalDate.now())
                .status("A")
                .build();

        when(attendanceCrudRepo.findById(id)).thenReturn(Optional.of(existingEntity));
        when(attendanceCrudRepo.save(any(Attendance.class))).thenReturn(updatedEntity);
        when(attendanceMapper.toDomain(updatedEntity)).thenReturn(domainToUpdate);

        // Act
        AttendanceDomain result = attendanceAdapter.update(id, domainToUpdate);

        // Assert
        assertEquals(domainToUpdate, result);
        verify(attendanceCrudRepo).findById(id);
        verify(attendanceCrudRepo).save(any(Attendance.class));
        verify(attendanceMapper).toDomain(updatedEntity);
    }



}
