package co.edu.gimnasiolorismalaguzzi.academyservice.modules.academic;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.AcademicPeriod;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.Subject;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectGroup;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectProfessor;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.AcademicPeriodMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectGroupMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectProfessorMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectGroupCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.SubjectGroupPortAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.UserMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.Groups;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.mapper.GroupsMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubjectGroupPortAdapterTest {

    @Mock
    private SubjectGroupCrudRepo subjectGroupCrudRepo;

    @Mock
    private SubjectGroupMapper subjectGroupMapper;
    @Mock
    private GroupsMapper groupMapper;
    @Mock
    private SubjectProfessorMapper subjectProfessorMapper;
    @Mock
    private AcademicPeriodMapper academicPeriodMapper;



    private SubjectGroupPortAdapter subjectGroupPortAdapter;

    private SubjectGroup subjectGroup;
    private SubjectGroupDomain subjectGroupDomain;
    private List<SubjectGroup> subjectGroups;
    private List<SubjectGroupDomain> subjectGroupDomains;


    @BeforeEach
    void setUp() {
        // Crear la instancia con el constructor
        subjectGroupPortAdapter = new SubjectGroupPortAdapter(subjectGroupMapper);

        // Inyectar manualmente el repositorio usando ReflectionTestUtils
        ReflectionTestUtils.setField(subjectGroupPortAdapter, "subjectGroupCrudRepo", subjectGroupCrudRepo);

        // Inicializar entidades
        Groups group = new Groups();
        group.setId(1);
        group.setGroupName("Group A");

        User professor = new User();
        professor.setId(1);
        professor.setFirstName("John");
        professor.setLastName("Doe");

        SubjectProfessor subjectProfessor = new SubjectProfessor();
        subjectProfessor.setId(1);
        subjectProfessor.setProfessor(professor);

        AcademicPeriod academicPeriod = new AcademicPeriod();
        academicPeriod.setId(1);
        academicPeriod.setName("First Period 2023");

        subjectGroup = new SubjectGroup();
        subjectGroup.setId(1);
        subjectGroup.setGroups(group);
        subjectGroup.setSubjectProfessor(subjectProfessor);
        subjectGroup.setAcademicPeriod(academicPeriod);

        // Inicializar dominio
        subjectGroupDomain = SubjectGroupDomain.builder()
                .id(1)
                .groups(groupMapper.toDomain(group))
                .subjectProfessor(subjectProfessorMapper.toDomain(subjectProfessor))
                .academicPeriod(academicPeriodMapper.toDomain(academicPeriod))
                .build();

        // Inicializar listas
        subjectGroups = Arrays.asList(subjectGroup);
        subjectGroupDomains = Arrays.asList(subjectGroupDomain);
    }

    @Test
    void findAll_ShouldReturnAllSubjectGroups() {
        // Arrange
        when(subjectGroupCrudRepo.findAll()).thenReturn(subjectGroups);
        when(subjectGroupMapper.toDomains(subjectGroups)).thenReturn(subjectGroupDomains);

        // Act
        List<SubjectGroupDomain> result = subjectGroupPortAdapter.findAll();

        // Assert
        assertEquals(subjectGroupDomains, result);
        verify(subjectGroupCrudRepo).findAll();
        verify(subjectGroupMapper).toDomains(subjectGroups);
    }

    @Test
    void getAllSubjectGroupsByStudentId_ShouldReturnMatchingSubjectGroups() {
        // Arrange
        Integer studentId = 1;
        String year = "2023";

        when(subjectGroupCrudRepo.findSubjectGroupsByStudentIdAndAcademicYear(studentId, year))
                .thenReturn(subjectGroups);
        when(subjectGroupMapper.toDomains(subjectGroups)).thenReturn(subjectGroupDomains);

        // Act
        List<SubjectGroupDomain> result = subjectGroupPortAdapter.getAllSubjectGroupsByStudentId(studentId, year);

        // Assert
        assertEquals(subjectGroupDomains, result);
        verify(subjectGroupCrudRepo).findSubjectGroupsByStudentIdAndAcademicYear(studentId, year);
        verify(subjectGroupMapper).toDomains(subjectGroups);
    }

    @Test
    void getAllSubjectByTeacher_ShouldReturnMatchingSubjectGroups() {
        // Arrange
        Integer teacherId = 1;
        Integer year = 2023;

        when(subjectGroupCrudRepo.getAllSubjectByTeacher(teacherId, year))
                .thenReturn(subjectGroups);
        when(subjectGroupMapper.toDomains(subjectGroups)).thenReturn(subjectGroupDomains);

        // Act
        List<SubjectGroupDomain> result = subjectGroupPortAdapter.getAllSubjectByTeacher(teacherId, year);

        // Assert
        assertEquals(subjectGroupDomains, result);
        verify(subjectGroupCrudRepo).getAllSubjectByTeacher(teacherId, year);
        verify(subjectGroupMapper).toDomains(subjectGroups);
    }

    @Test
    void getStudentListByGroupTeacherPeriod_ShouldReturnMatchingSubjectGroups() {
        // Arrange
        Integer groupId = 1;
        Integer subjectId = 1;
        Integer teacherId = 1;
        Integer periodId = 1;

        when(subjectGroupCrudRepo.findByGroups_IdAndSubjectProfessor_Subject_IdAndSubjectProfessor_Professor_IdAndAcademicPeriod_Id(
                groupId, subjectId, teacherId, periodId))
                .thenReturn(subjectGroups);
        when(subjectGroupMapper.toDomains(subjectGroups)).thenReturn(subjectGroupDomains);

        // Act
        List<SubjectGroupDomain> result = subjectGroupPortAdapter.getStudentListByGroupTeacherPeriod(
                groupId, subjectId, teacherId, periodId);

        // Assert
        assertEquals(subjectGroupDomains, result);
        verify(subjectGroupCrudRepo).findByGroups_IdAndSubjectProfessor_Subject_IdAndSubjectProfessor_Professor_IdAndAcademicPeriod_Id(
                groupId, subjectId, teacherId, periodId);
        verify(subjectGroupMapper).toDomains(subjectGroups);
    }

    @Test
    void findById_WhenSubjectGroupExists_ShouldReturnSubjectGroup() {
        // Arrange
        Integer id = 1;
        when(subjectGroupCrudRepo.findById(id)).thenReturn(Optional.of(subjectGroup));
        when(subjectGroupMapper.toDomain(subjectGroup)).thenReturn(subjectGroupDomain);

        // Act
        SubjectGroupDomain result = subjectGroupPortAdapter.findById(id);

        // Assert
        assertEquals(subjectGroupDomain, result);
        verify(subjectGroupCrudRepo).findById(id);
        verify(subjectGroupMapper).toDomain(subjectGroup);
    }

    @Test
    void findById_WhenSubjectGroupDoesNotExist_ShouldReturnNull() {
        // Arrange
        Integer id = 999;
        when(subjectGroupCrudRepo.findById(id)).thenReturn(Optional.empty());

        // Act
        SubjectGroupDomain result = subjectGroupPortAdapter.findById(id);

        // Assert
        assertNull(result);
        verify(subjectGroupCrudRepo).findById(id);
        verify(subjectGroupMapper, never()).toDomain(any());
    }

    @Test
    void save_ShouldSaveSubjectGroup() {
        // Arrange
        SubjectGroupDomain domainToSave = SubjectGroupDomain.builder()
                .groups( groupMapper.toDomain(subjectGroup.getGroups()))
                .subjectProfessor(subjectProfessorMapper.toDomain(subjectGroup.getSubjectProfessor()))
                .academicPeriod(academicPeriodMapper.toDomain(subjectGroup.getAcademicPeriod()))
                .build();

        SubjectGroup entityToSave = new SubjectGroup();
        entityToSave.setGroups(subjectGroup.getGroups());
        entityToSave.setSubjectProfessor(subjectGroup.getSubjectProfessor());
        entityToSave.setAcademicPeriod(subjectGroup.getAcademicPeriod());

        SubjectGroup savedEntity = new SubjectGroup();
        savedEntity.setId(1);
        savedEntity.setGroups(subjectGroup.getGroups());
        savedEntity.setSubjectProfessor(subjectGroup.getSubjectProfessor());
        savedEntity.setAcademicPeriod(subjectGroup.getAcademicPeriod());

        SubjectGroupDomain savedDomain = SubjectGroupDomain.builder()
                .id(1)
                .groups( groupMapper.toDomain(subjectGroup.getGroups()))
                .subjectProfessor(subjectProfessorMapper.toDomain(subjectGroup.getSubjectProfessor()))
                .academicPeriod(academicPeriodMapper.toDomain(subjectGroup.getAcademicPeriod()))
                .build();

        when(subjectGroupMapper.toEntity(domainToSave)).thenReturn(entityToSave);
        when(subjectGroupCrudRepo.save(entityToSave)).thenReturn(savedEntity);
        when(subjectGroupMapper.toDomain(savedEntity)).thenReturn(savedDomain);

        // Act
        SubjectGroupDomain result = subjectGroupPortAdapter.save(domainToSave);

        // Assert
        assertEquals(savedDomain, result);
        verify(subjectGroupMapper).toEntity(domainToSave);
        verify(subjectGroupCrudRepo).save(entityToSave);
        verify(subjectGroupMapper).toDomain(savedEntity);
    }

    @Test
    void update_WhenSubjectGroupDoesNotExist_ShouldThrowEntityNotFoundException() {
        // Arrange
        Integer id = 999;
        SubjectGroupDomain domainToUpdate = SubjectGroupDomain.builder()
                .id(999)
                .groups( groupMapper.toDomain(subjectGroup.getGroups()))
                .subjectProfessor(subjectProfessorMapper.toDomain(subjectGroup.getSubjectProfessor()))
                .academicPeriod(academicPeriodMapper.toDomain(subjectGroup.getAcademicPeriod()))
                .build();

        when(subjectGroupCrudRepo.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            subjectGroupPortAdapter.update(id, domainToUpdate);
        });

        verify(subjectGroupCrudRepo).findById(id);
        verify(subjectGroupCrudRepo, never()).save(any(SubjectGroup.class));
    }

}
