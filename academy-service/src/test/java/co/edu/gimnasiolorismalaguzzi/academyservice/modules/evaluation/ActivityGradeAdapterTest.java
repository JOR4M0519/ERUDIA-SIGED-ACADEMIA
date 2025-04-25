package co.edu.gimnasiolorismalaguzzi.academyservice.modules.evaluation;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.SubjectGradeAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceAcademicPeriodPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.ActivityDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.ActivityGradeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.ActivityGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.Activity;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.ActivityGrade;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.ActivityGroup;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.mapper.ActivityGradeMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.repository.ActivityGradeCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.ActivityGradeAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.persistence.PersistenceActivityGroupPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.persistence.PersistenceActivityPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.UserMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.AcademicPeriodDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.domain.AchievementGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.service.persistence.PersistenceAchievementGroups;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence.PersistenceGroupStudentPort;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActivityGradeAdapterTest {

    @Mock
    private ActivityGradeCrudRepo gradeCrudRepo;

    @Mock
    private ActivityGradeMapper gradeMapper;

    @Mock
    private PersistenceGroupStudentPort groupStudentPort;

    @Mock
    private PersistenceActivityPort activityPort;

    @Mock
    private PersistenceActivityGroupPort activityGroupPort;

    @Mock
    private PersistenceAcademicPeriodPort academicPeriodPort;

    @Mock
    private PersistenceAchievementGroups achievementGroupsPort;

    @Mock
    private SubjectGradeAdapter subjectGradePort;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private ActivityGradeAdapter adapter;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(adapter, "groupStudentsPort", groupStudentPort);
        ReflectionTestUtils.setField(adapter, "activityPort", activityPort);
        ReflectionTestUtils.setField(adapter, "activityGroupPort", activityGroupPort);
        ReflectionTestUtils.setField(adapter, "academicPeriodPort", academicPeriodPort);
        ReflectionTestUtils.setField(adapter, "achievementGroupsPort", achievementGroupsPort);
        ReflectionTestUtils.setField(adapter, "subjectGradePort", subjectGradePort);
        ReflectionTestUtils.setField(adapter, "userMapper", userMapper);
    }

    @Test
    void testFindAllReturnsMappedDomains() {
        List<ActivityGrade> entities = Arrays.asList(new ActivityGrade(), new ActivityGrade());
        List<ActivityGradeDomain> domains = Arrays.asList(ActivityGradeDomain.builder().build(), ActivityGradeDomain.builder().build());
        when(gradeCrudRepo.findAll()).thenReturn(entities);
        when(gradeMapper.toDomains(entities)).thenReturn(domains);

        List<ActivityGradeDomain> result = adapter.findAll();
        assertEquals(domains, result);
        verify(gradeCrudRepo).findAll();
        verify(gradeMapper).toDomains(entities);
    }

    @Test
    void testFindAllReturnsEmptyList() {
        when(gradeCrudRepo.findAll()).thenReturn(Collections.emptyList());
        when(gradeMapper.toDomains(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<ActivityGradeDomain> result = adapter.findAll();
        assertTrue(result.isEmpty());
        verify(gradeCrudRepo).findAll();
        verify(gradeMapper).toDomains(Collections.emptyList());
    }

    @Test
    void testFindByIdReturnsDomainWhenPresent() {
        ActivityGrade entity = new ActivityGrade();
        ActivityGradeDomain domain = ActivityGradeDomain.builder().build();
        when(gradeCrudRepo.findById(1)).thenReturn(Optional.of(entity));
        when(gradeMapper.toDomain(entity)).thenReturn(domain);

        ActivityGradeDomain result = adapter.findById(1);
        assertEquals(domain, result);
    }

    @Test
    void testFindByIdReturnsNullWhenNotPresent() {
        when(gradeCrudRepo.findById(2)).thenReturn(Optional.empty());

        ActivityGradeDomain result = adapter.findById(2);
        assertNull(result);
    }

    @Test
    void testSaveReturnsMappedDomain() {
        ActivityGradeDomain input = ActivityGradeDomain.builder().build();
        ActivityGrade entity = new ActivityGrade();
        ActivityGrade saved = new ActivityGrade();
        ActivityGradeDomain output = ActivityGradeDomain.builder().build();

        when(gradeMapper.toEntity(input)).thenReturn(entity);
        when(gradeCrudRepo.save(entity)).thenReturn(saved);
        when(gradeMapper.toDomain(saved)).thenReturn(output);

        ActivityGradeDomain result = adapter.save(input);
        assertEquals(output, result);
    }

    @Test
    void testUpdateThrowsNoSuchElementWhenNotPresent() {
        when(gradeCrudRepo.findById(5)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> adapter.update(5, ActivityGradeDomain.builder().build()));
    }

    @Test
    void testUpdateThrowsEntityNotFoundWhenActivityGroupMissing() {
        int id = 7;
        // Make sure we provide a complete ActivityGroupDomain with a non-null activity
        ActivityDomain activityDomain = ActivityDomain.builder().id(9).build();
        GroupsDomain groupsDomain = GroupsDomain.builder().id(10).build();

        ActivityGradeDomain input = ActivityGradeDomain.builder()
                .student(UserDomain.builder().id(8).build())
                .activity(ActivityGroupDomain.builder()
                        .activity(activityDomain)
                        .group(groupsDomain)
                        .build())
                .build();

        when(gradeCrudRepo.findById(id)).thenReturn(Optional.of(new ActivityGrade()));
        when(gradeMapper.toEntity(any())).thenReturn(new ActivityGrade());
        when(activityGroupPort.findByActivity_IdAndGroup_Id(9, 10)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> adapter.update(id, input));
    }

    @Test
    void testUpdateSuccess() {
        int id = 8;
        ActivityGrade existing = new ActivityGrade();
        ActivityGroup group = new ActivityGroup();
        group.setId(2);
        existing.setActivity(group);

        UserDomain student = UserDomain.builder().id(3).build();
        ActivityDomain activity = ActivityDomain.builder().id(8).build();
        GroupsDomain groupDomain = GroupsDomain.builder().id(2).build();

        ActivityGradeDomain input = ActivityGradeDomain.builder()
                .student(student)
                .activity(ActivityGroupDomain.builder()
                        .activity(activity)
                        .group(groupDomain)
                        .build())
                .score(new BigDecimal("4.5"))
                .comment("ok")
                .build();

        ActivityGrade savedEntity = new ActivityGrade();
        // Create output domain with the student to prevent null student issue
        ActivityGradeDomain outDomain = ActivityGradeDomain.builder()
                .student(student)
                .build();

        when(gradeCrudRepo.findById(id)).thenReturn(Optional.of(existing));
        when(gradeMapper.toEntity(input)).thenReturn(existing);
        when(activityGroupPort.findByActivity_IdAndGroup_Id(8, 2)).thenReturn(Optional.of(group));
        when(gradeCrudRepo.save(existing)).thenReturn(savedEntity);
        when(gradeMapper.toDomain(savedEntity)).thenReturn(outDomain);
        when(academicPeriodPort.getAllPeriodsByStatus("A")).thenReturn(Collections.singletonList(AcademicPeriodDomain.builder().id(1).build()));
        when(achievementGroupsPort.getKnowledgeAchievementListByPeriodAndGroupId(1, 2)).thenReturn(Collections.emptyList());

        ActivityGradeDomain result = adapter.update(id, input);
        assertEquals(outDomain, result);
        verify(subjectGradePort, never()).saveOrUpdateSubjectGrade(anyInt(), anyInt(), anyInt(), any(BigDecimal.class));
    }

    @Test
    void testUpdateHandlesGenericException() {
        when(gradeCrudRepo.findById(10)).thenThrow(new RuntimeException("db fail"));
        assertThrows(RuntimeException.class, () -> adapter.update(10, ActivityGradeDomain.builder().build()));
    }

    @Test
    void testGetGradeByActivityIdGroupIdReturnsDomains() {
        List<ActivityGrade> entities = Arrays.asList(new ActivityGrade());
        List<ActivityGradeDomain> domains = Collections.singletonList(ActivityGradeDomain.builder().build());
        when(gradeCrudRepo.findByActivityAndGroupId(3,4)).thenReturn(entities);
        when(gradeMapper.toDomains(entities)).thenReturn(domains);

        List<ActivityGradeDomain> result = adapter.getGradeByActivityIdGroupId(3,4);
        assertEquals(domains, result);
    }

    @Test
    void testGetGradeByActivityIdGroupIdReturnsEmptyList() {
        when(gradeCrudRepo.findByActivityAndGroupId(5,6)).thenReturn(Collections.emptyList());
        when(gradeMapper.toDomains(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<ActivityGradeDomain> result = adapter.getGradeByActivityIdGroupId(5,6);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetGradeByActivityGroupIdByStudentIdReturnsDomain() {
        ActivityGrade entity = new ActivityGrade();
        ActivityGradeDomain domain = ActivityGradeDomain.builder().build();
        when(gradeCrudRepo.findByActivity_IdAndStudent_Id(5,6)).thenReturn(entity);
        when(gradeMapper.toDomain(entity)).thenReturn(domain);

        assertEquals(domain, adapter.getGradeByActivityGroupIdByStudentId(5,6));
    }

    @Test
    void testGetGradeByActivityGroupIdByStudentIdReturnsNull() {
        when(gradeCrudRepo.findByActivity_IdAndStudent_Id(7,8)).thenReturn(null);
        when(gradeMapper.toDomain(null)).thenReturn(null);

        assertNull(adapter.getGradeByActivityGroupIdByStudentId(7,8));
    }

    @Test
    void testGetGradeByActivityIdByStudentIdReturnsDomain() {
        ActivityGrade entity = new ActivityGrade();
        ActivityGradeDomain domain = ActivityGradeDomain.builder().build();
        // Fix: Correct parameter order to match implementation (studentId, activityId)
        when(gradeCrudRepo.findByStudent_IdAndActivity_Activity_Id(7,8)).thenReturn(entity);
        when(gradeMapper.toDomain(entity)).thenReturn(domain);

        assertEquals(domain, adapter.getGradeByActivityIdByStudentId(8,7));
    }

    @Test
    void testGetGradeByActivityIdByStudentIdReturnsNull() {
        // Fix: Correct parameter order to match implementation (studentId, activityId)
        when(gradeCrudRepo.findByStudent_IdAndActivity_Activity_Id(9,10)).thenReturn(null);
        when(gradeMapper.toDomain(null)).thenReturn(null);

        assertNull(adapter.getGradeByActivityIdByStudentId(10,9));
    }
}