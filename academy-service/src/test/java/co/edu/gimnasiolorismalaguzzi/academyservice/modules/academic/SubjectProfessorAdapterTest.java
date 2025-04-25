package co.edu.gimnasiolorismalaguzzi.academyservice.modules.academic;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectProfessorDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.Subject;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectProfessor;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectProfessorMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectProfessorCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.SubjectProfessorAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceSubjectGroupPort;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubjectProfessorAdapterTest {

    @Mock
    private SubjectProfessorCrudRepo repo;

    @Mock
    private SubjectProfessorMapper mapper;

    @Mock
    private PersistenceSubjectGroupPort groupPort;

    private SubjectProfessorAdapter adapter;

    private Subject subject;
    private User professorEntity;
    private UserDomain professorDomain;
    private SubjectProfessor entity;
    private SubjectProfessorDomain domain;
    private List<SubjectProfessor> entities;
    private List<SubjectProfessorDomain> domains;

    @BeforeEach
    void setUp() {
        // Inicializar adapter e inyectar el mock de groupPort y mapper
        adapter = new SubjectProfessorAdapter(repo, mapper);
        ReflectionTestUtils.setField(adapter, "subjectGroupPortAdapter", groupPort);
        ReflectionTestUtils.setField(adapter, "professorMapper", mapper);

        // Datos de prueba
        subject = Subject.builder().id(10).subjectName("Math").status("A").build();
        professorEntity = User.builder().id(20).firstName("Jane").lastName("Doe").build();
        professorDomain = UserDomain.builder().id(20).firstName("Jane").lastName("Doe").build();

        entity = SubjectProfessor.builder()
                .id(1)
                .subject(subject)
                .professor(professorEntity)
                .build();

        domain = SubjectProfessorDomain.builder()
                .id(1)
                .subject(subject)
                .professor(professorDomain)
                .build();

        entities = Arrays.asList(entity);
        domains = Arrays.asList(domain);
    }

    // ===== findAll =====

    @Test
    void findAll_success() {
        when(repo.findAll()).thenReturn(entities);
        when(mapper.toDomains(entities)).thenReturn(domains);

        List<SubjectProfessorDomain> result = adapter.findAll();
        assertEquals(domains, result);

        verify(repo).findAll();
        verify(mapper).toDomains(entities);
    }

    @Test
    void findAll_failure() {
        when(repo.findAll()).thenThrow(new RuntimeException("DB error"));
        assertThrows(RuntimeException.class, () -> adapter.findAll());
    }

    // ===== findBySubjectId =====

    @Test
    void findBySubjectId_success() {
        when(repo.findBySubjectId(10)).thenReturn(entities);
        when(mapper.toDomains(entities)).thenReturn(domains);

        List<SubjectProfessorDomain> result = adapter.findBySubjectId(10);
        assertEquals(domains, result);

        verify(repo).findBySubjectId(10);
        verify(mapper).toDomains(entities);
    }

    @Test
    void findBySubjectId_empty() {
        when(repo.findBySubjectId(99)).thenReturn(Collections.emptyList());
        when(mapper.toDomains(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<SubjectProfessorDomain> result = adapter.findBySubjectId(99);
        assertTrue(result.isEmpty());
    }

    @Test
    void findBySubjectId_failure() {
        when(repo.findBySubjectId(10)).thenThrow(new RuntimeException("DB error"));
        assertThrows(RuntimeException.class, () -> adapter.findBySubjectId(10));
    }

    // ===== findById =====

    @Test
    void findById_exists() {
        when(repo.findById(1)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        SubjectProfessorDomain result = adapter.findById(1);
        assertEquals(domain, result);

        verify(repo).findById(1);
        verify(mapper).toDomain(entity);
    }

    @Test
    void findById_notExists() {
        when(repo.findById(2)).thenReturn(Optional.empty());
        SubjectProfessorDomain result = adapter.findById(2);
        assertNull(result);
        verify(repo).findById(2);
        verify(mapper, never()).toDomain(any());
    }

    @Test
    void findById_failure() {
        when(repo.findById(1)).thenThrow(new RuntimeException("DB error"));
        assertThrows(RuntimeException.class, () -> adapter.findById(1));
    }

    // ===== save =====

    @Test
    void save_success() {
        when(mapper.toEntity(domain)).thenReturn(entity);
        when(repo.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(domain);

        SubjectProfessorDomain result = adapter.save(domain);
        assertEquals(domain, result);

        verify(mapper).toEntity(domain);
        verify(repo).save(entity);
        verify(mapper).toDomain(entity);
    }

    @Test
    void save_failure() {
        when(mapper.toEntity(domain)).thenReturn(entity);
        when(repo.save(entity)).thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> adapter.save(domain));
    }

    // ===== update =====

    @Test
    void update_success() {
        when(repo.findById(1)).thenReturn(Optional.of(entity));
        when(repo.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(domain);

        SubjectProfessorDomain result = adapter.update(1, domain);
        assertEquals(domain, result);

        verify(repo).findById(1);
        verify(repo).save(entity);
        verify(mapper).toDomain(entity);
    }

    @Test
    void update_notExists() {
        when(repo.findById(999)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> adapter.update(999, domain));
        verify(repo).findById(999);
        verify(repo, never()).save(any());
    }

    @Test
    void update_saveFails() {
        when(repo.findById(1)).thenReturn(Optional.of(entity));
        doThrow(new RuntimeException("DB error")).when(repo).save(entity);

        assertThrows(RuntimeException.class, () -> adapter.update(1, domain));
        verify(repo).findById(1);
        verify(repo).save(entity);
    }

    // ===== delete =====

    @Test
    void delete_success() {
        // findById → non-null domain
        when(repo.findById(1)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);
        // no assignments → OK
        when(groupPort.findAllBySubjectProfessor(1)).thenReturn(Collections.emptyList());

        HttpStatus result = adapter.delete(1);
        assertEquals(HttpStatus.OK, result);
        verify(repo).deleteById(1);
    }

    @Test
    void delete_conflictWhenInUse() {
        when(repo.findById(1)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);
        // sí hay asignaciones → conflicto
        when(groupPort.findAllBySubjectProfessor(1))
                .thenReturn(new ArrayList(Arrays.asList(SubjectGroupDomain.builder().build())));

        AppException ex = assertThrows(AppException.class, () -> adapter.delete(1));
        assertEquals(HttpStatus.CONFLICT, ex.getCode());
        assertTrue(ex.getMessage().contains("No es posible eliminar"));
    }

    @Test
    void delete_groupPortThrows() {
        when(repo.findById(1)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);
        when(groupPort.findAllBySubjectProfessor(1))
                .thenThrow(new RuntimeException("Port error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> adapter.delete(1));
        assertEquals("Port error", ex.getMessage());
    }



    @Test
    void delete_deleteByIdFails() {
        when(repo.findById(1)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);
        when(groupPort.findAllBySubjectProfessor(1)).thenReturn(Collections.emptyList());
        doThrow(new RuntimeException("Delete error")).when(repo).deleteById(1);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> adapter.delete(1));
        assertEquals("Delete error", ex.getMessage());
    }
}
