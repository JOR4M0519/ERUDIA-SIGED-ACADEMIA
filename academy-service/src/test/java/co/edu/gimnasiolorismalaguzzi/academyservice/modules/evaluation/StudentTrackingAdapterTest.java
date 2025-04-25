package co.edu.gimnasiolorismalaguzzi.academyservice.modules.evaluation;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.AcademicPeriodDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.AcademicPeriod;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.StudentTrackingAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.repository.StudentTrackingCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.mapper.StudentTrackingMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.UserMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.StudentTracking;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.StudentTrackingDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.TrackingTypeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.TrackingType;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentTrackingAdapterTest {

    @Mock
    private StudentTrackingCrudRepo crudRepo;

    @Mock
    private StudentTrackingMapper studentTrackingMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private StudentTrackingAdapter adapter;

    private StudentTrackingDomain validTrackingDomain;
    private StudentTracking validTrackingEntity;
    private UserDomain studentDomain;
    private UserDomain professorDomain;
    private User studentEntity;
    private User professorEntity;
    private AcademicPeriodDomain periodDomain;
    private AcademicPeriod periodEntity;
    private TrackingTypeDomain trackingTypeDomain;
    private TrackingType trackingTypeEntity;

    @BeforeEach
    void setUp() {
        // inject autowired fields
        ReflectionTestUtils.setField(adapter, "studentTrackingMapper", studentTrackingMapper);
        ReflectionTestUtils.setField(adapter, "userMapper", userMapper);

        // Setup test data
        studentDomain = UserDomain.builder().id(100).firstName("Student").lastName("Test").build();
        professorDomain = UserDomain.builder().id(200).firstName("Professor").lastName("Test").build();
        periodDomain = AcademicPeriodDomain.builder().id(1).name("2023-1").build();
        trackingTypeDomain = TrackingTypeDomain.builder().id(1).type("Academic").build();

        studentEntity = new User();
        studentEntity.setId(100);
        studentEntity.setFirstName("Student");
        studentEntity.setLastName("Test");

        professorEntity = new User();
        professorEntity.setId(200);
        professorEntity.setFirstName("Professor");
        professorEntity.setLastName("Test");

        periodEntity = new AcademicPeriod();
        periodEntity.setId(1);
        periodEntity.setName("2023-1");

        trackingTypeEntity = new TrackingType();
        trackingTypeEntity.setId(1);
        trackingTypeEntity.setType("Academic");

        validTrackingDomain = StudentTrackingDomain.builder()
                .id(1)
                .student(studentDomain)
                .professor(professorDomain)
                .period(periodDomain)
                .trackingType(trackingTypeDomain)
                .situation("Test situation")
                .compromise("Test compromise")
                .followUp("Test follow up")
                .status("A")
                .build();

        validTrackingEntity = new StudentTracking();
        validTrackingEntity.setId(1);
        validTrackingEntity.setStudent(studentEntity);
        validTrackingEntity.setProfessor(professorEntity);
        validTrackingEntity.setPeriod(periodEntity);
        validTrackingEntity.setTrackingType(trackingTypeEntity);
        validTrackingEntity.setSituation("Test situation");
        validTrackingEntity.setCompromise("Test compromise");
        validTrackingEntity.setFollowUp("Test follow up");
        validTrackingEntity.setStatus("A");
    }

    @Test
    void testFindAllReturnsMappedDomains() {
        List<StudentTracking> entities = Arrays.asList(new StudentTracking(), new StudentTracking());
        StudentTrackingDomain d1 = StudentTrackingDomain.builder().build();
        StudentTrackingDomain d2 = StudentTrackingDomain.builder().build();
        List<StudentTrackingDomain> domains = Arrays.asList(d1, d2);
        when(crudRepo.findAll()).thenReturn(entities);
        when(studentTrackingMapper.toDomains(entities)).thenReturn(domains);

        List<StudentTrackingDomain> result = adapter.findAll();
        assertEquals(domains, result);
        verify(crudRepo).findAll();
        verify(studentTrackingMapper).toDomains(entities);
    }

    @Test
    void testFindAllReturnsEmptyList() {
        List<StudentTracking> entities = Collections.emptyList();
        List<StudentTrackingDomain> domains = Collections.emptyList();
        when(crudRepo.findAll()).thenReturn(entities);
        when(studentTrackingMapper.toDomains(entities)).thenReturn(domains);

        List<StudentTrackingDomain> result = adapter.findAll();
        assertTrue(result.isEmpty());
        verify(crudRepo).findAll();
        verify(studentTrackingMapper).toDomains(entities);
    }

    @Test
    void testFindAllHandlesDatabaseException() {
        when(crudRepo.findAll()).thenThrow(new DataAccessException("Database connection error") {});

        assertThrows(DataAccessException.class, () -> adapter.findAll());
        verify(crudRepo).findAll();
        verify(studentTrackingMapper, never()).toDomains(any());
    }

    @Test
    void testFindByIdReturnsDomainWhenPresent() {
        StudentTracking entity = new StudentTracking();
        StudentTrackingDomain domain = StudentTrackingDomain.builder().build();
        when(crudRepo.findById(1)).thenReturn(Optional.of(entity));
        when(studentTrackingMapper.toDomain(entity)).thenReturn(domain);

        StudentTrackingDomain result = adapter.findById(1);
        assertEquals(domain, result);
        verify(crudRepo).findById(1);
        verify(studentTrackingMapper).toDomain(entity);
    }

    @Test
    void testFindByIdReturnsNullWhenNotPresent() {
        when(crudRepo.findById(2)).thenReturn(Optional.empty());

        StudentTrackingDomain result = adapter.findById(2);
        assertNull(result);
        verify(crudRepo).findById(2);
        verify(studentTrackingMapper, never()).toDomain(any());
    }

    @Test
    void testFindByIdWithNullId() {
        when(crudRepo.findById(null)).thenReturn(Optional.empty());

        StudentTrackingDomain result = adapter.findById(null);
        assertNull(result);
        verify(crudRepo).findById(null);
    }

    @Test
    void testFindByIdHandlesDatabaseException() {
        when(crudRepo.findById(1)).thenThrow(new DataAccessException("Database error") {});

        assertThrows(DataAccessException.class, () -> adapter.findById(1));
        verify(crudRepo).findById(1);
    }

    @Test
    void testSaveReturnsMappedDomain() {
        StudentTrackingDomain inputDomain = StudentTrackingDomain.builder().build();
        StudentTracking entity = new StudentTracking();
        StudentTracking savedEntity = new StudentTracking();
        StudentTrackingDomain savedDomain = StudentTrackingDomain.builder().build();

        when(studentTrackingMapper.toEntity(inputDomain)).thenReturn(entity);
        when(crudRepo.save(entity)).thenReturn(savedEntity);
        when(studentTrackingMapper.toDomain(savedEntity)).thenReturn(savedDomain);

        StudentTrackingDomain result = adapter.save(inputDomain);
        assertEquals(savedDomain, result);
        verify(studentTrackingMapper).toEntity(inputDomain);
        verify(crudRepo).save(entity);
        verify(studentTrackingMapper).toDomain(savedEntity);
    }

    @Test
    void testSaveWithNullDomain() {
        when(studentTrackingMapper.toEntity(null)).thenReturn(null);
        when(crudRepo.save(null)).thenThrow(new IllegalArgumentException("Entity must not be null"));

        assertThrows(IllegalArgumentException.class, () -> adapter.save(null));
        verify(studentTrackingMapper).toEntity(null);
    }

    @Test
    void testSaveHandlesDataIntegrityViolation() {
        StudentTrackingDomain inputDomain = StudentTrackingDomain.builder().build();
        StudentTracking entity = new StudentTracking();

        when(studentTrackingMapper.toEntity(inputDomain)).thenReturn(entity);
        when(crudRepo.save(entity)).thenThrow(new DataIntegrityViolationException("Constraint violation"));

        assertThrows(DataIntegrityViolationException.class, () -> adapter.save(inputDomain));
        verify(studentTrackingMapper).toEntity(inputDomain);
        verify(crudRepo).save(entity);
    }

    @Test
    void testUpdateSuccess() {
        // Given
        int id = 1;
        StudentTrackingDomain domainToUpdate = validTrackingDomain;
        StudentTracking existingEntity = validTrackingEntity;
        StudentTracking updatedEntity = new StudentTracking();
        updatedEntity.setId(1);
        updatedEntity.setSituation("Updated situation");

        StudentTrackingDomain expectedDomain = StudentTrackingDomain.builder()
                .id(1)
                .situation("Updated situation")
                .build();

        // When
        when(crudRepo.findById(id)).thenReturn(Optional.of(existingEntity));
        when(userMapper.toEntity(domainToUpdate.getStudent())).thenReturn(studentEntity);
        when(userMapper.toEntity(domainToUpdate.getProfessor())).thenReturn(professorEntity);
        when(studentTrackingMapper.toEntity(domainToUpdate)).thenReturn(validTrackingEntity);
        when(crudRepo.save(any(StudentTracking.class))).thenReturn(updatedEntity);
        when(studentTrackingMapper.toDomain(updatedEntity)).thenReturn(expectedDomain);

        // Then
        StudentTrackingDomain result = adapter.update(id, domainToUpdate);

        assertEquals(expectedDomain, result);
        verify(crudRepo).findById(id);
        verify(crudRepo).save(any(StudentTracking.class));
        verify(studentTrackingMapper).toDomain(updatedEntity);
    }

    @Test
    void testUpdateThrowsEntityNotFoundException() {
        int id = 99;
        StudentTrackingDomain inputDomain = StudentTrackingDomain.builder().build();
        when(crudRepo.findById(id)).thenThrow(new EntityNotFoundException());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> adapter.update(id, inputDomain));
        assertEquals("Tracking with ID " + id + "not found!", ex.getMessage());
        verify(crudRepo).findById(id);
    }

    @Test
    void testUpdateThrowsNoSuchElementExceptionWhenNotPresent() {
        int id = 2;
        StudentTrackingDomain inputDomain = StudentTrackingDomain.builder().build();
        when(crudRepo.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> adapter.update(id, inputDomain));
        verify(crudRepo).findById(id);
    }

    @Test
    void testUpdateWithNullDomain() {
        int id = 1;
        when(crudRepo.findById(id)).thenReturn(Optional.of(validTrackingEntity));

        // Null domain should cause NullPointerException when we try to access its properties
        assertThrows(NullPointerException.class, () -> adapter.update(id, null));
        verify(crudRepo).findById(id);
    }

    @Test
    void testUpdateWithNullId() {
        StudentTrackingDomain inputDomain = StudentTrackingDomain.builder().build();

        // Methods called with null may throw different exceptions depending on implementation
        when(crudRepo.findById(null)).thenThrow(new IllegalArgumentException("ID cannot be null"));

        assertThrows(IllegalArgumentException.class, () -> adapter.update(null, inputDomain));
        verify(crudRepo).findById(null);
    }

    @Test
    void testDeleteSuccess() {
        int id = 1;
        when(crudRepo.existsById(id)).thenReturn(true);
        when(crudRepo.updateStatusById("I", id)).thenReturn(1);

        HttpStatus status = adapter.delete(id);
        assertEquals(HttpStatus.OK, status);
        verify(crudRepo).existsById(id);
        verify(crudRepo).updateStatusById("I", id);
    }

    @Test
    void testDeleteThrowsAppExceptionWhenNotFound() {
        int id = 2;
        when(crudRepo.existsById(id)).thenReturn(false);

        AppException ex = assertThrows(AppException.class, () -> adapter.delete(id));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getCode());
        assertTrue(ex.getMessage().contains("Internal Error"));
        verify(crudRepo).existsById(id);
        verify(crudRepo, never()).updateStatusById(anyString(), anyInt());
    }

    @Test
    void testDeleteWithNullId() {
        when(crudRepo.existsById(null)).thenThrow(new IllegalArgumentException("ID cannot be null"));

        AppException ex = assertThrows(AppException.class, () -> adapter.delete(null));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getCode());
        verify(crudRepo).existsById(null);
    }

    @Test
    void testDeleteThrowsAppExceptionOnException() {
        int id = 3;
        when(crudRepo.existsById(id)).thenReturn(true);
        when(crudRepo.updateStatusById("I", id)).thenThrow(new RuntimeException("DB fail"));

        AppException ex = assertThrows(AppException.class, () -> adapter.delete(id));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getCode());
        assertTrue(ex.getMessage().contains("Internal Error!"));
        verify(crudRepo).existsById(id);
        verify(crudRepo).updateStatusById("I", id);
    }

    @Test
    void testGetTrackingByStudentIdReturnsDomains() {
        int studentId = 10;
        List<StudentTracking> entities = Arrays.asList(new StudentTracking());
        List<StudentTrackingDomain> domains = Arrays.asList(StudentTrackingDomain.builder().build());
        when(crudRepo.findByStudent_Id(studentId)).thenReturn(entities);
        when(studentTrackingMapper.toDomains(entities)).thenReturn(domains);

        List<StudentTrackingDomain> result = adapter.getTrackingByStudentId(studentId);
        assertEquals(domains, result);
        verify(crudRepo).findByStudent_Id(studentId);
        verify(studentTrackingMapper).toDomains(entities);
    }

    @Test
    void testGetTrackingByStudentIdReturnsEmptyList() {
        int studentId = 11;
        when(crudRepo.findByStudent_Id(studentId)).thenReturn(Collections.emptyList());
        when(studentTrackingMapper.toDomains(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<StudentTrackingDomain> result = adapter.getTrackingByStudentId(studentId);
        assertTrue(result.isEmpty());
        verify(crudRepo).findByStudent_Id(studentId);
        verify(studentTrackingMapper).toDomains(Collections.emptyList());
    }

    @Test
    void testGetTrackingByStudentIdWithNullId() {
        when(crudRepo.findByStudent_Id(null)).thenReturn(Collections.emptyList());
        when(studentTrackingMapper.toDomains(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<StudentTrackingDomain> result = adapter.getTrackingByStudentId(null);
        assertTrue(result.isEmpty());
        verify(crudRepo).findByStudent_Id(null);
    }

    @Test
    void testGetTrackingByStudentIdHandlesDatabaseException() {
        int studentId = 12;
        when(crudRepo.findByStudent_Id(studentId)).thenThrow(new DataAccessException("Database error") {});

        assertThrows(DataAccessException.class, () -> adapter.getTrackingByStudentId(studentId));
        verify(crudRepo).findByStudent_Id(studentId);
    }

    @Test
    void testGetTrackingListStudentsCreatedByTeacherReturnsDomains() {
        int teacherId = 20;
        String status = "X";
        List<StudentTracking> entities = Arrays.asList(new StudentTracking());
        List<StudentTrackingDomain> domains = Arrays.asList(StudentTrackingDomain.builder().build());
        when(crudRepo.findByProfessor_IdAndStatusNotLike(teacherId, status)).thenReturn(entities);
        when(studentTrackingMapper.toDomains(entities)).thenReturn(domains);

        List<StudentTrackingDomain> result = adapter.getTrackingListStudentsCreatedByteacher(teacherId, status);
        assertEquals(domains, result);
        verify(crudRepo).findByProfessor_IdAndStatusNotLike(teacherId, status);
        verify(studentTrackingMapper).toDomains(entities);
    }

    @Test
    void testGetTrackingListStudentsCreatedByTeacherReturnsEmptyList() {
        int teacherId = 21;
        String status = "Y";
        when(crudRepo.findByProfessor_IdAndStatusNotLike(teacherId, status)).thenReturn(Collections.emptyList());
        when(studentTrackingMapper.toDomains(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<StudentTrackingDomain> result = adapter.getTrackingListStudentsCreatedByteacher(teacherId, status);
        assertTrue(result.isEmpty());
        verify(crudRepo).findByProfessor_IdAndStatusNotLike(teacherId, status);
        verify(studentTrackingMapper).toDomains(Collections.emptyList());
    }

    @Test
    void testGetTrackingListStudentsCreatedByTeacherWithNullParameters() {
        when(crudRepo.findByProfessor_IdAndStatusNotLike(null, null)).thenReturn(Collections.emptyList());
        when(studentTrackingMapper.toDomains(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<StudentTrackingDomain> result = adapter.getTrackingListStudentsCreatedByteacher(null, null);
        assertTrue(result.isEmpty());
        verify(crudRepo).findByProfessor_IdAndStatusNotLike(null, null);
    }

    @Test
    void testGetTrackingListStudentsCreatedByTeacherHandlesDatabaseException() {
        int teacherId = 22;
        String status = "Z";
        when(crudRepo.findByProfessor_IdAndStatusNotLike(teacherId, status))
                .thenThrow(new DataAccessException("Database error") {});

        assertThrows(DataAccessException.class,
                () -> adapter.getTrackingListStudentsCreatedByteacher(teacherId, status));
        verify(crudRepo).findByProfessor_IdAndStatusNotLike(teacherId, status);
    }
}