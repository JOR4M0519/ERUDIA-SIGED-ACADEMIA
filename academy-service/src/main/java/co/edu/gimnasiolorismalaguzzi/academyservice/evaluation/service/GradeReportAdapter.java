package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectGrade;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectGradeCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.GradeDistributionDTO;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.persistence.PersistenceGradeReportService;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.GroupsAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.GroupStudentsAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@PersistenceAdapter
@Slf4j
public class GradeReportAdapter implements PersistenceGradeReportService {


    private GroupsAdapter groupsAdapter;


    private GroupStudentsAdapter groupStudentsAdapter;

    private SubjectGradeCrudRepo subjectGradeCrudRepo;

    public GradeReportAdapter(GroupsAdapter groupsAdapter, GroupStudentsAdapter groupStudentsAdapter, SubjectGradeCrudRepo subjectGradeCrudRepo) {
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


}
