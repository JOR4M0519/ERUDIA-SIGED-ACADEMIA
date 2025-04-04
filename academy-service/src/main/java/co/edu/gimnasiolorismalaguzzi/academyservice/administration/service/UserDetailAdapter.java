package co.edu.gimnasiolorismalaguzzi.academyservice.administration.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.UserDetail;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.UserRole;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.UserDetailMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.IdTypeMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.UserMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository.UserDetailCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository.UserRoleCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceUserDetailPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDetailDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class UserDetailAdapter implements PersistenceUserDetailPort {


    private final UserDetailCrudRepo userDetailCrudRepo;
    private final UserRoleCrudRepo userRoleCrudRepo;


    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserDetailMapper userDetailMapper;

    @Autowired
    private IdTypeMapper typeMapper;



    public UserDetailAdapter(UserDetailCrudRepo userDetailCrudRepo, UserRoleCrudRepo userRoleCrudRepo) {
        this.userDetailCrudRepo = userDetailCrudRepo;
        this.userRoleCrudRepo = userRoleCrudRepo;
    }

    @Override
    public List<UserDetailDomain> findAll() {
        return this.userDetailMapper.toDomains(this.userDetailCrudRepo.findAll());
    }

    /*@Override
    public UserDetailDomain findById(Integer uuid) {
        Optional<UserDetail> userDetailOptional = Optional.ofNullable(userDetailCrudRepo.findByUser_Id(uuid));
        return userDetailOptional.map(userDetailMapper::toDomain).orElse(null);
    }
*/

    /**
     * Verifica si un usuario tiene rol de estudiante
     * @param userId ID del usuario a verificar
     * @return true si tiene rol de estudiante, false en caso contrario
     */
    @Override
    public boolean hasStudentRole(Integer userId) {
        try {
            // Obtenemos el usuario por su ID
            UserDomain userDomain = findByUser_Id(userId).getUser();

            if (userDomain == null ||
                    userDomain.getRoles() == null) {
                return false;
            }

            // Verificamos si alguno de los roles es "estudiante"
            return userDomain.getRoles().stream()
                    .anyMatch(role -> role != null &&
                            role.getRole() != null &&
                            "estudiante".equalsIgnoreCase(role.getRole().getRoleName()));
        } catch (Exception e) {
            log.error("Error al verificar rol de estudiante para usuario {}: {}", userId, e.getMessage());
            return false; // O lanzar una excepción dependiendo de tu manejo de errores
        }
    }


    @Override
    public UserDetailDomain findById(Integer uuid) {
        Optional<UserDetail> userDetailOptional = userDetailCrudRepo.findById(uuid);
        return userDetailOptional.map(userDetailMapper::toDomain).orElse(null);
    }

    @Override
    @Transactional
    public UserDetailDomain findByUser_Id(Integer id) {
        try {
            // Modificar para usar una consulta que cargue los roles
            UserDetail userDetail = userDetailCrudRepo.findByUser_Id(id);

            // Si el usuario existe, cargar explícitamente sus roles
            if (userDetail.getUser() != null) {
                // Obtener los roles directamente desde el repositorio
                List<UserRole> userRoles = userRoleCrudRepo.findByUserId(userDetail.getUser().getId());
                userDetail.getUser().setUserRoles(new LinkedHashSet<>(userRoles));
            }

            return userDetailMapper.toDomain(userDetail);
        } catch (EntityNotFoundException e) {
            log.error("Error getting UserDetail by id: {}", id, e);
            throw new AppException(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error getting UserDetail by id: {}", id, e);
            throw new AppException("Error getting UserDetail by id: " + id, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    @Transactional
    public UserDetailDomain save(UserDetailDomain userDetailDomain) {
        try {
            log.debug("Saving user detail: {}", userDetailDomain);

            // Validaciones
            if (userDetailDomain == null) {
                throw new AppException("UserDetailDomain cannot be null", HttpStatus.BAD_REQUEST);
            }
            if (userDetailDomain.getUser() == null) {
                throw new AppException("User reference cannot be null", HttpStatus.BAD_REQUEST);
            }

            // Convertir a entidad
            UserDetail userDetail = userDetailMapper.toEntity(userDetailDomain);

            // Guardar
            UserDetail savedUserDetail = userDetailCrudRepo.save(userDetail);

            // Convertir resultado a domain y retornar
            UserDetailDomain savedDomain = userDetailMapper.toDomain(savedUserDetail);
            log.debug("Successfully saved user detail with ID: {}", savedDomain.getId());

            return savedDomain;

        } catch (DataIntegrityViolationException e) {
            log.error("Error saving user detail - duplicate entry", e);
            throw new AppException("Duplicate entry found in user detail", HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error("Error saving user detail", e);
            throw new AppException("Error saving user detail: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @Override
    public UserDetailDomain update(Integer uuid, UserDetailDomain userDetailDomain) {
        try{
            UserDetail existingUserDetail = userDetailCrudRepo.findByUser_Id(uuid);

            // Actualizar solo los campos que no sean nulos
            if (userDetailDomain.getUser() != null) existingUserDetail.setUser(userMapper.toEntity(userDetailDomain.getUser()));
            if (userDetailDomain.getFirstName() != null) existingUserDetail.setFirstName(userDetailDomain.getFirstName());
            if (userDetailDomain.getMiddleName() != null) existingUserDetail.setMiddleName(userDetailDomain.getMiddleName());
            if (userDetailDomain.getLastName() != null) existingUserDetail.setLastName(userDetailDomain.getLastName());
            if (userDetailDomain.getSecondLastName() != null) existingUserDetail.setSecondLastName(userDetailDomain.getSecondLastName());
            if (userDetailDomain.getAddress() != null) existingUserDetail.setAddress(userDetailDomain.getAddress());
            if (userDetailDomain.getPhoneNumber() != null) existingUserDetail.setPhoneNumber(userDetailDomain.getPhoneNumber());
            if (userDetailDomain.getDateOfBirth() != null) existingUserDetail.setDateOfBirth(userDetailDomain.getDateOfBirth());
            if (userDetailDomain.getDni() != null) existingUserDetail.setDni(userDetailDomain.getDni());
            if (userDetailDomain.getIdType() != null) existingUserDetail.setIdType(typeMapper.toEntity(userDetailDomain.getIdType()));
            if (userDetailDomain.getNeighborhood() != null) existingUserDetail.setNeighborhood(userDetailDomain.getNeighborhood());
            if (userDetailDomain.getCity() != null) existingUserDetail.setCity(userDetailDomain.getCity());
            if (userDetailDomain.getPositionJob() != null) existingUserDetail.setPositionJob(userDetailDomain.getPositionJob());

            return userDetailMapper.toDomain(userDetailCrudRepo.save(existingUserDetail));

        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException("UserDetail with ID " + uuid + " not found");
        }
       }

    @Override
    public HttpStatus delete(Integer uuid) {
    /*    try{
            if (this.userDetailCrudRepository.existsById(id)) {
                this.userDetailCrudRepository.deleteById(id);
                return HttpStatus.OK;
            } else {
                throw new AppException("User ID doesnt exist", HttpStatus.NOT_FOUND);
            }
        }catch(Exception e){
            throw new AppException("INTERN ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }*/
        return HttpStatus.OK;
    }

    @Override
    public UserDetailDomain saveDetailUser(String uuid, UserDetailDomain user) {
        return null;
    }

    @Override
    public UserDetailDomain findByUsername(String username) {
        Optional<UserDetail> userDetailOptional = Optional.ofNullable(userDetailCrudRepo.findByUser_Username(username));
        return userDetailOptional.map(userDetailMapper::toDomain).orElse(null);
    }




}
