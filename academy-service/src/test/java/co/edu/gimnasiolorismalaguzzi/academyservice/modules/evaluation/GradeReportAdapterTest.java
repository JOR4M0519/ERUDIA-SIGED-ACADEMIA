package co.edu.gimnasiolorismalaguzzi.academyservice.modules.evaluation;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectGrade;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectGradeCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.*;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.GradeReportView;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.GradeReportAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupStudentsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.GroupStudentsAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.GroupsAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.test.util.ReflectionTestUtils;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GradeReportAdapterTest {

    @Mock
    private GroupsAdapter groupsAdapter;

    @Mock
    private GroupStudentsAdapter groupStudentsAdapter;

    @Mock
    private SubjectGradeCrudRepo subjectGradeCrudRepo;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private GradeReportAdapter adapter;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(adapter, "jdbcTemplate", jdbcTemplate);
    }

    @Test
    void testGetGradeDistributionSuccess() {
        // Setup one group
        GroupsDomain group = GroupsDomain.builder().build();
        group.setId(10); group.setGroupName("G1");
        when(groupsAdapter.getGroupsByLevelAndStatus("L1","A")).thenReturn(Collections.singletonList(group));
        // Students in group
        when(groupStudentsAdapter.getStudentsByGroupId(10)).thenReturn(
                Collections.singletonList(GroupStudentsDomain.builder().student(UserDomain.builder().id(1).build()).build())
        );
        // Grades for student
        SubjectGrade g1 = mock(SubjectGrade.class); when(g1.getTotalScore()).thenReturn(new BigDecimal("2.5"));
        SubjectGrade g2 = mock(SubjectGrade.class); when(g2.getTotalScore()).thenReturn(new BigDecimal("3.5"));
        SubjectGrade g3 = mock(SubjectGrade.class); when(g3.getTotalScore()).thenReturn(new BigDecimal("4.5"));
        when(subjectGradeCrudRepo.findByStudentIdsSubjectPeriodAndYear(
                Arrays.asList(1), 5, 2, 2025)).thenReturn(Arrays.asList(g1,g2,g3));

        List<GradeDistributionDTO> dist = adapter.getGradeDistribution(2025,2,"L1",5);
        assertEquals(1, dist.size());
        GradeDistributionDTO dto = dist.get(0);
        assertEquals("G1", dto.getGroupName());
        assertEquals(1, dto.getBasicCount());
        assertEquals(1, dto.getHighCount());
        assertEquals(1, dto.getSuperiorCount());
        assertEquals(3, dto.getTotalStudents());
    }

    @Test
    void testGetGradeDistributionSkipsEmptyStudents() {
        when(groupsAdapter.getGroupsByLevelAndStatus("L1","A")).thenReturn(
                Collections.singletonList(GroupsDomain.builder().id(20).build())
        );
        when(groupStudentsAdapter.getStudentsByGroupId(20)).thenReturn(Collections.emptyList());
        List<GradeDistributionDTO> dist = adapter.getGradeDistribution(2025,1,"L1",3);
        assertTrue(dist.isEmpty());
    }

    @Test
    void testGenerateGroupReportEmpty() {
        // stub JDBC for group report
        when(jdbcTemplate.query(contains("group_id = ? AND period_id = ?"), any(Object[].class), any(RowMapper.class)))
                .thenReturn(Collections.emptyList());

        List<StudentReportDTO> reps = adapter.generateGroupReport(1L,1L);
        assertTrue(reps.isEmpty());
    }

    @Test
    void testGenerateGroupReportNonEmpty() {
        // stub JDBC
        GradeReportView view = new GradeReportView();
        view.setStudentId(1L); view.setStudentName("S1");
        view.setGroupName("G1"); view.setGroupCode("C1"); view.setPeriodName("P1");
        view.setSubjectId(10L); view.setSubjectName("Sub1");
        view.setKnowledgeId(100L); view.setKnowledgeName("K1");
        view.setKnowledgePercentage(50); view.setAchievement("A");
        view.setTotalScore(new BigDecimal("4.0"));
        // Set score to prevent NullPointerException
        view.setScore(new BigDecimal("4.0"));
        view.setRecovered("false"); view.setComment("ok");
        when(jdbcTemplate.query(contains("group_id = ? AND period_id = ?"), any(Object[].class), any(RowMapper.class)))
                .thenReturn(Collections.singletonList(view));

        List<StudentReportDTO> reps = adapter.generateGroupReport(2L,2L);
        assertEquals(1, reps.size());
        StudentReportDTO sr = reps.get(0);
        assertEquals(1L, sr.getStudentId());
        assertEquals("S1", sr.getStudentName());
        assertEquals("G1", sr.getGroupName());
        assertFalse(sr.getSubjects().isEmpty());
        SubjectReportDTO sub = sr.getSubjects().get(0);
        assertEquals(10L, sub.getSubjectId());
        assertFalse(sub.getKnowledges().isEmpty());
    }

    @Test
    void testGenerateStudentReportNotFound() {
        when(jdbcTemplate.query(contains("student_id = ? AND period_id"), any(Object[].class), any(RowMapper.class)))
                .thenReturn(Collections.emptyList());
        assertThrows(AppException.class, () -> adapter.generateStudentReport(1L,2L,3L));
    }

    @Test
    void testGenerateStudentReportSuccess() {
        GradeReportView v = new GradeReportView();
        v.setStudentId(5L); v.setStudentName("Stu");
        v.setGroupName("G"); v.setGroupCode("C");
        v.setPeriodId(1L); v.setPeriodName("P1"); // Changed to P1 to help with period number extraction
        v.setDocumentNumber("D"); v.setDocumentType("T");
        v.setAcademicYear("2025"); v.setInasistencias(2);
        v.setSubjectId(20L); v.setSubjectName("Sub");
        v.setKnowledgeId(200L); v.setKnowledgeName("K");
        v.setKnowledgePercentage(40); v.setAchievement("Ach");
        v.setTotalScore(new BigDecimal("3.5"));
        // Add period number to prevent NullPointerException
        v.setPeriodNumber(1);
        // Add score to prevent NullPointerException
        v.setScore(new BigDecimal("3.5"));
        v.setRecovered("false"); v.setComment("Cmt");
        when(jdbcTemplate.query(contains("student_id = ? AND period_id"), any(Object[].class), any(RowMapper.class)))
                .thenReturn(Collections.singletonList(v));

        StudentReportDTO sr = adapter.generateStudentReport(10L,5L,1L);
        assertEquals(5L, sr.getStudentId());
        assertEquals("Stu", sr.getStudentName());
        assertEquals("G", sr.getGroupName());
        assertEquals("C", sr.getGroupCode());
        assertFalse(sr.getSubjects().isEmpty());
    }



    @Test
    void testGenerateStudentExcelReportThrowsWhenNotFound() {
        GradeReportAdapter spy = spy(adapter);
        doReturn(null).when(spy).generateStudentReport(1L,2L,3L);
        assertThrows(AppException.class, () -> spy.generateStudentExcelReport(1L,2L,3L));
    }


    @Test
    void testGenerateMultipleStudentReportsFiltersNulls() throws Exception {
        GradeReportAdapter spy = spy(adapter);
        // stub getStudentIdsFromGroup via jdbc
        when(jdbcTemplate.queryForList(anyString(), eq(Long.class), eq(5L))).thenReturn(Arrays.asList(1L,2L));
        // stub generateStudentPdfReport
        ByteArrayResource r1 = new ByteArrayResource(new byte[]{1});
        doReturn(r1).when(spy).generateStudentPdfReport(5L,1L,10L);
        doReturn(null).when(spy).generateStudentPdfReport(5L,2L,10L);

        Map<Long, ByteArrayResource> map = spy.generateMultipleStudentReports(5L,10L);
        assertEquals(1, map.size());
        assertTrue(map.containsKey(1L));
    }

    @Test
    void testGenerateMultipleSelectedStudentReports() throws Exception {
        GradeReportAdapter spy = spy(adapter);
        when(jdbcTemplate.queryForList(anyString(), eq(Long.class), eq(7L))).thenReturn(Arrays.asList(1L,2L,3L));
        ByteArrayResource r2 = new ByteArrayResource(new byte[]{2});
        doReturn(r2).when(spy).generateStudentPdfReport(7L,2L,20L);

        Map<Long, ByteArrayResource> map = spy.generateMultipleSelectedStudentReports(7L,20L, Arrays.asList(2L,4L));
        assertEquals(1, map.size());
        assertEquals(r2, map.get(2L));
    }
}