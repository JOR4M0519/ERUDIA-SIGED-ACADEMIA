package co.edu.gimnasiolorismalaguzzi.academyservice.modules.administration;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserRoleDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.RoleDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.UserRole;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.Role;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.UserRoleMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.UserMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.RoleMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository.UserRoleCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.UserRoleAdapter;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRoleAdapterTest {

    @Mock private UserRoleCrudRepo crudRepo;
    @Mock private UserRoleMapper userRoleMapper;
    @Mock private UserMapper userMapper;
    @Mock private RoleMapper roleMapper;
    @InjectMocks private UserRoleAdapter adapter;

    // --- findAll ---
    @Test
    void findAll_returnsMapped() {
        List<UserRole> entities = List.of(new UserRole());
        List<UserRoleDomain> domains = List.of(UserRoleDomain.builder().build());
        when(crudRepo.findAll()).thenReturn(entities);
        when(userRoleMapper.toDomains(entities)).thenReturn(domains);

        List<UserRoleDomain> result = adapter.findAll();

        assertEquals(domains, result);
        verify(crudRepo).findAll();
        verify(userRoleMapper).toDomains(entities);
    }

    @Test
    void findAll_empty() {
        when(crudRepo.findAll()).thenReturn(Collections.emptyList());
        when(userRoleMapper.toDomains(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<UserRoleDomain> result = adapter.findAll();

        assertTrue(result.isEmpty());
        verify(crudRepo).findAll();
        verify(userRoleMapper).toDomains(Collections.emptyList());
    }

    // --- findById ---
    @Test
    void findById_present() {
        UserRole entity = new UserRole();
        UserRoleDomain domain = UserRoleDomain.builder().build();
        when(crudRepo.findById(1)).thenReturn(Optional.of(entity));
        when(userRoleMapper.toDomain(entity)).thenReturn(domain);

        UserRoleDomain result = adapter.findById(1);

        assertEquals(domain, result);
        verify(crudRepo).findById(1);
        verify(userRoleMapper).toDomain(entity);
    }

    @Test
    void findById_absent() {
        when(crudRepo.findById(2)).thenReturn(Optional.empty());

        UserRoleDomain result = adapter.findById(2);

        assertNull(result);
        verify(crudRepo).findById(2);
        verify(userRoleMapper, never()).toDomain(any());
    }

    // --- save ---
    @Test
    void save_persistsEntity() {
        UserRoleDomain input = UserRoleDomain.builder().build();
        UserRole toSave = new UserRole();
        UserRole saved = new UserRole();
        UserRoleDomain out = UserRoleDomain.builder().build();

        when(userRoleMapper.toEntity(input)).thenReturn(toSave);
        when(crudRepo.save(toSave)).thenReturn(saved);
        when(userRoleMapper.toDomain(saved)).thenReturn(out);

        UserRoleDomain result = adapter.save(input);

        assertEquals(out, result);
        verify(userRoleMapper).toEntity(input);
        verify(crudRepo).save(toSave);
        verify(userRoleMapper).toDomain(saved);
    }

    // --- update ---
    @Test
    void update_present() {
        RoleDomain roleDomain = RoleDomain.builder().id(10).build();
        UserRoleDomain dto = UserRoleDomain.builder().id(20).role(roleDomain).build();
        UserRole existing = new UserRole();
        when(crudRepo.findById(5)).thenReturn(Optional.of(existing));
        Role roleEntity = new Role();
        when(roleMapper.toEntity(roleDomain)).thenReturn(roleEntity);
        when(crudRepo.save(existing)).thenReturn(existing);
        when(userRoleMapper.toDomain(existing)).thenReturn(dto);

        UserRoleDomain result = adapter.update(5, dto);

        assertEquals(dto, result);
        assertEquals(20, existing.getUser().getId());
        assertEquals(roleEntity, existing.getRole());
        verify(crudRepo).findById(5);
        verify(roleMapper).toEntity(roleDomain);
        verify(crudRepo).save(existing);
        verify(userRoleMapper).toDomain(existing);
    }

    @Test
    void update_absent_throwsEntityNotFound() {
        when(crudRepo.findById(6)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> adapter.update(6, UserRoleDomain.builder().build())
        );
        assertTrue(ex.getMessage().contains("UserRole with id: 6 not found!"));
        verify(crudRepo).findById(6);
    }

    // --- deleteByUserId ---
    @Test
    void deleteByUserId_null_returnsBadRequest() {
        HttpStatus status = adapter.deleteByUserId(null);
        assertEquals(HttpStatus.BAD_REQUEST, status);
        verifyNoInteractions(crudRepo);
    }

    @Test
    void deleteByUserId_success_returnsOk() {
        HttpStatus status = adapter.deleteByUserId(3);
        assertEquals(HttpStatus.OK, status);
        verify(crudRepo).deleteByUser_Id(3);
    }

    @Test
    void deleteByUserId_integrityViolation_returnsConflict() {
        doThrow(new DataIntegrityViolationException("err")).when(crudRepo).deleteByUser_Id(4);

        HttpStatus status = adapter.deleteByUserId(4);
        assertEquals(HttpStatus.CONFLICT, status);
        verify(crudRepo).deleteByUser_Id(4);
    }

    @Test
    void deleteByUserId_otherException_returnsInternalError() {
        doThrow(new RuntimeException("err")).when(crudRepo).deleteByUser_Id(5);

        HttpStatus status = adapter.deleteByUserId(5);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, status);
        verify(crudRepo).deleteByUser_Id(5);
    }

    // --- delete ---
    @Test
    void delete_success_returnsOk() {
        HttpStatus status = adapter.delete(7);
        assertEquals(HttpStatus.OK, status);
        verify(crudRepo).deleteById(7);
    }

    @Test
    void delete_failure_returnsConflict() {
        doThrow(new RuntimeException("err")).when(crudRepo).deleteById(8);

        HttpStatus status = adapter.delete(8);
        assertEquals(HttpStatus.CONFLICT, status);
        verify(crudRepo).deleteById(8);
    }

    // --- getStudents ---
    @Test
    void getStudents_returnsMapped() {
        List<User> users = List.of(new User());
        List<UserDomain> domains = List.of(UserDomain.builder().build());
        when(crudRepo.findAllStudents()).thenReturn(users);
        when(userMapper.toDomains(users)).thenReturn(domains);

        List<UserDomain> result = adapter.getStudents();
        assertEquals(domains, result);
        verify(crudRepo).findAllStudents();
        verify(userMapper).toDomains(users);
    }

    @Test
    void getAdministrativeUsers_returnsMapped() {
        List<User> users = List.of(new User());
        List<UserDomain> domains = List.of(UserDomain.builder().build());
        when(crudRepo.findAllAdministrativeUsers()).thenReturn(users);
        when(userMapper.toDomains(users)).thenReturn(domains);

        List<UserDomain> result = adapter.getAdministrativeUsers();
        assertEquals(domains, result);
        verify(crudRepo).findAllAdministrativeUsers();
        verify(userMapper).toDomains(users);
    }
}
