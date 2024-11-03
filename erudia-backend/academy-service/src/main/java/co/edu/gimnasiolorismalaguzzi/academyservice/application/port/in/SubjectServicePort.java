package co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.SubjectDomain;


import java.util.List;

public interface SubjectServicePort {
    List<SubjectDomain> getAllSubjects();
    SubjectDomain getSubjectById(Integer id);
    SubjectDomain createSubject(SubjectDomain subject);
    SubjectDomain updateSubject(Integer id, SubjectDomain subject);
    void deleteSubject(Integer id);
}
