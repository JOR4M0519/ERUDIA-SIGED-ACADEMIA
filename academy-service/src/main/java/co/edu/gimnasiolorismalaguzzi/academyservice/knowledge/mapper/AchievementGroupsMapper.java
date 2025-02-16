package co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.mapper;

import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.domain.AchievementGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.entity.AchievementGroup;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AchievementGroupsMapper {
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "subjectKnowledge", target = "subjectKnowledgeDomain"),
            @Mapping(source = "achievement", target = "achievement"),
            @Mapping(source = "group", target = "group"),
            @Mapping(source = "period", target = "period")
    })
    AchievementGroupDomain toDomains(AchievementGroup achievementGroup);

    @InheritInverseConfiguration
    AchievementGroup toEntity(AchievementGroupDomain achievementGroupDomain);

    List<AchievementGroupDomain> toDomains(List<AchievementGroup> achievementGroups);
    List<AchievementGroup> toEntities(List<AchievementGroupDomain> achievementGroupDomains);
}
