package co.edu.gimnasiolorismalaguzzi.academyservice.modules.student;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDetailDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceUserDetailPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupStudentsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.StudentPromotionDTO;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
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

    @Mock
    private PersistenceUserDetailPort persistenceUserDetailPort;

    @InjectMocks
    private GroupStudentsAdapter groupStudentsAdapter;


    private GroupStudent groupStudent;
    private GroupStudentsDomain groupStudentDomain;
    private List<GroupStudent> groupStudents;
    private List<GroupStudentsDomain> groupStudentDomains;
    private Groups group;
    private User student;
    private User mentor;
    private EducationalLevel educationalLevel;

    @BeforeEach
    void setUp() {
        // Initialize entities
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
                .promotionStatus("A")
                .build();

        groupStudent = GroupStudent.builder()
                .id(1)
                .group(group)
                .student(student)
                .status("A")
                .build();

        // Initialize domain
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
                .promotionStatus("A")
                .build();

        groupStudentDomain = GroupStudentsDomain.builder()
                .id(1)
                .group(groupDomain)
                .student(studentDomain)
                .status("A")
                .build();

        // Initialize lists
        groupStudents = Arrays.asList(groupStudent);
        groupStudentDomains = Arrays.asList(groupStudentDomain);
    }

    @Test
    void findAll_ShouldReturnAllGroupStudents() {
        when(groupStudentsCrudRepo.findAll()).thenReturn(groupStudents);
        when(groupStudentsMapper.toDomains(groupStudents)).thenReturn(groupStudentDomains);

        List<GroupStudentsDomain> result = groupStudentsAdapter.findAll();

        assertEquals(groupStudentDomains, result);
        verify(groupStudentsCrudRepo).findAll();
        verify(groupStudentsMapper).toDomains(groupStudents);
    }

    @Test
    void findById_WhenExists_ShouldReturnGroupStudent() {
        when(groupStudentsCrudRepo.findById(1)).thenReturn(Optional.of(groupStudent));
        when(groupStudentsMapper.toDomain(groupStudent)).thenReturn(groupStudentDomain);

        GroupStudentsDomain result = groupStudentsAdapter.findById(1);

        assertEquals(groupStudentDomain, result);
        verify(groupStudentsCrudRepo).findById(1);
        verify(groupStudentsMapper).toDomain(groupStudent);
    }

    @Test
    void findById_WhenNotExists_ShouldReturnNull() {
        when(groupStudentsCrudRepo.findById(999)).thenReturn(Optional.empty());

        GroupStudentsDomain result = groupStudentsAdapter.findById(999);

        assertNull(result);
        verify(groupStudentsCrudRepo).findById(999);
        verify(groupStudentsMapper, never()).toDomain(any());
    }

    @Test
    void findByStudentId_ShouldReturnGroupStudents() {
        when(groupStudentsCrudRepo.findByStudent_Id(1)).thenReturn(groupStudents);
        when(groupStudentsMapper.toDomains(groupStudents)).thenReturn(groupStudentDomains);

        List<GroupStudentsDomain> result = groupStudentsAdapter.findByStudentId(1);

        assertEquals(groupStudentDomains, result);
        verify(groupStudentsCrudRepo).findByStudent_Id(1);
        verify(groupStudentsMapper).toDomains(groupStudents);
    }

    @Test
    void getStudentsByGroupId_ShouldReturnStudents() {
        when(groupStudentsCrudRepo.findByGroupId(1)).thenReturn(groupStudents);
        when(groupStudentsMapper.toDomain(any(GroupStudent.class))).thenReturn(groupStudentDomain);

        List<GroupStudentsDomain> result = groupStudentsAdapter.getStudentsByGroupId(1);

        assertEquals(1, result.size());
        verify(groupStudentsCrudRepo).findByGroupId(1);
    }

    @Test
    void getGroupsStudentById_ShouldReturnGroupStudentsByStatusAndId() {
        when(groupStudentsCrudRepo.findByStudent_IdAndGroup_Status(1, "A")).thenReturn(groupStudents);
        when(groupStudentsMapper.toDomains(groupStudents)).thenReturn(groupStudentDomains);

        List<GroupStudentsDomain> result = groupStudentsAdapter.getGroupsStudentById(1, "A");

        assertEquals(groupStudentDomains, result);
        verify(groupStudentsCrudRepo).findByStudent_IdAndGroup_Status(1, "A");
        verify(groupStudentsMapper).toDomains(groupStudents);
    }

    @Test
    void getGroupsStudentByGroupId_ShouldReturnGroupStudentsByGroupId() {
        when(groupStudentsCrudRepo.findByGroup_IdAndGroup_StatusNotLike(1, "I")).thenReturn(groupStudents);
        when(groupStudentsMapper.toDomains(groupStudents)).thenReturn(groupStudentDomains);

        List<GroupStudentsDomain> result = groupStudentsAdapter.getGroupsStudentByGroupId(1, "I");

        assertEquals(groupStudentDomains, result);
        verify(groupStudentsCrudRepo).findByGroup_IdAndGroup_StatusNotLike(1, "I");
        verify(groupStudentsMapper).toDomains(groupStudents);
    }

    @Test
    void getListByMentorIdByYear_ShouldReturnGroupStudentsByMentorId() {
        when(groupStudentsCrudRepo.findByGroup_Mentor_Id(2)).thenReturn(groupStudents);
        when(groupStudentsMapper.toDomains(groupStudents)).thenReturn(groupStudentDomains);

        List<GroupStudentsDomain> result = groupStudentsAdapter.getListByMentorIdByYear(2, 2023);

        assertEquals(groupStudentDomains, result);
        verify(groupStudentsCrudRepo).findByGroup_Mentor_Id(2);
        verify(groupStudentsMapper).toDomains(groupStudents);
    }

    @Test
    void getGroupListByStatus_ShouldReturnGroupStudentsByStatus() {
        when(groupStudentsCrudRepo.findByStatus("A")).thenReturn(groupStudents);
        when(groupStudentsMapper.toDomains(groupStudents)).thenReturn(groupStudentDomains);

        List<GroupStudentsDomain> result = groupStudentsAdapter.getGroupListByStatus("A");

        assertEquals(groupStudentDomains, result);
        verify(groupStudentsCrudRepo).findByStatus("A");
        verify(groupStudentsMapper).toDomains(groupStudents);
    }

    @Test
    void save_ShouldSaveGroupStudent() {
        GroupStudentsDomain domainToSave = GroupStudentsDomain.builder()
                .group(GroupsDomain.builder().id(1).build())
                .student(UserDomain.builder().id(1).build())
                .status("A")
                .build();

        GroupStudent entityToSave = GroupStudent.builder()
                .group(group)
                .student(student)
                .status("A")
                .build();

        GroupStudent savedEntity = GroupStudent.builder()
                .id(1)
                .group(group)
                .student(student)
                .status("A")
                .build();

        when(groupStudentsMapper.toEntity(domainToSave)).thenReturn(entityToSave);
        when(groupStudentsCrudRepo.save(entityToSave)).thenReturn(savedEntity);
        when(groupStudentsMapper.toDomain(savedEntity)).thenReturn(groupStudentDomain);

        GroupStudentsDomain result = groupStudentsAdapter.save(domainToSave);

        assertEquals(groupStudentDomain, result);
        verify(groupStudentsMapper).toEntity(domainToSave);
        verify(groupStudentsCrudRepo).save(entityToSave);
        verify(groupStudentsMapper).toDomain(savedEntity);
    }

    @Test
    void update_WhenExists_ShouldUpdateGroupStudent() {
        GroupStudentsDomain domainToUpdate = GroupStudentsDomain.builder()
                .id(1)
                .group(GroupsDomain.builder().id(1).build())
                .student(UserDomain.builder().id(1).build())
                .status("I")
                .build();

        GroupStudent entityToUpdate = GroupStudent.builder()
                .id(1)
                .group(group)
                .student(student)
                .status("I")
                .build();

        when(groupStudentsCrudRepo.findById(1)).thenReturn(Optional.of(groupStudent));
        when(groupStudentsMapper.toEntity(domainToUpdate)).thenReturn(entityToUpdate);
        when(groupStudentsCrudRepo.save(any(GroupStudent.class))).thenReturn(entityToUpdate);
        when(groupStudentsMapper.toDomain(any(GroupStudent.class))).thenReturn(domainToUpdate);

        GroupStudentsDomain result = groupStudentsAdapter.update(1, domainToUpdate);

        assertEquals(domainToUpdate, result);
        verify(groupStudentsCrudRepo).findById(1);
        verify(groupStudentsCrudRepo).save(any(GroupStudent.class));
    }

    @Test
    void update_WhenNotExists_ShouldThrowNoSuchElementException() {
        GroupStudentsDomain domainToUpdate = GroupStudentsDomain.builder()
                .id(999)
                .group(GroupsDomain.builder().id(1).build())
                .student(UserDomain.builder().id(1).build())
                .build();

        when(groupStudentsCrudRepo.findById(999)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            groupStudentsAdapter.update(999, domainToUpdate);
        });

        verify(groupStudentsCrudRepo).findById(999);
        verify(groupStudentsCrudRepo, never()).save(any(GroupStudent.class));
    }



    @Test
    void promoteStudents_WhenNoStudentsPromoted_ShouldThrowBadRequest() {
        StudentPromotionDTO promotionDTO = new StudentPromotionDTO(Collections.emptyList(), 10);

        AppException exception = assertThrows(AppException.class, () -> {
            groupStudentsAdapter.promoteStudents(promotionDTO);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getCode());
    }



    @Test
    void delete_WhenExists_ShouldDeleteAndReturnOk() {
        when(groupStudentsCrudRepo.existsById(1)).thenReturn(true);
        when(groupStudentsCrudRepo.getReferenceById(1)).thenReturn(groupStudent);
        doNothing().when(groupStudentsCrudRepo).delete(groupStudent);

        HttpStatus result = groupStudentsAdapter.delete(1);

        assertEquals(HttpStatus.OK, result);
        verify(groupStudentsCrudRepo).existsById(1);
        verify(groupStudentsCrudRepo).getReferenceById(1);
        verify(groupStudentsCrudRepo).delete(groupStudent);
    }

    @Test
    void delete_WhenNotExists_ShouldThrowNotFound() {
        when(groupStudentsCrudRepo.existsById(999)).thenReturn(false);

        AppException exception = assertThrows(AppException.class, () -> {
            groupStudentsAdapter.delete(999);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getCode());
        verify(groupStudentsCrudRepo).existsById(999);
        verify(groupStudentsCrudRepo, never()).getReferenceById(anyInt());
        verify(groupStudentsCrudRepo, never()).delete(any(GroupStudent.class));
    }

    @Test
    void delete_WhenExceptionOccurs_ShouldThrowInternalServerError() {
        when(groupStudentsCrudRepo.existsById(1)).thenThrow(new RuntimeException("Database error"));

        AppException exception = assertThrows(AppException.class, () -> {
            groupStudentsAdapter.delete(1);
        });

        assertEquals("Internal Error", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getCode());
        verify(groupStudentsCrudRepo).existsById(1);
    }
}