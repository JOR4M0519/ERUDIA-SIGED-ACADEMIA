package co.edu.gimnasiolorismalaguzzi.academyservice.administration.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.*;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.Family;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.FamilyMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository.FamilyCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceFamilyPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceUserDetailPort;
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

    @Autowired
    private PersistenceUserDetailPort userDetailPort;

    public FamilyAdapter(FamilyCrudRepo familyCrudRepo) {
        this.familyCrudRepo = familyCrudRepo;
    }

    @Override
    public List<FamilyDomain> findAll() {
        return this.familyMapper.toDomains(this.familyCrudRepo.findAll());
    }

    @Override
    public List<UserFamilyRelationDomain> findAllWithRelatives() {
        try {
            log.info("Obteniendo todos los usuarios con sus relaciones familiares");

            // Obtener todos los usuarios
            List<UserDetailDomain> allUsers = userDetailPort.findAll();
            List<UserFamilyRelationDomain> result = new ArrayList<>();


            for (UserDetailDomain userDetail : allUsers) {
                if (userDetail.getUser() == null || userDetail.getUser().getId() == null) {
                    log.warn("Usuario sin ID encontrado, omitiendo: {}", userDetail);
                    continue;
                }

                Integer userId = userDetail.getId();
                boolean isStudent = false;

                // Verificar si el usuario tiene roles
                if (userDetail.getUser().getRoles() != null && !userDetail.getUser().getRoles().isEmpty()) {
                    // Verificar si alguno de los roles es "estudiante"
                    isStudent = userDetail.getUser().getRoles().stream()
                            .anyMatch(role -> role.getRole() != null &&
                                    "estudiante".equalsIgnoreCase(role.getRole().getRoleName()));
                }

                // Obtener relaciones familiares
                List<FamilyDomain> familyRelations = findRelativesByStudent(userId);

                // Crear el objeto de resultado
                UserFamilyRelationDomain userWithRelations = UserFamilyRelationDomain.builder()
                        .userDetail(userDetail)
                        .familyRelations(familyRelations)
                        .isStudent(isStudent)
                        .build();

                result.add(userWithRelations);
            }

            log.info("Se han obtenido {} usuarios con sus relaciones familiares", result.size());
            return result;

        } catch (Exception e) {
            log.error("Error al obtener usuarios con relaciones familiares: {}", e.getMessage(), e);
            throw new AppException("Error al obtener usuarios con relaciones familiares: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<FamilyDomain> findAllByRelationType(Integer relationTypeId) {
        return this.familyMapper.toDomains(familyCrudRepo.findByRelationship_Id(relationTypeId));
    }


    @Override
    public FamilyDomain findById(Integer integer) {
        Optional<Family> family = this.familyCrudRepo.findById(integer);
        return family.map(familyMapper::toDomain).orElse(null);
    }

    @Override
    public FamilyDomain save(FamilyDomain domain) {
        // Asegurarse de que los roles de estudiante y familiar estén correctamente asignados
        Family familyEntity = familyMapper.toEntity(domain);
        Family savedFamily = this.familyCrudRepo.save(familyEntity);
        return familyMapper.toDomain(savedFamily);
    }





    @Override
    public List<FamilyDomain> saveFamilyRelations(UserFamilyRelationDomain userFamilyRelationDomain) {
        log.info("Creando relaciones familiares para usuario");

        if (userFamilyRelationDomain == null) {
            throw new AppException("Los datos de la relación familiar no pueden ser nulos", HttpStatus.BAD_REQUEST);
        }

        // Verificamos que haya relaciones familiares
        if (userFamilyRelationDomain.getFamilyRelations() == null || userFamilyRelationDomain.getFamilyRelations().isEmpty()) {
            throw new AppException("Debe proporcionar al menos una relación familiar", HttpStatus.BAD_REQUEST);
        }

        List<FamilyDomain> savedRelations = new ArrayList<>();

        // Procesar cada relación familiar
        for (FamilyDomain familyDomain : userFamilyRelationDomain.getFamilyRelations()) {
            // Validamos que tengamos los datos necesarios
            if (familyDomain.getRelativeUser() == null || familyDomain.getRelativeUser().getId() == null) {
                throw new AppException("El ID del familiar (relativeUser.id) es requerido", HttpStatus.BAD_REQUEST);
            }

            if (familyDomain.getUser() == null || familyDomain.getUser().getId() == null) {
                throw new AppException("El ID del estudiante (user.id) es requerido", HttpStatus.BAD_REQUEST);
            }

            if (familyDomain.getRelationship() == null || familyDomain.getRelationship().getId() == null) {
                throw new AppException("El ID del tipo de relación es requerido", HttpStatus.BAD_REQUEST);
            }

            // NUEVA VALIDACIÓN 1: Verificar que al menos uno sea estudiante
            Integer studentId = familyDomain.getUser().getId();
            Integer relativeId = familyDomain.getRelativeUser().getId();

            boolean isStudentValid =  userDetailPort.hasStudentRole(relativeId); // El familiar no debería tener rol de estudiante
            boolean isRelativeValid =  userDetailPort.hasStudentRole(studentId);

            if (!isStudentValid) {
                throw new AppException("El usuario con ID " + studentId + " no tiene rol de estudiante", HttpStatus.BAD_REQUEST);
            }

            /*if (!isRelativeValid) {
                throw new AppException("El familiar con ID " + relativeId + " no puede tener rol de estudiante", HttpStatus.BAD_REQUEST);
            }*/

            // NUEVA VALIDACIÓN 2: Verificar que no exista la relación
            boolean relationExists = existsRelation(studentId, relativeId);
            if (relationExists) {
                throw new AppException("Ya existe una relación familiar entre los usuarios " + studentId + " y " + relativeId,
                        HttpStatus.CONFLICT);
            }

            // Guardamos la relación
            FamilyDomain savedRelation = save(familyDomain);
            savedRelations.add(savedRelation);
        }

        log.info("Se crearon {} relaciones familiares exitosamente", savedRelations.size());
        return savedRelations;
    }

    /**
     * Verifica si ya existe una relación entre un estudiante y un familiar
     * @param studentId ID del estudiante
     * @param relativeId ID del familiar
     * @return true si la relación existe, false en caso contrario
     */
    @Override
    public boolean existsRelation(Integer studentId, Integer relativeId) {
        return familyCrudRepo.existsByStudent_IdAndUser_Id(studentId,relativeId);
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

/*
    @Override
    public List<FamilyDomain> findRelativesByStudent(Integer id) {
        List<Family> relatives = familyCrudRepo.findRelativesByStudent(id);
        return familyMapper.toDomains(relatives);
    }
*/

   /* @Override
    public List<FamilyDomain> findRelativesByStudent(Integer userId) {
        List<Family> familyRelations = new ArrayList<>();
        List<Family> studentsOfRelative = new ArrayList<>();

            // Primero buscamos si el usuario es un estudiante y tiene familiares asociados
            List<Family> relativesOfStudent = familyCrudRepo.findByStudent_Id(userDetailPort.findById(userId).getUser().getId());
            familyRelations.addAll(relativesOfStudent);


            // Luego buscamos si el usuario es un familiar y está asociado a estudiantes
            studentsOfRelative = familyCrudRepo.findByUser_Id(userDetailPort.findById(userId).getUser().getId());

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
    }*/

    @Override
    public List<FamilyDomain> findRelativesByStudent(Integer userId) {
        // Use a map to ensure uniqueness by ID
        Map<Integer, Family> uniqueFamilyRelations = new HashMap<>();

        try {
            // Primero buscamos si el usuario es un estudiante y tiene familiares asociados
            List<Family> relativesOfStudent = familyCrudRepo.findByStudent_Id(userDetailPort.findById(userId).getUser().getId());
            relativesOfStudent.forEach(relation -> uniqueFamilyRelations.put(relation.getId(), relation));

            // Luego buscamos si el usuario es un familiar y está asociado a estudiantes
            List<Family> studentsOfRelative = familyCrudRepo.findByUser_Id(userDetailPort.findById(userId).getUser().getId());
            studentsOfRelative.forEach(relation -> uniqueFamilyRelations.put(relation.getId(), relation));

            // Si encontramos que el usuario es un familiar con estudiantes asociados,
            // necesitamos obtener también los otros familiares de esos estudiantes
            if (!studentsOfRelative.isEmpty()) {
                for (Family relation : studentsOfRelative) {
                    // Obtenemos el ID del estudiante asociado a este familiar
                    Integer studentId = relation.getStudent().getId();

                    // Buscamos todos los familiares de este estudiante (excepto el usuario actual)
                    List<Family> studentRelatives = familyCrudRepo.findByStudent_IdAndUserIdNot(
                            studentId, userId);

                    // Agregamos estos familiares al mapa para asegurar unicidad
                    studentRelatives.forEach(rel -> uniqueFamilyRelations.put(rel.getId(), rel));
                }
            }

            // Convertimos los valores del mapa a una lista
            List<Family> familyRelations = new ArrayList<>(uniqueFamilyRelations.values());

            return familyMapper.toDomains(familyRelations);

        } catch (Exception e) {
            log.error("Error al buscar relaciones familiares para el usuario {}: {}", userId, e.getMessage());
            return Collections.emptyList();
        }
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
