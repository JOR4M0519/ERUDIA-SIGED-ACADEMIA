package co.edu.gimnasiolorismalaguzzi.academyservice.adapter.out.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.adapter.out.persistence.entity.UserDetail;
import co.edu.gimnasiolorismalaguzzi.academyservice.adapter.out.persistence.mapper.UserDetailMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.adapter.out.persistence.repository.PersistenceUserDetailAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.adapter.out.persistence.repository.UserCrudRepository;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDetailDomain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class UserRepository implements PersistenceUserDetailAdapter {

    private final UserCrudRepository userCrudRepository; // Repositorio JPA

    @Autowired
    private UserDetailMapper userMapper; // Mapper inyectado

    public UserRepository(UserCrudRepository userCrudRepository) {
        this.userCrudRepository = userCrudRepository;
    }

    @Override
    public List<UserDetailDomain> findAllUsers() {
        return this.userMapper.toDomains(this.userCrudRepository.findAll());
    }

    @Override
    public UserDetailDomain findUserById(Integer id) {
        Optional<UserDetail> userDetailOptional = this.userCrudRepository.findById(id);
        return userDetailOptional.map(userMapper::toDomain).orElse(null);
    }

    @Override
    public UserDetailDomain saveUser(UserDetailDomain userDetailDomain) {
        UserDetail userDetail = userMapper.toEntity(userDetailDomain);
        UserDetail savedUserDetail = this.userCrudRepository.save(userDetail);
        return userMapper.toDomain(savedUserDetail);
    }

    @Override
    public UserDetailDomain updateUser(Integer id, UserDetailDomain userDetailDomain) {
        UserDetail userDetail = userMapper.toEntity(userDetailDomain);
        userDetail.setId(id); // Aseguramos que el ID se mantenga
        UserDetail updatedUserDetail = this.userCrudRepository.save(userDetail);
        return userMapper.toDomain(updatedUserDetail);
    }

    @Override
    public void deleteUser(Integer id) {
        if (this.userCrudRepository.existsById(id)) {
            this.userCrudRepository.deleteById(id);
        } else {
            log.warn("El usuario con ID {} no existe.", id);
        }
    }
}
