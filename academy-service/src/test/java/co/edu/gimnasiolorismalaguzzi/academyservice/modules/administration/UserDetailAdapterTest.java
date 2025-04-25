package co.edu.gimnasiolorismalaguzzi.academyservice.modules.administration;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.*;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.UserDetail;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.UserRole;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.*;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository.*;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.UserDetailAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailAdapterTest {

    @Mock private UserDetailCrudRepo userDetailCrudRepo;
    @Mock private UserRoleCrudRepo userRoleCrudRepo;
    @Mock private UserMapper userMapper;
    @Mock private UserDetailMapper userDetailMapper;
    @Mock private IdTypeMapper typeMapper;
    @InjectMocks private UserDetailAdapter adapter;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(adapter, "userMapper", userMapper);
        ReflectionTestUtils.setField(adapter, "userDetailMapper", userDetailMapper);
        ReflectionTestUtils.setField(adapter, "typeMapper", typeMapper);
    }

    @Test
    void findAll_returnsMapped() {
        List<UserDetail> entities = List.of(new UserDetail());
        List<UserDetailDomain> domains = List.of(UserDetailDomain.builder().build());
        when(userDetailCrudRepo.findAll()).thenReturn(entities);
        when(userDetailMapper.toDomains(entities)).thenReturn(domains);

        List<UserDetailDomain> result = adapter.findAll();
        assertEquals(domains, result);
    }

    @Test
    void findAll_empty() {
        when(userDetailCrudRepo.findAll()).thenReturn(Collections.emptyList());
        when(userDetailMapper.toDomains(Collections.emptyList())).thenReturn(Collections.emptyList());

        assertTrue(adapter.findAll().isEmpty());
    }

    @Test
    void hasStudentRole_true() {
        UserDetailDomain detail = UserDetailDomain.builder()
                .user(UserDomain.builder()
                        .roles(Set.of(UserRoleDomain.builder()
                                .role(RoleDomain.builder().roleName("estudiante").build())
                                .build()))
                        .build()).build();
        UserDetailAdapter spy = Mockito.spy(adapter);
        doReturn(detail).when(spy).findByUser_Id(1);

        assertTrue(spy.hasStudentRole(1));
    }

    @Test
    void hasStudentRole_false_noRoles() {
        UserDetailDomain detail = UserDetailDomain.builder()
                .user(UserDomain.builder().roles(Set.of()).build()).build();
        UserDetailAdapter spy = Mockito.spy(adapter);
        doReturn(detail).when(spy).findByUser_Id(2);

        assertFalse(spy.hasStudentRole(2));
    }

    @Test
    void hasStudentRole_exception_returnsFalse() {
        UserDetailAdapter spy = Mockito.spy(adapter);
        doThrow(new RuntimeException()).when(spy).findByUser_Id(3);
        assertFalse(spy.hasStudentRole(3));
    }

    @Test
    void findById_present() {
        UserDetail entity = new UserDetail();
        UserDetailDomain domain = UserDetailDomain.builder().build();
        when(userDetailCrudRepo.findById(1)).thenReturn(Optional.of(entity));
        when(userDetailMapper.toDomain(entity)).thenReturn(domain);

        assertEquals(domain, adapter.findById(1));
    }

    @Test
    void findById_absent() {
        when(userDetailCrudRepo.findById(2)).thenReturn(Optional.empty());
        assertNull(adapter.findById(2));
    }

    @Test
    void findByUserId_notFound_throwsAppException() {
        when(userDetailCrudRepo.findByUser_Id(2)).thenThrow(new EntityNotFoundException());
        AppException ex = assertThrows(AppException.class, () -> adapter.findByUser_Id(2));
        assertEquals(HttpStatus.NOT_FOUND, ex.getCode());
    }

    @Test
    void findByUserId_error_throwsAppException() {
        when(userDetailCrudRepo.findByUser_Id(3)).thenThrow(new RuntimeException());
        AppException ex = assertThrows(AppException.class, () -> adapter.findByUser_Id(3));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getCode());
    }

    @Test
    void save_success() {
        UserDetailDomain input = UserDetailDomain.builder().user(UserDomain.builder().build()).build();
        UserDetail toSave = new UserDetail();
        UserDetail saved = new UserDetail();
        when(userDetailMapper.toEntity(input)).thenReturn(toSave);
        when(userDetailCrudRepo.save(toSave)).thenReturn(saved);
        when(userDetailMapper.toDomain(saved)).thenReturn(input);

        assertEquals(input, adapter.save(input));
    }

    @Test
    void save_nullDomain_throwsAppException() {
        AppException ex = assertThrows(AppException.class, () -> adapter.save(null));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getCode());
    }

    @Test
    void save_nullUser_throwsAppException() {
        UserDetailDomain input = UserDetailDomain.builder().build();
        AppException ex = assertThrows(AppException.class, () -> adapter.save(input));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getCode());
    }

    @Test
    void save_dataIntegrity_throwsAppException() {
        UserDetailDomain input = UserDetailDomain.builder().user(UserDomain.builder().build()).build();
        when(userDetailMapper.toEntity(input)).thenReturn(new UserDetail());
        doThrow(new DataIntegrityViolationException("dup")).when(userDetailCrudRepo).save(any());
        AppException ex = assertThrows(AppException.class, () -> adapter.save(input));
        assertEquals(HttpStatus.CONFLICT, ex.getCode());
    }

    @Test
    void save_error_throwsAppException() {
        UserDetailDomain input = UserDetailDomain.builder().user(UserDomain.builder().build()).build();
        when(userDetailMapper.toEntity(input)).thenReturn(new UserDetail());
        doThrow(new RuntimeException("err")).when(userDetailCrudRepo).save(any());
        AppException ex = assertThrows(AppException.class, () -> adapter.save(input));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getCode());
    }

    @Test
    void update_success() {
        UserDetail entity = new UserDetail();
        when(userDetailCrudRepo.findByUser_Id(1)).thenReturn(entity);
        when(userDetailCrudRepo.save(entity)).thenReturn(entity);
        UserDetailDomain domain = UserDetailDomain.builder().build();
        when(userDetailMapper.toDomain(entity)).thenReturn(domain);
        assertEquals(domain, adapter.update(1, domain));
    }

    @Test
    void update_notFound_throwsEntityNotFound() {
        when(userDetailCrudRepo.findByUser_Id(2)).thenThrow(new EntityNotFoundException());
        assertThrows(EntityNotFoundException.class, () -> adapter.update(2, UserDetailDomain.builder().build()));
    }

    @Test
    void delete_returnsOk() {
        assertEquals(HttpStatus.OK, adapter.delete(1));
    }

    @Test
    void saveDetailUser_returnsNull() {
        assertNull(adapter.saveDetailUser("u", UserDetailDomain.builder().build()));
    }

    @Test
    void findByUsername_present() {
        UserDetail entity = new UserDetail();
        UserDetailDomain domain = UserDetailDomain.builder().build();
        when(userDetailCrudRepo.findByUser_Username("u")).thenReturn(entity);
        when(userDetailMapper.toDomain(entity)).thenReturn(domain);
        assertEquals(domain, adapter.findByUsername("u"));
    }

    @Test
    void findByUsername_absent() {
        when(userDetailCrudRepo.findByUser_Username("x")).thenReturn(null);
        assertNull(adapter.findByUsername("x"));
    }
}
