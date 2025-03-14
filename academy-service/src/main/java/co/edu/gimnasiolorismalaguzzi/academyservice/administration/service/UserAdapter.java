package co.edu.gimnasiolorismalaguzzi.academyservice.administration.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDetailDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserRegistrationDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceUserDetailPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceUserPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;


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



    public UserAdapter(UserCrudRepo userCrudRepo, FamilyAdapter familyAdapter, PersistenceUserDetailPort persistenceUserDetailPort, PersistenceGroupStudentPort persistenceGroupStudentPort) {
        this.userCrudRepo = userCrudRepo;
        this.persistenceUserDetailPort = persistenceUserDetailPort;
        this.persistenceGroupStudentPort = persistenceGroupStudentPort;
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

            GroupStudentsDomain savedGroupStudent = persistenceGroupStudentPort.save(groupStudentsDomain);
            if (savedGroupStudent == null) {
                // Si falla, eliminamos el usuario y el user detail
                userCrudRepo.deleteById(savedUser.getId());
                delete(savedUser.getUsername());
                throw new AppException("Error creating group student relation", HttpStatus.INTERNAL_SERVER_ERROR);
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

}
