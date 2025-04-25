package co.edu.gimnasiolorismalaguzzi.academyservice.modules.academic;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectKnowledgeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.Subject;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectKnowledge;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectKnowledgeMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectKnowledgeCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.SubjectKnowledgeAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.domain.AchievementGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.entity.Knowledge;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.service.persistence.PersistenceAchievementGroups;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubjectKnowledgeAdapterTest {

    @Mock private SubjectKnowledgeCrudRepo crudRepo;
    @Mock private SubjectKnowledgeMapper mapper;
    @Mock private PersistenceAchievementGroups achievementGroupsPort;

    private SubjectKnowledgeAdapter adapter;

    private Subject subject;
    private Knowledge knowledge;
    private SubjectKnowledge subjectKnowledge;
    private SubjectKnowledgeDomain subjectKnowledgeDomain;
    private List<SubjectKnowledge>  subjectKnowledges;
    private List<SubjectKnowledgeDomain> subjectKnowledgeDomains;

    @BeforeEach
    void setUp() {
        // Construye el adapter e inyecta el mock de achievementGroupsPort
        adapter = new SubjectKnowledgeAdapter(crudRepo, mapper);
        ReflectionTestUtils.setField(adapter, "achievementGroupsPort", achievementGroupsPort);
        ReflectionTestUtils.setField(adapter, "mapper", mapper);

        // Datos de ejemplo
        subject = Subject.builder()
                .id(1)
                .subjectName("Mathematics")
                .status("A")
                .build();
        knowledge = Knowledge.builder()
                .id(1)
                .name("Algebra")
                .build();

        subjectKnowledge = SubjectKnowledge.builder()
                .id(1)
                .idSubject(subject)
                .idKnowledge(knowledge)
                .build();

        subjectKnowledgeDomain = SubjectKnowledgeDomain.builder()
                .id(1)
                .idSubject(subject)
                .idKnowledge(knowledge)
                .build();

        subjectKnowledges = Arrays.asList(subjectKnowledge);
        subjectKnowledgeDomains = Arrays.asList(subjectKnowledgeDomain);
    }

    @Test
    void findAll_ShouldReturnAllSubjectKnowledges() {
        when(crudRepo.findAll()).thenReturn(subjectKnowledges);
        when(mapper.toDomains(subjectKnowledges)).thenReturn(subjectKnowledgeDomains);

        List<SubjectKnowledgeDomain> result = adapter.findAll();

        assertEquals(subjectKnowledgeDomains, result);
        verify(crudRepo).findAll();
        verify(mapper).toDomains(subjectKnowledges);
    }

    @Test
    void findById_WhenSubjectKnowledgeExists_ShouldReturnSubjectKnowledge() {
        when(crudRepo.findById(1)).thenReturn(Optional.of(subjectKnowledge));
        when(mapper.toDomain(subjectKnowledge)).thenReturn(subjectKnowledgeDomain);

        SubjectKnowledgeDomain result = adapter.findById(1);

        assertEquals(subjectKnowledgeDomain, result);
        verify(crudRepo).findById(1);
        verify(mapper).toDomain(subjectKnowledge);
    }

    @Test
    void findById_WhenSubjectKnowledgeDoesNotExist_ShouldReturnNull() {
        when(crudRepo.findById(999)).thenReturn(Optional.empty());

        SubjectKnowledgeDomain result = adapter.findById(999);

        assertNull(result);
        verify(crudRepo).findById(999);
        verify(mapper, never()).toDomain(any());
    }

    @Test
    void save_ShouldSaveSubjectKnowledge() {
        SubjectKnowledgeDomain dto = SubjectKnowledgeDomain.builder()
                .idSubject(subject)
                .idKnowledge(knowledge)
                .build();
        SubjectKnowledge entityToSave = SubjectKnowledge.builder()
                .idSubject(subject)
                .idKnowledge(knowledge)
                .build();
        SubjectKnowledge savedEntity = SubjectKnowledge.builder()
                .id(1)
                .idSubject(subject)
                .idKnowledge(knowledge)
                .build();
        SubjectKnowledgeDomain savedDto = SubjectKnowledgeDomain.builder()
                .id(1)
                .idSubject(subject)
                .idKnowledge(knowledge)
                .build();

        when(mapper.toEntity(dto)).thenReturn(entityToSave);
        when(crudRepo.save(entityToSave)).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(savedDto);

        SubjectKnowledgeDomain result = adapter.save(dto);

        assertEquals(savedDto, result);
        verify(mapper).toEntity(dto);
        verify(crudRepo).save(entityToSave);
        verify(mapper).toDomain(savedEntity);
    }

    @Test
    void update_WhenSubjectKnowledgeExists_ShouldUpdateAndReturnSubjectKnowledge() {
        SubjectKnowledgeDomain dto = SubjectKnowledgeDomain.builder()
                .id(1)
                .idSubject(subject)
                .idKnowledge(knowledge)
                .build();
        SubjectKnowledge existing = SubjectKnowledge.builder()
                .id(1)
                .idSubject(subject)
                .idKnowledge(knowledge)
                .build();
        SubjectKnowledge updatedEntity = SubjectKnowledge.builder()
                .id(1)
                .idSubject(subject)
                .idKnowledge(knowledge)
                .build();

        when(crudRepo.findById(1)).thenReturn(Optional.of(existing));
        when(crudRepo.save(existing)).thenReturn(updatedEntity);
        when(mapper.toDomain(updatedEntity)).thenReturn(dto);

        SubjectKnowledgeDomain result = adapter.update(1, dto);

        assertEquals(dto, result);
        verify(crudRepo).findById(1);
        verify(crudRepo).save(existing);
        verify(mapper).toDomain(updatedEntity);
    }

    @Test
    void update_WhenSubjectKnowledgeDoesNotExist_ShouldThrowNoSuchElement() {
        when(crudRepo.findById(999)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> adapter.update(999, subjectKnowledgeDomain));
        verify(crudRepo).findById(999);
        verify(crudRepo, never()).save(any());
    }

    // -------------------- DELETE SCENARIOS --------------------

    @Test
    void delete_WhenSubjectKnowledgeExists_ShouldDeleteAndReturnOkStatus() {
        when(crudRepo.existsById(1)).thenReturn(true);
        when(achievementGroupsPort.getAllBySubjectKnowledgeId(1)).thenReturn(Collections.emptyList());
        when(crudRepo.getReferenceById(1)).thenReturn(subjectKnowledge);
        doNothing().when(crudRepo).delete(subjectKnowledge);

        HttpStatus result = adapter.delete(1);

        assertEquals(HttpStatus.OK, result);
        verify(crudRepo).delete(subjectKnowledge);
    }

    @Test
    void delete_WhenSubjectKnowledgeUsedInAchievements_ShouldThrowConflict() {
        when(crudRepo.existsById(1)).thenReturn(true);
        when(achievementGroupsPort.getAllBySubjectKnowledgeId(1))
                .thenReturn(Collections.singletonList(AchievementGroupDomain.builder().build()));

        AppException ex = assertThrows(AppException.class, () -> adapter.delete(1));
        assertEquals(HttpStatus.CONFLICT, ex.getCode());
        assertTrue(ex.getMessage().contains("No es posible eliminar"));
    }

    @Test
    void delete_WhenSubjectKnowledgeDoesNotExist_ShouldThrowNotFound() {
        when(crudRepo.existsById(999)).thenReturn(false);

        AppException ex = assertThrows(AppException.class, () -> adapter.delete(999));
        assertEquals(HttpStatus.NOT_FOUND, ex.getCode());
        assertTrue(ex.getMessage().contains("Relation within Subject"));
    }

    @Test
    void delete_WhenGetAllByThrowsRuntimeException_ShouldPropagate() {
        when(crudRepo.existsById(1)).thenReturn(true);
        when(achievementGroupsPort.getAllBySubjectKnowledgeId(1))
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> adapter.delete(1));
        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void getAllKnowledgesBySubjectIdByPeriodId_ShouldReturnMatchingSubjectKnowledges() {
        when(crudRepo.findKnowledgesBySubjectId(1, 1)).thenReturn(subjectKnowledges);
        when(mapper.toDomains(subjectKnowledges)).thenReturn(subjectKnowledgeDomains);

        List<SubjectKnowledgeDomain> result = adapter.getAllKnowledgesBySubjectIdByPeriodId(1, 1);

        assertEquals(subjectKnowledgeDomains, result);
        verify(crudRepo).findKnowledgesBySubjectId(1, 1);
        verify(mapper).toDomains(subjectKnowledges);
    }
}
