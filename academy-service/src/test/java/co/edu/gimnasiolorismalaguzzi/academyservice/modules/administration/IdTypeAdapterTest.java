package co.edu.gimnasiolorismalaguzzi.academyservice.modules.administration;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.IdTypeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.IdType;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.IdTypeMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository.IdTypeCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.IdTypeAdapter;
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
class IdTypeAdapterTest {

    @Mock
    private IdTypeCrudRepo crudRepo;
    @Mock
    private IdTypeMapper mapper;
    @InjectMocks
    private IdTypeAdapter adapter;

    @BeforeEach
    void setUp() {
        // Inject the mapper since it's @Autowired in adapter
        ReflectionTestUtils.setField(adapter, "mapper", mapper);
    }

    // --- findAll ---

    @Test
    void findAll_returnsMappedDomains() {
        List<IdType> entities = List.of(new IdType());
        List<IdTypeDomain> domains = List.of(IdTypeDomain.builder().build());
        when(crudRepo.findAll()).thenReturn(entities);
        when(mapper.toDomains(entities)).thenReturn(domains);

        List<IdTypeDomain> result = adapter.findAll();

        assertEquals(domains, result);
        verify(crudRepo).findAll();
        verify(mapper).toDomains(entities);
    }

    @Test
    void findAll_emptyList() {
        when(crudRepo.findAll()).thenReturn(Collections.emptyList());
        when(mapper.toDomains(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<IdTypeDomain> result = adapter.findAll();

        assertTrue(result.isEmpty());
        verify(crudRepo).findAll();
        verify(mapper).toDomains(Collections.emptyList());
    }

    // --- findById ---

    @Test
    void findById_present() {
        IdType entity = new IdType();
        IdTypeDomain domain = IdTypeDomain.builder().build();
        when(crudRepo.findById(1)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        IdTypeDomain result = adapter.findById(1);

        assertEquals(domain, result);
        verify(crudRepo).findById(1);
        verify(mapper).toDomain(entity);
    }

    @Test
    void findById_absent() {
        when(crudRepo.findById(2)).thenReturn(Optional.empty());

        IdTypeDomain result = adapter.findById(2);

        assertNull(result);
        verify(crudRepo).findById(2);
        verify(mapper, never()).toDomain(any());
    }

    // --- save ---

    @Test
    void save_persistsEntity() {
        IdTypeDomain input = IdTypeDomain.builder().name("Passport").build();
        IdType toSave = new IdType();
        IdType saved = new IdType();
        IdTypeDomain out = IdTypeDomain.builder().build();

        when(mapper.toEntity(input)).thenReturn(toSave);
        when(crudRepo.save(toSave)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(out);

        IdTypeDomain result = adapter.save(input);

        assertEquals(out, result);
        verify(mapper).toEntity(input);
        verify(crudRepo).save(toSave);
        verify(mapper).toDomain(saved);
    }

    // --- update ---

    @Test
    void update_present() {
        IdTypeDomain dto = IdTypeDomain.builder().name("ID Card").build();
        IdType existing = new IdType();
        existing.setName("Old");
        when(crudRepo.findById(5)).thenReturn(Optional.of(existing));
        when(crudRepo.save(existing)).thenReturn(existing);
        when(mapper.toDomain(existing)).thenReturn(dto);

        IdTypeDomain result = adapter.update(5, dto);

        assertEquals(dto, result);
        assertEquals("ID Card", existing.getName());
        verify(crudRepo).findById(5);
        verify(crudRepo).save(existing);
        verify(mapper).toDomain(existing);
    }

    @Test
    void update_absent_throwsNoSuchElement() {
        when(crudRepo.findById(99)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
                adapter.update(99, IdTypeDomain.builder().build())
        );
        verify(crudRepo).findById(99);
    }

    // --- delete ---

    @Test
    void delete_alwaysConflict() {
        HttpStatus status = adapter.delete(10);
        assertEquals(HttpStatus.CONFLICT, status);
        // delete should not interact with crudRepo
        verifyNoInteractions(crudRepo);
    }
}
