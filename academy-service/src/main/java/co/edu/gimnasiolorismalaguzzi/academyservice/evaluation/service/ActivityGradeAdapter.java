package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.SubjectGradeAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceAcademicPeriodPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.UserMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.ActivityDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.ActivityGradeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.ActivityGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.ActivityGrade;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.ActivityGroup;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.mapper.ActivityGradeMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.repository.ActivityGradeCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.persistence.PersistanceActivityGradePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.persistence.PersistenceActivityGroupPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.persistence.PersistenceActivityPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.domain.AchievementGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.service.persistence.PersistenceAchievementGroups;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence.PersistenceGroupStudentPort;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@PersistenceAdapter
@Slf4j
public class ActivityGradeAdapter implements PersistanceActivityGradePort {

    private ActivityGradeCrudRepo activityGradeCrudRepo;

    @Autowired
    private PersistenceGroupStudentPort groupStudentsPort;

    @Autowired
    private PersistenceActivityPort activityPort;

    @Autowired
    private PersistenceActivityGroupPort activityGroupPort;


    @Autowired
    private PersistenceAcademicPeriodPort academicPeriodPort;

    @Autowired
    private PersistenceAchievementGroups achievementGroupsPort;

    @Autowired
    private SubjectGradeAdapter subjectGradePort;

    @Autowired
    private ActivityGradeMapper activityGradeMapper;

    @Autowired
    private UserMapper userMapper;


    public ActivityGradeAdapter(ActivityGradeCrudRepo activityGradeCrudRepo, ActivityGradeMapper activityGradeMapper){
        this.activityGradeMapper = activityGradeMapper;
        this.activityGradeCrudRepo = activityGradeCrudRepo;
    }

    @Override
    public List<ActivityGradeDomain> findAll() {
        return activityGradeMapper.toDomains(activityGradeCrudRepo.findAll());
    }

    @Override
    public ActivityGradeDomain findById(Integer integer) {
        Optional<ActivityGrade> activityGroup = this.activityGradeCrudRepo.findById(integer);
        return activityGroup.map(activityGradeMapper::toDomain).orElse(null);
    }

    @Override
    public ActivityGradeDomain save(ActivityGradeDomain entity) {
        ActivityGrade activityGroup = activityGradeMapper.toEntity(entity);
        ActivityGrade savedActivityGroup = this.activityGradeCrudRepo.save(activityGroup);
        return activityGradeMapper.toDomain(savedActivityGroup);
    }

    @Transactional
    @Override
    public ActivityGradeDomain update(Integer integer, ActivityGradeDomain entity) {
        try{
            Optional<ActivityGrade> existingActivityGrade = activityGradeCrudRepo.findById(integer);
            ActivityGrade activityGrade = activityGradeMapper.toEntity(entity);
            if (existingActivityGrade.isPresent()) {
                ActivityGrade gradeToUpdate = existingActivityGrade.get();

                // Mapear y asignar el estudiante
                gradeToUpdate.setStudent(userMapper.toEntity(entity.getStudent()));

                // Buscar el ActivityGroup usando los IDs de Activity y Group
                ActivityGroup activityGroup = activityGroupPort.findByActivity_IdAndGroup_Id(
                        entity.getActivity().getActivity().getId(),
                        entity.getActivity().getGroup().getId()
                ).orElseThrow(() -> new EntityNotFoundException("ActivityGroup not found for activityId "
                        + entity.getActivity().getActivity().getId()
                        + " and groupId "
                        + entity.getActivity().getGroup().getId()));

                // Asignar el ActivityGroup al grade
                gradeToUpdate.setActivity(activityGroup);
                gradeToUpdate.setScore(entity.getScore());
                gradeToUpdate.setComment(entity.getComment());
            }

            ActivityGradeDomain updatedGrade = activityGradeMapper.toDomain(activityGradeCrudRepo.save(existingActivityGrade.get()));

            //Update SubjectGrade

            //Se toma el id del estudiantes
            Integer studentId = updatedGrade.getStudent().getId();

            //Se toma el id del grupo de estudiantes
            Integer groupId = entity.getActivity().getGroup().getId();

            /*        groupStudentsPort.getGroupsStudentById(entity.getStudent().getId(),"A")
                    .get(0).getGroup().getId();
*/
            //Se toma el id del academicPeriod
            Integer academicPeriodId = academicPeriodPort.getAllPeriodsByStatus("A")
                    .get(0).getId();

            // Obtener la lista de achievementGroups
            List<AchievementGroupDomain> achievementGroupDomainList =
                    achievementGroupsPort.getKnowledgeAchievementListByPeriodAndGroupId(academicPeriodId, groupId);

            // Agrupar los achievementGroups por subjectId
            Map<Integer, List<AchievementGroupDomain>> achievementsBySubject = new HashMap<>();

            for (AchievementGroupDomain achievement : achievementGroupDomainList) {
                Integer subjectId = achievement.getSubjectKnowledge().getIdSubject().getId();

                if (!achievementsBySubject.containsKey(subjectId)) {
                    achievementsBySubject.put(subjectId, new ArrayList<>());
                }

                achievementsBySubject.get(subjectId).add(achievement);
            }

            // Iterar por cada materia (subject)
            for (Map.Entry<Integer, List<AchievementGroupDomain>> entry : achievementsBySubject.entrySet()) {
                Integer subjectId = entry.getKey();
                List<AchievementGroupDomain> subjectAchievements = entry.getValue();

                double subjectFinalScore = 0.0;

                // Iterar por cada achievement (knowledge) de la materia
                for (AchievementGroupDomain achievement : subjectAchievements) {
                    List<ActivityDomain> activityDomainList = activityPort.getAllActivitiesByAchievementGroupId(achievement.getId());

                    if (!activityDomainList.isEmpty()) {
                        // Calcular el promedio de las actividades del knowledge
                        double knowledgeScore = 0.0;
                        int validActivitiesCount = 0; // Contador para actividades con calificación

                        for (ActivityDomain activity : activityDomainList) {
                            // Obtener la calificación y verificar que no sea nula
                            ActivityGradeDomain grade = getGradeByActivityIdByStudentId(activity.getId(), studentId);

                            if (grade != null && grade.getScore() != null) {
                                knowledgeScore += grade.getScore().doubleValue();
                                validActivitiesCount++; // Incrementar solo si hay calificación válida
                            }
                        }
                        // Calcular el promedio solo si hay actividades válidas
                        if (validActivitiesCount > 0) {
                            double knowledgeAverage = knowledgeScore / validActivitiesCount;

                            // Obtener el porcentaje del knowledge
                            Integer knowledgePercentage = achievement.getSubjectKnowledge().getIdKnowledge().getPercentage();

                            // Aplicar el porcentaje al promedio del knowledge y sumarlo al total de la materia
                            double weightedKnowledgeScore = knowledgeAverage * (knowledgePercentage / 100.0);
                            subjectFinalScore += weightedKnowledgeScore;
                        }
                    }
                }


                // Guardar o actualizar el score final de la materia
                BigDecimal finalScore = new BigDecimal(subjectFinalScore).setScale(2, RoundingMode.HALF_UP);
                subjectGradePort.saveOrUpdateSubjectGrade(studentId, subjectId, academicPeriodId, finalScore);
            }


            return updatedGrade;
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Relation Activity Grade with ID " + integer + " Not found!");
        }
    }

    @Override
    public HttpStatus delete(Integer integer) {
        return null;
    }


    @Override
    public List<ActivityGradeDomain> getGradeByActivityIdGroupId(Integer id, Integer groupId) {
        return this.activityGradeMapper.toDomains(activityGradeCrudRepo.findByActivityAndGroupId(id,groupId));
    }

    @Override
    public ActivityGradeDomain getGradeByActivityGroupIdByStudentId(Integer activityId, Integer studentId) {
        return this.activityGradeMapper.toDomain(activityGradeCrudRepo.findByActivity_IdAndStudent_Id(activityId,studentId));
    }

    @Override
    public ActivityGradeDomain getGradeByActivityIdByStudentId(Integer activityId, Integer studentId) {
        return this.activityGradeMapper.toDomain(activityGradeCrudRepo.findByStudent_IdAndActivity_Activity_Id(studentId,activityId));
    }

}
