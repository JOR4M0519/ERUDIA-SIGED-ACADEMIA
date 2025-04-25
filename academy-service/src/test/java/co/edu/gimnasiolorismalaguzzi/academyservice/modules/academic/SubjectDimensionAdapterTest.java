package co.edu.gimnasiolorismalaguzzi.academyservice.modules.academic;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectDimensionDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.Subject;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectDimension;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectDimensionMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectDimensionCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.SubjectDimensionAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.entity.Dimension;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubjectDimensionAdapterTest {

    @Mock
    private SubjectDimensionCrudRepo repo;

    @Mock
    private SubjectDimensionMapper mapper;

    private SubjectDimensionAdapter adapter;

    private Subject subject;
    private Dimension dimension;
    private SubjectDimension entity;
    private SubjectDimensionDomain domain;
    private List<SubjectDimension> entities;
    private List<SubjectDimensionDomain> domains;

    @BeforeEach
    void setUp() {
        adapter = new SubjectDimensionAdapter(repo);
        ReflectionTestUtils.setField(adapter, "subjectDimensionMapper", mapper);

        subject = Subject.builder().id(1).subjectName("Math").status("A").build();
        dimension = Dimension.builder().id(1).name("Algebra").build();

        entity = SubjectDimension.builder()
                .id(1)
                .subject(subject)
                .dimension(dimension)
                .build();

        domain = SubjectDimensionDomain.builder()
                .id(1)
                .subject(subject)
                .dimension(dimension)
                .build();

        entities = Collections.singletonList(entity);
        domains = Collections.singletonList(domain);
    }

    //------------- findAll -------------

    @Test
    @DisplayName("findAll retorna todas las entidades mapeadas")
    void findAll_success() {
        when(repo.findAll()).thenReturn(entities);
        when(mapper.toDomains(entities)).thenReturn(domains);

        List<SubjectDimensionDomain> result = adapter.findAll();

        assertEquals(domains, result);
        verify(repo).findAll();
        verify(mapper).toDomains(entities);
    }

    @Test
    @DisplayName("findAll propaga RuntimeException si falla el repositorio")
    void findAll_failure() {
        when(repo.findAll()).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> adapter.findAll());
        assertTrue(ex.getMessage().contains("DB error"));
    }

    //--------- getAllByDimensionId ---------

    @Test
    @DisplayName("getAllByDimensionId retorna lista cuando existen registros")
    void getAllByDimensionId_success() {
        when(repo.findByDimension_Id(1)).thenReturn(entities);
        when(mapper.toDomains(entities)).thenReturn(domains);

        List<SubjectDimensionDomain> result = adapter.getAllByDimensionId(1);

        assertEquals(domains, result);
        verify(repo).findByDimension_Id(1);
        verify(mapper).toDomains(entities);
    }

    @Test
    @DisplayName("getAllByDimensionId retorna vacío si no hay registros")
    void getAllByDimensionId_empty() {
        when(repo.findByDimension_Id(99)).thenReturn(Collections.emptyList());
        when(mapper.toDomains(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<SubjectDimensionDomain> result = adapter.getAllByDimensionId(99);

        assertTrue(result.isEmpty());
        verify(repo).findByDimension_Id(99);
    }

    //------------- findById -------------

    @Test
    @DisplayName("findById devuelve dominio cuando existe")
    void findById_success() {
        when(repo.findById(1)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        SubjectDimensionDomain result = adapter.findById(1);

        assertEquals(domain, result);
        verify(repo).findById(1);
        verify(mapper).toDomain(entity);
    }

    @Test
    @DisplayName("findById devuelve null cuando no existe")
    void findById_notFound() {
        when(repo.findById(2)).thenReturn(Optional.empty());

        SubjectDimensionDomain result = adapter.findById(2);

        assertNull(result);
        verify(repo).findById(2);
        verify(mapper, never()).toDomain(any());
    }

    //-------------- save --------------

    @Test
    @DisplayName("save guarda y mapea correctamente")
    void save_success() {
        SubjectDimensionDomain input = SubjectDimensionDomain.builder()
                .subject(subject)
                .dimension(dimension)
                .build();
        SubjectDimension toSave = SubjectDimension.builder()
                .subject(subject)
                .dimension(dimension)
                .build();
        SubjectDimension saved = SubjectDimension.builder()
                .id(1)
                .subject(subject)
                .dimension(dimension)
                .build();

        when(mapper.toEntity(input)).thenReturn(toSave);
        when(repo.save(toSave)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(domain);

        SubjectDimensionDomain result = adapter.save(input);

        assertEquals(domain, result);
        verify(mapper).toEntity(input);
        verify(repo).save(toSave);
        verify(mapper).toDomain(saved);
    }

    @Test
    @DisplayName("save propaga RuntimeException si toEntity falla")
    void save_failure() {
        SubjectDimensionDomain input = SubjectDimensionDomain.builder().build();
        when(mapper.toEntity(input)).thenThrow(new RuntimeException("Map error"));

        assertThrows(RuntimeException.class, () -> adapter.save(input));
        verify(repo, never()).save(any());
    }

    //-------------- update -------------

    @Test
    @DisplayName("update modifica y devuelve dominio actualizado")
    void update_success() {
        SubjectDimensionDomain input = SubjectDimensionDomain.builder()
                .id(1)
                .subject(subject)
                .dimension(Dimension.builder().id(2).name("Geo").build())
                .build();
        SubjectDimension existing = SubjectDimension.builder().id(1).subject(subject).dimension(dimension).build();
        SubjectDimension updated = SubjectDimension.builder().id(1).subject(subject)
                .dimension(input.getDimension()).build();

        when(repo.findById(1)).thenReturn(Optional.of(existing));
        when(repo.save(existing)).thenReturn(updated);
        when(mapper.toDomain(updated)).thenReturn(input);

        SubjectDimensionDomain result = adapter.update(1, input);

        assertEquals(input, result);
        verify(repo).findById(1);
        verify(repo).save(existing);
        verify(mapper).toDomain(updated);
    }

    @Test
    @DisplayName("update lanza EntityNotFoundException cuando no existe")
    void update_notFound() {
        SubjectDimensionDomain input = SubjectDimensionDomain.builder().id(99).build();
        when(repo.findById(99)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> adapter.update(99, input));
        verify(repo).findById(99);
        verify(repo, never()).save(any());
    }

    @Test
    @DisplayName("update lanza EntityNotFoundException si save falla")
    void update_saveFails() {
        SubjectDimensionDomain input = SubjectDimensionDomain.builder().id(1).subject(subject).dimension(dimension).build();
        SubjectDimension existing = SubjectDimension.builder().id(1).subject(subject).dimension(dimension).build();

        when(repo.findById(1)).thenReturn(Optional.of(existing));
        doThrow(new RuntimeException("DB down")).when(repo).save(existing);

        assertThrows(EntityNotFoundException.class, () -> adapter.update(1, input));
        verify(repo).findById(1);
        verify(repo).save(existing);
    }

    //-------------- delete -------------

    @Test
    @DisplayName("delete retorna OK cuando existe y elimina")
    void delete_success() {
        when(repo.findById(1)).thenReturn(Optional.of(entity));

        HttpStatus status = adapter.delete(1);

        assertEquals(HttpStatus.OK, status);
        verify(repo).deleteById(1);
    }

    @Test
    @DisplayName("delete retorna NOT_FOUND cuando no existe")
    void delete_notFound() {
        when(repo.findById(2)).thenReturn(Optional.empty());

        HttpStatus status = adapter.delete(2);

        assertEquals(HttpStatus.NOT_FOUND, status);
        verify(repo, never()).deleteById(any());
    }

    @Test
    @DisplayName("delete lanza EntityNotFoundException si deleteById falla")
    void delete_failure() {
        when(repo.findById(1)).thenReturn(Optional.of(entity));
        doThrow(new RuntimeException("DB error")).when(repo).deleteById(1);

        assertThrows(EntityNotFoundException.class, () -> adapter.delete(1));
        verify(repo).deleteById(1);
    }
}
