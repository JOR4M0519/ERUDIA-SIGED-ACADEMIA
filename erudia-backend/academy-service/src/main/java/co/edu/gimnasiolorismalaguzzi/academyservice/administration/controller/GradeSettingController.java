package co.edu.gimnasiolorismalaguzzi.academyservice.administration.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.GradeSettingDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceGradeSettingPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/admin/grade-settings")
public class GradeSettingController {
    private final PersistenceGradeSettingPort persistenceGradeSettingPort;


    public GradeSettingController(PersistenceGradeSettingPort persistenceGradeSettingPort) {
        this.persistenceGradeSettingPort = persistenceGradeSettingPort;
    }

    @GetMapping
    public ResponseEntity<List<GradeSettingDomain>> getAllSettings(){
        List<GradeSettingDomain> gradeSettings = persistenceGradeSettingPort.findAll();
        return ResponseEntity.ok(gradeSettings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GradeSettingDomain> getSettingById(@PathVariable Integer id){
        GradeSettingDomain gradeSettingDomain = persistenceGradeSettingPort.findById(id);
        return ResponseEntity.ok(gradeSettingDomain);
    }

    @PostMapping
    public ResponseEntity<GradeSettingDomain> createSetting (@RequestBody GradeSettingDomain gradeSettingDomain){
        GradeSettingDomain createdSetting = persistenceGradeSettingPort.save(gradeSettingDomain);
        return ResponseEntity.ok(createdSetting);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GradeSettingDomain> updateSetting (@PathVariable Integer id, @RequestBody GradeSettingDomain gradeSettingDomain){
        GradeSettingDomain updatedSetting = persistenceGradeSettingPort.update(id, gradeSettingDomain);
        return ResponseEntity.ok(updatedSetting);
    }
}
