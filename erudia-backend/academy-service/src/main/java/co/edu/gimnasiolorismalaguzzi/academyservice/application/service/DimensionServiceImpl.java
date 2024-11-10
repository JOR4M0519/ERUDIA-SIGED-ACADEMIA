package co.edu.gimnasiolorismalaguzzi.academyservice.application.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.DimensionServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceDimensionPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.UseCase;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.DimensionDomain;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UseCase
public class DimensionServiceImpl implements DimensionServicePort {
    private final PersistenceDimensionPort dimensionPort;

    @Autowired
    public DimensionServiceImpl(PersistenceDimensionPort dimensionPort) {
        this.dimensionPort = dimensionPort;
    }

    @Override
    public List<DimensionDomain> getAllDimensions() {
        return dimensionPort.findAll();
    }

    @Override
    public DimensionDomain getDimensionById(Integer id) {
        return dimensionPort.findById(id);
    }

    @Override
    public DimensionDomain createDimension(DimensionDomain dimensionDomain) {
        return dimensionPort.save(dimensionDomain);
    }

    @Override
    public DimensionDomain updateDimension(Integer id, DimensionDomain dimensionDomain) {
        return dimensionPort.update(id, dimensionDomain);
    }

    @Override
    public void deleteDimension(Integer id) {
        dimensionPort.delete(id);
    }
}

