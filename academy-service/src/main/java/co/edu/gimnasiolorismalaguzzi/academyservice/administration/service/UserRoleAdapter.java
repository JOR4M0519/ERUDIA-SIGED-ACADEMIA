package co.edu.gimnasiolorismalaguzzi.academyservice.administration.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserRoleDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.UserRole;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.RoleMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.UserMapper;
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
    private UserRoleMapper userRoleMapper;
    private UserMapper userMapper;

    private final RoleMapper roleMapper;


    public UserRoleAdapter(UserRoleCrudRepo userRoleCrudRepo, UserRoleMapper userRoleMapper, UserMapper userMapper, RoleMapper roleMapper) {
        this.userRoleCrudRepo = userRoleCrudRepo;
        this.userRoleMapper = userRoleMapper;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
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
                userRole.setUser(User.builder().id(userRoleDomain.getId()).build());
                userRole.setRole(roleMapper.toEntity(userRoleDomain.getRole()));
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

    @Override
    public List<UserDomain> getStudents() {
        return userMapper.toDomains(this.userRoleCrudRepo.findAllStudents());
    }

    @Override
    public List<UserDomain> getAdministrativeUsers() {
        return userMapper.toDomains(this.userRoleCrudRepo.findAllAdministrativeUsers());
    }
}
