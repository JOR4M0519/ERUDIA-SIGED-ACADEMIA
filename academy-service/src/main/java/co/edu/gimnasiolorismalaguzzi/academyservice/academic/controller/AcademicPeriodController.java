package co.edu.gimnasiolorismalaguzzi.academyservice.academic.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.AcademicYearPercentageDTO;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceAcademicPeriodPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.AcademicPeriodDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/periods")
public class AcademicPeriodController {
    private final PersistenceAcademicPeriodPort academicPeriodServicePort;

    public AcademicPeriodController(PersistenceAcademicPeriodPort academicPeriodServicePort) {
        this.academicPeriodServicePort = academicPeriodServicePort;
    }

    @GetMapping
    public ResponseEntity<List<AcademicPeriodDomain>> getAllPeriods(){
        List<AcademicPeriodDomain> AcademicPeriodDomains = academicPeriodServicePort.findAll();
        return ResponseEntity.ok(AcademicPeriodDomains);
    }

    /**
     * Obtiene los periods académicos por un estaus (Activo, Inactivo y Finalizado)
     * @param status
     * @return Lista de periodos académiocos
     */
    @GetMapping("/{status}")
    public ResponseEntity<List<AcademicPeriodDomain>> getAllPeriodsByStatus(@PathVariable String status){
        List<AcademicPeriodDomain> AcademicPeriodDomains = academicPeriodServicePort.getAllPeriodsByStatus(status);
        return ResponseEntity.ok(AcademicPeriodDomains);
    }

    /**
     * Obtiene todos los periodos académicos activos por año
     * @param year
     * @return Lista de periodos académicos
     */
    @GetMapping("/active/{year}")
    public ResponseEntity<List<AcademicPeriodDomain>> getActivePeriodsByYear(@PathVariable String year){
        List<AcademicPeriodDomain> AcademicPeriodDomains = academicPeriodServicePort.getActivePeriodsByYear(year);
        return ResponseEntity.ok(AcademicPeriodDomains);
    }

    @GetMapping("/years/{year}")
    public ResponseEntity<List<AcademicPeriodDomain>> getPeriodsByYear(@PathVariable String year){
        List<AcademicPeriodDomain> AcademicPeriodDomains = academicPeriodServicePort.getPeriodsByYear(year);
        return ResponseEntity.ok(AcademicPeriodDomains);
    }

    @GetMapping("/settings/{settingId}/active/{year}")
    public ResponseEntity<List<AcademicPeriodDomain>> getPeriodsByYearAndSetting(
            @PathVariable String year,
            @PathVariable Integer settingId){
        List<AcademicPeriodDomain> AcademicPeriodDomains = academicPeriodServicePort.getPeriodsBySettingsAndYear(settingId,year);
        return ResponseEntity.ok(AcademicPeriodDomains);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcademicPeriodDomain> getAcademicPeriodById(@PathVariable Integer id) {
        AcademicPeriodDomain AcademicPeriod = academicPeriodServicePort.findById(id);
        return ResponseEntity.ok(AcademicPeriod);
    }

    @PostMapping()
    public ResponseEntity<AcademicPeriodDomain> createAcademicPeriod(@RequestBody AcademicPeriodDomain AcademicPeriodDomain) {
        AcademicPeriodDomain createdAcademicPeriod = academicPeriodServicePort.save(AcademicPeriodDomain);
        return ResponseEntity.ok(createdAcademicPeriod);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAcademicPeriod(@PathVariable Integer id, @RequestBody AcademicPeriodDomain academicPeriodDomain) {
        try {
            AcademicPeriodDomain updatedAcademicPeriod = academicPeriodServicePort.update(id, academicPeriodDomain);
            return ResponseEntity.ok(updatedAcademicPeriod);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AppException(e.getMessage(), HttpStatus.NOT_FOUND));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AppException(e.getMessage(),HttpStatus.BAD_REQUEST));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AppException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAcademicPeriod(@PathVariable Integer id) {
        academicPeriodServicePort.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Agregar esto a AcademicPeriodController.java
    @GetMapping("/years-percentages")
    public ResponseEntity<List<AcademicYearPercentageDTO>> getAcademicYearsWithPercentages() {
        List<AcademicYearPercentageDTO> yearPercentages = academicPeriodServicePort.getAcademicYearsWithPercentages();
        return ResponseEntity.ok(yearPercentages);
    }

    @GetMapping("/verify-year/{year}")
    public ResponseEntity<Boolean> verifyYearPercentages(@PathVariable Integer year) {
        Boolean isComplete = academicPeriodServicePort.verifyYearPercentages(year);
        return ResponseEntity.ok(isComplete);
    }


}
