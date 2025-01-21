package co.edu.gimnasiolorismalaguzzi.academyservice.student.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.EducationalLevelDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.PersistenceEducationalLevelPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@WebAdapter
@RestController
@RequestMapping("/api/academy/educational-levels")
public class EducationalLevelController {

    private final PersistenceEducationalLevelPort EducationalLevelServicePort;

    public EducationalLevelController(PersistenceEducationalLevelPort EducationalLevelServicePort) {
        this.EducationalLevelServicePort = EducationalLevelServicePort;
    }

    @GetMapping()
    @PreAuthorize("hasRole('admin_client_role')")
    public ResponseEntity<List<EducationalLevelDomain>> getAllEducationalLevels() {
        List<EducationalLevelDomain> EducationalLevels = EducationalLevelServicePort.findAll();
        return ResponseEntity.ok(EducationalLevels);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('user_client_role') or hasRole('admin_client_role')")
    public ResponseEntity<EducationalLevelDomain> getEducationalLevelById(@PathVariable Integer id) {
        EducationalLevelDomain EducationalLevel = EducationalLevelServicePort.findById(id);
        return ResponseEntity.ok(EducationalLevel);
    }

    @PostMapping()
    public ResponseEntity<EducationalLevelDomain> createEducationalLevel(@RequestBody EducationalLevelDomain EducationalLevelDomain) {
        EducationalLevelDomain createdEducationalLevel = EducationalLevelServicePort.save(EducationalLevelDomain);
        return ResponseEntity.ok(createdEducationalLevel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EducationalLevelDomain> updateEducationalLevel(@PathVariable Integer id, @RequestBody EducationalLevelDomain EducationalLevelDomain) {
        EducationalLevelDomain updatedEducationalLevel = EducationalLevelServicePort.update(id, EducationalLevelDomain);
        return ResponseEntity.ok(updatedEducationalLevel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEducationalLevel(@PathVariable Integer id) {
        EducationalLevelServicePort.delete(id);
        return ResponseEntity.noContent().build();
    }
}
