package co.edu.gimnasiolorismalaguzzi.academyservice.modules.evaluation;


import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.KnowledgeReportDTO;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.StudentReportDTO;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.SubjectReportDTO;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.GradeReportView;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.GradeReportAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupStudentsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.GroupStudentsAdapter;
import org.apache.pdfbox.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private GroupStudentsAdapter groupStudentsAdapter;

    @InjectMocks
    private GradeReportAdapter reportService;

    private GradeReportView sampleReportView;
    private List<GradeReportView> sampleReportData;
    private List<Map<String, Object>> periodScoresList;
    private List<GroupStudentsDomain> sampleGroupStudents;

    @BeforeEach
    void setUp() {
        // Setup sample period scores
        periodScoresList = new ArrayList<>();
        Map<String, Object> periodScore1 = new HashMap<>();
        periodScore1.put("period_number", 1);
        periodScore1.put("period_name", "Periodo 1");
        periodScore1.put("score", new BigDecimal("4.0"));
        periodScoresList.add(periodScore1);

        // Setup sample report view
        sampleReportView = new GradeReportView();
        sampleReportView.setStudentId(1L);
        sampleReportView.setStudentName("Juan Pérez");
        sampleReportView.setDocumentNumber("12345678");
        sampleReportView.setDocumentType("CC");
        sampleReportView.setGroupId(101L);
        sampleReportView.setGroupName("Quinto A");
        sampleReportView.setGroupCode("5A");
        sampleReportView.setPeriodId(201L);
        sampleReportView.setPeriodName("Periodo 1");
        sampleReportView.setPeriodNumber(1);
        sampleReportView.setSubjectId(301L);
        sampleReportView.setSubjectName("Matemáticas");
        sampleReportView.setTeacherName("Profesor Jiménez");
        sampleReportView.setKnowledgeId(401L);
        sampleReportView.setKnowledgeName("Álgebra Básica");
        sampleReportView.setKnowledgePercentage(30);
        sampleReportView.setAchievement("Comprende operaciones algebraicas básicas");
        sampleReportView.setScore(new BigDecimal("4.5"));
        sampleReportView.setDefinitivaScore(new BigDecimal("1.35"));
        sampleReportView.setTotalScore(new BigDecimal("4.5"));
        sampleReportView.setRecovered("false");
        sampleReportView.setComment("Excelente desempeño");
        sampleReportView.setAcademicYear("2023");
        sampleReportView.setInasistencias(3);
        sampleReportView.setPeriodScores(periodScoresList);

        // Create a list of report data
        sampleReportData = new ArrayList<>();
        sampleReportData.add(sampleReportView);

        // Second report view for the same student, different knowledge
        GradeReportView reportView2 = new GradeReportView();
        reportView2.setStudentId(1L);
        reportView2.setStudentName("Juan Pérez");
        reportView2.setDocumentNumber("12345678");
        reportView2.setDocumentType("CC");
        reportView2.setGroupId(101L);
        reportView2.setGroupName("Quinto A");
        reportView2.setGroupCode("5A");
        reportView2.setPeriodId(201L);
        reportView2.setPeriodName("Periodo 1");
        reportView2.setPeriodNumber(1);
        reportView2.setSubjectId(301L);
        reportView2.setSubjectName("Matemáticas");
        reportView2.setTeacherName("Profesor Jiménez");
        reportView2.setKnowledgeId(402L);
        reportView2.setKnowledgeName("Geometría");
        reportView2.setKnowledgePercentage(70);
        reportView2.setAchievement("Identifica formas geométricas y sus propiedades");
        reportView2.setScore(new BigDecimal("4.2"));
        reportView2.setDefinitivaScore(new BigDecimal("2.94"));
        reportView2.setTotalScore(new BigDecimal("4.5"));
        reportView2.setRecovered("false");
        reportView2.setComment("Excelente desempeño");
        reportView2.setAcademicYear("2023");
        reportView2.setInasistencias(3);
        reportView2.setPeriodScores(periodScoresList);

        sampleReportData.add(reportView2);

        // Setup sample group students
        sampleGroupStudents = new ArrayList<>();
        GroupStudentsDomain groupStudent = GroupStudentsDomain.builder().build();
        GroupsDomain group = GroupsDomain.builder().build();
        group.setId(101);
        groupStudent.setGroup(group);
        UserDomain student = new UserDomain();
        student.setId(1);
        groupStudent.setStudent(student);
        groupStudent.setStatus("A");
        sampleGroupStudents.add(groupStudent);
    }



    @Test
    void processSubjectAndKnowledge_NewSubject_AddsSubjectToStudent() {
        // Arrange
        StudentReportDTO studentReport = new StudentReportDTO();
        GradeReportView data = sampleReportView;

        // Act
        reportService.processSubjectAndKnowledge(studentReport, data);

        // Assert
        assertEquals(1, studentReport.getSubjects().size());
        SubjectReportDTO subject = studentReport.getSubjects().get(0);
        assertEquals(data.getSubjectId(), subject.getSubjectId());
        assertEquals(data.getSubjectName(), subject.getSubjectName());
        assertEquals(data.getTotalScore(), subject.getTotalScore());
        assertEquals(1, subject.getKnowledges().size());

        KnowledgeReportDTO knowledge = subject.getKnowledges().get(0);
        assertEquals(data.getKnowledgeId(), knowledge.getKnowledgeId());
        assertEquals(data.getKnowledgeName(), knowledge.getKnowledgeName());
        assertEquals(data.getKnowledgePercentage(), knowledge.getPercentage());
        assertEquals(data.getAchievement(), knowledge.getAchievement());
        assertEquals(data.getScore(), knowledge.getScore());
    }

    @Test
    void processSubjectAndKnowledge_ExistingSubject_AddsKnowledgeToSubject() {
        // Arrange
        StudentReportDTO studentReport = new StudentReportDTO();
        GradeReportView data1 = sampleReportView;

        // Process first knowledge
        reportService.processSubjectAndKnowledge(studentReport, data1);

        // Create second knowledge for same subject
        GradeReportView data2 = new GradeReportView();
        data2.setStudentId(1L);
        data2.setSubjectId(301L); // Same subject
        data2.setSubjectName("Matemáticas");
        data2.setKnowledgeId(402L); // Different knowledge
        data2.setKnowledgeName("Geometría");
        data2.setKnowledgePercentage(70);
        data2.setAchievement("Identifica formas geométricas");
        data2.setScore(new BigDecimal("4.2"));
        data2.setTotalScore(new BigDecimal("4.5"));

        // Act
        reportService.processSubjectAndKnowledge(studentReport, data2);

        // Assert
        assertEquals(1, studentReport.getSubjects().size()); // Still one subject
        SubjectReportDTO subject = studentReport.getSubjects().get(0);
        assertEquals(2, subject.getKnowledges().size()); // But now two knowledges

        boolean foundFirstKnowledge = false;
        boolean foundSecondKnowledge = false;

        for (KnowledgeReportDTO knowledge : subject.getKnowledges()) {
            if (knowledge.getKnowledgeId().equals(401L)) {
                foundFirstKnowledge = true;
                assertEquals("Álgebra Básica", knowledge.getKnowledgeName());
            } else if (knowledge.getKnowledgeId().equals(402L)) {
                foundSecondKnowledge = true;
                assertEquals("Geometría", knowledge.getKnowledgeName());
            }
        }

        assertTrue(foundFirstKnowledge);
        assertTrue(foundSecondKnowledge);
    }

    @Test
    void processSubjectAndKnowledge_DuplicateKnowledge_DoesNotAddDuplicate() {
        // Arrange
        StudentReportDTO studentReport = new StudentReportDTO();
        GradeReportView data = sampleReportView;

        // Process knowledge first time
        reportService.processSubjectAndKnowledge(studentReport, data);

        // Act - process same knowledge again
        reportService.processSubjectAndKnowledge(studentReport, data);

        // Assert
        assertEquals(1, studentReport.getSubjects().size());
        SubjectReportDTO subject = studentReport.getSubjects().get(0);
        assertEquals(1, subject.getKnowledges().size()); // Still only one knowledge
    }

    @Test
    void loadPreviousPeriodsScores_CurrentPeriodIsFirst_DoesNotLoadAnyPreviousPeriods() {
        // Arrange
        StudentReportDTO studentReport = new StudentReportDTO();
        studentReport.setAcademicYear("2023");

        // Act
        reportService.loadPreviousPeriodsScores(101L, 1L, 201L, 1, studentReport);

        // Assert
        // No exception should be thrown, and no additional DB queries should be made
        verify(jdbcTemplate, never()).queryForObject(anyString(), eq(Long.class), anyString(), anyInt());
    }
}