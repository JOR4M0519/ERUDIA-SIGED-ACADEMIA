package co.edu.gimnasiolorismalaguzzi.academyservice.student.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository.SubjectScheduleCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
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
import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class AttendanceAdapter implements PersistenceAttendancePort {
    private final SubjectScheduleCrudRepo subjectScheduleCrudRepo;
    private final AttendanceCrudRepo attendanceCrudRepo;
    private final AttendanceMapper attendanceMapper;

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
                existingAttendance.get().setStudent(attendanceDomain.getStudent());
                existingAttendance.get().setSchedule(attendanceDomain.getSchedule());
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
    @Override
    public List<AttendanceDomain> saveAll(List<AttendanceDomain> attendances,
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
                attendance.setSchedule(matchingSchedule);
            } else {
                // Usar el primer horario disponible si no hay coincidencia exacta
                if (!possibleSchedules.isEmpty()) {
                    attendance.setSchedule(possibleSchedules.getFirst());
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
    @Override
    public List<AttendanceDomain> updateAll(List<AttendanceDomain> attendances) {
        List<Attendance> updatedAttendances = attendances.stream()
                .map(attendance -> {
                    Optional<Attendance> existingAttendance = attendanceCrudRepo.findById(attendance.getId());
                    if (existingAttendance.isPresent()) {
                        Attendance current = existingAttendance.get();
                        current.setStudent(attendance.getStudent());
                        current.setSchedule(attendance.getSchedule());
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
