package co.edu.gimnasiolorismalaguzzi.academyservice.academic.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.SubjectServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceSubjectPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.UseCase;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectDomain;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UseCase
public class SubjectServiceImpl implements SubjectServicePort {

    private final PersistenceSubjectPort SubjectRepository;

    @Autowired
    public SubjectServiceImpl(PersistenceSubjectPort SubjectRepository) {
        this.SubjectRepository = SubjectRepository;
    }

    @Override
    public List<SubjectDomain> getAllSubjects() {
        return SubjectRepository.findAll();
    }

    @Override
    public SubjectDomain getSubjectById(Integer id) {
        return SubjectRepository.findById(id);
    }

    @Override
    public SubjectDomain createSubject(SubjectDomain SubjectDomain) {
        return SubjectRepository.save(SubjectDomain);
    }

    @Override
    public void deleteSubject(Integer id) {
        SubjectRepository.delete(id);
    }

    @Override
    public SubjectDomain updateSubject(Integer id, SubjectDomain SubjectDomain) {
        return SubjectRepository.update(id, SubjectDomain);
    }
}
