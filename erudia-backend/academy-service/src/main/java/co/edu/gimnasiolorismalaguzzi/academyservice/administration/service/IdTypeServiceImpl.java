package co.edu.gimnasiolorismalaguzzi.academyservice.administration.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.IdTypeServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceIdTypePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.UseCase;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.IdTypeDomain;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UseCase
public class IdTypeServiceImpl implements IdTypeServicePort {

    @Autowired
    private PersistenceIdTypePort idTypePort;

    @Override
    public List<IdTypeDomain> getAllTypes() {
        return idTypePort.findAll();
    }

    @Override
    public IdTypeDomain getTypeById(Integer id) {
        return idTypePort.findById(id);
    }

    @Override
    public IdTypeDomain createType(IdTypeDomain idTypeDomain) {
        return idTypePort.save(idTypeDomain);
    }

    @Override
    public IdTypeDomain updateType(Integer id, IdTypeDomain idTypeDomain) {
        return idTypePort.update(id,idTypeDomain);
    }
}
