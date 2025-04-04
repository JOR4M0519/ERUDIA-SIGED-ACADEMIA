package co.edu.gimnasiolorismalaguzzi.academyservice.administration.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.*;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceRolePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceUserDetailPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceUserPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceUserRolePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;


import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.UserMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository.UserCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupStudentsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence.PersistenceGroupStudentPort;
import jakarta.persistence.EntityNotFoundException;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@PersistenceAdapter
@Slf4j
public class UserAdapter implements PersistenceUserPort {

    private final UserCrudRepo userCrudRepo; // Repositorio JPA

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private KeycloakAdapter keycloakAdapter;

    private final PersistenceUserDetailPort persistenceUserDetailPort;
    private final PersistenceGroupStudentPort persistenceGroupStudentPort;

    private final PersistenceRolePort persistenceRolePort;
    private final PersistenceUserRolePort persistenceUserRolePort;



    public UserAdapter(UserCrudRepo userCrudRepo, FamilyAdapter familyAdapter, PersistenceUserDetailPort persistenceUserDetailPort, PersistenceGroupStudentPort persistenceGroupStudentPort, PersistenceRolePort persistenceRolePort, PersistenceUserRolePort persistenceUserRolePort) {
        this.userCrudRepo = userCrudRepo;
        this.persistenceUserDetailPort = persistenceUserDetailPort;
        this.persistenceGroupStudentPort = persistenceGroupStudentPort;
        this.persistenceRolePort = persistenceRolePort;
        this.persistenceUserRolePort = persistenceUserRolePort;
    }

    @Override
    public List<UserDomain> findAll() {
        return this.userMapper.toDomains(this.userCrudRepo.findAll());
    }

    @Override
    public UserDomain searchUserByUuid(String uuid) {
        Optional<User> userOptional = Optional.ofNullable(this.userCrudRepo.findByUuid(uuid));
        return userOptional.map(userMapper::toDomain).orElse(null);
    }


    @Override
    public String save(UserDomain userDomain) {
        User user = userMapper.toEntity(userDomain);
        User savedUser = null;
        try {
            savedUser = this.userCrudRepo.save(user);
        }catch (DataIntegrityViolationException e){
            keycloakAdapter.deleteUsersKeycloak(userDomain.getUsername());   //Si genera un error elimina rel usuario de Keycloak
            throw new AppException("The email or username already exist", HttpStatus.CONFLICT);
        }catch (Exception e){
            keycloakAdapter.deleteUsersKeycloak(userDomain.getUsername());   //Si genera un error elimina rel usuario de Keycloak
            throw new  AppException("An error ocurred", HttpStatus.CONFLICT);
        }

        return userMapper.toDomain(savedUser).toString();
    }

    @Override
    public UserDomain update(String uuid, UserDomain userDomain) {
        try{
            Optional<User> user = Optional.ofNullable(userCrudRepo.findByUuid(uuid));

            if (user.isPresent()){
                user.get().setEmail(userDomain.getEmail());
                user.get().setUsername(userDomain.getUsername());
                user.get().setPassword(userDomain.getPassword());
                user.get().setFirstName(userDomain.getFirstName());
                user.get().setLastName(userDomain.getLastName());
                user.get().setStatus(userDomain.getStatus());
                user.get().setEmail(userDomain.getEmail());
            }
            UserDomain userUpdated = userMapper.toDomain(userCrudRepo.save(user.get()));


            return  userUpdated;

        }catch (EntityNotFoundException e){
            throw new  AppException("UserDetail with ID"+ uuid + "not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void delete(String uuid) {
        try {
            User user = userCrudRepo.findByUuid(uuid);
            if (user == null) {
                throw new AppException("User not found with UUID: " + uuid, HttpStatus.NOT_FOUND);
            }

            // Eliminar el usuario
            userCrudRepo.delete(user);

        } catch (DataIntegrityViolationException e) {
            throw new AppException("Cannot delete user due to existing references", HttpStatus.CONFLICT);
        } catch (Exception e) {
            throw new AppException("Error deleting user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Agregar estos métodos a la clase existente

    @Transactional
    @Override
    public void updatePromotionStatus(Integer userId, String promotionStatus) {
        try {
            // Primero verificamos que el usuario existe
            User user = userCrudRepo.findById(userId)
                    .orElseThrow(() -> new AppException("User not found with ID: " + userId, HttpStatus.NOT_FOUND));

            // Actualizamos usando el nuevo promotionStatus que recibimos como parámetro
            userCrudRepo.updatePromotionStatusById(promotionStatus, userId);
        } catch (Exception e) {
            throw new AppException("Error updating promotion status for user: " + userId, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @Override
    public void updateBulkPromotionStatus(List<UserDomain> users) {
        try {
            for (UserDomain userDomain : users) {
                // Primero verificamos que el usuario existe
                User user = userCrudRepo.findById(userDomain.getId())
                        .orElseThrow(() -> new AppException("User not found with ID: " + userDomain.getId(), HttpStatus.NOT_FOUND));

                // Actualizamos usando el promotionStatus del userDomain
                userCrudRepo.updatePromotionStatusById(userDomain.getPromotionStatus(), userDomain.getId());
            }
        } catch (Exception e) {
            throw new AppException("Error updating bulk promotion status", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Transactional
    @Override
    public UserDetailDomain registerByGroupinStudentUser(UserRegistrationDomain registrationDomain) {
        // Validaciones previas
        if (registrationDomain.getUserDomain() == null) {
            throw new AppException("User domain is required", HttpStatus.BAD_REQUEST);
        }
        if (registrationDomain.getUserDetailDomain() == null) {
            throw new AppException("User detail domain is required", HttpStatus.BAD_REQUEST);
        }
        if (registrationDomain.getGroupId() == null) {
            throw new AppException("Group ID is required", HttpStatus.BAD_REQUEST);
        }

        try {
            log.info("Starting user registration process");

            // 1. Crear el usuario
            log.debug("Creating user");
            User user = userMapper.toEntity(registrationDomain.getUserDomain());
            User savedUser = userCrudRepo.save(user);
            if (savedUser == null) {
                throw new AppException("Error creating user", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            UserDomain savedUserDomain = userMapper.toDomain(savedUser);

            // 2. Asignar el usuario creado al userDetail y guardarlo
            log.debug("Creating user detail");
            registrationDomain.getUserDetailDomain().setUser(savedUserDomain);
            UserDetailDomain savedUserDetail = persistenceUserDetailPort.save(registrationDomain.getUserDetailDomain());
            if (savedUserDetail == null) {
                // Si falla, eliminamos el usuario creado
                userCrudRepo.deleteById(savedUser.getId());
                delete(savedUser.getUsername());
                throw new AppException("Error creating user detail", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // 3. Crear la relación en GroupStudent
            log.debug("Creating group student relation");
            GroupStudentsDomain groupStudentsDomain = GroupStudentsDomain.builder().build();
            groupStudentsDomain.setStudent(savedUserDomain);

            GroupsDomain groupsDomain = GroupsDomain.builder().build();
            groupsDomain.setId(registrationDomain.getGroupId());
            groupStudentsDomain.setGroup(groupsDomain);
            groupStudentsDomain.setStatus("A");

            GroupStudentsDomain savedGroupStudent = persistenceGroupStudentPort.save(groupStudentsDomain);
            if (savedGroupStudent == null) {
                // Si falla, eliminamos el usuario y el user detail
                userCrudRepo.deleteById(savedUser.getId());
                delete(savedUser.getUsername());
                throw new AppException("Error creating group student relation", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // 4. Crear la relación de RoleStudent
            log.debug("Creating student role relation");

            // Buscar el rol de "estudiante" en la base de datos
            List<RoleDomain> roles = persistenceRolePort.findAll();
            RoleDomain studentRole = null;
            for (RoleDomain role : roles) {
                if ("estudiante".equalsIgnoreCase(role.getRoleName())) {
                    studentRole = role;
                    break;
                }
            }

            if (studentRole == null) {
                log.error("Student role not found in the database");
                // Limpiar recursos creados si no se encuentra el rol
                persistenceGroupStudentPort.delete(savedGroupStudent.getId());
                userCrudRepo.deleteById(savedUser.getId());
                delete(savedUser.getUsername());
                throw new AppException("Student role not found in the database", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // Crear el objeto UserRoleDomain
            UserRoleDomain userRoleDomain = UserRoleDomain.builder().build();
            userRoleDomain.setUserId(savedUser.getId());
            userRoleDomain.setRole(studentRole);

            // Guardar la relación usuario-rol
            UserRoleDomain savedUserRole = persistenceUserRolePort.save(userRoleDomain);
            if (savedUserRole == null) {
                // Si falla, limpiamos todo lo creado anteriormente
                persistenceGroupStudentPort.delete(savedGroupStudent.getId());
                userCrudRepo.deleteById(savedUser.getId());
                delete(savedUser.getUsername());
                throw new AppException("Error creating student role relation", HttpStatus.INTERNAL_SERVER_ERROR);
            }


            log.info("User registration completed successfully");
            return savedUserDetail;

        } catch (DataIntegrityViolationException e) {
            log.error("Duplicate entry found during registration", e);
            if (registrationDomain.getUserDomain().getUsername() != null) {
                delete(registrationDomain.getUserDomain().getUsername());
            }
            throw new AppException("Duplicate entry found: " + e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error("Error in registration process", e);
            if (registrationDomain.getUserDomain().getUsername() != null) {
                delete(registrationDomain.getUserDomain().getUsername());
            }
            throw new AppException("Error in registration process: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @Override
    public UserDetailDomain registerAdministrativeUsers(UserRegistrationDomain registrationDomain) {
        // Validaciones previas
        if (registrationDomain.getUserDomain() == null) {
            throw new AppException("User domain is required", HttpStatus.BAD_REQUEST);
        }
        if (registrationDomain.getUserDetailDomain() == null) {
            throw new AppException("User detail domain is required", HttpStatus.BAD_REQUEST);
        }
        if (registrationDomain.getUserDomain().getRoles() == null || registrationDomain.getUserDomain().getRoles().isEmpty()) {
            throw new AppException("At least one role is required", HttpStatus.BAD_REQUEST);
        }

        try {
            log.info("Starting administrative user registration process");

            // 1. Crear el usuario
            log.debug("Creating user");
            User user = userMapper.toEntity(registrationDomain.getUserDomain());
            User savedUser = userCrudRepo.save(user);
            if (savedUser == null) {
                throw new AppException("Error creating user", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            UserDomain savedUserDomain = userMapper.toDomain(savedUser);

            // 2. Asignar el usuario creado al userDetail y guardarlo
            log.debug("Creating user detail");
            registrationDomain.getUserDetailDomain().setUser(savedUserDomain);
            UserDetailDomain savedUserDetail = persistenceUserDetailPort.save(registrationDomain.getUserDetailDomain());
            if (savedUserDetail == null) {
                // Si falla, eliminamos el usuario creado
                userCrudRepo.deleteById(savedUser.getId());
                delete(savedUser.getUsername());
                throw new AppException("Error creating user detail", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // 3. Crear las relaciones de roles para el usuario
            log.debug("Creating role relations for administrative user");

            if (registrationDomain.getUserDomain().getRoles().isEmpty()) {
                log.error("No roles provided for administrative user");
                // Limpiar recursos creados si no hay roles
                userCrudRepo.deleteById(savedUser.getId());
                delete(savedUser.getUsername());
                throw new AppException("Roles were not loaded", HttpStatus.BAD_REQUEST);
            }

            // Iterar sobre cada rol y crear la relación usuario-rol
            for (UserRoleDomain roleDomain : registrationDomain.getUserDomain().getRoles()) {
                UserRoleDomain userRoleDomain = UserRoleDomain.builder()
                        .userId(savedUser.getId())
                        .role(roleDomain.getRole())
                        .build();

                UserRoleDomain savedUserRole = persistenceUserRolePort.save(userRoleDomain);
                if (savedUserRole == null) {
                    // Si falla, limpiamos todo lo creado anteriormente
                    userCrudRepo.deleteById(savedUser.getId());
                    delete(savedUser.getUsername());
                    throw new AppException("Error creating role relation", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            log.info("Administrative user registration completed successfully");
            return savedUserDetail;

        } catch (DataIntegrityViolationException e) {
            log.error("Duplicate entry found during registration", e);
            if (registrationDomain.getUserDomain().getUsername() != null) {
                delete(registrationDomain.getUserDomain().getUsername());
            }
            throw new AppException("Duplicate entry found: " + e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error("Error in registration process", e);
            if (registrationDomain.getUserDomain().getUsername() != null) {
                delete(registrationDomain.getUserDomain().getUsername());
            }
            throw new AppException("Error in registration process: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @Override
    public void patchGeneralUser(Integer userId, UserRegistrationDomain registrationDomain) {
        // Validaciones previas
        if (userId == null) {
            throw new AppException("User ID is required", HttpStatus.BAD_REQUEST);
        }
        if (registrationDomain == null) {
            throw new AppException("Registration data is required", HttpStatus.BAD_REQUEST);
        }

        try {
            log.info("Starting user patch process for userId: {}", userId);

            // 1. Buscar el usuario existente
            User existingUser = userCrudRepo.findById(userId)
                    .orElseThrow(() -> new AppException("User not found with ID: " + userId, HttpStatus.NOT_FOUND));

            // Convertir a dominio para operaciones posteriores
            UserDomain existingUserDomain = userMapper.toDomain(existingUser);

            // 2. Actualizar los campos del usuario si están presentes en el objeto de entrada
            if (registrationDomain.getUserDomain() != null) {
                UserDomain userDomain = registrationDomain.getUserDomain();

                // Actualizar solo los campos que no son nulos
                if (userDomain.getEmail() != null) existingUser.setEmail(userDomain.getEmail());
                if (userDomain.getUsername() != null) existingUser.setUsername(userDomain.getUsername());
                if (userDomain.getPassword() != null) existingUser.setPassword(userDomain.getPassword());
                if (userDomain.getFirstName() != null) existingUser.setFirstName(userDomain.getFirstName());
                if (userDomain.getLastName() != null) existingUser.setLastName(userDomain.getLastName());
                if (userDomain.getStatus() != null) existingUser.setStatus(userDomain.getStatus());
                if (userDomain.getPromotionStatus() != null) existingUser.setPromotionStatus(userDomain.getPromotionStatus());

                // Guardar los cambios en el usuario
                userCrudRepo.save(existingUser);
                log.debug("User data updated for userId: {}", userId);
            }

            // 3. Actualizar el detalle del usuario si está presente
            if (registrationDomain.getUserDetailDomain() != null) {
                UserDetailDomain userDetailToUpdate = registrationDomain.getUserDetailDomain();

                // Buscar el detalle del usuario existente
                UserDetailDomain existingUserDetail = persistenceUserDetailPort.findById(userId);

                if (existingUserDetail != null) {
                    // Actualizar solo los campos que no son nulos
                    if (userDetailToUpdate.getIdType() != null) existingUserDetail.setIdType(userDetailToUpdate.getIdType());
                    if (userDetailToUpdate.getDni() != null) existingUserDetail.setDni(userDetailToUpdate.getDni());
                    if (userDetailToUpdate.getPhoneNumber() != null) existingUserDetail.setPhoneNumber(userDetailToUpdate.getPhoneNumber());
                    if (userDetailToUpdate.getAddress() != null) existingUserDetail.setAddress(userDetailToUpdate.getAddress());
                    if (userDetailToUpdate.getDateOfBirth() != null) existingUserDetail.setDateOfBirth(userDetailToUpdate.getDateOfBirth());
                    // Agregar los campos faltantes
                    if (userDetailToUpdate.getFirstName() != null) existingUserDetail.setFirstName(userDetailToUpdate.getFirstName());
                    if (userDetailToUpdate.getMiddleName() != null) existingUserDetail.setMiddleName(userDetailToUpdate.getMiddleName());
                    if (userDetailToUpdate.getLastName() != null) existingUserDetail.setLastName(userDetailToUpdate.getLastName());
                    if (userDetailToUpdate.getSecondLastName() != null) existingUserDetail.setSecondLastName(userDetailToUpdate.getSecondLastName());
                    if (userDetailToUpdate.getNeighborhood() != null) existingUserDetail.setNeighborhood(userDetailToUpdate.getNeighborhood());
                    if (userDetailToUpdate.getCity() != null) existingUserDetail.setCity(userDetailToUpdate.getCity());
                    if (userDetailToUpdate.getPositionJob() != null) existingUserDetail.setPositionJob(userDetailToUpdate.getPositionJob());

                    // Conservar la referencia al usuario
                    existingUserDetail.setUser(existingUserDomain);

                    // Guardar los cambios en el detalle del usuario
                    persistenceUserDetailPort.update(existingUserDetail.getUser().getId(), existingUserDetail);
                    log.debug("User detail updated for userDetailId: {}", existingUserDetail.getId());
                } else {
                    // Si no existe un detalle, crearlo (esto no debería ocurrir normalmente)
                    log.warn("User detail not found for userId: {}. Creating new one.", userId);
                    userDetailToUpdate.setUser(existingUserDomain);
                    persistenceUserDetailPort.save(userDetailToUpdate);
                    log.debug("New user detail created for userId: {}", userId);
                }
            }

            // 4. Actualizar roles si se proporcionan (solo para usuarios administrativos)
            if (registrationDomain.getUserDomain() != null &&
                    registrationDomain.getUserDomain().getRoles() != null) {

                // Obtener la lista actual de roles del usuario
                List<UserRoleDomain> allRoles = persistenceUserRolePort.findAll();

                // Filtrar los roles actuales del usuario
                List<UserRoleDomain> currentUserRoles = allRoles.stream()
                        .filter(role -> role.getUserId() != null && role.getUserId().equals(userId))
                        .collect(Collectors.toList());

                log.debug("Current roles for userId {}: {}", userId,
                        currentUserRoles.stream()
                                .map(role -> role.getRole() != null && role.getRole().getRoleName() != null ?
                                        role.getRole().getRoleName() : "null")
                                .collect(Collectors.joining(", ")));

                // Si la lista de roles está vacía, se eliminan todos los roles existentes
                if (registrationDomain.getUserDomain().getRoles().isEmpty()) {
                    log.debug("Removing all roles for userId: {}", userId);
                    for (UserRoleDomain userRoleDomain : currentUserRoles) {
                        persistenceUserRolePort.delete(userRoleDomain.getId());
                    }
                    log.debug("All roles removed for userId: {}", userId);
                } else {
                    // Conjunto de IDs de roles que debe tener el usuario (los nuevos)
                    Set<Integer> targetRoleIds = registrationDomain.getUserDomain().getRoles().stream()
                            .filter(role -> role != null && role.getRole() != null && role.getRole().getId() != null)
                            .map(role -> role.getRole().getId())
                            .collect(Collectors.toSet());

                    // Conjunto de IDs de roles actuales
                    Set<Integer> currentRoleIds = currentUserRoles.stream()
                            .filter(role -> role.getRole() != null && role.getRole().getId() != null)
                            .map(role -> role.getRole().getId())
                            .collect(Collectors.toSet());

                    // Eliminar roles que ya no están en la lista de destino
                    for (UserRoleDomain currentRole : currentUserRoles) {
                        if (currentRole.getRole() != null && currentRole.getRole().getId() != null &&
                                !targetRoleIds.contains(currentRole.getRole().getId())) {
                            log.debug("Removing role: {} (ID: {}) for userId: {}",
                                    currentRole.getRole().getRoleName(), currentRole.getRole().getId(), userId);
                            persistenceUserRolePort.delete(currentRole.getId());
                        }
                    }

                    // Agregar nuevos roles que no existen actualmente
                    for (UserRoleDomain roleDomain : registrationDomain.getUserDomain().getRoles()) {
                        // Validar que el rol no sea nulo antes de intentar guardar
                        if (roleDomain == null || roleDomain.getRole() == null || roleDomain.getRole().getId() == null) {
                            log.warn("Invalid role domain found in roles collection for userId: {}, skipping...", userId);
                            continue;
                        }

                        // Verificar si el rol ya está asignado al usuario
                        if (!currentRoleIds.contains(roleDomain.getRole().getId())) {
                            log.debug("Adding new role: {} for userId: {}", roleDomain.getRole().getRoleName(), userId);

                            UserRoleDomain userRoleDomain = UserRoleDomain.builder()
                                    .userId(userId)
                                    .role(roleDomain.getRole())
                                    .build();

                            persistenceUserRolePort.save(userRoleDomain);
                            log.debug("Role saved successfully: {}", roleDomain.getRole().getRoleName());
                        }
                    }
                    log.debug("Roles updated for userId: {}", userId);
                }
            }

            // Nota: No se modifica la asociación de grupo para estudiantes
            // Esto cumple con el requerimiento de no modificar el grupo para estudiantes

            log.info("User patch completed successfully for userId: {}", userId);

        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation during user patch", e);
            throw new AppException("Duplicate entry or constraint violation: " + e.getMessage(), HttpStatus.CONFLICT);
        } catch (AppException e) {
            log.error("Application exception during user patch", e);
            throw e; // Re-throw AppExceptions as they already have the appropriate status
        } catch (Exception e) {
            log.error("Error in user patch process", e);
            throw new AppException("Error in patch process: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
