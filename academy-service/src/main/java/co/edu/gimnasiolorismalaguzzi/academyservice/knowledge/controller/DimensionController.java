package co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.domain.DimensionDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.service.persistence.PersistenceDimensionPort;
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
}
