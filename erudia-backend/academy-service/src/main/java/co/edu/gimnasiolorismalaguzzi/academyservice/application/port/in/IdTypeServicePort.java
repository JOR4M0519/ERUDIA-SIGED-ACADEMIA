package co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.IdTypeDomain;

import java.util.List;

public interface IdTypeServicePort {
    List<IdTypeDomain> getAllTypes();
    IdTypeDomain getTypeById(Integer id);
    IdTypeDomain createType(IdTypeDomain idTypeDomain);
    IdTypeDomain updateType(Integer id, IdTypeDomain idTypeDomain);
}
