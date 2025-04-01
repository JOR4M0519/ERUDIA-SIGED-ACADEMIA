package co.edu.gimnasiolorismalaguzzi.academyservice.student.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.GradeSettingDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.GradeSetting;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceGradeSettingPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.EducationalLevelDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.EducationalLevel;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.mapper.EducationalLevelMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.repository.EduLevelCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence.PersistenceEducationalLevelPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence.PersistenceGroupsPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class EducationalLevelAdapter implements PersistenceEducationalLevelPort {

    private final EduLevelCrudRepo EduLevelCrudRepo; // Repositorio JPA

    private EducationalLevelMapper EducationalLevelMapper;

    @Autowired
    private PersistenceGroupsPort groupsPort;

    @Autowired
    private PersistenceGradeSettingPort gradeSettingPort;

    public EducationalLevelAdapter(EduLevelCrudRepo EduLevelCrudRepo, EducationalLevelMapper EducationalLevelMapper) {
        this.EduLevelCrudRepo = EduLevelCrudRepo;
        this.EducationalLevelMapper = EducationalLevelMapper;
    }

    @Override
    public List<EducationalLevelDomain> findAll() {
        return this.EducationalLevelMapper.toDomains(this.EduLevelCrudRepo.findAll());
    }

    @Override
    public EducationalLevelDomain findById(Integer id) {
        Optional<EducationalLevel> EducationalLevelOptional = this.EduLevelCrudRepo.findById(id);
        return EducationalLevelOptional.map(EducationalLevelMapper::toDomain).orElse(null);
    }

    @Override
    public EducationalLevelDomain save(EducationalLevelDomain EducationalLevelDomain) {

        EducationalLevelDomain.setLevelName(EducationalLevelDomain.getLevelName());
        EducationalLevelDomain.setStatus("A");

        EducationalLevel EducationalLevel = EducationalLevelMapper.toEntity(EducationalLevelDomain);
        EducationalLevel savedEducationalLevel = this.EduLevelCrudRepo.save(EducationalLevel);
        return EducationalLevelMapper.toDomain(savedEducationalLevel);
    }

    @Override
    public EducationalLevelDomain update(Integer id, EducationalLevelDomain entity) {

        try{
            Optional<EducationalLevel> existingEducationalLevel = EduLevelCrudRepo.findById(id);

            if(existingEducationalLevel.isPresent()){
                existingEducationalLevel.get().setLevelName(entity.getLevelName());
                existingEducationalLevel.get().setStatus(entity.getStatus());
            }

            return EducationalLevelMapper.toDomain(EduLevelCrudRepo.save(existingEducationalLevel.get()));

        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException("UserDetail with ID " + id + " not found");
        }
    }

    @Override
    public HttpStatus delete(Integer integer) {
        // Verificar si el nivel está siendo usado en otras tablas
        List<GroupsDomain> groupsDomainList = groupsPort.findByLevelId(integer);
        List<GradeSettingDomain> gradeSettingList = gradeSettingPort.findByLevelId(integer);

        try {
            if (!this.EduLevelCrudRepo.existsById(integer)) {
                throw new AppException("El nivel educativo no existe", HttpStatus.NOT_FOUND);
            }

            // Verificar si está siendo utilizado
            if (!groupsDomainList.isEmpty() || !gradeSettingList.isEmpty()) {
                throw new AppException(
                        "No es posible eliminar el nivel educativo porque está siendo utilizado por grupos o esquemas de calificación",
                        HttpStatus.IM_USED);
            }

            // Si no está siendo utilizado, actualizar el estado a inactivo
            EduLevelCrudRepo.updateStatusById("I", integer);
            return HttpStatus.OK;
        } catch (AppException e) {
            throw e; // Relanzar excepciones de la aplicación
        } catch (Exception e) {
            throw new AppException("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
