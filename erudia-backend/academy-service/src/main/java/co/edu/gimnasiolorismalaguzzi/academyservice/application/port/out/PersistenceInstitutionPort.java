package co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.InstitutionServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.InstitutionDomain;

public interface PersistenceInstitutionPort extends PersistencePort<InstitutionDomain, Integer>{
    InstitutionDomain saveInstitution(Integer id, InstitutionDomain institution);
}
