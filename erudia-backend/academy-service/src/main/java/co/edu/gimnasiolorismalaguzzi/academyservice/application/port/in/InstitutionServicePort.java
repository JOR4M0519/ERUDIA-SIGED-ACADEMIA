package co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.InstitutionDomain;

import java.util.List;

public interface InstitutionServicePort {
    List<InstitutionDomain> getAllInstitutions();
    InstitutionDomain getInstitutionById(Integer id);
    InstitutionDomain createInstitution(InstitutionDomain institution);
    InstitutionDomain updateInstitution(Integer id, InstitutionDomain institution);
    void deleteInstitution(Integer id);
}
