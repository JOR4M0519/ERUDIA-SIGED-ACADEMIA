package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.mapper.UserMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.repository.UserCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceUserPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDomain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class UserAdapter implements PersistenceUserPort {

    private final UserCrudRepo userCrudRepo; // Repositorio JPA

    @Autowired
    private UserMapper userMapper;

    public UserAdapter(UserCrudRepo userCrudRepo) {
        this.userCrudRepo = userCrudRepo;
    }

    @Override
    public List<UserDomain> findAll() {
        return this.userMapper.toDomains(this.userCrudRepo.findAll());
    }

    @Override
    public UserDomain findById(Integer id) {
        Optional<User> userOptional = this.userCrudRepo.findById(id);
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
            if (this.userCrudRepo.existsById(integer)) {
                userCrudRepo.updateStatusById("D",integer);
                return HttpStatus.OK;
            } else {
                throw new AppException("User ID doesnt exist", HttpStatus.NOT_FOUND);
            }
        }catch(Exception e){
            throw new AppException("INTERN ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
