package co.edu.gimnasiolorismalaguzzi.academyservice.modules.administration;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.InstitutionDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.Institution;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.InstitutionMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository.InstitutionCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.InstitutionAdapter;
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
class InstitutionAdapterTest {

    @Mock
    private InstitutionCrudRepo crudRepo;
    @Mock
    private InstitutionMapper mapper;
    @InjectMocks
    private InstitutionAdapter adapter;

    @BeforeEach
    void setUp() {
        // inject the mapper since it's @Autowired
        ReflectionTestUtils.setField(adapter, "institutionMapper", mapper);
    }

    // --- findAll ---
    @Test
    void findAll_returnsMappedDomains() {
        List<Institution> entities = List.of(new Institution());
        List<InstitutionDomain> domains = List.of(InstitutionDomain.builder().build());
        when(crudRepo.findAll()).thenReturn(entities);
        when(mapper.toDomains(entities)).thenReturn(domains);

        List<InstitutionDomain> result = adapter.findAll();

        assertEquals(domains, result);
        verify(crudRepo).findAll();
        verify(mapper).toDomains(entities);
    }

    @Test
    void findAll_emptyList() {
        when(crudRepo.findAll()).thenReturn(Collections.emptyList());
        when(mapper.toDomains(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<InstitutionDomain> result = adapter.findAll();

        assertTrue(result.isEmpty());
        verify(crudRepo).findAll();
        verify(mapper).toDomains(Collections.emptyList());
    }

    // --- findById ---
    @Test
    void findById_present() {
        Institution entity = new Institution();
        InstitutionDomain domain = InstitutionDomain.builder().build();
        when(crudRepo.findById(1)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        InstitutionDomain result = adapter.findById(1);

        assertEquals(domain, result);
        verify(crudRepo).findById(1);
        verify(mapper).toDomain(entity);
    }

    @Test
    void findById_absent() {
        when(crudRepo.findById(2)).thenReturn(Optional.empty());

        InstitutionDomain result = adapter.findById(2);

        assertNull(result);
        verify(crudRepo).findById(2);
        verify(mapper, never()).toDomain(any());
    }

    // --- findByNit ---
    @Test
    void findByNit_present() {
        Institution entity = new Institution();
        InstitutionDomain domain = InstitutionDomain.builder().build();
        when(crudRepo.findByNit("ABC123")).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(domain);

        InstitutionDomain result = adapter.findByNit("ABC123");

        assertEquals(domain, result);
        verify(crudRepo).findByNit("ABC123");
        verify(mapper).toDomain(entity);
    }

    @Test
    void findByNit_absent() {
        when(crudRepo.findByNit("NONE")).thenReturn(null);

        InstitutionDomain result = adapter.findByNit("NONE");

        assertNull(result);
        verify(crudRepo).findByNit("NONE");
        verify(mapper, never()).toDomain(any());
    }

    // --- save ---
    @Test
    void save_persistsEntity() {
        InstitutionDomain input = InstitutionDomain.builder().name("Inst").build();
        Institution toSave = new Institution();
        Institution saved = new Institution();
        InstitutionDomain out = InstitutionDomain.builder().build();

        when(mapper.toEntity(input)).thenReturn(toSave);
        when(crudRepo.save(toSave)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(out);

        InstitutionDomain result = adapter.save(input);

        assertEquals(out, result);
        verify(mapper).toEntity(input);
        verify(crudRepo).save(toSave);
        verify(mapper).toDomain(saved);
    }

    // --- update ---
    @Test
    void update_present() {
        InstitutionDomain dto = InstitutionDomain.builder().name("NewName").build();
        Institution existing = new Institution();
        existing.setName("OldName");
        when(crudRepo.findById(5)).thenReturn(Optional.of(existing));
        when(crudRepo.save(existing)).thenReturn(existing);
        when(mapper.toDomain(existing)).thenReturn(dto);

        InstitutionDomain result = adapter.update(5, dto);

        assertEquals(dto, result);
        assertEquals("NewName", existing.getName());
        verify(crudRepo).findById(5);
        verify(crudRepo).save(existing);
        verify(mapper).toDomain(existing);
    }

    @Test
    void update_absent_throwsNoSuchElement() {
        when(crudRepo.findById(99)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
                adapter.update(99, InstitutionDomain.builder().build())
        );
        verify(crudRepo).findById(99);
    }

    // --- delete ---
    @Test
    void delete_returnsNull() {
        // delete is not implemented and returns null
        assertNull(adapter.delete(10));
        verifyNoInteractions(crudRepo);
    }
}