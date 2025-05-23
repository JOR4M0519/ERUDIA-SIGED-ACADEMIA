package co.edu.gimnasiolorismalaguzzi.academyservice.student.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.EducationalLevelDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence.PersistenceEducationalLevelPort;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> deleteEducationalLevel(@PathVariable Integer id) {
        try {
            HttpStatus status = EducationalLevelServicePort.delete(id);
            return ResponseEntity.noContent().build();
        } catch (AppException e) {
            if (e.getCode() == HttpStatus.IM_USED) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(e.getMessage());
            } else if (e.getCode() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la solicitud");
        }
    }

}
