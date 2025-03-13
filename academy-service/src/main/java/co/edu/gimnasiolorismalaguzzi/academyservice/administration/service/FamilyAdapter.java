package co.edu.gimnasiolorismalaguzzi.academyservice.administration.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.FamilyDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.FamilyReportDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.Family;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.FamilyMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository.FamilyCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceFamilyPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.*;

@PersistenceAdapter
@Slf4j
public class FamilyAdapter implements PersistenceFamilyPort {

    private final FamilyCrudRepo familyCrudRepo;

    @Autowired
    private FamilyMapper familyMapper;

    public FamilyAdapter(FamilyCrudRepo familyCrudRepo) {
        this.familyCrudRepo = familyCrudRepo;
    }

    @Override
    public List<FamilyDomain> findAll() {
        return this.familyMapper.toDomains(this.familyCrudRepo.findAll());
    }

    @Override
    public FamilyDomain findById(Integer integer) {
        Optional<Family> family = this.familyCrudRepo.findById(integer);
        return family.map(familyMapper::toDomain).orElse(null);
    }

    @Override
    public FamilyDomain save(FamilyDomain domain) {
        Family familyEntity = familyMapper.toEntity(domain);
        Family savedFamily = this.familyCrudRepo.save(familyEntity);
        return familyMapper.toDomain(savedFamily);
    }

    @Override
    public FamilyDomain update(Integer integer, FamilyDomain domain) {
        try{
            Optional<Family> existingFamily = familyCrudRepo.findById(integer);

            /*if(existingFamily.isPresent()){
                existingFamily.get().setStudent(domain.getStudent());
                existingFamily.get().setUser(entity.getUser());
                existingFamily.get().setRelationship(entity.getRelationship());
            }*/
            return familyMapper.toDomain(familyCrudRepo.save(existingFamily.get()));
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Family relation with ID " + integer + "Not found!");
        }
    }

    @Override
    public HttpStatus delete(Integer integer) {
        try{
            if(this.familyCrudRepo.existsById(integer)){
                familyCrudRepo.delete(this.familyCrudRepo.getReferenceById(integer));
                return HttpStatus.OK;
            } else {
                throw new AppException("Family relation ID doesnt exist!", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            throw new AppException("Intern Error!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<FamilyDomain> findRelativesByStudent(Integer userId) {
        List<Family> familyRelations = new ArrayList<>();

        // Primero buscamos si el usuario es un estudiante y tiene familiares asociados
        List<Family> relativesOfStudent = familyCrudRepo.findByStudent_Id(userId);
        familyRelations.addAll(relativesOfStudent);

        // Luego buscamos si el usuario es un familiar y está asociado a estudiantes
        List<Family> studentsOfRelative = familyCrudRepo.findByUser_Id(userId);

        // Agregamos los estudiantes asociados al familiar
        familyRelations.addAll(studentsOfRelative);

        // Si encontramos que el usuario es un familiar con estudiantes asociados,
        // necesitamos obtener también los otros familiares de esos estudiantes
        if (!studentsOfRelative.isEmpty()) {
            // Conjunto para evitar duplicados
            Set<Family> otherRelatives = new HashSet<>();

            for (Family relation : studentsOfRelative) {
                // Obtenemos el ID del estudiante asociado a este familiar
                Integer studentId = relation.getStudent().getId();

                // Buscamos todos los familiares de este estudiante (excepto el usuario actual)
                List<Family> studentRelatives = familyCrudRepo.findByStudent_IdAndUserIdNot(
                        studentId, userId);

                // Agregamos estos familiares al conjunto
                otherRelatives.addAll(studentRelatives);
            }

            // Agregamos los familiares encontrados a la lista principal
            familyRelations.addAll(otherRelatives);
        }

        return familyMapper.toDomains(familyRelations);
    }


    /**
     * Devuelve la información del usuario de los estudiantes del familiar
     * @param relativeId
     * @return
     */
    @Override
    public List<FamilyDomain> findStudentsByRelativeId(Integer relativeId) {
        List<Family> relatives = familyCrudRepo.findByUser_Id(relativeId);
        return familyMapper.toDomains(relatives);
    }

    @Override
    public List<FamilyReportDomain> getAllFamilyReports() {
        try {
            List<Object[]> results = familyCrudRepo.getFamilyReports();
            List<FamilyReportDomain> reports = new ArrayList<>();

            for (Object[] result : results) {
                FamilyReportDomain report = new FamilyReportDomain();

                report.setCode((String) result[0]);
                report.setFamilyName((String) result[1]);

                // Convertir a Long si es necesario
                report.setTotalMembers(result[2] instanceof Long ?
                    (Long) result[2] :
                    Long.valueOf(((Number) result[2]).longValue()));
                report.setActiveStudents(result[3] instanceof Long ?
                    (Long) result[3] :
                    Long.valueOf(((Number) result[3]).longValue()));

                // Manejar el array de IDs de estudiantes
                if (result[4] instanceof java.sql.Array) {
                    try {
                    java.sql.Array sqlArray = (java.sql.Array) result[4];
                    Integer[] studentIdsArray = (Integer[]) sqlArray.getArray();
                    report.setStudentIds(Arrays.asList(studentIdsArray));
        } catch (Exception e) {
                        log.error("Error al convertir array de IDs de estudiantes: {}", e.getMessage());
                        report.setStudentIds(new ArrayList<>());
        }
    }

                reports.add(report);
            }

            return reports;
        } catch (Exception e) {
            log.error("Error al obtener el reporte de familias: {}", e.getMessage());
            throw new RuntimeException("Error al obtener el reporte de familias", e);
        }
    }

    @Override
    public FamilyDomain saveById(Integer id, FamilyDomain familyDomain) {

        UserDomain user = new UserDomain(id);
        FamilyDomain familyToSave = new FamilyDomain(null,user,familyDomain.getUser(),familyDomain.getRelationship());

        Family familyEntity = familyMapper.toEntity( familyToSave);
        Family savedFamily = this.familyCrudRepo.save(familyEntity);
        return familyMapper.toDomain(savedFamily);
    }
}
