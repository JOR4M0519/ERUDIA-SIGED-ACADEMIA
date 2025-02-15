package co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.domain.AchievementGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.domain.DimensionDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.service.persistence.PersistenceAchievementGroups;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/achievements-group")
public class AchievementGroupsController {
    private final PersistenceAchievementGroups port;

    public AchievementGroupsController(PersistenceAchievementGroups port) {
        this.port = port;
    }

    @GetMapping
    public ResponseEntity<List<AchievementGroupDomain>> getAllAchievements(){
        List<AchievementGroupDomain> achievementGroupDomains = port.findAll();
        return ResponseEntity.ok(achievementGroupDomains);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AchievementGroupDomain> getAchievementById(@PathVariable Integer id){
        AchievementGroupDomain achievementGroupDomain = port.findById(id);
        return ResponseEntity.ok(achievementGroupDomain);
    }

    @PostMapping
    public ResponseEntity<AchievementGroupDomain> createAchievement(@RequestBody AchievementGroupDomain achievementGroupDomain){
        AchievementGroupDomain createdAchievement = port.save(achievementGroupDomain);
        return ResponseEntity.ok(createdAchievement);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AchievementGroupDomain> updateDimension(@PathVariable Integer id, @RequestBody AchievementGroupDomain achievementGroupDomain){
        AchievementGroupDomain updatedAchievement = port.update(id,achievementGroupDomain);
        return ResponseEntity.ok(updatedAchievement);
    }
}
