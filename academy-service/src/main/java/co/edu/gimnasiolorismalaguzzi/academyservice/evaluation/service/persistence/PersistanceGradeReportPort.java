package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.StudentReportDTO;
import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;
import java.util.List;

public interface PersistanceGradeReportPort {
    List<StudentReportDTO> generateGroupReport(Long groupId, Long periodId);
    StudentReportDTO generateStudentReport(Long groupId, Long studentId, Long periodId);
    ByteArrayResource generateExcelReport(Long groupId, Long periodId) throws IOException;
    ByteArrayResource generateStudentExcelReport(Long groupId, Long studentId, Long periodId) throws IOException;

    ByteArrayResource generatePdfReport(Long groupId, Long periodId) throws IOException;
    ByteArrayResource generateStudentPdfReport(Long groupId, Long studentId, Long periodId) throws IOException;
}
