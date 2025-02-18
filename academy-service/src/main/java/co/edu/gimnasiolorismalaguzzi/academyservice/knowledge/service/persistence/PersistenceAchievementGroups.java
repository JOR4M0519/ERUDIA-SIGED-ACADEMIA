package co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.domain.AchievementGroupDomain;

import java.util.List;

public interface PersistenceAchievementGroups extends PersistencePort<AchievementGroupDomain, Integer> {
    List<AchievementGroupDomain> getKnowledgeAchievementBySubjectId(Integer subjectId,Integer groupId,Integer periodId);
}
