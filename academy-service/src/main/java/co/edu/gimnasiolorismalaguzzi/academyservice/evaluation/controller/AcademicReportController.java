package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.StudentReportDTO;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.GradeReportAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/api/academy/reports")
public class AcademicReportController {

    private final GradeReportAdapter reportService;

    @Autowired
    public AcademicReportController(GradeReportAdapter reportService) {
        this.reportService = reportService;
    }

/*
    @GetMapping("/group/{groupId}/period/{periodId}")
    public ResponseEntity<List<StudentReportDTO>> getGroupReport(
            @PathVariable Long groupId,
            @PathVariable Long periodId) {
        List<StudentReportDTO> report = reportService.generateGroupReport(groupId, periodId);
        return ResponseEntity.ok(report);
    }
*/

    @GetMapping("/group/{groupId}/student/{studentId}/period/{periodId}")
    public ResponseEntity<StudentReportDTO> getStudentReport(
            @PathVariable Long groupId,
            @PathVariable Long studentId,
            @PathVariable Long periodId) {
        StudentReportDTO report = reportService.generateStudentReport(groupId, studentId, periodId);
        if (report == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(report);
    }

/*    @GetMapping("/pdf/group/{groupId}/period/{periodId}")
    public ResponseEntity<Resource> getGroupReportPdf(@PathVariable Long groupId, @PathVariable Long periodId) throws IOException {
        ByteArrayResource resource = reportService.generatePdfReport(groupId, periodId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_grupo.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }*/

    @GetMapping("/pdf/group/{groupId}/period/{periodId}")
    public ResponseEntity<Resource> getGroupReportPdf(@PathVariable Long groupId, @PathVariable Long periodId) throws IOException {
        // Obtener la lista de reportes por estudiante
        Map<Long, ByteArrayResource> studentReports = reportService.generateMultipleStudentReports(groupId, periodId);

        if (studentReports.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Crear un archivo ZIP con todos los PDFs
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(baos);

        for (Map.Entry<Long, ByteArrayResource> entry : studentReports.entrySet()) {
            Long studentId = entry.getKey();
            ByteArrayResource pdfResource = entry.getValue();

            ZipEntry zipEntry = new ZipEntry("boletin_estudiante_" + studentId + "_periodo_" + periodId + ".pdf");
            zipOut.putNextEntry(zipEntry);
            zipOut.write(pdfResource.getByteArray());
            zipOut.closeEntry();
        }

        zipOut.close();

        ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=boletines_grupo_" + groupId + "_periodo_" + periodId + ".zip")
                .body(resource);
    }

    @GetMapping("/view/group/{groupId}/student/{studentId}/period/{periodId}")
    public ResponseEntity<Resource> viewStudentReport(
            @PathVariable Long studentId,
            @PathVariable Long groupId,
            @PathVariable Long periodId) throws IOException {

        ByteArrayResource resource = reportService.generateStudentPdfReport(groupId, studentId, periodId);

        if (resource == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=boletin_estudiante_" + studentId + "_periodo_" + periodId + ".pdf")
                .body(resource);
    }

    @GetMapping("/view/group/{groupId}/period/{periodId}")
    public ResponseEntity<Resource> viewGroupReport(
            @PathVariable Long groupId,
            @PathVariable Long periodId) throws IOException {

        // Obtener la lista de reportes por estudiante
        Map<Long, ByteArrayResource> studentReports = reportService.generateMultipleStudentReports(groupId, periodId);

        if (studentReports.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Crear un archivo ZIP con todos los PDFs
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(baos);

        for (Map.Entry<Long, ByteArrayResource> entry : studentReports.entrySet()) {
            Long studentId = entry.getKey();
            ByteArrayResource pdfResource = entry.getValue();

            ZipEntry zipEntry = new ZipEntry("boletin_estudiante_" + studentId + "_periodo_" + periodId + ".pdf");
            zipOut.putNextEntry(zipEntry);
            zipOut.write(pdfResource.getByteArray());
            zipOut.closeEntry();
        }

        zipOut.close();

        ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=boletines_grupo_" + groupId + "_periodo_" + periodId + ".zip")
                .body(resource);
    }


    @GetMapping("/pdf/group/{groupId}/period/{periodId}/students")
    public ResponseEntity<Resource> getSelectedStudentsReportPdf(
            @PathVariable Long groupId,
            @PathVariable Long periodId,
            @RequestParam List<Long> studentIds) throws IOException {

        // Obtener reportes solo para los estudiantes seleccionados
        Map<Long, ByteArrayResource> studentReports = reportService.generateMultipleSelectedStudentReports(groupId, periodId, studentIds);

        if (studentReports.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Crear un archivo ZIP con los PDFs seleccionados
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(baos);

        for (Map.Entry<Long, ByteArrayResource> entry : studentReports.entrySet()) {
            Long studentId = entry.getKey();
            ByteArrayResource pdfResource = entry.getValue();

            ZipEntry zipEntry = new ZipEntry("boletin_estudiante_" + studentId + "_periodo_" + periodId + ".pdf");
            zipOut.putNextEntry(zipEntry);
            zipOut.write(pdfResource.getByteArray());
            zipOut.closeEntry();
        }

        zipOut.close();

        ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=boletines_seleccionados_grupo_" + groupId + "_periodo_" + periodId + ".zip")
                .body(resource);
    }



    @GetMapping("/pdf/group/{groupId}/student/{studentId}/period/{periodId}")
    public ResponseEntity<Resource> getStudentReportPdf(@PathVariable Long groupId, @PathVariable Long studentId, @PathVariable Long periodId) throws IOException {
        ByteArrayResource resource = reportService.generateStudentPdfReport(groupId, studentId, periodId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_estudiante.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    // Endpoint para visualización en línea (sin descarga)
    @GetMapping("/group/{groupId}/period/{periodId}")
    public ResponseEntity<ByteArrayResource> viewGroupReportPdf(
            @PathVariable Long groupId,
            @PathVariable Long periodId) throws IOException {

        // Obtener la lista de reportes por estudiante
        Map<Long, ByteArrayResource> studentReports = reportService.generateMultipleStudentReports(groupId, periodId);

        if (studentReports.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Si solo hay un estudiante, devolver su PDF directamente
        if (studentReports.size() == 1) {
            ByteArrayResource resource = studentReports.values().iterator().next();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
                    .body(resource);
        }

        // Si hay múltiples estudiantes, crear un archivo ZIP con todos los PDFs
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zipOut = new ZipOutputStream(baos);

            for (Map.Entry<Long, ByteArrayResource> entry : studentReports.entrySet()) {
                Long studentId = entry.getKey();
                ByteArrayResource pdfResource = entry.getValue();

                ZipEntry zipEntry = new ZipEntry("student_" + studentId + ".pdf");
                zipOut.putNextEntry(zipEntry);
                zipOut.write(pdfResource.getByteArray());
                zipOut.closeEntry();
            }

            zipOut.close();

            ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=group_reports.zip")
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Mantener el endpoint existente para reportes individuales
    @GetMapping("/student/{studentId}/period/{periodId}")
    public ResponseEntity<ByteArrayResource> viewStudentReportPdf(
            @PathVariable Long studentId,
            @PathVariable Long periodId) throws IOException {
        ByteArrayResource resource = reportService.generatePdfReport(studentId, periodId);

        if (resource == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=student_" + studentId + "_report.pdf")
                .body(resource);
    }


    @GetMapping("/excel/group/{groupId}/period/{periodId}")
    public ResponseEntity<Resource> getGroupReportExcel(
            @PathVariable Long groupId,
            @PathVariable Long periodId) throws IOException {
        ByteArrayResource resource = reportService.generateExcelReport(groupId, periodId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_grupo.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(resource);
    }

    @GetMapping("/excel/group/{groupId}/student/{studentId}/period/{periodId}")
    public ResponseEntity<Resource> getStudentReportExcel(
            @PathVariable Long groupId,
            @PathVariable Long studentId,
            @PathVariable Long periodId) throws IOException {
        ByteArrayResource resource = reportService.generateStudentExcelReport(groupId, studentId, periodId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_estudiante.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(resource);
    }
}
