package co.edu.gimnasiolorismalaguzzi.academyservice.modules.student;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectScheduleDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectSchedule;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectScheduleMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectScheduleCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.UserMapper;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.*;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AttendanceAdapterTest {

    @Mock private AttendanceCrudRepo attendanceCrudRepo;
    @Mock private AttendanceMapper attendanceMapper;
    @Mock private SubjectScheduleCrudRepo subjectScheduleCrudRepo;
    @Mock private UserMapper userMapper;
    @Mock private SubjectScheduleMapper subjectScheduleMapper;

    @InjectMocks private AttendanceAdapter adapter;

    private Attendance attendance;
    private AttendanceDomain attendanceDomain;
    private List<Attendance> attendances;
    private List<AttendanceDomain> attendanceDomains;
    private SubjectSchedule subjectSchedule;
    private SubjectScheduleDomain subjectScheduleDomain;
    private User user;
    private UserDomain userDomain;
    private OffsetDateTime recordedAt;

    @BeforeEach
    void setUp() {
        // inject autowired mappers
        ReflectionTestUtils.setField(adapter, "userMapper", userMapper);
        ReflectionTestUtils.setField(adapter, "subjectScheduleMapper", subjectScheduleMapper);

        user = User.builder().id(1).firstName("John").lastName("Doe").build();
        userDomain = UserDomain.builder().id(1).build();
        recordedAt = OffsetDateTime.of(2025, 4, 24, 8, 30, 0, 0, ZoneOffset.UTC);

        subjectSchedule = SubjectSchedule.builder()
                .id(1)
                .dayOfWeek("MONDAY")
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(9, 0))
                .build();
        subjectScheduleDomain = SubjectScheduleDomain.builder()
                .id(1)
                .dayOfWeek("MONDAY")
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(9, 0))
                .build();

        attendance = Attendance.builder()
                .id(1)
                .student(user)
                .schedule(subjectSchedule)
                .attendanceDate(LocalDate.now())
                .status("P")
                .recordedAt(recordedAt)
                .build();
        attendanceDomain = AttendanceDomain.builder()
                .id(1)
                .student(userDomain)
                .schedule(subjectScheduleDomain)
                .attendanceDate(LocalDate.now())
                .status("P")
                .recordedAt(recordedAt)
                .build();

        attendances = List.of(attendance);
        attendanceDomains = List.of(attendanceDomain);
    }

    @Test
    void findAll_ShouldReturnAllAttendances() {
        when(attendanceCrudRepo.findAll()).thenReturn(attendances);
        when(attendanceMapper.toDomains(attendances)).thenReturn(attendanceDomains);

        List<AttendanceDomain> result = adapter.findAll();

        assertEquals(attendanceDomains, result);
        verify(attendanceCrudRepo).findAll();
        verify(attendanceMapper).toDomains(attendances);
    }

    @Test
    void getHistoricalAttendance_ShouldReturnHistoricalAttendance() {
        when(attendanceCrudRepo.getHistoricalAttendance(1,2,3)).thenReturn(attendances);
        when(attendanceMapper.toDomains(attendances)).thenReturn(attendanceDomains);

        List<AttendanceDomain> result = adapter.getHistoricalAttendance(1,2,3);

        assertEquals(attendanceDomains, result);
        verify(attendanceCrudRepo).getHistoricalAttendance(1,2,3);
        verify(attendanceMapper).toDomains(attendances);
    }

    @Test
    void findById_WhenAttendanceExists_ShouldReturnAttendance() {
        when(attendanceCrudRepo.findById(1)).thenReturn(Optional.of(attendance));
        when(attendanceMapper.toDomain(attendance)).thenReturn(attendanceDomain);

        AttendanceDomain result = adapter.findById(1);

        assertEquals(attendanceDomain, result);
        verify(attendanceCrudRepo).findById(1);
        verify(attendanceMapper).toDomain(attendance);
    }

    @Test
    void findById_WhenAttendanceDoesNotExist_ShouldReturnNull() {
        when(attendanceCrudRepo.findById(999)).thenReturn(Optional.empty());

        AttendanceDomain result = adapter.findById(999);

        assertNull(result);
        verify(attendanceCrudRepo).findById(999);
        verify(attendanceMapper, never()).toDomain(any());
    }

    @Test
    void save_ShouldSaveAttendance() {
        AttendanceDomain input = AttendanceDomain.builder()
                .student(userDomain)
                .schedule(subjectScheduleDomain)
                .attendanceDate(LocalDate.now())
                .status("P")
                .recordedAt(recordedAt)
                .build();
        Attendance toSave = Attendance.builder()
                .student(user)
                .schedule(subjectSchedule)
                .attendanceDate(LocalDate.now())
                .status("P")
                .recordedAt(recordedAt)
                .build();
        Attendance savedEntity = attendance;

        when(attendanceMapper.toEntity(input)).thenReturn(toSave);
        when(attendanceCrudRepo.save(toSave)).thenReturn(savedEntity);
        when(attendanceMapper.toDomain(savedEntity)).thenReturn(attendanceDomain);

        AttendanceDomain result = adapter.save(input);

        assertEquals(attendanceDomain, result);
        verify(attendanceMapper).toEntity(input);
        verify(attendanceCrudRepo).save(toSave);
        verify(attendanceMapper).toDomain(savedEntity);
    }

    @Test
    void update_WhenAttendanceExists_ShouldUpdateAndReturnAttendance() {
        AttendanceDomain update = attendanceDomain.builder().status("A").build();
        when(attendanceCrudRepo.findById(1)).thenReturn(Optional.of(attendance));
        Attendance updatedEntity = attendance.builder().status("A").build();
        when(attendanceCrudRepo.save(attendance)).thenReturn(updatedEntity);
        when(attendanceMapper.toDomain(updatedEntity)).thenReturn(update);

        AttendanceDomain result = adapter.update(1, update);

        assertEquals(update, result);
        verify(attendanceCrudRepo).findById(1);
        verify(attendanceCrudRepo).save(attendance);
        verify(attendanceMapper).toDomain(updatedEntity);
    }

    @Test
    void update_WhenAttendanceDoesNotExist_ShouldThrowNoSuchElementException() {
        when(attendanceCrudRepo.findById(5)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> adapter.update(5, attendanceDomain));
    }

    @Test
    void saveAllValidateSchedule_WhenNoSchedules_ShouldThrowIllegalStateException() {
        when(subjectScheduleCrudRepo.getSubjectSchedules(anyInt(),anyInt(),anyInt(),anyInt()))
                .thenReturn(new LinkedList<>());
        assertThrows(IllegalStateException.class,
                () -> adapter.saveAllValidateSchedule(attendanceDomains,1,1,1,1));
    }

    @Test
    void saveAllValidateSchedule_WhenScheduleMatches_ShouldSaveAndReturnDomains() {
        LinkedList<SubjectSchedule> schedules = new LinkedList<>();
        schedules.add(subjectSchedule);
        when(subjectScheduleCrudRepo.getSubjectSchedules(1,1,1,1)).thenReturn(schedules);
        when(subjectScheduleMapper.toDomain(subjectSchedule)).thenReturn(subjectScheduleDomain);
        when(attendanceMapper.toEntity(attendanceDomain)).thenReturn(attendance);
        when(attendanceCrudRepo.saveAll(List.of(attendance))).thenReturn(List.of(attendance));
        when(attendanceMapper.toDomains(List.of(attendance))).thenReturn(attendanceDomains);

        List<AttendanceDomain> result = adapter.saveAllValidateSchedule(attendanceDomains,1,1,1,1);
        assertEquals(attendanceDomains, result);
    }

    @Test
    void saveAllNullAttendance_ShouldSaveAndReturnDomains() {
        when(attendanceMapper.toEntity(attendanceDomain)).thenReturn(attendance);
        when(attendanceCrudRepo.saveAll(List.of(attendance))).thenReturn(List.of(attendance));
        when(attendanceMapper.toDomains(List.of(attendance))).thenReturn(attendanceDomains);

        List<AttendanceDomain> result = adapter.saveAllNullAttendance(attendanceDomains,1,1,1,1);
        assertEquals(attendanceDomains, result);
    }



    @Test
    void updateAll_ShouldUpdateAllAttendances() {
        when(attendanceCrudRepo.findById(1)).thenReturn(Optional.of(attendance));
        when(userMapper.toEntity(userDomain)).thenReturn(user);
        when(subjectScheduleMapper.toEntity(subjectScheduleDomain)).thenReturn(subjectSchedule);
        when(attendanceCrudRepo.saveAll(anyList())).thenReturn(List.of(attendance));
        when(attendanceMapper.toDomains(anyList())).thenReturn(attendanceDomains);

        List<AttendanceDomain> result = adapter.updateAll(attendanceDomains);

        assertEquals(attendanceDomains, result);
        verify(attendanceCrudRepo).findById(1);
        verify(attendanceCrudRepo).saveAll(anyList());
        verify(attendanceMapper).toDomains(anyList());
    }

    @Test
    void updateAll_WhenAttendanceDoesNotExist_ShouldThrowEntityNotFoundException() {
        when(attendanceCrudRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> adapter.updateAll(attendanceDomains));
    }

    @Test
    void deleteAll_WhenAttendancesExist_ShouldReturnOkStatus() {
        when(attendanceCrudRepo.findAllById(anyList())).thenReturn(List.of(attendance));
        doNothing().when(attendanceCrudRepo).deleteAllById(anyList());

        HttpStatus result = adapter.deleteAll(List.of(1, 2));

        assertEquals(HttpStatus.OK, result);
        verify(attendanceCrudRepo).findAllById(anyList());
        verify(attendanceCrudRepo).deleteAllById(anyList());
    }

    @Test
    void deleteAll_WhenAttendancesDoNotExist_ShouldReturnNotFoundStatus() {
        when(attendanceCrudRepo.findAllById(anyList())).thenReturn(Collections.emptyList());

        HttpStatus result = adapter.deleteAll(List.of(999));

        assertEquals(HttpStatus.NOT_FOUND, result);
        verify(attendanceCrudRepo).findAllById(anyList());
        verify(attendanceCrudRepo, never()).deleteAllById(anyList());
    }

    @Test
    void deleteAll_WhenExceptionOccurs_ShouldReturnInternalServerErrorStatus() {
        when(attendanceCrudRepo.findAllById(anyList())).thenThrow(new RuntimeException("Database error"));

        HttpStatus result = adapter.deleteAll(List.of(1));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result);
    }
}