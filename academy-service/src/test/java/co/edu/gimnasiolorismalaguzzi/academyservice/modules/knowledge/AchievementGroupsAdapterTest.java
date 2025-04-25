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
    void getKnowledgeAchievementListByPeriodAndGroupId_emptyList() {
        when(crudRepo.findByPeriod_IdAndGroup_Id(9,10)).thenReturn(Collections.emptyList());
        when(mapper.toDomains(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<AchievementGroupDomain> result = adapter.getKnowledgeAchievementListByPeriodAndGroupId(9,10);

        assertTrue(result.isEmpty());
        verify(crudRepo).findByPeriod_IdAndGroup_Id(9,10);
        verify(mapper).toDomains(Collections.emptyList());
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