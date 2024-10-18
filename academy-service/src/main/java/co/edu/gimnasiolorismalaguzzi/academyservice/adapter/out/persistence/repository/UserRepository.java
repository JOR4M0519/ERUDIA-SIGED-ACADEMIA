package co.edu.gimnasiolorismalaguzzi.academyservice.adapter.out.persistence.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.adapter.out.persistence.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.adapter.out.persistence.mapper.IdTypeMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.adapter.out.persistence.mapper.UserMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.adapter.out.persistence.mapper.UserMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.adapter.out.persistence.repository.PersistenceUserAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.adapter.out.persistence.repository.UserCrudRepository;
import co.edu.gimnasiolorismalaguzzi.academyservice.adapter.out.persistence.repository.UserCrudRepository;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDomain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class UserRepository implements PersistenceUserAdapter {

    private final UserCrudRepository userCrudRepository; // Repositorio JPA

    @Autowired
    private UserMapper userMapper;

    public UserRepository(UserCrudRepository userCrudRepository) {
        this.userCrudRepository = userCrudRepository;
    }

    @Override
    public List<UserDomain> findAll() {
        return this.userMapper.toDomains(this.userCrudRepository.findAll());
    }

    @Override
    public UserDomain findById(Integer id) {
        Optional<User> userOptional = this.userCrudRepository.findById(id);
        return userOptional.map(userMapper::toDomain).orElse(null);
    }


    @Override
    public UserDomain save(Integer integer, UserDomain entity) {
        return null;
    }

    @Override
    public UserDomain update(Integer integer, UserDomain entity) {
        return null;
    }

    @Override
    public HttpStatus delete(Integer integer) {
        try{
            if (this.userCrudRepository.existsById(integer)) {
                userCrudRepository.updateStatusById("D",integer);
                return HttpStatus.OK;
            } else {
                throw new AppException("User ID doesnt exist", HttpStatus.NOT_FOUND);
            }
        }catch(Exception e){
            throw new AppException("INTERN ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
