package co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.EducationalLevelDomain;

import java.util.List;

public interface EducationalLevelServicePort {
    List<EducationalLevelDomain> getAllEducationalLevels();
    EducationalLevelDomain getEducationalLevelById(Integer id);
    EducationalLevelDomain createEducationalLevel(EducationalLevelDomain Level);
    EducationalLevelDomain updateEducationalLevel(Integer id, EducationalLevelDomain Level);
    void deleteEducationalLevel(Integer id);
}
