package co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain;

import lombok.Data;

@Data
public class GradeSettingDomain {
    private Integer id;
    private Integer levelId;
    private String name;
    private String description;
    private Integer minimumGrade;
    private Integer passGrade;
    private Integer maximumGrade;
}
