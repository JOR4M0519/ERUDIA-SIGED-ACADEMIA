package co.edu.gimnasiolorismalaguzzi.academyservice.administration.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserRoleDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.UserRole;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.UserRoleMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository.UserRoleCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceUserRolePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class UserRoleAdapter implements PersistenceUserRolePort {

    private final UserRoleCrudRepo userRoleCrudRepo;

    @Autowired
    private UserRoleMapper userRoleMapper;

    public UserRoleAdapter(UserRoleCrudRepo userRoleCrudRepo) {
        this.userRoleCrudRepo = userRoleCrudRepo;
    }

    @Override
    public List<UserRoleDomain> findAll() {
        return userRoleMapper.toDomains(this.userRoleCrudRepo.findAll());
    }

    @Override
    public UserRoleDomain findById(Integer integer) {
        Optional<UserRole> userRole = this.userRoleCrudRepo.findById(integer);
        return userRole.map(userRoleMapper::toDomain).orElse(null);
    }

    @Override
    public UserRoleDomain save(UserRoleDomain userRoleDomain) {
        UserRole userRole = userRoleMapper.toEntity(userRoleDomain);
        UserRole savedUserRole = this.userRoleCrudRepo.save(userRole);
        return userRoleMapper.toDomain(savedUserRole);
    }

    @Override
    public UserRoleDomain update(Integer integer, UserRoleDomain userRoleDomain) {
        try {
            Optional<UserRole> existingUserRole = userRoleCrudRepo.findById(integer);
            if (existingUserRole.isPresent()) {
                UserRole userRole = existingUserRole.get();
                userRole.setUser(userRoleDomain.getUser());
                userRole.setRole(userRoleDomain.getRole());
                return userRoleMapper.toDomain(userRoleCrudRepo.save(userRole));
            }
            throw new EntityNotFoundException("UserRole with id: " + integer + " not found!");
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("UserRole with id: " + integer + " not found!");
        }
    }

    @Override
    public HttpStatus delete(Integer integer) {
        try {
            userRoleCrudRepo.deleteById(integer);
            return HttpStatus.OK;
        } catch (Exception e) {
            return HttpStatus.CONFLICT;
        }
    }
}
