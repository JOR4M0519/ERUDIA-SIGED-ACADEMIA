package co.edu.gimnasiolorismalaguzzi.academyservice.modules.academic;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectProfessorDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.Subject;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.SubjectAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.SubjectProfessorAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubjectAdapterTest {

    @Mock
    private SubjectCrudRepo subjectCrudRepo;

    @Mock
    private SubjectMapper subjectMapper;

    @Mock
    private SubjectProfessorAdapter subjectProfessorAdapter;

    private SubjectAdapter subjectAdapter;

    private Subject subject;
    private SubjectDomain subjectDomain;
    private List<Subject> subjects;
    private List<SubjectDomain> subjectDomains;
    private UserDomain professor;
    private List<UserDomain> professors;
    private SubjectProfessorDomain subjectProfessorDomain;
    private List<SubjectProfessorDomain> subjectProfessorDomains;

    @BeforeEach
    void setUp() {
        subjectAdapter = new SubjectAdapter(subjectCrudRepo, subjectProfessorAdapter);
        ReflectionTestUtils.setField(subjectAdapter, "subjectMapper", subjectMapper);

        professor = UserDomain.builder().id(1).firstName("John").lastName("Doe").build();
        professors = Collections.singletonList(professor);

        subject = Subject.builder().id(1).subjectName("Mathematics").status("A").build();
        subjectDomain = SubjectDomain.builder()
                .id(1)
                .subjectName("Mathematics")
                .status("A")
                .professor(professors)
                .build();

        subjects = Collections.singletonList(subject);
        subjectDomains = Collections.singletonList(subjectDomain);

        subjectProfessorDomain = SubjectProfessorDomain.builder()
                .id(1)
                .subject(subject)
                .professor(professor)
                .build();
        subjectProfessorDomains = Collections.singletonList(subjectProfessorDomain);
    }

    //============== findAll ==============

    @Test
    @DisplayName("findAll retorna lista de SubjectDomain con profesores")
    void findAll_success() {
        when(subjectCrudRepo.findAll()).thenReturn(subjects);
        when(subjectMapper.toDomains(subjects)).thenReturn(subjectDomains);
        when(subjectProfessorAdapter.findBySubjectId(1)).thenReturn(subjectProfessorDomains);

        List<SubjectDomain> result = subjectAdapter.findAll();

        assertEquals(subjectDomains, result);
        assertEquals(professors, result.get(0).getProfessor());
        verify(subjectCrudRepo).findAll();
        verify(subjectMapper).toDomains(subjects);
        verify(subjectProfessorAdapter).findBySubjectId(1);
    }

    @Test
    @DisplayName("findAll propaga RuntimeException si falla el repositorio")
    void findAll_failure() {
        when(subjectCrudRepo.findAll()).thenThrow(new RuntimeException("DB down"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> subjectAdapter.findAll());
        assertTrue(ex.getMessage().contains("DB down"));
    }

    //============== findById ==============

    @Test
    @DisplayName("findById retorna SubjectDomain cuando existe")
    void findById_exists() {
        when(subjectCrudRepo.findById(1)).thenReturn(Optional.of(subject));
        when(subjectMapper.toDomain(subject)).thenReturn(subjectDomain);

        SubjectDomain result = subjectAdapter.findById(1);

        assertEquals(subjectDomain, result);
        verify(subjectCrudRepo).findById(1);
        verify(subjectMapper).toDomain(subject);
    }

    @Test
    @DisplayName("findById retorna null cuando no existe")
    void findById_notExists() {
        when(subjectCrudRepo.findById(999)).thenReturn(Optional.empty());

        SubjectDomain result = subjectAdapter.findById(999);

        assertNull(result);
        verify(subjectCrudRepo).findById(999);
        verify(subjectMapper, never()).toDomain(any());
    }

    //============== save ==============

    @Test
    @DisplayName("save guarda asignatura y relaciones con profesor")
    void save_success_withProfessors() {
        SubjectDomain input = SubjectDomain.builder()
                .subjectName("Physics")
                .professor(professors)
                .build();
        Subject toSave = Subject.builder().subjectName("Physics").status("A").build();
        Subject saved = Subject.builder().id(2).subjectName("Physics").status("A").build();
        SubjectDomain savedDomain = SubjectDomain.builder().id(2).subjectName("Physics").status("A").build();

        when(subjectMapper.toEntity(input)).thenReturn(toSave);
        when(subjectCrudRepo.save(toSave)).thenReturn(saved);
        when(subjectMapper.toDomain(saved)).thenReturn(savedDomain);

        SubjectDomain result = subjectAdapter.save(input);

        assertEquals("A", input.getStatus());
        assertEquals(savedDomain, result);
        verify(subjectMapper).toEntity(input);
        verify(subjectCrudRepo).save(toSave);
        verify(subjectMapper).toDomain(saved);
        verify(subjectProfessorAdapter).save(argThat(sp ->
                sp.getProfessor().getId() == 1 && sp.getSubject().equals(saved)
        ));
    }

    @Test
    @DisplayName("save no falla si no se pasan profesores")
    void save_success_withoutProfessors() {
        SubjectDomain input = SubjectDomain.builder().subjectName("History").professor(Collections.emptyList()).build();
        Subject toSave = Subject.builder().subjectName("History").status("A").build();
        Subject saved = Subject.builder().id(3).subjectName("History").status("A").build();
        SubjectDomain savedDomain = SubjectDomain.builder().id(3).subjectName("History").status("A").build();

        when(subjectMapper.toEntity(input)).thenReturn(toSave);
        when(subjectCrudRepo.save(toSave)).thenReturn(saved);
        when(subjectMapper.toDomain(saved)).thenReturn(savedDomain);

        SubjectDomain result = subjectAdapter.save(input);

        assertEquals(savedDomain, result);
        verify(subjectProfessorAdapter, never()).save(any());
    }

    @Test
    @DisplayName("save propaga RuntimeException si falla el repositorio")
    void save_failure() {
        SubjectDomain input = SubjectDomain.builder().subjectName("Biology").professor(professors).build();
        when(subjectMapper.toEntity(any())).thenThrow(new RuntimeException("Mapping error"));

        assertThrows(RuntimeException.class, () -> subjectAdapter.save(input));
    }

    //============== update ==============

    @Test
    @DisplayName("update lanza EntityNotFoundException si no existe")
    void update_notExists() {
        when(subjectCrudRepo.findById(999)).thenReturn(Optional.empty());
        SubjectDomain dom = SubjectDomain.builder().id(999).subjectName("X").status("A").build();

        assertThrows(EntityNotFoundException.class, () -> subjectAdapter.update(999, dom));
        verify(subjectCrudRepo).findById(999);
    }


    @Test
    @DisplayName("update modifica nombre, estado y relaciones de profesores")
    void update_success() {
        // Arrange
        Subject existing = Subject.builder().id(1).subjectName("Math").status("A").build();
        SubjectDomain domInput = SubjectDomain.builder()
                .subjectName("Math II").status("A")
                .professor(Arrays.asList(professor, UserDomain.builder().id(2).build()))
                .build();
        Subject updatedEntity = Subject.builder().id(1).subjectName("Math II").status("A").build();
        SubjectDomain updatedDomain = SubjectDomain.builder().id(1).subjectName("Math II").status("A").build();

        when(subjectCrudRepo.findById(1)).thenReturn(Optional.of(existing));

        // stub secuencial: primero devuelve la relación antigua (professor id=1),
        // luego la lista nueva (solo id=2)
        when(subjectProfessorAdapter.findBySubjectId(1))
                .thenReturn(Collections.singletonList(subjectProfessorDomain),
                        Collections.singletonList(
                                SubjectProfessorDomain.builder()
                                        .id(2)
                                        .professor(UserDomain.builder().id(2).build())
                                        .build()));

        when(subjectCrudRepo.save(existing)).thenReturn(updatedEntity);
        when(subjectMapper.toDomain(updatedEntity)).thenReturn(updatedDomain);

        // Act
        SubjectDomain result = subjectAdapter.update(1, domInput);

        // Assert
        assertEquals(updatedDomain, result);
        // Solo esperamos un save de relación nueva (id=2), no delete
        verify(subjectProfessorAdapter, never()).delete(anyInt());
        verify(subjectProfessorAdapter).save(argThat(sp -> sp.getProfessor().getId() == 2));
        verify(subjectCrudRepo).save(existing);
    }

    @Test
    @DisplayName("update propaga RuntimeException si falla el repositorio al guardar")
    void update_failure() {
        // Arrange
        Subject existing = Subject.builder().id(1).subjectName("X").status("A").build();
        when(subjectCrudRepo.findById(1)).thenReturn(Optional.of(existing));
        // Forzamos excepción en save
        doThrow(new RuntimeException("DB error")).when(subjectCrudRepo).save(existing);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> subjectAdapter.update(1, subjectDomain));

        verify(subjectCrudRepo).findById(1);
        verify(subjectCrudRepo).save(existing);
    }

    //============== delete ==============

    @Test
    @DisplayName("delete lanza NOT_FOUND si la materia no existe")
    void delete_notExists() {
        when(subjectCrudRepo.findById(999)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> subjectAdapter.delete(999));
        assertEquals(HttpStatus.NOT_FOUND, ex.getCode());
        assertEquals("La materia no existe", ex.getMessage());
    }

    @Test
    @DisplayName("delete elimina materia existente y devuelve OK")
    void delete_success() {
        when(subjectCrudRepo.findById(1)).thenReturn(Optional.of(subject));
        when(subjectMapper.toDomain(subject)).thenReturn(subjectDomain);

        HttpStatus status = subjectAdapter.delete(1);
        assertEquals(HttpStatus.OK, status);
        verify(subjectCrudRepo).deleteById(1);
    }

    @Test
    @DisplayName("delete lanza CONFLICT si deleteById falla")
    void delete_failure() {
        when(subjectCrudRepo.findById(1)).thenReturn(Optional.of(subject));
        when(subjectMapper.toDomain(subject)).thenReturn(subjectDomain);
        doThrow(new RuntimeException("DB down")).when(subjectCrudRepo).deleteById(1);

        AppException ex = assertThrows(AppException.class, () -> subjectAdapter.delete(1));
        assertEquals(HttpStatus.CONFLICT, ex.getCode());
        assertEquals("Se tuvo un error al eliminar la relación", ex.getMessage());
    }
}
