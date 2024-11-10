package co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.DimensionDomain;

import java.util.List;

public interface DimensionServicePort {
    List<DimensionDomain> getAllDimensions();
    DimensionDomain getDimensionById(Integer id);
    DimensionDomain createDimension(DimensionDomain dimensionDomain);
    DimensionDomain updateDimension(Integer id, DimensionDomain dimensionDomain);
    void deleteDimension(Integer id);
}
