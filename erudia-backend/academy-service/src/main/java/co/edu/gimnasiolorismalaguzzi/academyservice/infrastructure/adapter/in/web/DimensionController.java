package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.in.web;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.DimensionServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.DimensionDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/dimensions")
public class DimensionController {
    private final DimensionServicePort dimensionServicePort;

    public DimensionController(DimensionServicePort dimensionServicePort) {
        this.dimensionServicePort = dimensionServicePort;
    }

    @GetMapping
    public ResponseEntity<List<DimensionDomain>> getAllDimensions(){
        List<DimensionDomain> dimensionDomainList = dimensionServicePort.getAllDimensions();
        return ResponseEntity.ok(dimensionDomainList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DimensionDomain> getDimensionById(Integer id){
        DimensionDomain dimensionDomain = dimensionServicePort.getDimensionById(id);
        return ResponseEntity.ok(dimensionDomain);
    }

    @PostMapping
    public ResponseEntity<DimensionDomain> createDimension(@RequestBody DimensionDomain dimensionDomain){
        DimensionDomain createdDimension = dimensionServicePort.createDimension(dimensionDomain);
        return ResponseEntity.ok(createdDimension);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DimensionDomain> updateDimension(@PathVariable Integer id, @RequestBody DimensionDomain dimensionDomain){
        DimensionDomain updatedDimension = dimensionServicePort.updateDimension(id,dimensionDomain);
        return ResponseEntity.ok(updatedDimension);
    }
}
