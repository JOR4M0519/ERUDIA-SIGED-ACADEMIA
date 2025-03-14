package co.edu.gimnasiolorismalaguzzi.academyservice.modules.academic;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectScheduleDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectGroup;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectSchedule;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectGroupMapper;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubjectScheduleAdapterTest {

    @Mock
    private SubjectScheduleCrudRepo subjectScheduleCrudRepo;

    @Mock
    private SubjectScheduleMapper subjectScheduleMapper;
    @Mock
    private SubjectGroupMapper subjectGroupMapper;


    private SubjectScheduleAdapter subjectScheduleAdapter;

    private SubjectSchedule subjectSchedule;
    private SubjectScheduleDomain subjectScheduleDomain;
    private List<SubjectSchedule> subjectSchedules;
    private List<SubjectScheduleDomain> subjectScheduleDomains;

    @BeforeEach
    void setUp() {
        // Crear la instancia con el constructor
        subjectScheduleAdapter = new SubjectScheduleAdapter(subjectScheduleCrudRepo, subjectScheduleMapper);

        // Inicializar entidades
        SubjectGroup subjectGroup = new SubjectGroup();
        subjectGroup.setId(1);

        subjectSchedule = SubjectSchedule.builder()
                .id(1)
                .subjectGroup(subjectGroup)
                .dayOfWeek("MONDAY")
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(9, 0))
                .status("A")
                .build();

        // Inicializar dominio
        subjectScheduleDomain = SubjectScheduleDomain.builder()
                .id(1)
                .subjectGroup(subjectGroupMapper.toDomain(subjectGroup))
                .dayOfWeek("MONDAY")
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(9, 0))
                .status("A")
                .build();

        // Inicializar listas
        subjectSchedules = Arrays.asList(subjectSchedule);
        subjectScheduleDomains = Arrays.asList(subjectScheduleDomain);
    }

    @Test
    void findAll_ShouldReturnAllSubjectSchedules() {
        // Arrange
        when(subjectScheduleCrudRepo.findAll()).thenReturn(subjectSchedules);
        when(subjectScheduleMapper.toDomains(subjectSchedules)).thenReturn(subjectScheduleDomains);

        // Act
        List<SubjectScheduleDomain> result = subjectScheduleAdapter.findAll();

        // Assert
        assertEquals(subjectScheduleDomains, result);
        verify(subjectScheduleCrudRepo).findAll();
        verify(subjectScheduleMapper).toDomains(subjectSchedules);
    }

    @Test
    void findById_WhenSubjectScheduleExists_ShouldReturnSubjectSchedule() {
        // Arrange
        Integer id = 1;
        when(subjectScheduleCrudRepo.findById(id)).thenReturn(Optional.of(subjectSchedule));
        when(subjectScheduleMapper.toDomain(subjectSchedule)).thenReturn(subjectScheduleDomain);

        // Act
        SubjectScheduleDomain result = subjectScheduleAdapter.findById(id);

        // Assert
        assertEquals(subjectScheduleDomain, result);
        verify(subjectScheduleCrudRepo).findById(id);
        verify(subjectScheduleMapper).toDomain(subjectSchedule);
    }

    @Test
    void findById_WhenSubjectScheduleDoesNotExist_ShouldReturnNull() {
        // Arrange
        Integer id = 999;
        when(subjectScheduleCrudRepo.findById(id)).thenReturn(Optional.empty());

        // Act
        SubjectScheduleDomain result = subjectScheduleAdapter.findById(id);

        // Assert
        assertNull(result);
        verify(subjectScheduleCrudRepo).findById(id);
        verify(subjectScheduleMapper, never()).toDomain(any());
    }

    @Test
    void save_ShouldSaveSubjectSchedule() {
        // Arrange
        SubjectScheduleDomain domainToSave = SubjectScheduleDomain.builder()
                .subjectGroup(subjectGroupMapper.toDomain(subjectSchedule.getSubjectGroup()))
                .dayOfWeek("TUESDAY")
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .status("A")
                .build();

        SubjectSchedule entityToSave = SubjectSchedule.builder()
                .subjectGroup(subjectSchedule.getSubjectGroup())
                .dayOfWeek("TUESDAY")
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .status("A")
                .build();

        SubjectSchedule savedEntity = SubjectSchedule.builder()
                .id(2)
                .subjectGroup(subjectSchedule.getSubjectGroup())
                .dayOfWeek("TUESDAY")
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .status("A")
                .build();

        SubjectScheduleDomain savedDomain = SubjectScheduleDomain.builder()
                .id(2)
                .subjectGroup(subjectGroupMapper.toDomain(subjectSchedule.getSubjectGroup()))
                .dayOfWeek("TUESDAY")
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .status("A")
                .build();

        when(subjectScheduleMapper.toEntity(domainToSave)).thenReturn(entityToSave);
        when(subjectScheduleCrudRepo.save(entityToSave)).thenReturn(savedEntity);
        when(subjectScheduleMapper.toDomain(savedEntity)).thenReturn(savedDomain);

        // Act
        SubjectScheduleDomain result = subjectScheduleAdapter.save(domainToSave);

        // Assert
        assertEquals(savedDomain, result);
        verify(subjectScheduleMapper).toEntity(domainToSave);
        verify(subjectScheduleCrudRepo).save(entityToSave);
        verify(subjectScheduleMapper).toDomain(savedEntity);
    }


}
