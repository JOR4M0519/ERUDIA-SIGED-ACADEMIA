package co.edu.gimnasiolorismalaguzzi.academyservice.administration.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.InstitutionServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceInstitutionPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.UseCase;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.InstitutionDomain;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UseCase
public class InstitutionServiceImpl implements InstitutionServicePort {

    @Autowired
    private PersistenceInstitutionPort institutionPort;

    @Override
    public List<InstitutionDomain> getAllInstitutions() {
        return institutionPort.findAll();
    }

    @Override
    public InstitutionDomain getInstitutionById(Integer id) {
        return institutionPort.findById(id);
    }

    @Override
    public InstitutionDomain createInstitution(InstitutionDomain institution) {
        return institutionPort.save(institution);
    }

    @Override
    public InstitutionDomain updateInstitution(Integer id, InstitutionDomain institution) {
        return institutionPort.update(id,institution);
    }

    @Override
    public void deleteInstitution(Integer id) {
        institutionPort.delete(id);
    }
}
