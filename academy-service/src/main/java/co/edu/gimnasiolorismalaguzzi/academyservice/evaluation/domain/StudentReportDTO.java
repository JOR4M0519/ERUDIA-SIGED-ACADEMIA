package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StudentReportDTO {
    private Long studentId;
    private String studentName;
    private String documentType;
    private String documentNumber;
    private String groupName;
    private String groupCode;
    private Long groupId;
    private String periodName;
    private Long periodId;
    private String academicYear;
    private String grade;
    private String jornada;
    private String nivelEducacion;
    private Integer inasistencias;
    private List<SubjectReportDTO> subjects = new ArrayList<>();
}
