package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.UserDetail;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.UserDetailMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.IdTypeMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.UserMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository.UserDetailCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceUserDetailPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDetailDomain;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class UserDetailAdapter implements PersistenceUserDetailPort {


    private final UserDetailCrudRepo userDetailCrudRepo;

    @Autowired
    private UserAdapter userAdapter;
    @Autowired
    private UserMapper userMapper;



    @Autowired
    private UserDetailMapper userDetailMapper;

    @Autowired
    private IdTypeMapper typeMapper;


    public UserDetailAdapter(UserDetailCrudRepo userDetailCrudRepo) {
        this.userDetailCrudRepo = userDetailCrudRepo;
    }
    @Override
    public List<UserDetailDomain> findAll() {
        return this.userDetailMapper.toDomains(this.userDetailCrudRepo.findAll());
    }

    @Override
    public UserDetailDomain findById(String uuid) {
        Optional<UserDetail> userDetailOptional = Optional.ofNullable(userDetailCrudRepo.findByUser_Uuid(uuid));
        return userDetailOptional.map(userDetailMapper::toDomain).orElse(null);
    }

    @Override
    public UserDetailDomain save(UserDetailDomain entity) {
        return null;
    }

    @Override
    public UserDetailDomain saveDetailUser(String uuid, UserDetailDomain userDetailDomain) {
        userDetailDomain.setUser(userAdapter.searchUserByUuid(uuid));
        UserDetail userDetail = userDetailMapper.toEntity(userDetailDomain);
        UserDetail savedUserDetail = this.userDetailCrudRepo.save(userDetail);
        return userDetailMapper.toDomain(savedUserDetail);
    }

    @Override
    public UserDetailDomain update(String uuid, UserDetailDomain userDetailDomain) {
        try{
            UserDetail existingUserDetail = userDetailCrudRepo.findByUser_Uuid(uuid);

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
    public HttpStatus delete(String uuid) {
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
