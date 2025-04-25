package co.edu.gimnasiolorismalaguzzi.academyservice.modules.evaluation;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.SubjectGroupPortAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.ActivityGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.ActivityGroup;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.mapper.ActivityGroupMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.repository.ActivityGroupCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.ActivityGroupAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.persistence.PersistenceActivityGroupPort;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActivityGroupAdapterTest {

    @Mock
    private ActivityGroupCrudRepo crudRepo;

    @Mock
    private SubjectGroupPortAdapter subjectGroupPort;

    @Mock
    private ActivityGroupMapper mapper;

    @InjectMocks
    private ActivityGroupAdapter adapter;

    @Test
    void testFindAllReturnsMappedDomains() {
        List<ActivityGroup> entities = Arrays.asList(new ActivityGroup(), new ActivityGroup());
        List<ActivityGroupDomain> domains = Arrays.asList(
                ActivityGroupDomain.builder().build(),
                ActivityGroupDomain.builder().build());
        when(crudRepo.findAll()).thenReturn(entities);
        when(mapper.toDomains(entities)).thenReturn(domains);

        List<ActivityGroupDomain> result = adapter.findAll();
        assertEquals(domains, result);
        verify(crudRepo).findAll();
        verify(mapper).toDomains(entities);
    }

    @Test
    void testFindAllReturnsEmptyList() {
        when(crudRepo.findAll()).thenReturn(Collections.emptyList());
        when(mapper.toDomains(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<ActivityGroupDomain> result = adapter.findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByIdReturnsDomainWhenPresent() {
        ActivityGroup entity = new ActivityGroup();
        ActivityGroupDomain domain = ActivityGroupDomain.builder().build();
        when(crudRepo.findById(1)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        ActivityGroupDomain result = adapter.findById(1);
        assertEquals(domain, result);
    }

    @Test
    void testFindByIdReturnsNullWhenNotPresent() {
        when(crudRepo.findById(2)).thenReturn(Optional.empty());
        ActivityGroupDomain result = adapter.findById(2);
        assertNull(result);
    }

    @Test
    void testSaveReturnsMappedDomain() {
        ActivityGroupDomain input = ActivityGroupDomain.builder().build();
        ActivityGroup entity = new ActivityGroup();
        ActivityGroup saved = new ActivityGroup();
        ActivityGroupDomain output = ActivityGroupDomain.builder().build();

        when(mapper.toEntity(input)).thenReturn(entity);
        when(crudRepo.save(entity)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(output);

        ActivityGroupDomain result = adapter.save(input);
        assertEquals(output, result);
    }

    @Test
    void testUpdateSuccess() {
        int id = 3;
        ActivityGroup existing = new ActivityGroup();
        existing.setStartDate(LocalDate.of(2025,1,1));
        existing.setEndDate(LocalDate.of(2025,1,2));

        ActivityGroupDomain inputDomain = ActivityGroupDomain.builder().build();
        inputDomain.setActivity(null);
        inputDomain.setGroup(null);
        inputDomain.setStartDate(LocalDate.of(2025,2,1));
        inputDomain.setEndDate(LocalDate.of(2025,2,2));
        ActivityGroup updatedEntity = new ActivityGroup();
        ActivityGroupDomain output = ActivityGroupDomain.builder().build();

        when(crudRepo.findById(id)).thenReturn(Optional.of(existing));
        when(mapper.toEntity(inputDomain)).thenReturn(updatedEntity);
        // simulate save returning existing mutated
        when(crudRepo.save(existing)).thenReturn(existing);
        when(mapper.toDomain(existing)).thenReturn(output);

        ActivityGroupDomain result = adapter.update(id, inputDomain);
        assertEquals(output, result);
    }

    @Test
    void testUpdateThrowsNoSuchElementWhenNotPresent() {
        when(crudRepo.findById(4)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> adapter.update(4, ActivityGroupDomain.builder().build()));
    }

    @Test
    void testUpdateThrowsEntityNotFoundWhenSaveError() {
        int id = 5;
        ActivityGroupDomain domain = ActivityGroupDomain.builder().build();
        when(crudRepo.findById(id)).thenThrow(new EntityNotFoundException());
        assertThrows(EntityNotFoundException.class, () -> adapter.update(id, domain));
    }

    @Test
    void testDeleteReturnsNull() {
        assertNull(adapter.delete(1));
    }

    @Test
    void testFindActivitiesByGroupIdReturnsDomains() {
        int groupId = 6;
        String status = "X";
        List<ActivityGroup> entities = Collections.singletonList(new ActivityGroup());
        List<ActivityGroupDomain> domains = Collections.singletonList(ActivityGroupDomain.builder().build());
        when(crudRepo.findByGroup_IdAndActivity_StatusNotLike(groupId, status)).thenReturn(entities);
        when(mapper.toDomains(entities)).thenReturn(domains);

        List<ActivityGroupDomain> result = adapter.findActivitiesByGroupId(groupId, status);
        assertEquals(domains, result);
    }


    @Test
    void testGetAllActivityBySubjectAndPeriodAndGroupIdAndStatusNotLike() {
        int subj=1, per=2, grp=3;
        String status="Z";
        List<ActivityGroup> entities = Arrays.asList(new ActivityGroup());
        List<ActivityGroupDomain> domains = Arrays.asList(ActivityGroupDomain.builder().build());
        when(crudRepo.findActivityBySubjectAndPeriodAndGroupIdAndStatusNotLike(per, subj, grp, status)).thenReturn(entities);
        when(mapper.toDomains(entities)).thenReturn(domains);

        List<ActivityGroupDomain> result = adapter.getAllActivityBySubjectAndPeriodAndGroupIdAndStatusNotLike(subj, per, grp, status);
        assertEquals(domains, result);
    }

    @Test
    void testGetAllActivity_ByPeriodSubjectGroup() {
        int subjProf=3, per=4, grp=5;
        String status="Y";
        List<ActivityGroup> entities = Arrays.asList(new ActivityGroup());
        List<ActivityGroupDomain> domains = Arrays.asList(ActivityGroupDomain.builder().build());
        when(crudRepo.findActivityGroupsByFilters(per, subjProf, grp, status)).thenReturn(entities);
        when(mapper.toDomains(entities)).thenReturn(domains);

        List<ActivityGroupDomain> result = adapter.getAllActivity_ByPeriodSubjectGroup(subjProf, per, grp, status);
        assertEquals(domains, result);
    }

    @Test
    void testGetRangeDateActivityByActivityIdReturnsDomain() {
        int actId=11;
        ActivityGroup entity = new ActivityGroup();
        ActivityGroupDomain domain = ActivityGroupDomain.builder().build();
        when(crudRepo.findFirstByActivity_Id(actId)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(domain);

        ActivityGroupDomain result = adapter.getRangeDateActivityByActivityId(actId);
        assertEquals(domain, result);
    }

    @Test
    void testGetRangeDateActivityByActivityIdReturnsNull() {
        when(crudRepo.findFirstByActivity_Id(12)).thenReturn(null);
        ActivityGroupDomain result = adapter.getRangeDateActivityByActivityId(12);
        assertNull(result);
    }

    @Test
    void testFindByActivity_IdAndGroup_IdReturnsOptional() {
        ActivityGroup entity = new ActivityGroup();
        when(crudRepo.findByActivity_IdAndGroup_Id(13,14)).thenReturn(Optional.of(entity));
        Optional<ActivityGroup> result = adapter.findByActivity_IdAndGroup_Id(13,14);
        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    void testFindByActivity_IdAndGroup_IdReturnsEmpty() {
        when(crudRepo.findByActivity_IdAndGroup_Id(15,16)).thenReturn(Optional.empty());
        Optional<ActivityGroup> result = adapter.findByActivity_IdAndGroup_Id(15,16);
        assertFalse(result.isPresent());
    }
}
