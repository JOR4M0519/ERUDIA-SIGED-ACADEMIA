package co.edu.gimnasiolorismalaguzzi.academyservice.modules.administration;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.*;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.UserDetail;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.Role;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.KeycloakAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.UserAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.*;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.UserMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence.PersistenceGroupStudentPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAdapterTest {

    @Mock private co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository.UserCrudRepo userCrudRepo;
    @Mock private UserMapper userMapper;
    @Mock private KeycloakAdapter keycloakAdapter;

    @InjectMocks private UserAdapter adapter;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(adapter, "userMapper", userMapper);
        ReflectionTestUtils.setField(adapter, "keycloakAdapter", keycloakAdapter);
    }

    @Test
    void findAll_returnsMapped() {
        List<User> entities = List.of(new User());
        List<UserDomain> domains = List.of(UserDomain.builder().build());
        when(userCrudRepo.findAll()).thenReturn(entities);
        when(userMapper.toDomains(entities)).thenReturn(domains);

        List<UserDomain> result = adapter.findAll();

        assertEquals(domains, result);
        verify(userCrudRepo).findAll();
        verify(userMapper).toDomains(entities);
    }

    @Test
    void findAll_empty() {
        when(userCrudRepo.findAll()).thenReturn(List.of());
        when(userMapper.toDomains(List.of())).thenReturn(List.of());

        List<UserDomain> result = adapter.findAll();
        assertTrue(result.isEmpty());
        verify(userCrudRepo).findAll();
    }

    @Test
    void searchUserByUuid_present() {
        User entity = new User();
        UserDomain domain = UserDomain.builder().build();
        when(userCrudRepo.findByUuid("uuid1")).thenReturn(entity);
        when(userMapper.toDomain(entity)).thenReturn(domain);

        UserDomain result = adapter.searchUserByUuid("uuid1");
        assertEquals(domain, result);
    }

    @Test
    void searchUserByUuid_absent() {
        when(userCrudRepo.findByUuid("none")).thenReturn(null);
        assertNull(adapter.searchUserByUuid("none"));
    }

    @Test
    void save_success() {
        UserDomain input = UserDomain.builder().username("u").build();
        User toSave = new User();
        User saved = new User();
        UserDomain out = UserDomain.builder().username("u").build();
        when(userMapper.toEntity(input)).thenReturn(toSave);
        when(userCrudRepo.save(toSave)).thenReturn(saved);
        when(userMapper.toDomain(saved)).thenReturn(out);

        String result = adapter.save(input);
        assertEquals(out.toString(), result);
    }

    @Test
    void save_dataIntegrity_throwsAppException() {
        UserDomain input = UserDomain.builder().username("u").build();
        User toSave = new User();
        when(userMapper.toEntity(input)).thenReturn(toSave);
        doThrow(new DataIntegrityViolationException("dup")).when(userCrudRepo).save(toSave);

        AppException ex = assertThrows(AppException.class, () -> adapter.save(input));
        assertEquals(HttpStatus.CONFLICT, ex.getCode());

    }

    @Test
    void save_generalException_throwsAppException() {
        UserDomain input = UserDomain.builder().username("u").build();
        User toSave = new User();
        when(userMapper.toEntity(input)).thenReturn(toSave);
        doThrow(new RuntimeException("err")).when(userCrudRepo).save(toSave);

        AppException ex = assertThrows(AppException.class, () -> adapter.save(input));
        assertEquals(HttpStatus.CONFLICT, ex.getCode());

    }

    @Test
    void update_success() {
        String uuid = "u1";
        User entity = new User();
        UserDomain dto = UserDomain.builder().username("new").build();
        when(userCrudRepo.findByUuid(uuid)).thenReturn(entity);
        when(userCrudRepo.save(entity)).thenReturn(entity);
        when(userMapper.toDomain(entity)).thenReturn(dto);

        UserDomain result = adapter.update(uuid, dto);
        assertEquals(dto, result);
    }

    @Test
    void update_absent_throwsNoSuchElement() {
        when(userCrudRepo.findByUuid("none")).thenReturn(null);
        assertThrows(NoSuchElementException.class, () -> adapter.update("none", UserDomain.builder().build()));
    }

    @Test
    void delete_success() {
        User entity = new User();
        when(userCrudRepo.findByUuid("u1")).thenReturn(entity);
        assertDoesNotThrow(() -> adapter.delete("u1"));
        verify(userCrudRepo).delete(entity);
    }

    @Test
    void delete_userNotFound_throwsAppException() {
        when(userCrudRepo.findByUuid("u2")).thenReturn(null);
        AppException ex = assertThrows(AppException.class, () -> adapter.delete("u2"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getCode());
    }

    @Test
    void delete_dataIntegrity_throwsAppException() {
        User entity = new User();
        when(userCrudRepo.findByUuid("u3")).thenReturn(entity);
        doThrow(new DataIntegrityViolationException("err")).when(userCrudRepo).delete(entity);
        AppException ex = assertThrows(AppException.class, () -> adapter.delete("u3"));
        assertEquals(HttpStatus.CONFLICT, ex.getCode());
    }

    @Test
    void delete_otherException_throwsAppException() {
        User entity = new User();
        when(userCrudRepo.findByUuid("u4")).thenReturn(entity);
        doThrow(new RuntimeException("err")).when(userCrudRepo).delete(entity);
        AppException ex = assertThrows(AppException.class, () -> adapter.delete("u4"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getCode());
    }

    @Test
    void updatePromotionStatus_success() {
        Integer userId = 1;
        User user = new User();
        when(userCrudRepo.findById(userId)).thenReturn(Optional.of(user));
        adapter.updatePromotionStatus(userId, "p");
        verify(userCrudRepo).updatePromotionStatusById("p", userId);
    }

    @Test
    void updatePromotionStatus_notFound_throwsAppException() {
        when(userCrudRepo.findById(2)).thenReturn(Optional.empty());
        AppException ex = assertThrows(AppException.class, () -> adapter.updatePromotionStatus(2, "p"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getCode());
    }

    @Test
    void updateBulkPromotionStatus_success() {
        UserDomain u1 = UserDomain.builder().id(1).promotionStatus("a").build();
        UserDomain u2 = UserDomain.builder().id(2).promotionStatus("b").build();
        when(userCrudRepo.findById(1)).thenReturn(Optional.of(new User()));
        when(userCrudRepo.findById(2)).thenReturn(Optional.of(new User()));
        adapter.updateBulkPromotionStatus(List.of(u1, u2));
        verify(userCrudRepo).updatePromotionStatusById("a", 1);
        verify(userCrudRepo).updatePromotionStatusById("b", 2);
    }

    @Test
    void updateBulkPromotionStatus_notFound_throwsAppException() {
        UserDomain u = UserDomain.builder().id(3).promotionStatus("x").build();
        when(userCrudRepo.findById(3)).thenReturn(Optional.empty());
        AppException ex = assertThrows(AppException.class, () -> adapter.updateBulkPromotionStatus(List.of(u)));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getCode());
    }
}