package co.edu.gimnasiolorismalaguzzi.academyservice.application.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.EducationalLevelServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceEducationalLevelPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.UseCase;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.EducationalLevelDomain;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UseCase
public class EducationalLevelServiceImpl implements EducationalLevelServicePort {

    private final PersistenceEducationalLevelPort EducationalLevelRepository;

    @Autowired
    public EducationalLevelServiceImpl(PersistenceEducationalLevelPort EducationalLevelRepository) {
        this.EducationalLevelRepository = EducationalLevelRepository;
    }

    @Override
    public List<EducationalLevelDomain> getAllEducationalLevels() {
        return EducationalLevelRepository.findAll();
    }

    @Override
    public EducationalLevelDomain getEducationalLevelById(Integer id) {
        return EducationalLevelRepository.findById(id);
    }

    @Override
    public EducationalLevelDomain createEducationalLevel(EducationalLevelDomain EducationalLevelDomain) {
        return EducationalLevelRepository.save(EducationalLevelDomain);
    }

    @Override
    public void deleteEducationalLevel(Integer id) {
        EducationalLevelRepository.delete(id);
    }

    @Override
    public EducationalLevelDomain updateEducationalLevel(Integer id, EducationalLevelDomain EducationalLevelDomain) {
        return EducationalLevelRepository.update(id, EducationalLevelDomain);
    }
}
