package co.edu.gimnasiolorismalaguzzi.academyservice.application.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.AcademicPeriodServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceAcademicPeriodPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.UseCase;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.AcademicPeriodDomain;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UseCase
public class AcademicPeriodServiceImpl implements AcademicPeriodServicePort {

    private final PersistenceAcademicPeriodPort academicPeriodRepository;

    @Autowired
    public AcademicPeriodServiceImpl(PersistenceAcademicPeriodPort academicPeriodRepository) {
        this.academicPeriodRepository = academicPeriodRepository;
    }

    @Override
    public List<AcademicPeriodDomain> getAllPeriods() {
        return academicPeriodRepository.findAll();
    }

    @Override
    public AcademicPeriodDomain getPeriodById(Integer id) {
        return academicPeriodRepository.findById(id);
    }

    @Override
    public AcademicPeriodDomain createPeriod(AcademicPeriodDomain academicPeriodDomain) {
        return academicPeriodRepository.save(academicPeriodDomain);
    }

    @Override
    public AcademicPeriodDomain updatePeriod(Integer id, AcademicPeriodDomain academicPeriodDomain) {
        return academicPeriodRepository.update(id, academicPeriodDomain);
    }

    @Override
    public void deletePeriod(Integer id) {
        academicPeriodRepository.delete(id);
    }
}
