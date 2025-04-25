package co.edu.gimnasiolorismalaguzzi.academyservice.modules.knowledge;

import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.domain.AchievementGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.entity.AchievementGroup;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.mapper.AchievementGroupsMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.repository.AchievementGroupsCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.service.AchievementGroupsAdapter;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AchievementGroupsAdapterTest {

    @Mock private AchievementGroupsCrudRepo crudRepo;
    @Mock private AchievementGroupsMapper mapper;
    @InjectMocks private AchievementGroupsAdapter adapter;

    @BeforeEach
    void setUp() {
        // inject the crud repo since it's @Autowired in adapter
        ReflectionTestUtils.setField(adapter, "achievementGroupsCrudRepo", crudRepo);
    }

    @Test
    void findAll_returnsMapped() {
        List<AchievementGroup> entities = List.of(new AchievementGroup());
        List<AchievementGroupDomain> domains = List.of(AchievementGroupDomain.builder().build());
        when(crudRepo.findAll()).thenReturn(entities);
        when(mapper.toDomains(entities)).thenReturn(domains);

        List<AchievementGroupDomain> result = adapter.findAll();

        assertEquals(domains, result);
        verify(crudRepo).findAll();
        verify(mapper).toDomains(entities);
    }

    @Test
    void findAll_emptyList() {
        when(crudRepo.findAll()).thenReturn(Collections.emptyList());
        when(mapper.toDomains(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<AchievementGroupDomain> result = adapter.findAll();

        assertTrue(result.isEmpty());
        verify(crudRepo).findAll();
        verify(mapper).toDomains(Collections.emptyList());
    }

    @Test
    void findById_present() {
        AchievementGroup entity = new AchievementGroup();
        AchievementGroupDomain domain = AchievementGroupDomain.builder().build();
        when(crudRepo.findById(1)).thenReturn(Optional.of(entity));
        when(mapper.toDomains(entity)).thenReturn(domain);

        AchievementGroupDomain result = adapter.findById(1);

        assertEquals(domain, result);
        verify(crudRepo).findById(1);
        verify(mapper).toDomains(entity);
    }

    @Test
    void findById_absent() {
        when(crudRepo.findById(2)).thenReturn(Optional.empty());

        AchievementGroupDomain result = adapter.findById(2);

        assertNull(result);
        verify(crudRepo).findById(2);
        verify(mapper, never()).toDomains(any(AchievementGroup.class));
    }

    @Test
    void save_persistsEntity() {
        AchievementGroupDomain input = AchievementGroupDomain.builder().build();
        AchievementGroup toSave = new AchievementGroup();
        AchievementGroup saved = new AchievementGroup();
        AchievementGroupDomain out = AchievementGroupDomain.builder().build();

        when(mapper.toEntity(input)).thenReturn(toSave);
        when(crudRepo.save(toSave)).thenReturn(saved);
        when(mapper.toDomains(saved)).thenReturn(out);

        AchievementGroupDomain result = adapter.save(input);

        assertEquals(out, result);
        verify(mapper).toEntity(input);
        verify(crudRepo).save(toSave);
        verify(mapper).toDomains(saved);
    }

    @Test
    void update_present() {
        AchievementGroupDomain dto = AchievementGroupDomain.builder().build();
        AchievementGroup existing = new AchievementGroup();
        when(crudRepo.findById(5)).thenReturn(Optional.of(existing));
        when(mapper.toEntity(dto)).thenReturn(existing);
        when(crudRepo.save(existing)).thenReturn(existing);
        when(mapper.toDomains(existing)).thenReturn(dto);

        AchievementGroupDomain result = adapter.update(5, dto);

        assertEquals(dto, result);
        verify(crudRepo).findById(5);
        verify(crudRepo).save(existing);
        verify(mapper, atLeastOnce()).toEntity(dto);
        verify(mapper).toDomains(existing);
    }

    @Test
    void update_absent_throwsNoSuchElement() {
        when(crudRepo.findById(10)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> adapter.update(10, AchievementGroupDomain.builder().build()));
        verify(crudRepo).findById(10);
    }

    @Test
    void delete_returnsIAmATeapot() {
        assertEquals(HttpStatus.I_AM_A_TEAPOT, adapter.delete(1));
    }

    @Test
    void getKnowledgeAchievementBySubjectId_returnsMapped() {
        List<AchievementGroup> entities = List.of(new AchievementGroup());
        List<AchievementGroupDomain> domains = List.of(AchievementGroupDomain.builder().build());
        when(crudRepo.findBySubjectKnowledge_IdSubject_IdAndGroup_IdAndPeriod_Id(1,2,3)).thenReturn(entities);
        when(mapper.toDomains(entities)).thenReturn(domains);

        List<AchievementGroupDomain> result = adapter.getKnowledgeAchievementBySubjectId(1,2,3);

        assertEquals(domains, result);
        verify(crudRepo).findBySubjectKnowledge_IdSubject_IdAndGroup_IdAndPeriod_Id(1,2,3);
        verify(mapper).toDomains(entities);
    }

    @Test
    void getKnowledgeAchievementBySubjectId_emptyList() {
        when(crudRepo.findBySubjectKnowledge_IdSubject_IdAndGroup_IdAndPeriod_Id(4,5,6)).thenReturn(Collections.emptyList());
        when(mapper.toDomains(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<AchievementGroupDomain> result = adapter.getKnowledgeAchievementBySubjectId(4,5,6);

        assertTrue(result.isEmpty());
        verify(crudRepo).findBySubjectKnowledge_IdSubject_IdAndGroup_IdAndPeriod_Id(4,5,6);
        verify(mapper).toDomains(Collections.emptyList());
    }

    @Test
    void getKnowledgeAchievementListByPeriodAndGroupId_returnsMapped() {
        List<AchievementGroup> entities = List.of(new AchievementGroup());
        List<AchievementGroupDomain> domains = List.of(AchievementGroupDomain.builder().build());
        when(crudRepo.findByPeriod_IdAndGroup_Id(7,8)).thenReturn(entities);
        when(mapper.toDomains(entities)).thenReturn(domains);

        List<AchievementGroupDomain> result = adapter.getKnowledgeAchievementListByPeriodAndGroupId(7,8);

        assertEquals(domains, result);
        verify(crudRepo).findByPeriod_IdAndGroup_Id(7,8);
        verify(mapper).toDomains(entities);
    }

    @Test
    void getKnowledgeAchievementListByPeriodAndGroupId_emptyList() {
        when(crudRepo.findByPeriod_IdAndGroup_Id(9,10)).thenReturn(Collections.emptyList());
        when(mapper.toDomains(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<AchievementGroupDomain> result = adapter.getKnowledgeAchievementListByPeriodAndGroupId(9,10);

        assertTrue(result.isEmpty());
        verify(crudRepo).findByPeriod_IdAndGroup_Id(9,10);
        verify(mapper).toDomains(Collections.emptyList());
    }

    @Test
    void getAllBySubjectKnowledgeId_returnsMapped() {
        List<AchievementGroup> entities = List.of(new AchievementGroup());
        List<AchievementGroupDomain> domains = List.of(AchievementGroupDomain.builder().build());
        when(crudRepo.findBySubjectKnowledge_Id(11)).thenReturn(entities);
        when(mapper.toDomains(entities)).thenReturn(domains);

        List<AchievementGroupDomain> result = adapter.getAllBySubjectKnowledgeId(11);

        assertEquals(domains, result);
        verify(crudRepo).findBySubjectKnowledge_Id(11);
        verify(mapper).toDomains(entities);
    }

    @Test
    void getAllBySubjectKnowledgeId_emptyList() {
        when(crudRepo.findBySubjectKnowledge_Id(12)).thenReturn(Collections.emptyList());
        when(mapper.toDomains(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<AchievementGroupDomain> result = adapter.getAllBySubjectKnowledgeId(12);

        assertTrue(result.isEmpty());
        verify(crudRepo).findBySubjectKnowledge_Id(12);
        verify(mapper).toDomains(Collections.emptyList());
    }
}