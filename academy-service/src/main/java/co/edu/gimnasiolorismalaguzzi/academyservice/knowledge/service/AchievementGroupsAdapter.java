package co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.domain.AchievementGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.entity.AchievementGroup;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.mapper.AchievementGroupsMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.repository.AchievementGroupsCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.service.persistence.PersistenceAchievementGroups;
import com.netflix.discovery.converters.Auto;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@Slf4j
@PersistenceAdapter
public class AchievementGroupsAdapter implements PersistenceAchievementGroups {

    private final AchievementGroupsMapper achievementGroupsMapper;

    @Autowired
    private AchievementGroupsCrudRepo achievementGroupsCrudRepo;

    public AchievementGroupsAdapter(AchievementGroupsMapper achievementGroupsMapper) {
        this.achievementGroupsMapper = achievementGroupsMapper;
    }

    @Override
    public List<AchievementGroupDomain> findAll() {
        return this.achievementGroupsMapper.toDomains(achievementGroupsCrudRepo.findAll());
    }

    @Override
    public AchievementGroupDomain findById(Integer integer) {
        Optional<AchievementGroup> achievementGroupDomain = this.achievementGroupsCrudRepo.findById(integer);
        return achievementGroupDomain.map(achievementGroupsMapper::toDomains).orElse(null);
    }

    @Override
    public AchievementGroupDomain save(AchievementGroupDomain domain) {
        AchievementGroup achievementGroup = achievementGroupsMapper.toEntity(domain);
        AchievementGroup savedAchievement = this.achievementGroupsCrudRepo.save(achievementGroup);
        return achievementGroupsMapper.toDomains(savedAchievement);
    }

    @Override
    public AchievementGroupDomain update(Integer integer, AchievementGroupDomain domain) {
        try{
            Optional<AchievementGroup> existingAchievement = this.achievementGroupsCrudRepo.findById(integer);
            if(existingAchievement.isPresent()){
                existingAchievement.get().setAchievement(achievementGroupsMapper.toEntity(domain).getAchievement());
                existingAchievement.get().setGroup(achievementGroupsMapper.toEntity(domain).getGroup());
                existingAchievement.get().setPeriod(achievementGroupsMapper.toEntity(domain).getPeriod());
                existingAchievement.get().setKnowledge(achievementGroupsMapper.toEntity(domain).getKnowledge());
            }
            return achievementGroupsMapper.toDomains(achievementGroupsCrudRepo.save(existingAchievement.get()));
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Knowledge with ID " + integer + "not found!");
        }
    }

    @Override
    public HttpStatus delete(Integer integer) {
        //NO hay necesidad de borrar un achievement
        return HttpStatus.I_AM_A_TEAPOT;
    }
}
