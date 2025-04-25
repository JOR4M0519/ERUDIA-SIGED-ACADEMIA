package co.edu.gimnasiolorismalaguzzi.academyservice.modules.administration;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.RoleDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.Role;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.RoleMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository.RoleCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.RoleAdapter;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleAdapterTest {

    @Mock private RoleCrudRepo crudRepo;
    @Mock private RoleMapper mapper;
    @InjectMocks private RoleAdapter adapter;

    @BeforeEach
    void setUp() {
        // inject the mapper field
        ReflectionTestUtils.setField(adapter, "roleMapper", mapper);
    }

    // --- findAll ---

    @Test
    void findAll_returnsMapped() {
        List<Role> entities = List.of(new Role());
        List<RoleDomain> domains = List.of(RoleDomain.builder().build());
        when(crudRepo.findAll()).thenReturn(entities);
        when(mapper.toDomains(entities)).thenReturn(domains);

        List<RoleDomain> result = adapter.findAll();

        assertEquals(domains, result);
        verify(crudRepo).findAll();
        verify(mapper).toDomains(entities);
    }

    @Test
    void findAll_emptyList() {
        when(crudRepo.findAll()).thenReturn(Collections.emptyList());
        when(mapper.toDomains(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<RoleDomain> result = adapter.findAll();

        assertTrue(result.isEmpty());
        verify(crudRepo).findAll();
        verify(mapper).toDomains(Collections.emptyList());
    }

    // --- findById ---

    @Test
    void findById_present() {
        Role entity = new Role();
        RoleDomain domain = RoleDomain.builder().build();
        when(crudRepo.findById(1)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        RoleDomain result = adapter.findById(1);

        assertEquals(domain, result);
        verify(crudRepo).findById(1);
        verify(mapper).toDomain(entity);
    }

    @Test
    void findById_absent() {
        when(crudRepo.findById(2)).thenReturn(Optional.empty());

        RoleDomain result = adapter.findById(2);

        assertNull(result);
        verify(crudRepo).findById(2);
        verify(mapper, never()).toDomain(any());
    }

    // --- save ---

    @Test
    void save_persistsEntity() {
        RoleDomain input = RoleDomain.builder().roleName("Admin").build();
        Role toSave = new Role();
        Role saved = new Role();
        RoleDomain out = RoleDomain.builder().build();

        when(mapper.toEntity(input)).thenReturn(toSave);
        when(crudRepo.save(toSave)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(out);

        RoleDomain result = adapter.save(input);

        assertEquals(out, result);
        verify(mapper).toEntity(input);
        verify(crudRepo).save(toSave);
        verify(mapper).toDomain(saved);
    }

    // --- update ---

    @Test
    void update_present() {
        RoleDomain dto = RoleDomain.builder().roleName("User").status(true).build();
        Role existing = new Role();
        existing.setRoleName("Old");
        existing.setStatus(false);
        when(crudRepo.findById(5)).thenReturn(Optional.of(existing));
        when(crudRepo.save(existing)).thenReturn(existing);
        when(mapper.toDomain(existing)).thenReturn(dto);

        RoleDomain result = adapter.update(5, dto);

        assertEquals(dto, result);
        assertEquals("User", existing.getRoleName());
        assertEquals(true, existing.getStatus());
        verify(crudRepo).findById(5);
        verify(crudRepo).save(existing);
        verify(mapper).toDomain(existing);
    }

    @Test
    void update_absent_throwsEntityNotFound() {
        when(crudRepo.findById(10)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> adapter.update(10, RoleDomain.builder().build())
        );
        assertTrue(ex.getMessage().contains("Role with id: 10 not found!"));
        verify(crudRepo).findById(10);
    }

    // --- delete ---

    @Test
    void delete_success_returnsOk() {
        // no exception from deleteById
        HttpStatus status = adapter.delete(1);
        assertEquals(HttpStatus.OK, status);
        verify(crudRepo).deleteById(1);
    }

    @Test
    void delete_failure_returnsConflict() {
        doThrow(new RuntimeException("error")).when(crudRepo).deleteById(2);

        HttpStatus status = adapter.delete(2);

        assertEquals(HttpStatus.CONFLICT, status);
        verify(crudRepo).deleteById(2);
    }
}
