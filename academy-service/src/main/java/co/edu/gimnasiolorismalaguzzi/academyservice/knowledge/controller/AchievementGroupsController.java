package co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.domain.AchievementGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.service.persistence.PersistenceAchievementGroups;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/achievements-group")
public class AchievementGroupsController {
    private final PersistenceAchievementGroups achievementGroups;

    public AchievementGroupsController(PersistenceAchievementGroups achievementGroups) {
        this.achievementGroups = achievementGroups;
    }

    @GetMapping
    public ResponseEntity<List<AchievementGroupDomain>> getAllAchievements(){
        List<AchievementGroupDomain> achievementGroupDomains = achievementGroups.findAll();
        return ResponseEntity.ok(achievementGroupDomains);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AchievementGroupDomain> getAchievementById(@PathVariable Integer id){
        AchievementGroupDomain achievementGroupDomain = achievementGroups.findById(id);
        return ResponseEntity.ok(achievementGroupDomain);
    }

    @GetMapping("/subjects/{id}/groups/{id2}")
    public ResponseEntity<List<AchievementGroupDomain>> getKnowledgeAchievementBySubjectId(@PathVariable Integer id, @PathVariable Integer id2){
        List<AchievementGroupDomain> achievementGroupDomains = achievementGroups.getKnowledgeAchievementBySubjectId(id, id2);
        return ResponseEntity.ok(achievementGroupDomains);
    }

    @PostMapping
    public ResponseEntity<AchievementGroupDomain> createAchievement(@RequestBody AchievementGroupDomain achievementGroupDomain){
        AchievementGroupDomain createdAchievement = achievementGroups.save(achievementGroupDomain);
        return ResponseEntity.ok(createdAchievement);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AchievementGroupDomain> updateDimension(@PathVariable Integer id, @RequestBody AchievementGroupDomain achievementGroupDomain){
        AchievementGroupDomain updatedAchievement = achievementGroups.update(id,achievementGroupDomain);
        return ResponseEntity.ok(updatedAchievement);
    }
}
