package co.edu.gimnasiolorismalaguzzi.academyservice.modules.evaluation;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.ActivityDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.ActivityGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.CreateActivityFront;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.Activity;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.ActivityGroup;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.mapper.ActivityGroupMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.mapper.ActivityMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.repository.ActivityCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.repository.ActivityGroupCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.domain.AchievementGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.entity.AchievementGroup;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupStudentsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActivityAdapterTest {

    @Mock
    private ActivityCrudRepo activityCrudRepo;

    @Mock
    private ActivityGroupCrudRepo activityGroupCrudRepo;

    @Mock
    private ActivityMapper activityMapper;

    @Mock
    private ActivityGroupMapper activityGroupMapper;

    @InjectMocks
    private co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.ActivityAdapter adapter;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(adapter, "activityGroupMapper", activityGroupMapper);
    }

    @Test
    void testFindAllReturnsMappedDomains() {
        List<Activity> entities = Arrays.asList(new Activity(), new Activity());
        List<ActivityDomain> domains = Arrays.asList(ActivityDomain.builder().build(), ActivityDomain.builder().build());
        when(activityCrudRepo.findAll()).thenReturn(entities);
        when(activityMapper.toDomains(entities)).thenReturn(domains);

        List<ActivityDomain> result = adapter.findAll();
        assertEquals(domains, result);
        verify(activityCrudRepo).findAll();
        verify(activityMapper).toDomains(entities);
    }

    @Test
    void testFindByIdReturnsDomainWhenPresent() {
        // Arrange
        int id = 1;

        // Crear AchievementGroup
        AchievementGroup achievementGroup = new AchievementGroup();
        achievementGroup.setId(1);

        // Crear Activity con sus dependencias
        Activity entity = new Activity();
        entity.setId(id);
        entity.setAchievementGroup(achievementGroup);

        ActivityDomain domain = ActivityDomain.builder().build();

        // Configurar los mocks
        when(activityCrudRepo.findById(id)).thenReturn(Optional.of(entity));
        when(activityMapper.toDomain(entity)).thenReturn(domain);

        // Act
        ActivityDomain result = adapter.findById(id);

        // Assert
        assertEquals(domain, result);
        verify(activityCrudRepo).findById(id);
        verify(activityMapper).toDomain(entity);
    }


    @Test
    void testFindByIdReturnsNullWhenNotPresent() {
        int id = 2;
        when(activityCrudRepo.findById(id)).thenReturn(Optional.empty());

        ActivityDomain result = adapter.findById(id);
        assertNull(result);
        verify(activityCrudRepo).findById(id);
        verify(activityMapper, never()).toDomain(any());
    }

    @Test
    void testSaveReturnsMappedDomain() {
        ActivityDomain input = ActivityDomain.builder().build();
        Activity entity = new Activity();
        Activity saved = new Activity();
        ActivityDomain output = ActivityDomain.builder().build();

        when(activityMapper.toEntity(input)).thenReturn(entity);
        when(activityCrudRepo.save(entity)).thenReturn(saved);
        when(activityMapper.toDomain(saved)).thenReturn(output);

        ActivityDomain result = adapter.save(input);
        assertEquals(output, result);
        verify(activityMapper).toEntity(input);
        verify(activityCrudRepo).save(entity);
        verify(activityMapper).toDomain(saved);
    }

    @Test
    void testCreateActivityAndGroupSuccess() {
        CreateActivityFront front = CreateActivityFront.builder()
                .activityName("actName")
                .description("desc")
                .achievementGroup(AchievementGroupDomain.builder().id(5).build())
                .group(GroupStudentsDomain.builder().id(10).build())
                .startDate(LocalDate.of(2025, 1, 1))
                .endDate(LocalDate.of(2025, 1, 2))
                .build();

        Activity mappedActivity = new Activity();
        mappedActivity.setAchievementGroup(null);
        Activity savedActivity = new Activity();
        ActivityDomain domainOut = ActivityDomain.builder().build();
        ActivityGroup mappedGroup = new ActivityGroup();
        ActivityGroup savedGroup = new ActivityGroup();

        when(activityMapper.toEntity(any(ActivityDomain.class))).thenReturn(mappedActivity);
        when(activityCrudRepo.save(any(Activity.class))).thenReturn(savedActivity);
        when(activityGroupMapper.toEntity(any(ActivityGroupDomain.class))).thenReturn(mappedGroup);
        when(activityGroupCrudRepo.save(any(ActivityGroup.class))).thenReturn(savedGroup);
        when(activityMapper.toDomain(savedActivity)).thenReturn(domainOut);

        ActivityDomain result = adapter.createActivityAndGroup(front);
        assertEquals(domainOut, result);
        verify(activityMapper).toEntity(any(ActivityDomain.class));
        verify(activityCrudRepo).save(any(Activity.class));
        verify(activityGroupMapper).toEntity(any(ActivityGroupDomain.class));
        verify(activityGroupCrudRepo).save(any(ActivityGroup.class));
        verify(activityMapper).toDomain(savedActivity);
    }

    @Test
    void testCreateActivityAndGroupHandlesException() {
        CreateActivityFront front = CreateActivityFront.builder().activityName("x").description("y")
                .achievementGroup(AchievementGroupDomain.builder().build())
                .group(GroupStudentsDomain.builder().build())
                .startDate(LocalDate.now()).endDate(LocalDate.now())
                .build();
        when(activityMapper.toEntity(any(ActivityDomain.class))).thenThrow(new RuntimeException("fail"));

        AppException ex = assertThrows(AppException.class, () -> adapter.createActivityAndGroup(front));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getCode());
        assertTrue(ex.getMessage().contains("Error creating activity and group"));
    }

    @Test
    void testUpdateActivityAndGroupSuccess() {
        int id = 1;

        CreateActivityFront front = CreateActivityFront.builder()
                .activityName("a").description("b")
                .achievementGroup(AchievementGroupDomain.builder().id(7).build())
                .status("S")
                .group(GroupStudentsDomain.builder().id(8).build())
                .startDate(LocalDate.of(2025,1,1)).endDate(LocalDate.of(2025,1,3))
                .build();
        Activity existing = new Activity();
        Activity saved = new Activity();
        ActivityGroup existingGroup = new ActivityGroup();
        ActivityDomain outDomain = ActivityDomain.builder().build();

        when(activityCrudRepo.findById(id)).thenReturn(Optional.of(existing));
        when(activityMapper.toEntity(any(ActivityDomain.class))).thenReturn(new Activity());
        when(activityCrudRepo.save(existing)).thenReturn(saved);
        when(activityGroupCrudRepo.findFirstByActivity_Id(id)).thenReturn(existingGroup);
        when(activityGroupCrudRepo.save(existingGroup)).thenReturn(existingGroup);
        when(activityMapper.toDomain(saved)).thenReturn(outDomain);

        ActivityDomain result = adapter.updateActivityAndGroup(id, front);
        assertEquals(outDomain, result);
        verify(activityCrudRepo).findById(id);
        verify(activityCrudRepo).save(existing);
        verify(activityGroupCrudRepo).findFirstByActivity_Id(id);
        verify(activityGroupCrudRepo).save(existingGroup);
        verify(activityMapper).toDomain(saved);
    }

    @Test
    void testUpdateActivityAndGroupNotFoundActivity() {
        when(activityCrudRepo.findById(99)).thenReturn(Optional.empty());
        CreateActivityFront front = CreateActivityFront.builder().build();

        AppException ex = assertThrows(AppException.class, () -> adapter.updateActivityAndGroup(99, front));
        assertEquals(HttpStatus.NOT_FOUND, ex.getCode());
        assertTrue(ex.getMessage().contains("Entity not found"));
    }

    @Test
    void testUpdateActivityAndGroupHandlesException() {
        int id = 3;
        when(activityCrudRepo.findById(id)).thenReturn(Optional.of(new Activity()));
        when(activityMapper.toEntity(any(ActivityDomain.class))).thenReturn(new Activity());
        when(activityCrudRepo.save(any(Activity.class))).thenThrow(new RuntimeException("DB error"));
        CreateActivityFront front = CreateActivityFront.builder().build();

        AppException ex = assertThrows(AppException.class, () -> adapter.updateActivityAndGroup(id, front));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getCode());
        assertTrue(ex.getMessage().contains("Error updating activity and group"));
    }

    @Test
    void testUpdateActivityKnowledgeIdSuccess() {
        int id = 4;
        Activity existing = new Activity();
        Activity saved = new Activity();
        ActivityDomain out = ActivityDomain.builder().build();
        when(activityCrudRepo.findById(id)).thenReturn(Optional.of(existing));
        when(activityCrudRepo.save(existing)).thenReturn(saved);
        when(activityMapper.toDomain(saved)).thenReturn(out);

        ActivityDomain result = adapter.updateActivityKnowledgeId(id, 99);
        assertEquals(out, result);
        verify(activityCrudRepo).findById(id);
        verify(activityCrudRepo).save(existing);
        verify(activityMapper).toDomain(saved);
    }

    @Test
    void testUpdateActivityKnowledgeIdThrowsWhenNotPresent() {
        when(activityCrudRepo.findById(5)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> adapter.updateActivityKnowledgeId(5, 10));
        verify(activityCrudRepo).findById(5);
    }

    @Test
    void testGenericUpdateSuccess() {
        int id = 6;
        Activity existing = new Activity();
        Activity saved = new Activity();
        ActivityDomain domainIn = ActivityDomain.builder().build();
        ActivityDomain domainOut = ActivityDomain.builder().build();
        when(activityCrudRepo.findById(id)).thenReturn(Optional.of(existing));
        when(activityMapper.toEntity(domainIn)).thenReturn(new Activity());
        when(activityCrudRepo.save(existing)).thenReturn(saved);
        when(activityMapper.toDomain(saved)).thenReturn(domainOut);

        ActivityDomain result = adapter.update(id, domainIn);
        assertEquals(domainOut, result);
        verify(activityCrudRepo).findById(id);
        verify(activityCrudRepo).save(existing);
        verify(activityMapper).toDomain(saved);
    }

    @Test
    void testGenericUpdateThrowsWhenNotPresent() {
        when(activityCrudRepo.findById(7)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> adapter.update(7, ActivityDomain.builder().build()));
        verify(activityCrudRepo).findById(7);
    }

    @Test
    void testDeleteSuccess() {
        int id = 8;
        when(activityCrudRepo.existsById(id)).thenReturn(true);
        when(activityCrudRepo.updateStatusById("I", id)).thenReturn(1);

        HttpStatus status = adapter.delete(id);
        assertEquals(HttpStatus.OK, status);
        verify(activityCrudRepo).existsById(id);
        verify(activityCrudRepo).updateStatusById("I", id);
    }

    @Test
    void testDeleteThrowsWhenNotFound() {
        int id = 9;
        when(activityCrudRepo.existsById(id)).thenReturn(false);
        AppException ex = assertThrows(AppException.class, () -> adapter.delete(id));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getCode());
        verify(activityCrudRepo).existsById(id);
    }

    @Test
    void testDeleteHandlesException() {
        int id = 10;
        when(activityCrudRepo.existsById(id)).thenReturn(true);
        when(activityCrudRepo.updateStatusById("I", id)).thenThrow(new RuntimeException("fail"));

        AppException ex = assertThrows(AppException.class, () -> adapter.delete(id));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getCode());
        verify(activityCrudRepo).existsById(id);
        verify(activityCrudRepo).updateStatusById("I", id);
    }

    @Test
    void testGetAllActivitiesWithKnowledgesAchievements() {
        List<Activity> entities = Arrays.asList(new Activity());
        List<ActivityDomain> domains = Arrays.asList(ActivityDomain.builder().build());
        when(activityCrudRepo.findAll()).thenReturn(entities);
        when(activityMapper.toDomains(entities)).thenReturn(domains);

        List<ActivityDomain> result = adapter.getAllActivitiesWithKnowledgesAchievements(1);
        assertEquals(domains, result);
        verify(activityCrudRepo).findAll();
        verify(activityMapper).toDomains(entities);
    }

    @Test
    void testGetAllActivitiesByAchievementGroupId() {
        int id = 11;
        List<Activity> entities = Arrays.asList(new Activity());
        List<ActivityDomain> domains = Arrays.asList(ActivityDomain.builder().build());
        when(activityCrudRepo.findByAchievementGroup_Id(id)).thenReturn(entities);
        when(activityMapper.toDomains(entities)).thenReturn(domains);

        List<ActivityDomain> result = adapter.getAllActivitiesByAchievementGroupId(id);
        assertEquals(domains, result);
        verify(activityCrudRepo).findByAchievementGroup_Id(id);
        verify(activityMapper).toDomains(entities);
    }
}
