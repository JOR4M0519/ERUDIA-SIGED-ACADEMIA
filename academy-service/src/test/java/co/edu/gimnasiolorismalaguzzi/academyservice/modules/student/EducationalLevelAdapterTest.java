package co.edu.gimnasiolorismalaguzzi.academyservice.modules.student;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.EducationalLevelDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.EducationalLevel;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.mapper.EducationalLevelMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.repository.EduLevelCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.EducationalLevelAdapter;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EducationalLevelAdapterTest {

    @Mock
    private EduLevelCrudRepo eduLevelCrudRepo;

    @Mock
    private EducationalLevelMapper educationalLevelMapper;

    private EducationalLevelAdapter educationalLevelAdapter;

    private EducationalLevel educationalLevel;
    private EducationalLevelDomain educationalLevelDomain;
    private List<EducationalLevel> educationalLevels;
    private List<EducationalLevelDomain> educationalLevelDomains;

    @BeforeEach
    void setUp() {
        // Crear la instancia con el constructor e inyectar manualmente el mapper
        educationalLevelAdapter = new EducationalLevelAdapter(eduLevelCrudRepo);

        // Inicializar entidades
        educationalLevel = EducationalLevel.builder()
                .id(1)
                .levelName("Primaria")
                .status("A")
                .build();

        // Inicializar dominio
        educationalLevelDomain = EducationalLevelDomain.builder()
                .id(1)
                .levelName("Primaria")
                .status("A")
                .build();

        // Inicializar listas
        educationalLevels = Arrays.asList(educationalLevel);
        educationalLevelDomains = Arrays.asList(educationalLevelDomain);
    }



    @Test
    void update_WhenEducationalLevelDoesNotExist_ShouldThrowNoSuchElementException() {
        // Arrange
        Integer id = 999;
        EducationalLevelDomain domainToUpdate = EducationalLevelDomain.builder()
                .id(999)
                .levelName("Bachillerato")
                .status("A")
                .build();

        when(eduLevelCrudRepo.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            educationalLevelAdapter.update(id, domainToUpdate);
        });

        verify(eduLevelCrudRepo).findById(id);
        verify(eduLevelCrudRepo, never()).save(any(EducationalLevel.class));
    }

}
