package co.edu.gimnasiolorismalaguzzi.academyservice.modules.administration;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.*;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.Family;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.FamilyMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository.FamilyCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.FamilyAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceUserDetailPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;

import java.sql.Array;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FamilyAdapterTest {

    @Mock FamilyCrudRepo familyCrudRepo;
    @Mock FamilyMapper familyMapper;
    @Mock PersistenceUserDetailPort userDetailPort;

    @InjectMocks FamilyAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // findAll
    @Test
    void findAll_returnsMappedDomains() {
        List<Family> entities = List.of(new Family());
        List<FamilyDomain> domains = List.of(new FamilyDomain());
        when(familyCrudRepo.findAll()).thenReturn(entities);
        when(familyMapper.toDomains(entities)).thenReturn(domains);

        List<FamilyDomain> result = adapter.findAll();

        assertEquals(domains, result);
        verify(familyCrudRepo).findAll();
        verify(familyMapper).toDomains(entities);
    }

    // findAllWithRelatives – éxito
    @Test
    void findAllWithRelatives_success() {
        // Simulamos dos usuarios
        UserRoleDomain role = UserRoleDomain.builder().role(RoleDomain.builder().roleName("estudiante").build()).build();
        UserDomain u1 = UserDomain.builder().id(1).roles(Set.of(role)
        ).build();
        UserDetailDomain ud1 = UserDetailDomain.builder().user(u1).build();
        when(userDetailPort.findAll()).thenReturn(List.of(ud1));

        // findRelativesByStudent devuelve lista vacía
        when(familyCrudRepo.findByStudent_Id(1)).thenReturn(Collections.emptyList());
        when(familyCrudRepo.findByUser_Id(1)).thenReturn(Collections.emptyList());

        var result = adapter.findAllWithRelatives();

        assertEquals(1, result.size());
        assertTrue(result.get(0).isStudent());
    }

    // findAllWithRelatives – fallo interno
    @Test
    void findAllWithRelatives_throwsOnError() {
        when(userDetailPort.findAll()).thenThrow(new RuntimeException("DB fail"));

        AppException ex = assertThrows(AppException.class, () -> adapter.findAllWithRelatives());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getCode());
        assertTrue(ex.getMessage().contains("Error al obtener usuarios con relaciones familiares"));
    }

    // findAllByRelationType
    @Test
    void findAllByRelationType_mapsCorrectly() {
        List<Family> fams = List.of(new Family());
        List<FamilyDomain> doms = List.of(new FamilyDomain());
        when(familyCrudRepo.findByRelationship_Id(42)).thenReturn(fams);
        when(familyMapper.toDomains(fams)).thenReturn(doms);

        assertEquals(doms, adapter.findAllByRelationType(42));
    }

    // findById – existe
    @Test
    void findById_present() {
        Family e = new Family(); FamilyDomain d = new FamilyDomain();
        when(familyCrudRepo.findById(7)).thenReturn(Optional.of(e));
        when(familyMapper.toDomain(e)).thenReturn(d);

        assertEquals(d, adapter.findById(7));
    }

    // findById – no existe
    @Test
    void findById_absent() {
        when(familyCrudRepo.findById(7)).thenReturn(Optional.empty());
        assertNull(adapter.findById(7));
    }

    // save simple
    @Test
    void save_persistsEntity() {
        FamilyDomain dom = new FamilyDomain();
        Family ent = new Family();
        Family saved = new Family();
        FamilyDomain out = new FamilyDomain();
        when(familyMapper.toEntity(dom)).thenReturn(ent);
        when(familyCrudRepo.save(ent)).thenReturn(saved);
        when(familyMapper.toDomain(saved)).thenReturn(out);

        assertEquals(out, adapter.save(dom));
    }

    // saveFamilyRelations – null
    @Test
    void saveFamilyRelations_null_throwsBadRequest() {
        AppException ex = assertThrows(AppException.class, () -> adapter.saveFamilyRelations(null));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getCode());
    }

    // saveFamilyRelations – lista vacía
    @Test
    void saveFamilyRelations_empty_throwsBadRequest() {
        UserFamilyRelationDomain req = UserFamilyRelationDomain.builder().familyRelations(Collections.emptyList()).build();
        AppException ex = assertThrows(AppException.class, () -> adapter.saveFamilyRelations(req));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getCode());
    }

    // saveFamilyRelations – missing IDs
    @Test
    void saveFamilyRelations_missingIds_throwsBadRequest() {
        FamilyDomain f = new FamilyDomain(); // sin user ni relative ni relationship
        UserFamilyRelationDomain req = UserFamilyRelationDomain.builder().familyRelations(List.of(f)).build();
        AppException ex = assertThrows(AppException.class, () -> adapter.saveFamilyRelations(req));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getCode());
    }

    // saveFamilyRelations – usuario no estudiante
    @Test
    void saveFamilyRelations_userNotStudent_throwsBadRequest() {
        UserDomain student = UserDomain.builder().id(1).build();
        UserDomain relative = UserDomain.builder().id(2).build();
        FamilyDomain f = FamilyDomain.builder()
                .user(student).relativeUser(relative)
                .relationship(RelationshipDomain.builder().id(5).build())
                .build();
        UserFamilyRelationDomain req = UserFamilyRelationDomain.builder().familyRelations(List.of(f)).build();
        when(userDetailPort.hasStudentRole(1)).thenReturn(false);

        AppException ex = assertThrows(AppException.class, () -> adapter.saveFamilyRelations(req));
        assertTrue(ex.getMessage().contains("no tiene rol de estudiante"));
    }

    // saveFamilyRelations – relación existente
    @Test
    void saveFamilyRelations_exists_throwsConflict() {
        UserDomain student = UserDomain.builder().id(1).build();
        UserDomain relative = UserDomain.builder().id(2).build();
        FamilyDomain f = FamilyDomain.builder()
                .user(student).relativeUser(relative)
                .relationship(RelationshipDomain.builder().id(5).build())
                .build();
        UserFamilyRelationDomain req = UserFamilyRelationDomain.builder().familyRelations(List.of(f)).build();
        when(userDetailPort.hasStudentRole(1)).thenReturn(true);
        when(adapter.existsRelation(1,2)).thenReturn(true);

        AppException ex = assertThrows(AppException.class, () -> adapter.saveFamilyRelations(req));
        assertEquals(HttpStatus.CONFLICT, ex.getCode());
    }

    // saveFamilyRelations – éxito
    @Test
    void saveFamilyRelations_success() {
        UserDomain student = UserDomain.builder().id(1).build();
        UserDomain relative = UserDomain.builder().id(2).build();
        FamilyDomain f = FamilyDomain.builder()
                .user(student).relativeUser(relative)
                .relationship(RelationshipDomain.builder().id(5).build())
                .build();
        FamilyDomain saved = new FamilyDomain();
        UserFamilyRelationDomain req = UserFamilyRelationDomain.builder().familyRelations(List.of(f)).build();
        when(userDetailPort.hasStudentRole(1)).thenReturn(true);
        when(adapter.existsRelation(1,2)).thenReturn(false);
        when(adapter.save(f)).thenReturn(saved);

        List<FamilyDomain> out = adapter.saveFamilyRelations(req);
        assertEquals(1, out.size());
        assertSame(saved, out.get(0));
    }

    // existsRelation
    @Test
    void existsRelation_delegatesToRepo() {
        when(familyCrudRepo.existsByStudent_IdAndUser_Id(3,4)).thenReturn(true);
        assertTrue(adapter.existsRelation(3,4));
    }

    // update – presente
    @Test
    void update_present() {
        Family ent = new Family(); FamilyDomain dom = new FamilyDomain();
        when(familyCrudRepo.findById(9)).thenReturn(Optional.of(ent));
        when(familyCrudRepo.save(ent)).thenReturn(ent);
        when(familyMapper.toDomain(ent)).thenReturn(dom);

        assertEquals(dom, adapter.update(9, dom));
    }

    // update – ausente
    @Test
    void update_absent_throws() {
        when(familyCrudRepo.findById(9)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> adapter.update(9, new FamilyDomain()));
    }

    // delete – éxito
    @Test
    void delete_present() {
        Family ent = new Family();
        when(familyCrudRepo.existsById(8)).thenReturn(true);
        when(familyCrudRepo.getReferenceById(8)).thenReturn(ent);

        assertEquals(HttpStatus.OK, adapter.delete(8));
        verify(familyCrudRepo).delete(ent);
    }

    // delete – no existe
    @Test
    void delete_absent_throwsNotFound() {
        when(familyCrudRepo.existsById(8)).thenReturn(false);
        AppException ex = assertThrows(AppException.class, () -> adapter.delete(8));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getCode());
    }

    // delete – error interno
    @Test
    void delete_error_throwsInternal() {
        when(familyCrudRepo.existsById(8)).thenReturn(true);
        doThrow(new RuntimeException("boom")).when(familyCrudRepo).getReferenceById(8);
        AppException ex = assertThrows(AppException.class, () -> adapter.delete(8));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getCode());
    }

    // findRelativesByStudent – éxito
    @Test
    void findRelativesByStudent_success() {
        Family rel = new Family(); FamilyDomain dom = new FamilyDomain();
        when(familyCrudRepo.findByStudent_Id(2)).thenReturn(List.of(rel));
        when(familyCrudRepo.findByUser_Id(2)).thenReturn(List.of());
        when(familyCrudRepo.findByStudent_IdAndUserIdNot(anyInt(), anyInt())).thenReturn(List.of());
        when(familyMapper.toDomains(List.of(rel))).thenReturn(List.of(dom));

        assertEquals(List.of(dom), adapter.findRelativesByStudent(2));
    }

    // findRelativesByStudent – excepción internal
    @Test
    void findRelativesByStudent_error_returnsEmpty() {
        when(familyCrudRepo.findByStudent_Id(2)).thenThrow(new RuntimeException("db"));
        assertTrue(adapter.findRelativesByStudent(2).isEmpty());
    }

    // findStudentsByRelativeId
    @Test
    void findStudentsByRelativeId_delegatesAndMaps() {
        Family ent = new Family(); FamilyDomain dom = new FamilyDomain();
        when(familyCrudRepo.findByUser_Id(5)).thenReturn(List.of(ent));
        when(familyMapper.toDomains(List.of(ent))).thenReturn(List.of(dom));

        assertEquals(List.of(dom), adapter.findStudentsByRelativeId(5));
    }

    // getAllFamilyReports – éxito
    @Test
    void getAllFamilyReports_success() throws Exception {
        Object[] row = new Object[]{
                "C1","Perez", 5L, 3L,
                mock(Array.class, invocation -> {
                    if ("getArray".equals(invocation.getMethod().getName())) {
                        return new Integer[]{1,2,3};
                    }
                    return RETURNS_DEFAULTS.answer(invocation);
                })
        };
        when(familyCrudRepo.getFamilyReports()).thenReturn(List.<Object[]>of(row));

        var reports = adapter.getAllFamilyReports();
        assertEquals(1, reports.size());
        assertEquals("C1", reports.get(0).getCode());
        assertEquals(List.of(1,2,3), reports.get(0).getStudentIds());
    }

    // getAllFamilyReports – fallo
    @Test
    void getAllFamilyReports_error_throws() {
        when(familyCrudRepo.getFamilyReports()).thenThrow(new RuntimeException("fail"));
        assertThrows(RuntimeException.class, () -> adapter.getAllFamilyReports());
    }

    // saveById
    @Test
    void saveById_createsWithGivenUser() {
        FamilyDomain input = new FamilyDomain(null, new UserDomain(10), new UserDomain(20), RelationshipDomain.builder().build());
        Family ent = new Family(); FamilyDomain dom = new FamilyDomain();
        when(familyMapper.toEntity(any())).thenReturn(ent);
        when(familyCrudRepo.save(ent)).thenReturn(ent);
        when(familyMapper.toDomain(ent)).thenReturn(dom);

        assertEquals(dom, adapter.saveById(10, input));
        // Verificamos que el campo user del entity mapeado sea el del ID 10
    }
}
