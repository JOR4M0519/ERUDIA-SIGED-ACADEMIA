package co.edu.gimnasiolorismalaguzzi.academyservice.administration.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.RoleDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.Role;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.RoleMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository.RoleCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceRolePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class RoleAdapter implements PersistenceRolePort {

    private final RoleCrudRepo roleCrudRepo;

    @Autowired
    private RoleMapper roleMapper;

    public RoleAdapter(RoleCrudRepo roleCrudRepo) {
        this.roleCrudRepo = roleCrudRepo;
    }

    @Override
    public List<RoleDomain> findAll() {
        return roleMapper.toDomains(this.roleCrudRepo.findAll());
    }

    @Override
    public RoleDomain findById(Integer integer) {
        Optional<Role> role = this.roleCrudRepo.findById(integer);
        return role.map(roleMapper::toDomain).orElse(null);
    }

    @Override
    public RoleDomain save(RoleDomain roleDomain) {
        Role role = roleMapper.toEntity(roleDomain);
        Role savedRole = this.roleCrudRepo.save(role);
        return roleMapper.toDomain(savedRole);
    }

    @Override
    public RoleDomain update(Integer integer, RoleDomain roleDomain) {
        try {
            Optional<Role> existingRole = roleCrudRepo.findById(integer);
            if (existingRole.isPresent()) {
                Role role = existingRole.get();
                role.setRoleName(roleDomain.getRoleName());
                role.setStatus(roleDomain.getStatus());
                return roleMapper.toDomain(roleCrudRepo.save(role));
            }
            throw new EntityNotFoundException("Role with id: " + integer + " not found!");
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Role with id: " + integer + " not found!");
        }
    }

    @Override
    public HttpStatus delete(Integer integer) {
        try {
            roleCrudRepo.deleteById(integer);
            return HttpStatus.OK;
        } catch (Exception e) {
            return HttpStatus.CONFLICT;
        }
    }
}
