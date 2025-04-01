package co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.domain.DimensionDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.service.persistence.PersistenceDimensionPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/dimensions")
public class DimensionController {
    private final PersistenceDimensionPort dimensionServicePort;

    public DimensionController(PersistenceDimensionPort dimensionServicePort) {
        this.dimensionServicePort = dimensionServicePort;
    }

    @GetMapping
    public ResponseEntity<List<DimensionDomain>> getAllDimensions(){
        List<DimensionDomain> dimensionDomainList = dimensionServicePort.findAll();
        return ResponseEntity.ok(dimensionDomainList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DimensionDomain> getDimensionById(Integer id){
        DimensionDomain dimensionDomain = dimensionServicePort.findById(id);
        return ResponseEntity.ok(dimensionDomain);
    }

    @PostMapping
    public ResponseEntity<DimensionDomain> createDimension(@RequestBody DimensionDomain dimensionDomain){
        DimensionDomain createdDimension = dimensionServicePort.save(dimensionDomain);
        return ResponseEntity.ok(createdDimension);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DimensionDomain> updateDimension(@PathVariable Integer id, @RequestBody DimensionDomain dimensionDomain){
        DimensionDomain updatedDimension = dimensionServicePort.update(id,dimensionDomain);
        return ResponseEntity.ok(updatedDimension);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteKnowledge(@PathVariable Integer id) {
        try {
            dimensionServicePort.delete(id);
            return ResponseEntity.noContent().build();
        } catch (AppException e) {
            if (e.getCode() == HttpStatus.CONFLICT) {
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
