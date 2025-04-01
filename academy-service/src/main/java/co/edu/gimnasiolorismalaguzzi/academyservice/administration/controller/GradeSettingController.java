package co.edu.gimnasiolorismalaguzzi.academyservice.administration.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.GradeSettingDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceGradeSettingPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/grade-settings")
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
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSetting(@PathVariable Integer id) {
        try {
            HttpStatus status = persistenceGradeSettingPort.delete(id);
            return ResponseEntity.ok("Se eliminó correctamente el registro");
        } catch (AppException e) {
            if (e.getCode() == HttpStatus.IM_USED) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(e.getMessage());
            }
            throw e;
        }
    }

}
