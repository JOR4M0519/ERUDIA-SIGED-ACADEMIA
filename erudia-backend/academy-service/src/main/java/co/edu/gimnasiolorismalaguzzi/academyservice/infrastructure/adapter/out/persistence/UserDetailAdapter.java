package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.UserDetail;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.mapper.UserDetailMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.mapper.IdTypeMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.repository.UserDetailCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceUserDetailPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDetailDomain;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class UserDetailAdapter implements PersistenceUserDetailPort {


    private final UserDetailCrudRepo userDetailCrudRepo; // Repositorio JPA


    @Autowired
    private UserAdapter userAdapter;

    @Autowired
    private UserDetailMapper userDetailMapper;

    @Autowired// Mapper inyectado
    private IdTypeMapper typeMapper; // M


    public UserDetailAdapter(UserDetailCrudRepo userDetailCrudRepo) {
        this.userDetailCrudRepo = userDetailCrudRepo;
    }
    @Override
    public List<UserDetailDomain> findAll() {
        return this.userDetailMapper.toDomains(this.userDetailCrudRepo.findAll());
    }

    @Override
    public UserDetailDomain findById(Integer id) {
        Optional<UserDetail> userDetailOptional = Optional.ofNullable(this.userDetailCrudRepo.findByUser_Id(id));
        return userDetailOptional.map(userDetailMapper::toDomain).orElse(null);
    }

    @Override
    public UserDetailDomain save(UserDetailDomain entity) {
        return null;
    }

    @Override
    public UserDetailDomain saveDetailUser(Integer id, UserDetailDomain userDetailDomain) {
        userDetailDomain.setUser(userAdapter.findById(id));
        userDetailDomain.setId(this.userDetailCrudRepo.findByUser_Id(id).getId());
        UserDetail userDetail = userDetailMapper.toEntity(userDetailDomain);
        UserDetail savedUserDetail = this.userDetailCrudRepo.save(userDetail);
        return userDetailMapper.toDomain(savedUserDetail);
    }

    @Override
    public UserDetailDomain update(Integer id, UserDetailDomain userDetailDomain) {
        try{
            UserDetail existingUserDetail = userDetailCrudRepo.findByUser_Id(id);

            // Actualizar solo los campos que no sean nulos
            //if (userDetailDomain.getUser() != null) existingUserDetail.setUser(userMapper.toDomain(userDetailDomain.getId()));
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
            throw new EntityNotFoundException("UserDetail with ID " + id + " not found");
        }
       }

    @Override
    public HttpStatus delete(Integer id) {
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
}
