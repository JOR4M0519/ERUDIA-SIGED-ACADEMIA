package co.edu.gimnasiolorismalaguzzi.academyservice.administration.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;


import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.UserMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository.UserCrudRepo;
import jakarta.persistence.EntityNotFoundException;

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


    public UserAdapter(UserCrudRepo userCrudRepo) {
        this.userCrudRepo = userCrudRepo;
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

    }
}
