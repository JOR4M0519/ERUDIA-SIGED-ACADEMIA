package co.edu.gimnasiolorismalaguzzi.academyservice.modules.administration;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.FamilyDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.RelationshipDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.Relationship;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.RelationshipMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository.RelationshipCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.RelationshipAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceFamilyPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RelationshipAdapterTest {

    @Mock private RelationshipCrudRepo crudRepo;
    @Mock private RelationshipMapper mapper;
    @Mock private PersistenceFamilyPort familyPort;
    @InjectMocks private RelationshipAdapter adapter;

    // --- findAll ---

    @Test
    void findAll_returnsMappedDomains() {
        List<Relationship> entities = List.of(new Relationship());
        List<RelationshipDomain> domains = List.of(RelationshipDomain.builder().build());
        when(crudRepo.findAll()).thenReturn(entities);
        when(mapper.toDomains(entities)).thenReturn(domains);

        List<RelationshipDomain> result = adapter.findAll();

        assertEquals(domains, result);
        verify(crudRepo).findAll();
        verify(mapper).toDomains(entities);
    }

    @Test
    void findAll_emptyList() {
        when(crudRepo.findAll()).thenReturn(Collections.emptyList());
        when(mapper.toDomains(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<RelationshipDomain> result = adapter.findAll();

        assertTrue(result.isEmpty());
        verify(crudRepo).findAll();
        verify(mapper).toDomains(Collections.emptyList());
    }

    // --- findById ---

    @Test
    void findById_present() {
        Relationship entity = new Relationship();
        RelationshipDomain domain = RelationshipDomain.builder().build();
        when(crudRepo.findById(1)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        RelationshipDomain result = adapter.findById(1);

        assertEquals(domain, result);
        verify(crudRepo).findById(1);
        verify(mapper).toDomain(entity);
    }

    @Test
    void findById_absent() {
        when(crudRepo.findById(2)).thenReturn(Optional.empty());

        RelationshipDomain result = adapter.findById(2);

        assertNull(result);
        verify(crudRepo).findById(2);
        verify(mapper, never()).toDomain(any());
    }

    // --- save ---

    @Test
    void save_persistsEntity() {
        RelationshipDomain input = RelationshipDomain.builder().relationshipType("Friend").build();
        Relationship toSave = new Relationship();
        Relationship saved = new Relationship();
        RelationshipDomain out = RelationshipDomain.builder().build();

        when(mapper.toEntity(input)).thenReturn(toSave);
        when(crudRepo.save(toSave)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(out);

        RelationshipDomain result = adapter.save(input);

        assertEquals(out, result);
        verify(mapper).toEntity(input);
        verify(crudRepo).save(toSave);
        verify(mapper).toDomain(saved);
    }

    // --- update ---

    @Test
    void update_present() {
        RelationshipDomain dto = RelationshipDomain.builder().relationshipType("Sibling").build();
        Relationship existing = new Relationship();
        existing.setRelationshipType("Cousin");
        when(crudRepo.findById(5)).thenReturn(Optional.of(existing));
        when(crudRepo.save(existing)).thenReturn(existing);
        when(mapper.toDomain(existing)).thenReturn(dto);

        RelationshipDomain result = adapter.update(5, dto);

        assertEquals(dto, result);
        assertEquals("Sibling", existing.getRelationshipType());
        verify(crudRepo).findById(5);
        verify(crudRepo).save(existing);
        verify(mapper).toDomain(existing);
    }

    @Test
    void update_absent_throwsNoSuchElement() {
        when(crudRepo.findById(10)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
                adapter.update(10, RelationshipDomain.builder().build())
        );
        verify(crudRepo).findById(10);
    }

    // --- delete ---

    @Test
    void delete_noUsage_returnsOk() {
        // stub findById -> non-null domain
        Relationship entity = new Relationship();
        RelationshipDomain domain = RelationshipDomain.builder().build();
        when(crudRepo.findById(7)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);
        // no family usage
        when(familyPort.findAllByRelationType(7)).thenReturn(Collections.emptyList());

        HttpStatus status = adapter.delete(7);

        assertEquals(HttpStatus.OK, status);
        verify(crudRepo).findById(7);
        verify(mapper).toDomain(entity);
        verify(familyPort).findAllByRelationType(7);
        verify(crudRepo).deleteById(7);
    }

    @Test
    void delete_withUsage_throwsConflict() {
        Relationship entity = new Relationship();
        RelationshipDomain domain = RelationshipDomain.builder().build();
        when(crudRepo.findById(8)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);
        // simulate usage
        when(familyPort.findAllByRelationType(8)).thenReturn(List.of(FamilyDomain.builder().build()));

        AppException ex = assertThrows(AppException.class, () -> adapter.delete(8));
        assertEquals(HttpStatus.CONFLICT, ex.getCode());
        assertTrue(ex.getMessage().contains("No es posible eliminar el saber"));
        verify(crudRepo).findById(8);
        verify(mapper).toDomain(entity);
        verify(familyPort).findAllByRelationType(8);
        verify(crudRepo, never()).deleteById(anyInt());
    }
}
