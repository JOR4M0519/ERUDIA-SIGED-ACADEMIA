package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service;


import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectGrade;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectGradeCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.*;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.GradeReportView;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.repository.GradeReportCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.persistence.PersistanceGradeReportPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.persistence.PersistenceGradeReportService;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.GroupsAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.GroupStudentsAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import com.lowagie.text.pdf.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@PersistenceAdapter
@Slf4j
public class GradeReportAdapter implements PersistenceGradeReportService, PersistanceGradeReportPort {

    private GradeReportCrudRepo reportRepository;
    private SubjectGradeCrudRepo subjectGradeCrudRepo;

    private GroupsAdapter groupsAdapter;
    private GroupStudentsAdapter groupStudentsAdapter;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public GradeReportAdapter(GradeReportCrudRepo reportRepository, GroupsAdapter groupsAdapter, GroupStudentsAdapter groupStudentsAdapter, SubjectGradeCrudRepo subjectGradeCrudRepo) {
        this.reportRepository = reportRepository;
        this.groupsAdapter = groupsAdapter;
        this.groupStudentsAdapter = groupStudentsAdapter;
        this.subjectGradeCrudRepo = subjectGradeCrudRepo;
    }

    @Override
    public List<GradeDistributionDTO> getGradeDistribution(Integer year, Integer periodId, String levelId, Integer subjectId) {
        // 1. Obtener todos los grupos activos del nivel educativo especificado
        List<GroupsDomain> groups = groupsAdapter.getGroupsByLevelAndStatus(levelId, "A");

        List<GradeDistributionDTO> result = new ArrayList<>();

        // 2. Para cada grupo, obtener la distribución de notas
        for (GroupsDomain group : groups) {
            // 3. Obtener los estudiantes del grupo
            List<Integer> studentIds = groupStudentsAdapter.getStudentsByGroupId(group.getId())
                    .stream()
                    .map(student -> student.getStudent().getId())
                    .collect(Collectors.toList());

            if (studentIds.isEmpty()) {
                continue;
            }

            // 4. Obtener las notas de los estudiantes para la materia y periodo específicos
            List<SubjectGrade> grades = subjectGradeCrudRepo.findByStudentIdsSubjectPeriodAndYear(
                    studentIds, subjectId, periodId, year);

            // 5. Contar las notas por categoría
            int basicCount = 0;
            int highCount = 0;
            int superiorCount = 0;

            for (SubjectGrade grade : grades) {
                double score = grade.getTotalScore().doubleValue();
                if (score >= 0 && score < 3) {
                    basicCount++;
                } else if (score >= 3 && score < 4) {
                    highCount++;
                } else if (score >= 4 && score <= 5) {
                    superiorCount++;
                }
            }

            // 6. Crear el DTO con los resultados
            GradeDistributionDTO distributionDTO = GradeDistributionDTO.builder()
                    .groupName(group.getGroupName())
                    .basicCount(basicCount)
                    .highCount(highCount)
                    .superiorCount(superiorCount)
                    .totalStudents(grades.size())
                    .build();

            result.add(distributionDTO);
        }

        return result;
    }

    // Método para obtener datos directamente con JDBC
    private List<GradeReportView> getReportDataWithJdbc(Long groupId, Long periodId) {
        String sql = "SELECT * FROM  v_academic_report " +
                "WHERE group_id = ? AND period_id = ? " +
                "ORDER BY student_name, subject_name, knowledge_name";

        return jdbcTemplate.query(sql, new Object[]{groupId, periodId}, new RowMapper<GradeReportView>() {
            @Override
            public GradeReportView mapRow(ResultSet rs, int rowNum) throws SQLException {
                GradeReportView view = new GradeReportView();
                view.setGradeId(rs.getLong("grade_id"));
                view.setStudentId(rs.getLong("student_id"));
                view.setStudentName(rs.getString("student_name"));
                view.setSubjectId(rs.getLong("subject_id"));
                view.setSubjectName(rs.getString("subject_name"));
                view.setPeriodId(rs.getLong("period_id"));
                view.setPeriodName(rs.getString("period_name"));
                view.setTotalScore(rs.getBigDecimal("total_score"));
                view.setRecovered(rs.getString("recovered"));
                view.setComment(rs.getString("comment"));
                view.setGroupId(rs.getLong("group_id"));
                view.setGroupName(rs.getString("group_name"));
                view.setGroupCode(rs.getString("group_code"));
                view.setSubjectKnowledgeId(rs.getLong("subject_knowledge_id"));
                view.setKnowledgeId(rs.getLong("knowledge_id"));
                view.setKnowledgeName(rs.getString("knowledge_name"));
                view.setKnowledgePercentage(rs.getInt("knowledge_percentage"));
                view.setAchievementGroupId(rs.getLong("achievement_group_id"));
                view.setAchievement(rs.getString("achievement"));

                // Intentar obtener los campos adicionales si existen
                try {
                    view.setDocumentNumber(rs.getString("document_number"));
                    view.setDocumentType(rs.getString("document_type"));
                    view.setAcademicYear(rs.getString("academic_year"));
                    view.setTeacherName(rs.getString("teacher_name"));
                    view.setInasistencias(rs.getInt("inasistencias"));
                    view.setScore(rs.getBigDecimal("score"));
                    view.setDefinitivaScore(rs.getBigDecimal("definitiva_score"));
                    view.setPeriodNumber(rs.getInt("period_number"));

                    // Intentar obtener period_scores como JSONB
                    String periodScoresJson = rs.getString("period_scores");
                    if (periodScoresJson != null) {
                        ObjectMapper mapper = new ObjectMapper();
                        view.setPeriodScores(mapper.readValue(periodScoresJson,
                                new TypeReference<List<Map<String,Object>>>() {}));
                    }
                } catch (SQLException | IOException e) {
                    log.debug("Algunos campos extendidos no están disponibles en la vista", e);
                }

                return view;
            }
        });
    }

    private List<GradeReportView> getReportDataWithJdbc(Long groupId, Long studentId, Long periodId) {
        String sql = "SELECT * FROM v_academic_report " +
                "WHERE group_id = ? AND student_id = ? AND period_id = ? " +
                "ORDER BY subject_name, knowledge_name";

        return jdbcTemplate.query(sql, new Object[]{groupId, studentId, periodId}, new RowMapper<GradeReportView>() {
            @Override
            public GradeReportView mapRow(ResultSet rs, int rowNum) throws SQLException {
                GradeReportView view = new GradeReportView();
                view.setGradeId(rs.getLong("grade_id"));
                view.setStudentId(rs.getLong("student_id"));
                view.setStudentName(rs.getString("student_name"));
                view.setDocumentNumber(rs.getString("document_number"));
                view.setDocumentType(rs.getString("document_type"));
                view.setSubjectId(rs.getLong("subject_id"));
                view.setSubjectName(rs.getString("subject_name"));
                view.setPeriodId(rs.getLong("period_id"));
                view.setPeriodName(rs.getString("period_name"));
                view.setAcademicYear(rs.getString("academic_year"));
                view.setTotalScore(rs.getBigDecimal("total_score"));
                view.setRecovered(rs.getString("recovered"));
                view.setComment(rs.getString("comment"));
                view.setGroupId(rs.getLong("group_id"));
                view.setGroupName(rs.getString("group_name"));
                view.setGroupCode(rs.getString("group_code"));
                view.setSubjectKnowledgeId(rs.getLong("subject_knowledge_id"));
                view.setKnowledgeId(rs.getLong("knowledge_id"));
                view.setKnowledgeName(rs.getString("knowledge_name"));
                view.setKnowledgePercentage(rs.getInt("knowledge_percentage"));
                view.setAchievementGroupId(rs.getLong("achievement_group_id"));
                view.setAchievement(rs.getString("achievement"));
                view.setScore(rs.getBigDecimal("score"));
                view.setDefinitivaScore(rs.getBigDecimal("definitiva_score"));
                view.setPeriodNumber(rs.getInt("period_number"));
                view.setTeacherName(rs.getString("teacher_name"));
                view.setInasistencias(rs.getInt("inasistencias"));

                // Mapeo del campo JSON period_scores
                try {
                    String periodScoresJson = rs.getString("period_scores");
                    if (periodScoresJson != null) {
                        ObjectMapper mapper = new ObjectMapper();
                        view.setPeriodScores(mapper.readValue(periodScoresJson,
                                new TypeReference<List<Map<String, Object>>>() {}));
                    }
                } catch (Exception e) {
                    log.error("Error parsing period_scores JSON", e);
                }

                return view;
            }
        });
    }


    @Override
    public List<StudentReportDTO> generateGroupReport(Long groupId, Long periodId) {
        // Usar JDBC directamente para evitar problemas con JPA
        List<GradeReportView> reportData = getReportDataWithJdbc(groupId, periodId);

        log.info("Recuperados {} registros de la base de datos para el grupo {} y periodo {}",
                reportData.size(), groupId, periodId);

        // Agregar logs para verificar los datos recibidos
        log.info("Primeros registros recuperados:");
        Map<Long, Integer> knowledgeTypeCounts = new HashMap<>();

        for (int i = 0; i < Math.min(10, reportData.size()); i++) {
            GradeReportView data = reportData.get(i);
            log.info("  Estudiante: {}, Materia: {}, Conocimiento: {} (ID: {})",
                    data.getStudentName(), data.getSubjectName(),
                    data.getKnowledgeName(), data.getKnowledgeId());

            // Contar los tipos de conocimiento para verificar
            Long knowledgeId = data.getKnowledgeId();
            knowledgeTypeCounts.put(knowledgeId, knowledgeTypeCounts.getOrDefault(knowledgeId, 0) + 1);
        }

        log.info("Distribución de tipos de conocimiento en los primeros registros: {}", knowledgeTypeCounts);

        return processGroupReportData(reportData);
    }

    private List<StudentReportDTO> processGroupReportData(List<GradeReportView> reportData) {
        Map<Long, StudentReportDTO> studentReportsMap = new HashMap<>();

        for (GradeReportView data : reportData) {
            // Agrupar por estudiante
            StudentReportDTO studentReport = studentReportsMap.computeIfAbsent(
                    data.getStudentId(),
                    id -> {
                        StudentReportDTO report = new StudentReportDTO();
                        report.setStudentId(data.getStudentId());
                        report.setStudentName(data.getStudentName());
                        report.setGroupName(data.getGroupName());
                        report.setGroupCode(data.getGroupCode());
                        report.setPeriodName(data.getPeriodName());

                        // Intentar establecer campos adicionales si están disponibles
                        if (data.getDocumentNumber() != null) {
                            report.setDocumentNumber(data.getDocumentNumber());
                        }
                        if (data.getDocumentType() != null) {
                            report.setDocumentType(data.getDocumentType());
                        }
                        if (data.getAcademicYear() != null) {
                            report.setAcademicYear(data.getAcademicYear());
                        }

                        // Extraer grado y jornada del nombre del grupo
                        String[] groupParts = data.getGroupName().split(" ");
                        if (groupParts.length > 0) {
                            report.setGrade(groupParts[0]);
                            report.setJornada("Mañana"); // Por defecto
                        }
                        report.setNivelEducacion("Primaria"); // Por defecto

                        return report;
                    }
            );

            // Procesar materias y conocimientos
            processSubjectAndKnowledge(studentReport, data);
        }

        return new ArrayList<>(studentReportsMap.values());
    }

    public void processSubjectAndKnowledge(StudentReportDTO studentReport, GradeReportView data) {
        // Buscar o crear la materia
        SubjectReportDTO subjectReport = studentReport.getSubjects().stream()
                .filter(s -> s.getSubjectId().equals(data.getSubjectId()))
                .findFirst()
                .orElseGet(() -> {
                    SubjectReportDTO newSubject = new SubjectReportDTO();
                    newSubject.setSubjectId(data.getSubjectId());
                    newSubject.setSubjectName(data.getSubjectName());
                    newSubject.setTotalScore(data.getTotalScore());
                    newSubject.setRecovered(Boolean.valueOf(data.getRecovered()));
                    newSubject.setComment(data.getComment());

                    // Asignar siempre el nombre del profesor (nunca "No asignado")
                    if (data.getTeacherName() != null && !data.getTeacherName().trim().isEmpty()) {
                        newSubject.setTeacherName(data.getTeacherName().trim());
                    } else {
                        // Si por alguna razón no hubiera profesor, usar una descripción más específica
                        newSubject.setTeacherName("Institución");
                    }

                    // Establecer el área (por defecto es el nombre de la materia)
                    newSubject.setArea(data.getSubjectName());

                    // Calcular el desempeño basado en la nota total
                    calculatePerformance(newSubject);

                    // Agregar información de notas por periodo si está disponible
                    if (data.getPeriodScores() != null) {
                        newSubject.setPeriodScores(data.getPeriodScores());
                    } else {
                        // Inicializar una lista vacía para evitar NullPointerException
                        newSubject.setPeriodScores(new ArrayList<>());
                    }

                    studentReport.getSubjects().add(newSubject);
                    return newSubject;
                });

        // Actualizar el nombre del profesor si la materia ya existe y no tiene profesor
        if (data.getTeacherName() != null && !data.getTeacherName().trim().isEmpty() &&
                (subjectReport.getTeacherName() == null || subjectReport.getTeacherName().equals("Institución"))) {
            subjectReport.setTeacherName(data.getTeacherName().trim());
        }

        // Si la materia ya tiene notas por periodo pero es un arreglo vacío, usar la información de data
        if (subjectReport.getPeriodScores() == null) {
            subjectReport.setPeriodScores(data.getPeriodScores());
        } else if (data.getPeriodScores() != null && !data.getPeriodScores().isEmpty()) {
            // Asegurarse de que todas las notas de periodos anteriores estén incluidas
            for (Map<String, Object> periodScore : data.getPeriodScores()) {
                // Verificar si este periodo ya existe en la lista
                boolean exists = false;
                for (Map<String, Object> existingScore : subjectReport.getPeriodScores()) {
                    if (existingScore.get("period_number").equals(periodScore.get("period_number"))) {
                        exists = true;
                        break;
                    }
                }

                // Si no existe, agregar
                if (!exists) {
                    subjectReport.getPeriodScores().add(periodScore);
                }
            }
        }

        // El resto del código para procesar conocimientos permanece igual
        boolean knowledgeExists = false;
        for (KnowledgeReportDTO k : subjectReport.getKnowledges()) {
            if (k.getKnowledgeId().equals(data.getKnowledgeId())) {
                knowledgeExists = true;
                break;
            }
        }

        if (!knowledgeExists) {
            KnowledgeReportDTO knowledgeReport = new KnowledgeReportDTO();
            knowledgeReport.setKnowledgeId(data.getKnowledgeId());
            knowledgeReport.setKnowledgeName(data.getKnowledgeName());
            knowledgeReport.setPercentage(data.getKnowledgePercentage());
            knowledgeReport.setAchievement(data.getAchievement());
            knowledgeReport.setScore(data.getScore());

            if (data.getDefinitivaScore() != null) {
                knowledgeReport.setDefinitivaScore(data.getDefinitivaScore());
            } else {
                BigDecimal percentage = new BigDecimal(data.getKnowledgePercentage()).divide(new BigDecimal("100"));
                knowledgeReport.setDefinitivaScore(knowledgeReport.getScore().multiply(percentage));
            }

            subjectReport.getKnowledges().add(knowledgeReport);
        }
    }
    // Método para calcular el desempeño
    public void calculatePerformance(SubjectReportDTO subject) {
        BigDecimal totalScore = subject.getTotalScore();

        if (totalScore.compareTo(new BigDecimal("4.6")) >= 0) {
            subject.setDesempenio("SUPERIOR");
        } else if (totalScore.compareTo(new BigDecimal("4.0")) >= 0) {
            subject.setDesempenio("ALTO");
        } else if (totalScore.compareTo(new BigDecimal("3.0")) >= 0) {
            subject.setDesempenio("BÁSICO");
        } else {
            subject.setDesempenio("BAJO");
        }
    }

    @Override
    public ByteArrayResource generateExcelReport(Long groupId, Long periodId) throws IOException {
        List<StudentReportDTO> reportData = generateGroupReport(groupId, periodId);
        return createExcelReport(reportData);
    }

    @Override
    public ByteArrayResource generateStudentExcelReport(Long groupId, Long studentId, Long periodId) throws IOException {
        StudentReportDTO reportData = generateStudentReport(groupId, studentId, periodId);
        if (reportData == null) {
            throw new AppException("No se encontró reporte para el estudiante", HttpStatus.NOT_FOUND);
        }

        List<StudentReportDTO> reportList = new ArrayList<>();
        reportList.add(reportData);
        return createExcelReport(reportList);
    }

    public ByteArrayResource createExcelReport(List<StudentReportDTO> reportData) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Reporte Académico");

            // Crear estilos
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = (Font) workbook.createFont();
            headerFont.setStyle(1);;
            headerStyle.setFont((org.apache.poi.ss.usermodel.Font) headerFont);

            // Crear encabezados
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Estudiante", "Materia", "Conocimiento", "Logro", "Porcentaje", "Nota Final"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Llenar datos
            int rowNum = 1;
            for (StudentReportDTO student : reportData) {
                for (SubjectReportDTO subject : student.getSubjects()) {
                    for (KnowledgeReportDTO knowledge : subject.getKnowledges()) {
                        Row row = sheet.createRow(rowNum++);
                        row.createCell(0).setCellValue(student.getStudentName());
                        row.createCell(1).setCellValue(subject.getSubjectName());
                        row.createCell(2).setCellValue(knowledge.getKnowledgeName());
                        row.createCell(3).setCellValue(knowledge.getAchievement());
                        row.createCell(4).setCellValue(knowledge.getPercentage() + "%");
                        row.createCell(5).setCellValue(subject.getTotalScore().doubleValue());
                    }
                }
            }

            // Ajustar ancho de columnas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Convertir a ByteArrayResource
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            byte[] bytes = outputStream.toByteArray();

            return new ByteArrayResource(bytes);
        }
    }


    public Map<Long, ByteArrayResource> generateMultipleStudentReports(Long groupId, Long periodId) throws IOException {
        // Obtener todos los estudiantes del grupo
        List<Long> studentIds = getStudentIdsFromGroup(groupId);
        Map<Long, ByteArrayResource> reports = new HashMap<>();

        // Generar un reporte PDF para cada estudiante
        for (Long studentId : studentIds) {
            ByteArrayResource report = generateStudentPdfReport(groupId,studentId, periodId);
            if (report != null) {
                reports.put(studentId, report);
            }
        }

        return reports;
    }

    public Map<Long, ByteArrayResource> generateMultipleSelectedStudentReports(Long groupId, Long periodId, List<Long> selectedStudentIds) throws IOException {
        Map<Long, ByteArrayResource> reports = new HashMap<>();

        // Filtrar solo los estudiantes seleccionados que pertenecen al grupo
        List<Long> groupStudentIds = getStudentIdsFromGroup(groupId);
        List<Long> validStudentIds = selectedStudentIds.stream()
                .filter(groupStudentIds::contains)
                .collect(Collectors.toList());

        // Generar un reporte PDF para cada estudiante seleccionado
        for (Long studentId : validStudentIds) {
            ByteArrayResource report = generateStudentPdfReport(groupId, studentId, periodId);
            if (report != null) {
                reports.put(studentId, report);
            }
        }

        return reports;
    }



    private List<Long> getStudentIdsFromGroup(Long groupId) {
        // Implementar la lógica para obtener todos los IDs de estudiantes de un grupo
        String sql = "SELECT student_id FROM group_students WHERE group_id = ? AND status = 'A'";
        return jdbcTemplate.queryForList(sql, Long.class, groupId);
    }

    @Override
    public ByteArrayResource generatePdfReport(Long groupId, Long periodId) throws IOException {
        // Si se necesita mantener la compatibilidad con código existente que espera un único PDF,
        // podemos crear un PDF combinado con todos los reportes de estudiantes
        List<StudentReportDTO> reportData = generateGroupReport(groupId, periodId);

        // Si hay un solo estudiante, devuelve su reporte
        if (reportData.size() == 1) {
            return createDetailedPdfReport(reportData.getFirst());
        }

        // Si hay múltiples estudiantes, podemos generar un PDF combinado
        // o utilizar el método que ya existe para generar múltiples reportes
        Map<Long, ByteArrayResource> individualReports = generateMultipleStudentReports(groupId, periodId);

        // Crear un PDF combinado o retornar el primer reporte (dependiendo del comportamiento deseado)
        // Opción 1: Devolver el primero (comportamiento actual pero explícito)
        // return individualReports.values().iterator().next();

        // Opción 2: Combinar todos los PDFs en uno solo (recomendado)
        return combineMultiplePdfs(individualReports.values());
    }

    /**
     * Método auxiliar para combinar múltiples PDFs en un solo recurso
     */
    private ByteArrayResource combineMultiplePdfs(Collection<ByteArrayResource> pdfResources) throws IOException {
        ByteArrayOutputStream mergedPdfOutput = new ByteArrayOutputStream();

        try (PDDocument mergedDocument = new PDDocument()) {
            PDFMergerUtility pdfMerger = new PDFMergerUtility();

            for (ByteArrayResource resource : pdfResources) {
                try (PDDocument doc = PDDocument.load(resource.getInputStream())) {
                    for (int i = 0; i < doc.getNumberOfPages(); i++) {
                        PDPage page = doc.getPage(i);
                        mergedDocument.addPage(page);
                    }
                }
            }

            mergedDocument.save(mergedPdfOutput);
        }

        return new ByteArrayResource(mergedPdfOutput.toByteArray());
    }

    @Override
    public ByteArrayResource generateStudentPdfReport(Long groupId, Long studentId, Long periodId) throws IOException {


        if(groupId == 0){
            groupId = groupStudentsAdapter.getGroupsStudentById(Math.toIntExact(studentId),"A").getFirst().getGroup().getId().longValue();
        }

        StudentReportDTO reportData = generateStudentReport(groupId, studentId, periodId);
        if (reportData == null) {
            throw new AppException("No se encontró reporte para el estudiante", HttpStatus.NOT_FOUND);
        }

        List<StudentReportDTO> reportList = new ArrayList<>();
        reportList.add(reportData);
        return createDetailedPdfReport(reportData);
    }

    // Método para generar el reporte individual por estudiante
    @Override
    public StudentReportDTO generateStudentReport(Long groupId, Long studentId, Long periodId) {
        List<GradeReportView> reportData = getReportDataWithJdbc(groupId, studentId, periodId);

        if (reportData == null || reportData.isEmpty()) {
            throw new AppException("No se encontró reporte para el estudiante", HttpStatus.NOT_FOUND);
        }

        // Crear una estructura para los datos del estudiante a partir del primer registro
        GradeReportView firstData = reportData.get(0);
        StudentReportDTO studentReport = new StudentReportDTO();
        studentReport.setStudentId(firstData.getStudentId());
        studentReport.setStudentName(firstData.getStudentName());
        studentReport.setDocumentNumber(firstData.getDocumentNumber());
        studentReport.setDocumentType(firstData.getDocumentType());
        studentReport.setGroupName(firstData.getGroupName());
        studentReport.setGroupCode(firstData.getGroupCode());
        studentReport.setGroupId(firstData.getGroupId());
        studentReport.setPeriodId(firstData.getPeriodId());
        studentReport.setPeriodName(firstData.getPeriodName());
        studentReport.setAcademicYear(firstData.getAcademicYear());
        studentReport.setInasistencias(firstData.getInasistencias() != null ? firstData.getInasistencias() : 0);

        // Obtener el número del periodo actual
        int currentPeriodNumber = firstData.getPeriodNumber();

        // Extraer información adicional del grupo
        String[] groupParts = studentReport.getGroupName().split(" ");
        if (groupParts.length > 0) {
            studentReport.setGrade(groupParts[0]);
            studentReport.setJornada("Mañana"); // Por defecto
        }
        studentReport.setNivelEducacion("Primaria"); // Por defecto

        // Procesar cada registro para crear las materias y conocimientos
        for (GradeReportView data : reportData) {
            processSubjectAndKnowledge(studentReport, data);
        }

        // Si estamos en un periodo > 1, recuperar notas de periodos anteriores
        if (currentPeriodNumber > 1) {
            loadPreviousPeriodsScores(groupId, studentId, periodId, currentPeriodNumber, studentReport);
        }

        return studentReport;
    }

    // Método para cargar notas de periodos anteriores
// Método actualizado para cargar notas de periodos anteriores
    public void loadPreviousPeriodsScores(Long groupId, Long studentId, Long currentPeriodId,
                                          int currentPeriodNumber, StudentReportDTO studentReport) {

        // Para cada periodo anterior
        for (int prevPeriodNumber = 1; prevPeriodNumber < currentPeriodNumber; prevPeriodNumber++) {
            // Crear una variable final para usar en lambda
            final int currentPrevPeriodNumber = prevPeriodNumber;

            // Consulta para obtener el ID del periodo anterior (corregida)
            String periodIdQuery = "SELECT id FROM academic_period WHERE EXTRACT(year FROM start_date) = ? " +
                    "ORDER BY start_date LIMIT 1 OFFSET ?";

            Long prevPeriodId = null;
            try {
                prevPeriodId = jdbcTemplate.queryForObject(
                        periodIdQuery,
                        Long.class,
                        studentReport.getAcademicYear(),
                        currentPrevPeriodNumber - 1); // Restamos 1 porque el OFFSET comienza en 0
            } catch (Exception e) {
                log.warn("No se pudo obtener el periodo {} para el año {}: {}",
                        currentPrevPeriodNumber, studentReport.getAcademicYear(), e.getMessage());
                continue;
            }

            if (prevPeriodId == null) {
                continue;
            }

            // Resto del código para obtener y mapear notas...
            List<GradeReportView> prevPeriodData = getReportDataWithJdbc(groupId, studentId, prevPeriodId);

            if (prevPeriodData == null || prevPeriodData.isEmpty()) {
                continue;
            }

            // Mapear notas de periodo anterior a materias existentes
            for (GradeReportView prevData : prevPeriodData) {
                for (SubjectReportDTO subject : studentReport.getSubjects()) {
                    if (subject.getSubjectId().equals(prevData.getSubjectId())) {
                        // Crear la estructura si no existe
                        if (subject.getPeriodScores() == null) {
                            subject.setPeriodScores(new ArrayList<>());
                        }

                        // Crear el objeto de puntaje de periodo
                        Map<String, Object> periodScore = new HashMap<>();
                        periodScore.put("period_number", currentPrevPeriodNumber);
                        periodScore.put("period_name", "Periodo " + currentPrevPeriodNumber);
                        periodScore.put("score", prevData.getTotalScore());

                        // Agregar a la lista si no existe ya
                        boolean exists = false;
                        for (Map<String, Object> ps : subject.getPeriodScores()) {
                            if (ps.get("period_number").equals(currentPrevPeriodNumber)) {
                                exists = true;
                                break;
                            }
                        }

                        if (!exists) {
                            subject.getPeriodScores().add(periodScore);
                        }

                        break;
                    }
                }
            }
        }
    }
    // Método simple para procesar datos del estudiante (compatible con versiones anteriores)
    private StudentReportDTO processSimpleStudentReportData(List<GradeReportView> reportData) {
        if (reportData.isEmpty()) {
            return null;
        }

        GradeReportView firstData = reportData.get(0);
        StudentReportDTO studentReport = new StudentReportDTO();
        studentReport.setStudentId(firstData.getStudentId());
        studentReport.setStudentName(firstData.getStudentName());
        studentReport.setGroupName(firstData.getGroupName());
        studentReport.setGroupCode(firstData.getGroupCode());
        studentReport.setPeriodName(firstData.getPeriodName());

        // Extraer información adicional del grupo
        String[] groupParts = studentReport.getGroupName().split(" ");
        if (groupParts.length > 0) {
            studentReport.setGrade(groupParts[0]);
            studentReport.setJornada("Mañana"); // Por defecto
        }
        studentReport.setNivelEducacion("Primaria"); // Por defecto

        for (GradeReportView data : reportData) {
            processSubjectAndKnowledge(studentReport, data);
        }

        return studentReport;
    }

    // Método para procesar los datos del reporte con información detallada
    private StudentReportDTO processStudentReportData(
            List<GradeReportView> reportData,
            Object[] studentDetails,
            Map<Long, String> teachersBySubject) {

        StudentReportDTO studentReport = new StudentReportDTO();

        // Configurar datos del estudiante
        studentReport.setStudentId(((Number) studentDetails[0]).longValue());
        studentReport.setStudentName((String) studentDetails[1] + " " + (String) studentDetails[2]);
        studentReport.setDocumentNumber((String) studentDetails[3]);
        studentReport.setDocumentType((String) studentDetails[4]);
        studentReport.setGroupName((String) studentDetails[5]);
        studentReport.setGroupCode((String) studentDetails[6]);
        studentReport.setPeriodName((String) studentDetails[7]);
        studentReport.setAcademicYear((String) studentDetails[8]);

        // Extraer información adicional del grupo
        String[] groupParts = studentReport.getGroupName().split(" ");
        if (groupParts.length > 0) {
            studentReport.setGrade(groupParts[0]);
            if (groupParts.length > 1) {
                studentReport.setJornada("Mañana"); // Por defecto
            }
        }
        studentReport.setNivelEducacion("Primaria"); // Por defecto
        log.info("Datos obtenidos de la BD: {}", reportData.size());
        // Procesar materias y conocimientos
        for (GradeReportView data : reportData) {
            // Buscar o crear la materia
            log.info("Materia: {}, Saber: {}", data.getSubjectName(), data.getKnowledgeName());

            SubjectReportDTO subjectReport = studentReport.getSubjects().stream()
                    .filter(s -> s.getSubjectId().equals(data.getSubjectId()))
                    .findFirst()
                    .orElseGet(() -> {
                        SubjectReportDTO newSubject = new SubjectReportDTO();
                        newSubject.setSubjectId(data.getSubjectId());
                        newSubject.setSubjectName(data.getSubjectName());
                        newSubject.setTotalScore(data.getTotalScore());
                        newSubject.setRecovered(Boolean.valueOf(data.getRecovered()));
                        newSubject.setComment(data.getComment());
                        newSubject.setTeacherName(teachersBySubject.getOrDefault(data.getSubjectId(), ""));
                        newSubject.setArea(data.getSubjectName()); // Por defecto

                        // Calcular desempeño
                        calculatePerformance(newSubject);

                        studentReport.getSubjects().add(newSubject);
                        return newSubject;
                    });

            // Verificar si el conocimiento ya existe
            boolean knowledgeExists = subjectReport.getKnowledges().stream()
                    .anyMatch(k -> k.getKnowledgeId().equals(data.getKnowledgeId()));

            if (!knowledgeExists) {
                KnowledgeReportDTO knowledgeReport = new KnowledgeReportDTO();
                knowledgeReport.setKnowledgeId(data.getKnowledgeId());
                knowledgeReport.setKnowledgeName(data.getKnowledgeName());
                knowledgeReport.setPercentage(data.getKnowledgePercentage());
                knowledgeReport.setAchievement(data.getAchievement());

                // Usar la nota real que viene de la base de datos
                knowledgeReport.setScore(data.getScore());

                // Usar la nota definitiva de la base de datos o calcularla
                if (data.getDefinitivaScore() != null) {
                    knowledgeReport.setDefinitivaScore(data.getDefinitivaScore());
                } else {
                    BigDecimal percentage = new BigDecimal(knowledgeReport.getPercentage()).divide(new BigDecimal("100"));
                    knowledgeReport.setDefinitivaScore(knowledgeReport.getScore().multiply(percentage));
                }

                subjectReport.getKnowledges().add(knowledgeReport);
            }
        }

        return studentReport;
    }

    private ByteArrayResource createPdfReport(List<StudentReportDTO> reportData) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 36, 36, 54, 36);
            PdfWriter writer = PdfWriter.getInstance(document, baos);

            document.open();

            // Información del encabezado
            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Paragraph title = new Paragraph("Reporte Académico", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(10);
            document.add(title);

            // Agregar información del grupo y periodo si está disponible
            if (!reportData.isEmpty()) {
                Font infoFont = new Font(Font.HELVETICA, 12);
                StudentReportDTO firstStudent = reportData.get(0);

                Paragraph groupInfo = new Paragraph("Grupo: " + firstStudent.getGroupName(), infoFont);
                groupInfo.setAlignment(Element.ALIGN_CENTER);
                document.add(groupInfo);

                Paragraph periodInfo = new Paragraph("Periodo: " + firstStudent.getPeriodName(), infoFont);
                periodInfo.setAlignment(Element.ALIGN_CENTER);
                periodInfo.setSpacingAfter(20);
                document.add(periodInfo);
            }

            // Crear tabla para los datos
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Definir anchos relativos de columnas
            float[] columnWidths = {3f, 1.5f, 2f, 3f, 3f, 1.5f, 1.5f};
            table.setWidths(columnWidths);

            // Agregar encabezados
            String[] headers = {"Estudiante", "Grupo", "Materia", "Conocimiento", "Logro", "Porcentaje", "Nota Final"};
            Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);

            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(Color.LIGHT_GRAY);
                cell.setPadding(5);
                table.addCell(cell);
            }

            // Agregar datos
            Font dataFont = new Font(Font.HELVETICA, 10);

            // Usamos un contador para alternar el color de fondo
            int rowCount = 0;

            for (StudentReportDTO student : reportData) {
                for (SubjectReportDTO subject : student.getSubjects()) {
                    // Verificar que haya conocimientos
                    if (subject.getKnowledges() == null || subject.getKnowledges().isEmpty()) {
                        log.warn("La materia {} no tiene conocimientos asociados", subject.getSubjectName());
                        continue;
                    }

                    // Ordenar los conocimientos por ID para mantener un orden consistente
                    List<KnowledgeReportDTO> sortedKnowledges = subject.getKnowledges().stream()
                            .sorted((k1, k2) -> k1.getKnowledgeId().compareTo(k2.getKnowledgeId()))
                            .collect(Collectors.toList());

                    for (KnowledgeReportDTO knowledge : sortedKnowledges) {
                        // Alternar color de fondo para mejor legibilidad
                        Color backgroundColor = (rowCount % 2 == 0) ? Color.WHITE : new Color(240, 240, 240);
                        rowCount++;

                        // Estudiante
                        PdfPCell studentCell = new PdfPCell(new Phrase(student.getStudentName(), dataFont));
                        studentCell.setBackgroundColor(backgroundColor);
                        studentCell.setPadding(5);
                        table.addCell(studentCell);

                        // Grupo
                        PdfPCell groupCell = new PdfPCell(new Phrase(student.getGroupCode(), dataFont));
                        groupCell.setBackgroundColor(backgroundColor);
                        groupCell.setPadding(5);
                        table.addCell(groupCell);

                        // Materia
                        PdfPCell subjectCell = new PdfPCell(new Phrase(subject.getSubjectName(), dataFont));
                        subjectCell.setBackgroundColor(backgroundColor);
                        subjectCell.setPadding(5);
                        table.addCell(subjectCell);

                        // Conocimiento
                        PdfPCell knowledgeCell = new PdfPCell(new Phrase(knowledge.getKnowledgeName(), dataFont));
                        knowledgeCell.setBackgroundColor(backgroundColor);
                        knowledgeCell.setPadding(5);
                        table.addCell(knowledgeCell);

                        // Logro
                        PdfPCell achievementCell = new PdfPCell(new Phrase(knowledge.getAchievement(), dataFont));
                        achievementCell.setBackgroundColor(backgroundColor);
                        achievementCell.setPadding(5);
                        table.addCell(achievementCell);

                        // Porcentaje
                        PdfPCell percentageCell = new PdfPCell(new Phrase(knowledge.getPercentage() + "%", dataFont));
                        percentageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        percentageCell.setBackgroundColor(backgroundColor);
                        percentageCell.setPadding(5);
                        table.addCell(percentageCell);

                        // Nota final
                        PdfPCell scoreCell = new PdfPCell(new Phrase(subject.getTotalScore().toString(), dataFont));
                        scoreCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        scoreCell.setBackgroundColor(backgroundColor);
                        scoreCell.setPadding(5);
                        table.addCell(scoreCell);
                    }
                }
            }

            document.add(table);
            document.close();

            return new ByteArrayResource(baos.toByteArray());
        }
    }

    // Método para crear el PDF detallado
    private ByteArrayResource createDetailedPdfReport(StudentReportDTO studentReport) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 36, 36, 54, 36);
            PdfWriter writer = PdfWriter.getInstance(document, baos);

            document.open();

            // Agregar encabezado institucional
            addInstitutionalHeader(document);

            // Agregar información del estudiante
            addStudentInfo(document, studentReport);


            for (SubjectReportDTO subject : studentReport.getSubjects()) {
                // Ordenamos los conocimientos por ID para mostrarlos en orden consistente
                subject.setKnowledges(subject.getKnowledges().stream()
                        .sorted(Comparator.comparing(KnowledgeReportDTO::getKnowledgeId))
                        .collect(Collectors.toList()));

                addSubjectInfo(document, subject, studentReport);
            }

            // Para cada materia
            for (SubjectReportDTO subject : studentReport.getSubjects()) {
                addSubjectInfo(document, subject, studentReport);
            }

            document.close();

            return new ByteArrayResource(baos.toByteArray());
        }
    }

    // Método para agregar el encabezado institucional
    private void addInstitutionalHeader(Document document) throws DocumentException {
        // Crear tabla para el encabezado
        PdfPTable headerTable = new PdfPTable(3);
        headerTable.setWidthPercentage(100);

        try {
            headerTable.setWidths(new float[] {1, 2, 1});
        } catch (DocumentException e) {
            log.error("Error al configurar anchos de tabla: {}", e.getMessage());
        }

        // Celda para el logo (se podría agregar una imagen)
        PdfPCell logoCell = new PdfPCell(new Phrase("Gimnasio Loris Malaguzzi"));
        logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        logoCell.setBorder(Rectangle.BOX);
        logoCell.setPadding(10);
        headerTable.addCell(logoCell);

        // Celda para el título
        PdfPCell titleCell = new PdfPCell();
        titleCell.setBorder(Rectangle.BOX);

        Paragraph systemTitle = new Paragraph("SISTEMA DE GESTION DE CALIDAD",
                new Font(Font.HELVETICA, 12, Font.BOLD));
        systemTitle.setAlignment(Element.ALIGN_CENTER);

        Paragraph reportTitle = new Paragraph("INFORME ACADÉMICO",
                new Font(Font.HELVETICA, 12));
        reportTitle.setAlignment(Element.ALIGN_CENTER);

        titleCell.addElement(systemTitle);
        titleCell.addElement(reportTitle);
        titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        titleCell.setPadding(10);
        headerTable.addCell(titleCell);

        // Celda para el código
        PdfPCell codeCell = new PdfPCell();
        codeCell.setBorder(Rectangle.BOX);

        Paragraph codeText = new Paragraph("Código: GEC11-P02-F03",
                new Font(Font.HELVETICA, 10));
        codeText.setAlignment(Element.ALIGN_LEFT);

        Paragraph versionText = new Paragraph("Versión: 3.0",
                new Font(Font.HELVETICA, 10));
        versionText.setAlignment(Element.ALIGN_LEFT);

        Paragraph dateText = new Paragraph("Actualización: marzo 30 de 2021",
                new Font(Font.HELVETICA, 10));
        dateText.setAlignment(Element.ALIGN_LEFT);

        codeCell.addElement(codeText);
        codeCell.addElement(versionText);
        codeCell.addElement(dateText);
        codeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        codeCell.setPadding(10);
        headerTable.addCell(codeCell);

        document.add(headerTable);

        // Título del informe
        Paragraph reportHeader = new Paragraph("INFORME VALORATIVO DEL RENDIMIENTO ACADÉMICO",
                new Font(Font.HELVETICA, 12, Font.BOLD));
        reportHeader.setAlignment(Element.ALIGN_CENTER);
        reportHeader.setSpacingBefore(10);
        reportHeader.setSpacingAfter(10);
        document.add(reportHeader);
    }

    // Método para agregar información del estudiante
    private void addStudentInfo(Document document, StudentReportDTO studentReport) throws DocumentException {
        // Tabla para información del estudiante
        PdfPTable studentTable = new PdfPTable(4);
        studentTable.setWidthPercentage(100);

        try {
            studentTable.setWidths(new float[] {1, 3, 2, 1});
        } catch (DocumentException e) {
            log.error("Error al configurar anchos de tabla: {}", e.getMessage());
        }

        // Fila 1: Estudiante y Documento
        PdfPCell studentLabelCell = new PdfPCell(new Phrase("ESTUDIANTE:",
                new Font(Font.HELVETICA, 10, Font.BOLD)));
        studentLabelCell.setBorder(Rectangle.BOX);
        studentLabelCell.setPadding(5);
        studentTable.addCell(studentLabelCell);

        PdfPCell studentNameCell = new PdfPCell(new Phrase(studentReport.getStudentName(),
                new Font(Font.HELVETICA, 10)));
        studentNameCell.setBorder(Rectangle.BOX);
        studentNameCell.setPadding(5);
        studentTable.addCell(studentNameCell);

        PdfPCell docLabelCell = new PdfPCell(new Phrase("DOCUMENTO DE IDENTIDAD:",
                new Font(Font.HELVETICA, 10, Font.BOLD)));
        docLabelCell.setBorder(Rectangle.BOX);
        docLabelCell.setPadding(5);
        studentTable.addCell(docLabelCell);

        PdfPCell docNumberCell = new PdfPCell(new Phrase(studentReport.getDocumentNumber(),
                new Font(Font.HELVETICA, 10)));
        docNumberCell.setBorder(Rectangle.BOX);
        docNumberCell.setPadding(5);
        studentTable.addCell(docNumberCell);

        // Fila 2: Año, Grado, Jornada, Periodo, Nivel
        PdfPCell yearLabelCell = new PdfPCell(new Phrase("AÑO",
                new Font(Font.HELVETICA, 10, Font.BOLD)));
        yearLabelCell.setBorder(Rectangle.BOX);
        yearLabelCell.setPadding(5);
        studentTable.addCell(yearLabelCell);

        PdfPCell gradeLabelCell = new PdfPCell(new Phrase("GRADO",
                new Font(Font.HELVETICA, 10, Font.BOLD)));
        gradeLabelCell.setBorder(Rectangle.BOX);
        gradeLabelCell.setPadding(5);
        studentTable.addCell(gradeLabelCell);

        PdfPCell jornadaLabelCell = new PdfPCell(new Phrase("JORNADA",
                new Font(Font.HELVETICA, 10, Font.BOLD)));
        jornadaLabelCell.setBorder(Rectangle.BOX);
        jornadaLabelCell.setPadding(5);
        studentTable.addCell(jornadaLabelCell);

        PdfPCell periodLabelCell = new PdfPCell(new Phrase("PERIODO",
                new Font(Font.HELVETICA, 10, Font.BOLD)));
        periodLabelCell.setBorder(Rectangle.BOX);
        periodLabelCell.setPadding(5);
        studentTable.addCell(periodLabelCell);

        // Fila 3: Valores
        PdfPCell yearValueCell = new PdfPCell(new Phrase(studentReport.getAcademicYear(),
                new Font(Font.HELVETICA, 10)));
        yearValueCell.setBorder(Rectangle.BOX);
        yearValueCell.setPadding(5);
        studentTable.addCell(yearValueCell);

        PdfPCell gradeValueCell = new PdfPCell(new Phrase(studentReport.getGrade() + " " + studentReport.getGroupCode(),
                new Font(Font.HELVETICA, 10)));
        gradeValueCell.setBorder(Rectangle.BOX);
        gradeValueCell.setPadding(5);
        studentTable.addCell(gradeValueCell);

        PdfPCell jornadaValueCell = new PdfPCell(new Phrase(studentReport.getJornada(),
                new Font(Font.HELVETICA, 10)));
        jornadaValueCell.setBorder(Rectangle.BOX);
        jornadaValueCell.setPadding(5);
        studentTable.addCell(jornadaValueCell);

        PdfPCell periodValueCell = new PdfPCell(new Phrase(studentReport.getPeriodName(),
                new Font(Font.HELVETICA, 10)));
        periodValueCell.setBorder(Rectangle.BOX);
        periodValueCell.setPadding(5);
        studentTable.addCell(periodValueCell);

        document.add(studentTable);
        document.add(new Paragraph(" ")); // Espacio
    }

    // Método para agregar información de una materia
    private void addSubjectInfo(Document document, SubjectReportDTO subject, StudentReportDTO studentReport) throws DocumentException {
        // Definir formato para números decimales (solo 1 decimal)
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.0");

        // Tabla para el docente
        PdfPTable teacherTable = new PdfPTable(1);
        teacherTable.setWidthPercentage(100);

        PdfPCell teacherLabelCell = new PdfPCell(new Phrase("DOCENTE",
                new Font(Font.HELVETICA, 10, Font.BOLD)));
        teacherLabelCell.setBorder(Rectangle.BOX);
        teacherLabelCell.setPadding(5);
        teacherTable.addCell(teacherLabelCell);

        // El docente nunca debería ser "No asignado"
        PdfPCell teacherNameCell = new PdfPCell(new Phrase(subject.getTeacherName(),
                new Font(Font.HELVETICA, 10)));
        teacherNameCell.setBorder(Rectangle.BOX);
        teacherNameCell.setPadding(5);
        teacherTable.addCell(teacherNameCell);

        document.add(teacherTable);

        // Tabla para el área y asistencias
        PdfPTable areaTable = new PdfPTable(6);
        areaTable.setWidthPercentage(100);

        try {
            areaTable.setWidths(new float[] {2, 1, 1, 1, 1, 2});
        } catch (DocumentException e) {
            log.error("Error al configurar anchos de tabla: {}", e.getMessage());
        }

        // Encabezado de área
        PdfPCell areaLabelCell = new PdfPCell(new Phrase("AREA",
                new Font(Font.HELVETICA, 10, Font.BOLD)));
        areaLabelCell.setBorder(Rectangle.BOX);
        areaLabelCell.setPadding(5);
        areaTable.addCell(areaLabelCell);

        PdfPCell inasistenciasLabelCell = new PdfPCell(new Phrase("INASISTENCIAS",
                new Font(Font.HELVETICA, 10, Font.BOLD)));
        inasistenciasLabelCell.setBorder(Rectangle.BOX);
        inasistenciasLabelCell.setPadding(5);
        areaTable.addCell(inasistenciasLabelCell);

        PdfPCell p1LabelCell = new PdfPCell(new Phrase("1P",
                new Font(Font.HELVETICA, 10, Font.BOLD)));
        p1LabelCell.setBorder(Rectangle.BOX);
        p1LabelCell.setPadding(5);
        areaTable.addCell(p1LabelCell);

        PdfPCell p2LabelCell = new PdfPCell(new Phrase("2P",
                new Font(Font.HELVETICA, 10, Font.BOLD)));
        p2LabelCell.setBorder(Rectangle.BOX);
        p2LabelCell.setPadding(5);
        areaTable.addCell(p2LabelCell);

        PdfPCell p3LabelCell = new PdfPCell(new Phrase("3P",
                new Font(Font.HELVETICA, 10, Font.BOLD)));
        p3LabelCell.setBorder(Rectangle.BOX);
        p3LabelCell.setPadding(5);
        areaTable.addCell(p3LabelCell);

        PdfPCell p4LabelCell = new PdfPCell(new Phrase("DESEMPEÑO",
                new Font(Font.HELVETICA, 10, Font.BOLD)));
        p4LabelCell.setBorder(Rectangle.BOX);
        p4LabelCell.setPadding(5);
        areaTable.addCell(p4LabelCell);

        // Valores de área
        PdfPCell areaNameCell = new PdfPCell(new Phrase(subject.getSubjectName().toUpperCase(),
                new Font(Font.HELVETICA, 10, Font.BOLD)));
        areaNameCell.setBorder(Rectangle.BOX);
        areaNameCell.setPadding(5);
        areaTable.addCell(areaNameCell);

        PdfPCell inasistenciasValueCell = new PdfPCell(new Phrase(studentReport.getInasistencias() != null ?
                studentReport.getInasistencias().toString() : "0",
                new Font(Font.HELVETICA, 10)));
        inasistenciasValueCell.setBorder(Rectangle.BOX);
        inasistenciasValueCell.setPadding(5);
        areaTable.addCell(inasistenciasValueCell);

        // Obtener el número de periodo actual
        int currentPeriodNumber = 0;
        try {
            currentPeriodNumber = Integer.parseInt(studentReport.getPeriodName().replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            log.warn("No se pudo extraer el número de periodo de {}", studentReport.getPeriodName());
        }

        // Inicializar celdas para los 3 periodos
        PdfPCell[] periodCells = new PdfPCell[3];
        for (int i = 0; i < 3; i++) {
            periodCells[i] = new PdfPCell(new Phrase("", new Font(Font.HELVETICA, 10)));
            periodCells[i].setBorder(Rectangle.BOX);
            periodCells[i].setPadding(5);
        }

        // Para el periodo actual, siempre mostrar la nota actual
        if (currentPeriodNumber >= 1 && currentPeriodNumber <= 3) {
            periodCells[currentPeriodNumber - 1] = new PdfPCell(new Phrase(
                    df.format(subject.getTotalScore()),
                    new Font(Font.HELVETICA, 10)));
            periodCells[currentPeriodNumber - 1].setBorder(Rectangle.BOX);
            periodCells[currentPeriodNumber - 1].setPadding(5);
        }

        // Asignar notas de periodos anteriores si están disponibles
        // Para periodos anteriores, usar la información de periodScores
        if (subject.getPeriodScores() != null) {
            for (Map<String, Object> periodScore : subject.getPeriodScores()) {
                // Obtener número de periodo
                int periodNumber;
                Object periodNumberObj = periodScore.get("period_number");

                if (periodNumberObj instanceof String) {
                    String periodNumberStr = (String) periodNumberObj;
                    // Si es algo como "P1", extraer solo el número
                    if (periodNumberStr.startsWith("P")) {
                        periodNumberStr = periodNumberStr.substring(1);
                    }
                    periodNumber = Integer.parseInt(periodNumberStr);
                } else if (periodNumberObj instanceof Number) {
                    periodNumber = ((Number) periodNumberObj).intValue();
                } else {
                    continue; // No podemos determinar el número de periodo
                }

                // Si es un periodo anterior al actual y está en el rango 1-3
                if (periodNumber != currentPeriodNumber && periodNumber >= 1 && periodNumber <= 3) {
                    // Obtener la nota
                    Object scoreObj = periodScore.get("score");
                    BigDecimal score = null;

                    if (scoreObj instanceof BigDecimal) {
                        score = (BigDecimal) scoreObj;
                    } else if (scoreObj instanceof Number) {
                        score = new BigDecimal(((Number) scoreObj).toString());
                    } else if (scoreObj instanceof String) {
                        try {
                            score = new BigDecimal((String) scoreObj);
                        } catch (Exception e) {
                            continue;
                        }
                    }

                    // Asignar la nota formateada a la celda correspondiente
                    if (score != null) {
                        periodCells[periodNumber - 1] = new PdfPCell(new Phrase(
                                df.format(score), new Font(Font.HELVETICA, 10)));
                        periodCells[periodNumber - 1].setBorder(Rectangle.BOX);
                        periodCells[periodNumber - 1].setPadding(5);
                    }
                }
            }
        }

        // Para el periodo actual, asignar la nota total
        if (currentPeriodNumber >= 1 && currentPeriodNumber <= 3) {
            periodCells[currentPeriodNumber - 1] = new PdfPCell(new Phrase(
                    df.format(subject.getTotalScore()),
                    new Font(Font.HELVETICA, 10)));
            periodCells[currentPeriodNumber - 1].setBorder(Rectangle.BOX);
            periodCells[currentPeriodNumber - 1].setPadding(5);
        }

        // Agregar las celdas a la tabla
        areaTable.addCell(periodCells[0]); // 1P
        areaTable.addCell(periodCells[1]); // 2P
        areaTable.addCell(periodCells[2]); // 3P

        // Desempeño
        PdfPCell desempenoCell = new PdfPCell();
        desempenoCell.setBorder(Rectangle.BOX);
        desempenoCell.setPadding(5);

        Paragraph desempenoText = new Paragraph(subject.getDesempenio(),
                new Font(Font.HELVETICA, 10, Font.BOLD));
        desempenoText.setAlignment(Element.ALIGN_CENTER);

        desempenoCell.addElement(desempenoText);
        areaTable.addCell(desempenoCell);

        document.add(areaTable);

        // El resto del método permanece igual...
        // Tabla para conocimientos
        PdfPTable knowledgeTable = new PdfPTable(4);
        knowledgeTable.setWidthPercentage(100);

        try {
            knowledgeTable.setWidths(new float[] {1, 6, 1, 1});
        } catch (DocumentException e) {
            log.error("Error al configurar anchos de tabla: {}", e.getMessage());
        }

        // Para cada conocimiento
        for (KnowledgeReportDTO knowledge : subject.getKnowledges()) {
            // Nombre del conocimiento
            PdfPCell knowledgeNameCell = new PdfPCell(new Phrase(knowledge.getKnowledgeName(),
                    new Font(Font.HELVETICA, 10, Font.BOLD)));
            knowledgeNameCell.setBorder(Rectangle.BOX);
            knowledgeNameCell.setPadding(5);
            knowledgeTable.addCell(knowledgeNameCell);

            // Descripción del logro
            PdfPCell achievementCell = new PdfPCell(new Phrase(knowledge.getAchievement(),
                    new Font(Font.HELVETICA, 10)));
            achievementCell.setBorder(Rectangle.BOX);
            achievementCell.setPadding(5);
            knowledgeTable.addCell(achievementCell);

            // Nota parcial - FORMATO CON UN DECIMAL
            PdfPCell scoreCell = new PdfPCell(new Phrase(
                    df.format(knowledge.getScore()),
                    new Font(Font.HELVETICA, 10)));
            scoreCell.setBorder(Rectangle.BOX);
            scoreCell.setPadding(5);
            scoreCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            knowledgeTable.addCell(scoreCell);

            // Porcentaje
            PdfPCell percentageCell = new PdfPCell(new Phrase(knowledge.getPercentage() + "%",
                    new Font(Font.HELVETICA, 10)));
            percentageCell.setBorder(Rectangle.BOX);
            percentageCell.setPadding(5);
            percentageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            knowledgeTable.addCell(percentageCell);
        }

        document.add(knowledgeTable);

        // Evaluación general
        PdfPTable evaluationTable = new PdfPTable(2);
        evaluationTable.setWidthPercentage(100);

        try {
            evaluationTable.setWidths(new float[] {2, 6});
        } catch (DocumentException e) {
            log.error("Error al configurar anchos de tabla: {}", e.getMessage());
        }

        PdfPCell evalLabelCell = new PdfPCell(new Phrase("EVALUACIÓN GENERAL",
                new Font(Font.HELVETICA, 10, Font.BOLD)));
        evalLabelCell.setBorder(Rectangle.BOX);
        evalLabelCell.setPadding(5);
        //evaluationTable.addCell(evalLabelCell);

        PdfPCell commentCell = new PdfPCell(new Phrase(subject.getComment() != null ? subject.getComment() : "Evaluación general.",
                new Font(Font.HELVETICA, 10)));
        commentCell.setBorder(Rectangle.BOX);
        commentCell.setPadding(5);
        evaluationTable.addCell(commentCell);

        document.add(evaluationTable);
        document.add(new Paragraph(" ")); // Espacio entre materias
    }

}

