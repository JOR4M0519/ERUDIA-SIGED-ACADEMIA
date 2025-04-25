package co.edu.gimnasiolorismalaguzzi.academyservice.modules.academic;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.AcademicPeriodDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectProfessorDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectScheduleDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.AcademicPeriod;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectGroup;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectProfessor;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectKnowledgeMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.Groups;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupStudentsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectGroupMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.mapper.GroupsMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectProfessorMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.AcademicPeriodMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectGroupCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.SubjectGroupPortAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceSubjectSchedulePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence.PersistenceGroupStudentPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
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
class SubjectGroupPortAdapterTest {

    @Mock SubjectGroupCrudRepo repo;
    @Mock SubjectGroupMapper mapper;
    @Mock PersistenceSubjectSchedulePort schedulePort;
    @Mock PersistenceGroupStudentPort groupStudentPort;

    private SubjectGroupPortAdapter adapter;

    private SubjectGroup entity;
    private SubjectGroupDomain domain;
    private List<SubjectGroup> entities;
    private List<SubjectGroupDomain> domains;

    @BeforeEach
    void setUp() {
        // Inyectar constructor y campo repo
        adapter = new SubjectGroupPortAdapter(schedulePort, mapper, groupStudentPort);
        ReflectionTestUtils.setField(adapter, "subjectGroupCrudRepo", repo);

        // Datos de prueba
        Groups group = new Groups(); group.setId(10); group.setGroupName("G1");
        GroupsDomain groupsDomain = GroupsDomain.builder().id(10).groupName("G1").build();

        User professor = new User(); professor.setId(20); professor.setFirstName("John"); professor.setLastName("Doe");
        UserDomain professorDomain = UserDomain.builder().id(20).firstName("John").lastName("Doe").build();

        SubjectProfessor sp = new SubjectProfessor(); sp.setId(30); sp.setProfessor(professor);
        SubjectProfessorDomain spDomain = SubjectProfessorDomain.builder().id(30).professor(professorDomain).build();

        AcademicPeriod ap = new AcademicPeriod(); ap.setId(40); ap.setName("2023-01");
        AcademicPeriodDomain apDomain = AcademicPeriodDomain.builder().id(40).name("2023-01").build();

        entity = new SubjectGroup();
        entity.setId(1);
        entity.setGroups(group);
        entity.setSubjectProfessor(sp);
        entity.setAcademicPeriod(ap);

        // usa directamente los mocks en la construcción del dominio
        domain = SubjectGroupDomain.builder()
                .id(1)
                .groups(groupsDomain)
                .subjectProfessor(spDomain)
                .academicPeriod(apDomain)
                .build();

        entities = Collections.singletonList(entity);
        domains = Collections.singletonList(domain);
    }

    //----------- findAll ------------

    @Test @DisplayName("findAll retorna lista mapeada")
    void findAll_success() {
        when(repo.findAll()).thenReturn(entities);
        when(mapper.toDomains(entities)).thenReturn(domains);

        List<SubjectGroupDomain> res = adapter.findAll();
        assertEquals(domains, res);
        verify(repo).findAll();
    }

    @Test @DisplayName("findAll propaga RuntimeException si falla repo")
    void findAll_failure() {
        when(repo.findAll()).thenThrow(new RuntimeException("DB"));
        assertThrows(RuntimeException.class, () -> adapter.findAll());
    }

    //------ findAllBySubjectProfessor ------

    @Test @DisplayName("findAllBySubjectProfessor retorna lista correcta")
    void findAllBySubjectProfessor_success() {
        when(repo.findBySubjectProfessor_Id(20)).thenReturn(entities);
        when(mapper.toDomains(entities)).thenReturn(domains);

        List<SubjectGroupDomain> res = adapter.findAllBySubjectProfessor(20);
        assertEquals(domains, res);
        verify(repo).findBySubjectProfessor_Id(20);
    }

    @Test @DisplayName("findAllBySubjectProfessor retorna vacío si no hay datos")
    void findAllBySubjectProfessor_empty() {
        when(repo.findBySubjectProfessor_Id(99)).thenReturn(Collections.emptyList());
        when(mapper.toDomains(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<SubjectGroupDomain> res = adapter.findAllBySubjectProfessor(99);
        assertTrue(res.isEmpty());
    }

    //------ getAllSubjectGroupsByStudentId ------

    @Test @DisplayName("getAllSubjectGroupsByStudentId retorna lista")
    void getAllSubjectGroupsByStudentId_success() {
        when(repo.findSubjectGroupsByStudentIdAndAcademicYear(5, "2023"))
                .thenReturn(entities);
        when(mapper.toDomains(entities)).thenReturn(domains);

        List<SubjectGroupDomain> res = adapter.getAllSubjectGroupsByStudentId(5, "2023");
        assertEquals(domains, res);
        verify(repo).findSubjectGroupsByStudentIdAndAcademicYear(5, "2023");
    }

    @Test @DisplayName("getAllSubjectGroupsByStudentId retorna vacío si no hay")
    void getAllSubjectGroupsByStudentId_empty() {
        when(repo.findSubjectGroupsByStudentIdAndAcademicYear(6, "2022"))
                .thenReturn(Collections.emptyList());
        when(mapper.toDomains(Collections.emptyList())).thenReturn(Collections.emptyList());

        assertTrue(adapter.getAllSubjectGroupsByStudentId(6, "2022").isEmpty());
    }

    //------ getAllSubjectGroupsByStudentIdPeriodId & ByPeriod (idénticos) ------

    @Test @DisplayName("getAllSubjectGroupsByStudentIdPeriodId retorna lista")
    void byStudentPeriodId_success() {
        when(repo.findSubjectGroupsByStudentIdAndPeriodId(7,8))
                .thenReturn(entities);
        when(mapper.toDomains(entities)).thenReturn(domains);

        assertEquals(domains, adapter.getAllSubjectGroupsByStudentIdPeriodId(7,8));
        assertEquals(domains, adapter.getAllSubjectGroupsByStudentIdByPeriod(7,8));
    }

    //------ getAllSubjectByTeacher ------

    @Test @DisplayName("getAllSubjectByTeacher retorna lista")
    void getAllSubjectByTeacher_success() {
        when(repo.getAllSubjectByTeacher(2, 2023)).thenReturn(entities);
        when(mapper.toDomains(entities)).thenReturn(domains);

        assertEquals(domains, adapter.getAllSubjectByTeacher(2, 2023));
        verify(repo).getAllSubjectByTeacher(2, 2023);
    }

    @Test @DisplayName("getAllSubjectByTeacher retorna vacío si no hay")
    void getAllSubjectByTeacher_empty() {
        when(repo.getAllSubjectByTeacher(3, 2021)).thenReturn(Collections.emptyList());
        when(mapper.toDomains(Collections.emptyList())).thenReturn(Collections.emptyList());

        assertTrue(adapter.getAllSubjectByTeacher(3, 2021).isEmpty());
    }

    //------ getStudentListByGroupTeacherPeriod ------


    @Test @DisplayName("getStudentListByGroupTeacherPeriod retorna vacío si no hay grupos")
    void getStudentListByGroupTeacherPeriod_empty() {
        when(repo.findByGroups_IdAndSubjectProfessor_Subject_IdAndSubjectProfessor_Professor_IdAndAcademicPeriod_Id(2,2,21,41))
                .thenReturn(Collections.emptyList());
        assertTrue(adapter.getStudentListByGroupTeacherPeriod(2,2,21,41).isEmpty());
    }

    //------ getAllSubjectGroupsByPeriodAndLevel ------

    @Test @DisplayName("getAllSubjectGRoupsByPeriodAndLevel retorna lista")
    void getAllSubjectByPeriodAndLevel_success() {
        when(repo.findByAcademicPeriod_IdAndGroups_StatusAndGroups_Level_Id(40, "A", 5))
                .thenReturn(entities);
        when(mapper.toDomains(entities)).thenReturn(domains);

        assertEquals(domains, adapter.getAllSubjectGRoupsByPeriodAndLevel(40,5));
    }

    @Test @DisplayName("getAllSubjectGRoupsByPeriodAndLevel retorna vacío si no hay")
    void getAllSubjectByPeriodAndLevel_empty() {
        when(repo.findByAcademicPeriod_IdAndGroups_StatusAndGroups_Level_Id(41, "A", 6))
                .thenReturn(Collections.emptyList());
        when(mapper.toDomains(Collections.emptyList())).thenReturn(Collections.emptyList());

        assertTrue(adapter.getAllSubjectGRoupsByPeriodAndLevel(41,6).isEmpty());
    }

    //------ getGroupsStudentsBy... ------
    @Test @DisplayName("getGroupsStudentsByPeriodIdAndSubjectProfessorId retorna lista")
    void getGroupsStudentsByPeriod_nc1() {
        // prepara mapper y repo
        when(repo.findByAcademicPeriod_IdAndSubjectProfessor_Subject_Id(40,1))
                .thenReturn(entities);
        when(mapper.toDomains(entities)).thenReturn(domains);
        GroupStudentsDomain gs = GroupStudentsDomain.builder().build();
        gs.setGroup(domain.getGroups());
        when(groupStudentPort.getGroupsStudentByGroupId(10,"I"))
                .thenReturn(Collections.singletonList(gs));

        List<GroupStudentsDomain> res = adapter.getGroupsStudentsByPeriodIdAndSubjectProfessorId(40,1);
        assertEquals(1, res.size());
    }



    @Test @DisplayName("getGroupsStudentsByPeriodIdAndSubjectProfessorIdAndGroupId retorna lista")
    void getGroupsStudentsByPeriodIdAndSubjectProfessorIdAndGroupId_success() {
        when(repo.findByAcademicPeriod_IdAndSubjectProfessor_IdAndGroups_Id(40,20,10))
                .thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(domain);
        GroupStudentsDomain gs = GroupStudentsDomain.builder().build();
        gs.setGroup(domain.getGroups());
        when(groupStudentPort.getGroupsStudentByGroupId(10, "I"))
                .thenReturn(Collections.singletonList(gs));

        List<GroupStudentsDomain> res = adapter.getGroupsStudentsByPeriodIdAndSubjectProfessorIdAndGroupId(40,20,10);
        assertEquals(1, res.size());
    }

    @Test @DisplayName("getGroupsStudentsByPeriodIdAndSubjectProfessorIdAndGroupId retorna vacío si toDomain falla")
    void getGroupsStudentsByPeriod_emptyNc2() {
        when(repo.findByAcademicPeriod_IdAndSubjectProfessor_IdAndGroups_Id(41,21,11))
                .thenReturn(null);
        assertThrows(NullPointerException.class, () ->
                adapter.getGroupsStudentsByPeriodIdAndSubjectProfessorIdAndGroupId(41,21,11));
    }

    //------ getSubjectsByGroupIdAndPeriodId ------
    @Test @DisplayName("getSubjectsByGroupIdAndPeriodId retorna dominios")
    void getSubjectsByGroupIdAndPeriodId_success() {
        when(repo.findByGroups_IdAndAcademicPeriod_Id(10,40))
                .thenReturn(entities);
        when(mapper.toDomains(entities)).thenReturn(domains);
        assertEquals(domains, adapter.getSubjectsByGroupIdAndPeriodId(10,40));
    }

    @Test @DisplayName("getSubjectsByGroupIdAndPeriodId retorna vacío")
    void getSubjectsByGroupIdAndPeriodId_empty() {
        when(repo.findByGroups_IdAndAcademicPeriod_Id(11,41))
                .thenReturn(Collections.emptyList());
        when(mapper.toDomains(Collections.emptyList())).thenReturn(Collections.emptyList());
        assertTrue(adapter.getSubjectsByGroupIdAndPeriodId(11,41).isEmpty());
    }

    //------ findById ------
    @Test @DisplayName("findById retorna dominio cuando existe")
    void findById_exists() {
        when(repo.findById(1)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);
        assertEquals(domain, adapter.findById(1));
    }

    @Test @DisplayName("findById retorna null cuando no existe")
    void findById_notExists() {
        when(repo.findById(2)).thenReturn(Optional.empty());
        assertNull(adapter.findById(2));
    }


    //------ update ------
    @Test @DisplayName("update modifica y devuelve dominio")
    void update_success() {
        SubjectGroupDomain input = domain;
        when(repo.findById(1)).thenReturn(Optional.of(entity));
        when(mapper.toEntity(input)).thenReturn(entity);
        when(repo.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(input);

        SubjectGroupDomain res = adapter.update(1, input);
        assertEquals(input, res);
    }


    //------ delete ------
    @Test @DisplayName("delete elimina y devuelve OK")
    void delete_success() {
        when(repo.existsById(1)).thenReturn(true);
        when(repo.getReferenceById(1)).thenReturn(entity);

        assertEquals(HttpStatus.OK, adapter.delete(1));
        verify(repo).delete(entity);
    }


    @Test @DisplayName("delete lanza INTERNAL_SERVER_ERROR si getReference falla")
    void delete_failure() {
        when(repo.existsById(1)).thenReturn(true);
        when(repo.getReferenceById(1)).thenThrow(new RuntimeException("X"));
        AppException ex = assertThrows(AppException.class, () -> adapter.delete(1));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getCode());
    }
}
