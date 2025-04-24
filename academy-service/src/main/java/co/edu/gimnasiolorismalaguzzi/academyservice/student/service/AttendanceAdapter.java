package co.edu.gimnasiolorismalaguzzi.academyservice.student.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper.SubjectScheduleMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectScheduleCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.UserMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.AttendanceDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.Attendance;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.mapper.AttendanceMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.repository.AttendanceCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence.PersistenceAttendancePort;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class AttendanceAdapter implements PersistenceAttendancePort {
    private final SubjectScheduleCrudRepo subjectScheduleCrudRepo;
    private final AttendanceCrudRepo attendanceCrudRepo;
    private final AttendanceMapper attendanceMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SubjectScheduleMapper subjectScheduleMapper;

    @Autowired
    public AttendanceAdapter(AttendanceCrudRepo attendanceCrudRepo,
                        AttendanceMapper attendanceMapper,
                        SubjectScheduleCrudRepo subjectScheduleCrudRepo) {
        this.attendanceCrudRepo = attendanceCrudRepo;
        this.attendanceMapper = attendanceMapper;
        this.subjectScheduleCrudRepo = subjectScheduleCrudRepo;
    }

    @Override
    public List<AttendanceDomain> findAll() {
        return attendanceMapper.toDomains(attendanceCrudRepo.findAll());
    }

    @Override
    public List<AttendanceDomain> getHistoricalAttendance(Integer groupId, Integer subjectId, Integer periodId) {
        return attendanceMapper.toDomains(attendanceCrudRepo.getHistoricalAttendance(groupId,subjectId,periodId));
    }

    @Override
    public AttendanceDomain findById(Integer integer) {
        Optional<Attendance> attendance = this.attendanceCrudRepo.findById(integer);
        return attendance.map(attendanceMapper::toDomain).orElse(null);
    }

    @Override
    public AttendanceDomain save(AttendanceDomain attendanceDomain) {
        Attendance attendance = attendanceMapper.toEntity(attendanceDomain);
        Attendance savedAttendance = this.attendanceCrudRepo.save(attendance);
        return attendanceMapper.toDomain(savedAttendance);
    }

    @Override
    public AttendanceDomain update(Integer integer, AttendanceDomain attendanceDomain) {
        try{
            Optional<Attendance> existingAttendance = attendanceCrudRepo.findById(integer);
            if(existingAttendance.isPresent()){
                existingAttendance.get().setStudent(userMapper.toEntity(attendanceDomain.getStudent()));
                existingAttendance.get().setSchedule(subjectScheduleMapper.toEntity(attendanceDomain.getSchedule()));
                existingAttendance.get().setAttendanceDate(attendanceDomain.getAttendanceDate());
                existingAttendance.get().setStatus(attendanceDomain.getStatus());
                existingAttendance.get().setRecordedAt(attendanceDomain.getRecordedAt());
            }
            return attendanceMapper.toDomain(attendanceCrudRepo.save(existingAttendance.get()));
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Attendance with ID " + integer + " Not found!");
        }
    }

    @Transactional
    public List<AttendanceDomain> saveAllValidateSchedule(List<AttendanceDomain> attendances,
                                          Integer groupId,
                                          Integer subjectId,
                                          Integer professorId,
                                          Integer periodId) {
        // Obtener todos los horarios posibles para estos parámetros una sola vez
        var possibleSchedules = subjectScheduleCrudRepo.getSubjectSchedules(
                groupId, periodId, subjectId, professorId);

        if (possibleSchedules.isEmpty()) {
            log.warn("No se encontraron horarios para grupo:{}, asignatura:{}, profesor:{}, periodo:{}",
                    groupId, subjectId, professorId, periodId);
        }

        // Procesar cada asistencia usando las características de Java 21
        for (var attendance : attendances) {
            // Obtener el día de la semana y la hora del recordedAt
            var recordTime = attendance.getRecordedAt();
            var dayOfWeek = recordTime.getDayOfWeek();
            var timeOfDay = recordTime.toLocalTime();

            // Encontrar el horario que coincide con el día y hora de la asistencia
            var matchingSchedule = possibleSchedules.stream()
                    .filter(schedule -> {
                        // Verificar si el día de la semana coincide (usando switch expressions de Java 21)
                        boolean dayMatches = switch (schedule.getDayOfWeek().toUpperCase()) {
                            case "MONDAY", "LUNES" -> dayOfWeek == DayOfWeek.MONDAY;
                            case "TUESDAY", "MARTES" -> dayOfWeek == DayOfWeek.TUESDAY;
                            case "WEDNESDAY", "MIÉRCOLES", "MIERCOLES" -> dayOfWeek == DayOfWeek.WEDNESDAY;
                            case "THURSDAY", "JUEVES" -> dayOfWeek == DayOfWeek.THURSDAY;
                            case "FRIDAY", "VIERNES" -> dayOfWeek == DayOfWeek.FRIDAY;
                            case "SATURDAY", "SÁBADO", "SABADO" -> dayOfWeek == DayOfWeek.SATURDAY;
                            case "SUNDAY", "DOMINGO" -> dayOfWeek == DayOfWeek.SUNDAY;
                            default -> false;
                        };

                        // Verificar si la hora está dentro del rango del horario
                        var startTime = schedule.getStartTime();
                        var endTime = schedule.getEndTime();
                        var timeInRange = !timeOfDay.isBefore(startTime) && !timeOfDay.isAfter(endTime);

                        return dayMatches && timeInRange;
                    })
                    .findFirst()
                    .orElse(null);

            if (matchingSchedule != null) {
                // Asignar el horario encontrado a la asistencia
                attendance.setSchedule(subjectScheduleMapper.toDomain(matchingSchedule));
            } else {
                // Usar el primer horario disponible si no hay coincidencia exacta
                if (!possibleSchedules.isEmpty()) {
                    attendance.setSchedule(subjectScheduleMapper.toDomain(possibleSchedules.getFirst()));
                    log.info("No se encontró un horario exacto para la asistencia en {}. Usando el primer horario disponible.", recordTime);
                } else {
                    log.error("No hay horarios disponibles para asignar a la asistencia en: {}", recordTime);
                    throw new IllegalStateException("No hay horarios disponibles para asignar a la asistencia");
                }
            }
        }

        // Convertir a entidades y guardar usando métodos de Java 21
        var attendanceEntities = attendances.stream()
                .map(attendanceMapper::toEntity)
                .toList();

        var savedAttendances = attendanceCrudRepo.saveAll(attendanceEntities);
        return attendanceMapper.toDomains(savedAttendances);
    }

    @Transactional
    public List<AttendanceDomain> saveAllNullAttendance(List<AttendanceDomain> attendances,
                                                                   Integer groupId,
                                                                   Integer subjectId,
                                                                   Integer professorId,
                                                                   Integer periodId) {
        // Convertir directamente a entidades y guardar sin validación de horarios
        var attendanceEntities = attendances.stream()
                .map(attendanceMapper::toEntity)
                .toList();
        subjectScheduleCrudRepo.getSubjectSchedules(groupId,periodId,subjectId,professorId);

        var savedAttendances = attendanceCrudRepo.saveAll(attendanceEntities);
        log.info("Se guardaron {} asistencias sin validación de horario", savedAttendances.size());

        return attendanceMapper.toDomains(savedAttendances);
    }


    @Transactional
    @Override
    public List<AttendanceDomain> saveAll(List<AttendanceDomain> attendances,
                                          Integer groupId,
                                          Integer subjectId,
                                          Integer professorId,
                                          Integer periodId) {
        // Obtener el horario para estos parámetros
        var schedules = subjectScheduleCrudRepo.getSubjectSchedules(groupId, periodId, subjectId, professorId);

        // Si existe un horario, asignarlo a las asistencias que no tengan uno
        if (!schedules.isEmpty()) {
            var schedule = schedules.getFirst();  // Tomamos el primer (y presumiblemente único) horario
            log.info("Se encontró horario ID: {} para asignar a las asistencias", schedule.getId());

            // Asignar el horario a las asistencias que no tienen uno
            for (var attendance : attendances) {
                if (attendance.getSchedule() == null) {
                    attendance.setSchedule(subjectScheduleMapper.toDomain(schedule));
                }
            }
        } else {
            log.warn("No se encontró horario para grupo:{}, asignatura:{}, profesor:{}, periodo:{}",
                    groupId, subjectId, professorId, periodId);
        }

        // Validar asistencias duplicadas (mismo estudiante, horario y fecha)
        List<AttendanceDomain> duplicateAttendances = new ArrayList<>();
        List<AttendanceDomain> validAttendances = new ArrayList<>();

        for (AttendanceDomain attendance : attendances) {
            if (attendance.getStudent() == null || attendance.getSchedule() == null || attendance.getAttendanceDate() == null) {
                log.warn("Asistencia con datos incompletos: estudiante, horario o fecha son nulos");
                continue;
            }

            // Verificar si ya existe una asistencia para este estudiante en este horario y fecha
            boolean duplicateExists = attendanceCrudRepo.existsByStudentIdAndScheduleIdAndAttendanceDate(
                    attendance.getStudent().getId(),
                    attendance.getSchedule().getId(),
                    attendance.getAttendanceDate()
            );

            if (duplicateExists) {
                duplicateAttendances.add(attendance);
                log.warn("El estudiante {} ya tiene asistencia registrada para el horario {} en la fecha {}",
                        attendance.getStudent().getId(), attendance.getSchedule().getId(), attendance.getAttendanceDate());
            } else {
                validAttendances.add(attendance);
            }
        }

        if (!duplicateAttendances.isEmpty()) {
            log.error("Se encontraron {} registros de asistencia duplicados", duplicateAttendances.size());
            throw new AppException("No se pueden guardar asistencias duplicadas. Hay " +
                    duplicateAttendances.size() + " estudiantes que ya tienen asistencia registrada para la misma fecha y horario.",
                    HttpStatus.CONFLICT);
        }

        if (validAttendances.isEmpty()) {
            log.warn("No hay asistencias válidas para guardar después de la validación de duplicados");
            return List.of();
        }

        // Convertir a entidades y guardar
        var attendanceEntities = validAttendances.stream()
                .map(attendanceMapper::toEntity)
                .toList();

        var savedAttendances = attendanceCrudRepo.saveAll(attendanceEntities);
        log.info("Se guardaron {} asistencias", savedAttendances.size());

        return attendanceMapper.toDomains(savedAttendances);
    }

    @Transactional
    @Override
    public List<AttendanceDomain> updateAll(List<AttendanceDomain> attendances) {
        List<Attendance> updatedAttendances = attendances.stream()
                .map(attendance -> {
                    Optional<Attendance> existingAttendance = attendanceCrudRepo.findById(attendance.getId());
                    if (existingAttendance.isPresent()) {
                        Attendance current = existingAttendance.get();
                        current.setStudent(userMapper.toEntity(attendance.getStudent()));
                        current.setSchedule(subjectScheduleMapper.toEntity(attendance.getSchedule()));
                        current.setAttendanceDate(attendance.getAttendanceDate());
                        current.setStatus(attendance.getStatus());
                        current.setRecordedAt(attendance.getRecordedAt());
                        return current;
                    } else {
                        throw new EntityNotFoundException("Attendance with ID " + attendance.getId() + " not found!");
                    }
                })
                .toList();

        List<Attendance> savedAttendances = attendanceCrudRepo.saveAll(updatedAttendances);
        return attendanceMapper.toDomains(savedAttendances);
    }

    @Override
    public HttpStatus delete(Integer integer) {
        return null;
    }

    @Transactional
    @Override
    public HttpStatus deleteAll(List<Integer> ids) {
        try {
            List<Attendance> existingAttendances = attendanceCrudRepo.findAllById(ids);
            if (existingAttendances.isEmpty()) {
                return HttpStatus.NOT_FOUND;
            }

            attendanceCrudRepo.deleteAllById(ids);
            return HttpStatus.OK;
        } catch (Exception e) {
            log.error("Error deleting attendances with ids {}: {}", ids, e.getMessage());
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
