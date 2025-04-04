package co.edu.gimnasiolorismalaguzzi.academyservice.modules.student;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupStudentsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.EducationalLevel;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.GroupStudent;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.Groups;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.mapper.GroupStudentsMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.repository.GroupStudentsCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.GroupStudentsAdapter;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GroupStudentsTest {

    @Mock
    private GroupStudentsCrudRepo groupStudentsCrudRepo;

    @Mock
    private GroupStudentsMapper groupStudentsMapper;

    private GroupStudentsAdapter groupStudentsAdapter;

    private GroupStudent groupStudent;
    private GroupStudentsDomain groupStudentDomain;
    private List<GroupStudent> groupStudents;
    private List<GroupStudentsDomain> groupStudentDomains;
    private Groups group;
    private User student;
    private EducationalLevel educationalLevel;
    private User mentor;

    @BeforeEach
    void setUp() {
        // Crear la instancia con el constructor e inyectar manualmente el mapper
        groupStudentsAdapter = new GroupStudentsAdapter(groupStudentsCrudRepo,groupStudentsMapper);

        // Inicializar entidades
        educationalLevel = new EducationalLevel();
        educationalLevel.setId(1);
        educationalLevel.setLevelName("Primaria");

        mentor = User.builder()
                .id(2)
                .username("mentor")
                .email("mentor@example.com")
                .password("password")
                .status("A")
                .lastName("Mentor")
                .firstName("Test")
                .promotionStatus("NA")
                .build();

        group = Groups.builder()
                .id(1)
                .level(educationalLevel)
                .groupCode("A001")
                .groupName("Grupo A")
                .mentor(mentor)
                .status("A")
                .build();

        student = User.builder()
                .id(1)
                .username("student")
                .email("student@example.com")
                .password("password")
                .status("A")
                .lastName("Doe")
                .firstName("John")
                .promotionStatus("NA")
                .build();

        groupStudent = GroupStudent.builder()
                .id(1)
                .group(group)
                .student(student)
                .build();

        // Inicializar dominio
        UserDomain mentorDomain = UserDomain.builder()
                .id(2)
                .username("mentor")
                .email("mentor@example.com")
                .status("A")
                .lastName("Mentor")
                .firstName("Test")
                .promotionStatus("NA")
                .build();

        GroupsDomain groupDomain = GroupsDomain.builder()
                .id(1)
                .groupName("Grupo A")
                .status("A")
                .mentor(mentorDomain)
                .build();

        UserDomain studentDomain = UserDomain.builder()
                .id(1)
                .username("student")
                .email("student@example.com")
                .status("A")
                .lastName("Doe")
                .firstName("John")
                .promotionStatus("NA")
                .build();

        groupStudentDomain = GroupStudentsDomain.builder()
                .id(1)
                .group(GroupsDomain.builder().id(1).build())
                .student(UserDomain.builder().id(1).build())
                .build();

        // Inicializar listas
        groupStudents = Arrays.asList(groupStudent);
        groupStudentDomains = Arrays.asList(groupStudentDomain);
    }

    @Test
    void findAll_ShouldReturnAllGroupStudents() {
        // Arrange
        when(groupStudentsCrudRepo.findAll()).thenReturn(groupStudents);
        when(groupStudentsMapper.toDomains(groupStudents)).thenReturn(groupStudentDomains);

        // Act
        List<GroupStudentsDomain> result = groupStudentsAdapter.findAll();

        // Assert
        assertEquals(groupStudentDomains, result);
        verify(groupStudentsCrudRepo).findAll();
        verify(groupStudentsMapper).toDomains(groupStudents);
    }

    @Test
    void findById_WhenGroupStudentExists_ShouldReturnGroupStudent() {
        // Arrange
        Integer id = 1;
        when(groupStudentsCrudRepo.findById(id)).thenReturn(Optional.of(groupStudent));
        when(groupStudentsMapper.toDomain(groupStudent)).thenReturn(groupStudentDomain);

        // Act
        GroupStudentsDomain result = groupStudentsAdapter.findById(id);

        // Assert
        assertEquals(groupStudentDomain, result);
        verify(groupStudentsCrudRepo).findById(id);
        verify(groupStudentsMapper).toDomain(groupStudent);
    }

    @Test
    void findById_WhenGroupStudentDoesNotExist_ShouldReturnNull() {
        // Arrange
        Integer id = 999;
        when(groupStudentsCrudRepo.findById(id)).thenReturn(Optional.empty());

        // Act
        GroupStudentsDomain result = groupStudentsAdapter.findById(id);

        // Assert
        assertNull(result);
        verify(groupStudentsCrudRepo).findById(id);
        verify(groupStudentsMapper, never()).toDomain(any());
    }

    @Test
    void getGroupsStudentById_ShouldReturnGroupStudentsByStudentIdAndStatus() {
        // Arrange
        int studentId = 1;
        String status = "A";

        when(groupStudentsCrudRepo.findByStudent_IdAndGroup_Status(studentId, status)).thenReturn(groupStudents);
        when(groupStudentsMapper.toDomains(groupStudents)).thenReturn(groupStudentDomains);

        // Act
        List<GroupStudentsDomain> result = groupStudentsAdapter.getGroupsStudentById(studentId, status);

        // Assert
        assertEquals(groupStudentDomains, result);
        verify(groupStudentsCrudRepo).findByStudent_IdAndGroup_Status(studentId, status);
        verify(groupStudentsMapper).toDomains(groupStudents);
    }

    @Test
    void getGroupsStudentByGroupId_ShouldReturnGroupStudentsByGroupIdAndStatusNotLike() {
        // Arrange
        Integer groupId = 1;
        String statusNotLike = "I";

        when(groupStudentsCrudRepo.findByGroup_IdAndGroup_StatusNotLike(groupId, statusNotLike)).thenReturn(groupStudents);
        when(groupStudentsMapper.toDomains(groupStudents)).thenReturn(groupStudentDomains);

        // Act
        List<GroupStudentsDomain> result = groupStudentsAdapter.getGroupsStudentByGroupId(groupId, statusNotLike);

        // Assert
        assertEquals(groupStudentDomains, result);
        verify(groupStudentsCrudRepo).findByGroup_IdAndGroup_StatusNotLike(groupId, statusNotLike);
        verify(groupStudentsMapper).toDomains(groupStudents);
    }

    @Test
    void getListByMentorIdByYear_ShouldReturnGroupStudentsByMentorId() {
        // Arrange
        Integer mentorId = 2;
        Integer year = 2023;

        when(groupStudentsCrudRepo.findByGroup_Mentor_Id(mentorId)).thenReturn(groupStudents);
        when(groupStudentsMapper.toDomains(groupStudents)).thenReturn(groupStudentDomains);

        // Act
        List<GroupStudentsDomain> result = groupStudentsAdapter.getListByMentorIdByYear(mentorId, year);

        // Assert
        assertEquals(groupStudentDomains, result);
        verify(groupStudentsCrudRepo).findByGroup_Mentor_Id(mentorId);
        verify(groupStudentsMapper).toDomains(groupStudents);
    }

    @Test
    void getGroupListByStatus_ShouldReturnGroupStudentsByGroupStatus() {
        // Arrange
        String status = "A";

        when(groupStudentsCrudRepo.findByGroup_Status(status)).thenReturn(groupStudents);
        when(groupStudentsMapper.toDomains(groupStudents)).thenReturn(groupStudentDomains);

        // Act
        List<GroupStudentsDomain> result = groupStudentsAdapter.getGroupListByStatus(status);

        // Assert
        assertEquals(groupStudentDomains, result);
        verify(groupStudentsCrudRepo).findByGroup_Status(status);
        verify(groupStudentsMapper).toDomains(groupStudents);
    }

    @Test
    void save_ShouldSaveGroupStudent() {
        // Arrange
        GroupStudentsDomain domainToSave = GroupStudentsDomain.builder()
                .id(1)
                .group(GroupsDomain.builder().id(1).build())
                .student(UserDomain.builder().id(1).build())
                .build();


        GroupStudent entityToSave = GroupStudent.builder()
                .group(group)
                .student(student)
                .build();

        GroupStudent savedEntity = GroupStudent.builder()
                .id(1)
                .group(group)
                .student(student)
                .build();

        when(groupStudentsMapper.toEntity(domainToSave)).thenReturn(entityToSave);
        when(groupStudentsCrudRepo.save(entityToSave)).thenReturn(savedEntity);
        when(groupStudentsMapper.toDomain(savedEntity)).thenReturn(groupStudentDomain);

        // Act
        GroupStudentsDomain result = groupStudentsAdapter.save(domainToSave);

        // Assert
        assertEquals(groupStudentDomain, result);
        verify(groupStudentsMapper).toEntity(domainToSave);
        verify(groupStudentsCrudRepo).save(entityToSave);
        verify(groupStudentsMapper).toDomain(savedEntity);
    }



    @Test
    void update_WhenGroupStudentDoesNotExist_ShouldThrowEntityNotFoundException() {
        // Arrange
        Integer id = 999;
        GroupStudentsDomain domainToUpdate = GroupStudentsDomain.builder()
                .id(999)
                .group(GroupsDomain.builder().id(1).build())
                .student(UserDomain.builder().id(1).build())
                .build();

        when(groupStudentsCrudRepo.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            groupStudentsAdapter.update(id, domainToUpdate);
        });

        verify(groupStudentsCrudRepo).findById(id);
        verify(groupStudentsCrudRepo, never()).save(any(GroupStudent.class));
    }

    @Test
    void delete_WhenGroupStudentExists_ShouldDeleteAndReturnOkStatus() {
        // Arrange
        Integer id = 1;
        GroupStudent groupStudentToDelete = GroupStudent.builder()
                .id(1)
                .group(group)
                .student(student)
                .build();

        when(groupStudentsCrudRepo.existsById(id)).thenReturn(true);
        when(groupStudentsCrudRepo.getReferenceById(id)).thenReturn(groupStudentToDelete);
        doNothing().when(groupStudentsCrudRepo).delete(groupStudentToDelete);

        // Act
        HttpStatus result = groupStudentsAdapter.delete(id);

        // Assert
        assertEquals(HttpStatus.OK, result);
        verify(groupStudentsCrudRepo).existsById(id);
        verify(groupStudentsCrudRepo).getReferenceById(id);
        verify(groupStudentsCrudRepo).delete(groupStudentToDelete);
    }

    @Test
    void delete_WhenGroupStudentDoesNotExist_ShouldThrowAppException() {
        // Arrange
        Integer id = 999;

        when(groupStudentsCrudRepo.existsById(id)).thenReturn(false);

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> {
            groupStudentsAdapter.delete(id);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getCode());
        verify(groupStudentsCrudRepo).existsById(id);
        verify(groupStudentsCrudRepo, never()).getReferenceById(anyInt());
        verify(groupStudentsCrudRepo, never()).delete(any(GroupStudent.class));
    }

    @Test
    void delete_WhenExceptionOccurs_ShouldThrowAppExceptionWithInternalServerError() {
        // Arrange
        Integer id = 1;

        when(groupStudentsCrudRepo.existsById(id)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> {
            groupStudentsAdapter.delete(id);
        });

        assertEquals("Internal Error", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getCode());
        verify(groupStudentsCrudRepo).existsById(id);
    }

}
