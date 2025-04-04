package co.edu.gimnasiolorismalaguzzi.academyservice.modules.knowledge;

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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KnowledgeAdapterTest {

    @Mock
    private KnowledgeCrudRepo knowledgeCrudRepo;

    @Mock
    private KnowledgeMapper knowledgeMapper;

    private KnowledgeAdapter knowledgeAdapter;

    private Knowledge knowledge;
    private KnowledgeDomain knowledgeDomain;
    private List<Knowledge> knowledgeList;
    private List<KnowledgeDomain> knowledgeDomainList;

    @BeforeEach
    void setUp() {
        knowledgeAdapter = new KnowledgeAdapter(knowledgeCrudRepo, knowledgeMapper);

        // Inicializar entidades para pruebas
        knowledge = Knowledge.builder()
                .id(1)
                .name("Conocimiento Test")
                .percentage(20)
                .build();

        // Creamos un KnowledgeDomain simple ya que no tenemos todos los detalles
        knowledgeDomain = new KnowledgeDomain();
        // Asumimos que tiene setters o builder basado en la convención

        knowledgeList = Arrays.asList(knowledge);
        knowledgeDomainList = Arrays.asList(knowledgeDomain);
    }

    @Test
    void findAll_ShouldReturnAllKnowledge() {
        // Arrange
        when(knowledgeCrudRepo.findAll()).thenReturn(knowledgeList);
        when(knowledgeMapper.toDomains(knowledgeList)).thenReturn(knowledgeDomainList);

        // Act
        List<KnowledgeDomain> result = knowledgeAdapter.findAll();

        // Assert
        assertEquals(knowledgeDomainList, result);
        verify(knowledgeCrudRepo).findAll();
        verify(knowledgeMapper).toDomains(knowledgeList);
    }

    @Test
    void findById_WhenKnowledgeExists_ShouldReturnKnowledge() {
        // Arrange
        Integer id = 1;
        when(knowledgeCrudRepo.findById(id)).thenReturn(Optional.of(knowledge));
        when(knowledgeMapper.toDomain(knowledge)).thenReturn(knowledgeDomain);

        // Act
        KnowledgeDomain result = knowledgeAdapter.findById(id);

        // Assert
        assertEquals(knowledgeDomain, result);
        verify(knowledgeCrudRepo).findById(id);
        verify(knowledgeMapper).toDomain(knowledge);
    }

    @Test
    void findById_WhenKnowledgeDoesNotExist_ShouldReturnNull() {
        // Arrange
        Integer id = 999;
        when(knowledgeCrudRepo.findById(id)).thenReturn(Optional.empty());

        // Act
        KnowledgeDomain result = knowledgeAdapter.findById(id);

        // Assert
        assertNull(result);
        verify(knowledgeCrudRepo).findById(id);
        verify(knowledgeMapper, never()).toDomain(any());
    }

    @Test
    void save_ShouldSaveKnowledge() {
        // Arrange
        KnowledgeDomain domainToSave = new KnowledgeDomain();
        Knowledge entityToSave = new Knowledge();
        Knowledge savedEntity = Knowledge.builder()
                .id(1)
                .name("Nuevo Conocimiento")
                .percentage(20)
                .build();
        KnowledgeDomain savedDomain = new KnowledgeDomain();

        when(knowledgeMapper.toEntity(domainToSave)).thenReturn(entityToSave);
        when(knowledgeCrudRepo.save(entityToSave)).thenReturn(savedEntity);
        when(knowledgeMapper.toDomain(savedEntity)).thenReturn(savedDomain);

        // Act
        KnowledgeDomain result = knowledgeAdapter.save(domainToSave);

        // Assert
        assertEquals(savedDomain, result);
        verify(knowledgeMapper).toEntity(domainToSave);
        verify(knowledgeCrudRepo).save(entityToSave);
        verify(knowledgeMapper).toDomain(savedEntity);
    }

    @Test
    void update_WhenKnowledgeExists_ShouldUpdateAndReturnKnowledge() {
        // Arrange
        Integer id = 1;
        KnowledgeDomain domainToUpdate = new KnowledgeDomain();

        Knowledge existingEntity = Knowledge.builder()
                .id(1)
                .name("Conocimiento Original")
                .percentage(20)
                .build();

        Knowledge updatedEntity = Knowledge.builder()
                .id(1)
                .name("Conocimiento Actualizado")
                .percentage(20)
                .build();

        KnowledgeDomain updatedDomain = new KnowledgeDomain();

        when(knowledgeCrudRepo.findById(id)).thenReturn(Optional.of(existingEntity));
        when(knowledgeCrudRepo.save(any(Knowledge.class))).thenReturn(updatedEntity);
        when(knowledgeMapper.toDomain(updatedEntity)).thenReturn(updatedDomain);

        // Act
        KnowledgeDomain result = knowledgeAdapter.update(id, domainToUpdate);

        // Assert
        assertEquals(updatedDomain, result);
        verify(knowledgeCrudRepo).findById(id);
        verify(knowledgeCrudRepo).save(any(Knowledge.class));
        verify(knowledgeMapper).toDomain(updatedEntity);
    }

}
