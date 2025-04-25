package co.edu.gimnasiolorismalaguzzi.academyservice.modules.knowledge;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectKnowledgeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceSubjectKnowledgePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.domain.KnowledgeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.entity.Knowledge;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.mapper.KnowledgeMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.repository.KnowledgeCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.service.KnowledgeAdapter;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KnowledgeAdapterTest {

    @Mock private KnowledgeCrudRepo knowledgeCrudRepo;
    @Mock private KnowledgeMapper knowledgeMapper;
    @Mock private PersistenceSubjectKnowledgePort subjectKnowledgePort;
    @InjectMocks private KnowledgeAdapter adapter;

    private Knowledge knowledge;
    private KnowledgeDomain knowledgeDomain;
    private List<Knowledge> knowledgeList;
    private List<KnowledgeDomain> knowledgeDomainList;

    @BeforeEach
    void setUp() {
        // inject autowired field
        ReflectionTestUtils.setField(adapter, "subjectKnowledgePort", subjectKnowledgePort);

        knowledge = Knowledge.builder()
                .id(1)
                .name("Conocimiento Test")
                .status("A")
                .percentage(20)
                .build();

        knowledgeDomain = new KnowledgeDomain();
        knowledgeDomain.setId(1);
        knowledgeDomain.setName("Conocimiento Test");
        knowledgeDomain.setStatus("A");
        knowledgeDomain.setPercentage(20);

        knowledgeList = List.of(knowledge);
        knowledgeDomainList = List.of(knowledgeDomain);
    }

    @Test
    void findAll_ShouldReturnAllKnowledge() {
        when(knowledgeCrudRepo.findAll()).thenReturn(knowledgeList);
        when(knowledgeMapper.toDomains(knowledgeList)).thenReturn(knowledgeDomainList);

        List<KnowledgeDomain> result = adapter.findAll();

        assertEquals(knowledgeDomainList, result);
        verify(knowledgeCrudRepo).findAll();
        verify(knowledgeMapper).toDomains(knowledgeList);
    }

    @Test
    void findAll_emptyList() {
        when(knowledgeCrudRepo.findAll()).thenReturn(Collections.emptyList());
        when(knowledgeMapper.toDomains(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<KnowledgeDomain> result = adapter.findAll();

        assertTrue(result.isEmpty());
        verify(knowledgeCrudRepo).findAll();
        verify(knowledgeMapper).toDomains(Collections.emptyList());
    }

    @Test
    void findById_WhenKnowledgeExists_ShouldReturnKnowledge() {
        when(knowledgeCrudRepo.findById(1)).thenReturn(Optional.of(knowledge));
        when(knowledgeMapper.toDomain(knowledge)).thenReturn(knowledgeDomain);

        KnowledgeDomain result = adapter.findById(1);

        assertEquals(knowledgeDomain, result);
        verify(knowledgeCrudRepo).findById(1);
        verify(knowledgeMapper).toDomain(knowledge);
    }

    @Test
    void findById_WhenKnowledgeDoesNotExist_ShouldReturnNull() {
        when(knowledgeCrudRepo.findById(2)).thenReturn(Optional.empty());

        KnowledgeDomain result = adapter.findById(2);

        assertNull(result);
        verify(knowledgeCrudRepo).findById(2);
        verify(knowledgeMapper, never()).toDomain(any());
    }

    @Test
    void save_ShouldSaveKnowledge() {
        KnowledgeDomain input = new KnowledgeDomain();
        input.setName("Nuevo"); input.setStatus("A"); input.setPercentage(10);
        Knowledge toSave = new Knowledge();
        Knowledge saved = Knowledge.builder().id(2).name("Nuevo").status("A").percentage(10).build();
        KnowledgeDomain out = new KnowledgeDomain();
        out.setId(2);

        when(knowledgeMapper.toEntity(input)).thenReturn(toSave);
        when(knowledgeCrudRepo.save(toSave)).thenReturn(saved);
        when(knowledgeMapper.toDomain(saved)).thenReturn(out);

        KnowledgeDomain result = adapter.save(input);

        assertEquals(out, result);
        verify(knowledgeMapper).toEntity(input);
        verify(knowledgeCrudRepo).save(toSave);
        verify(knowledgeMapper).toDomain(saved);
    }

    @Test
    void update_WhenKnowledgeExists_ShouldUpdateAndReturnKnowledge() {
        KnowledgeDomain dto = new KnowledgeDomain(); dto.setName("Upd"); dto.setStatus("I"); dto.setPercentage(30);
        when(knowledgeCrudRepo.findById(1)).thenReturn(Optional.of(knowledge));
        Knowledge updated = Knowledge.builder().id(1).name("Upd").status("I").percentage(30).build();
        when(knowledgeCrudRepo.save(knowledge)).thenReturn(updated);
        KnowledgeDomain out = new KnowledgeDomain();
        when(knowledgeMapper.toDomain(updated)).thenReturn(out);

        KnowledgeDomain result = adapter.update(1, dto);
        assertEquals(out, result);
        verify(knowledgeCrudRepo).findById(1);
        verify(knowledgeCrudRepo).save(knowledge);
        verify(knowledgeMapper).toDomain(updated);
    }

    @Test
    void update_WhenKnowledgeDoesNotExist_ShouldThrowNoSuchElementException() {
        when(knowledgeCrudRepo.findById(3)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> adapter.update(3, knowledgeDomain));
    }

    @Test
    void updateStatusById_WhenKnowledgeExists_ShouldUpdateStatus() {
        KnowledgeDomain dto = new KnowledgeDomain(); dto.setName(knowledge.getName()); dto.setStatus("I");
        when(knowledgeCrudRepo.findById(1)).thenReturn(Optional.of(knowledge));
        Knowledge saved = Knowledge.builder().id(1).name(knowledge.getName()).status("I").percentage(20).build();
        when(knowledgeCrudRepo.save(knowledge)).thenReturn(saved);
        KnowledgeDomain out = new KnowledgeDomain();
        when(knowledgeMapper.toDomain(saved)).thenReturn(out);

        KnowledgeDomain result = adapter.updateStatusById(1, dto);
        assertEquals(out, result);
        verify(knowledgeCrudRepo).findById(1);
        verify(knowledgeCrudRepo).save(knowledge);
        verify(knowledgeMapper).toDomain(saved);
    }

    @Test
    void updateStatusById_WhenKnowledgeDoesNotExist_ShouldThrowNoSuchElementException() {
        when(knowledgeCrudRepo.findById(4)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> adapter.updateStatusById(4, knowledgeDomain));
    }

    @Test
    void delete_WhenKnowledgeDoesNotExist_ShouldThrowNullPointerException() {
        when(knowledgeCrudRepo.findById(5)).thenReturn(Optional.empty());
        assertThrows(NullPointerException.class, () -> adapter.delete(5));
    }


    @Test
    void delete_WhenNotUsed_ShouldDeleteAndReturnOk() {
        when(knowledgeCrudRepo.findById(2)).thenReturn(Optional.of(knowledge));
        when(adapter.findById(2)).thenReturn(knowledgeDomain);
        when(subjectKnowledgePort.getAllSubjectKnowledgeByKnowledgeId(2)).thenReturn(Collections.emptyList());

        HttpStatus status = adapter.delete(2);
        assertEquals(HttpStatus.OK, status);
        verify(knowledgeCrudRepo).deleteById(2);
    }
}
