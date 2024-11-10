package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceAttendancePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.AttendanceDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.Attendance;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.mapper.AttendanceMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.repository.AttendanceCrudRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class AttendanceAdapter implements PersistenceAttendancePort {

    private final AttendanceCrudRepo crudRepo;

    @Autowired
    private final AttendanceMapper mapper;

    public AttendanceAdapter(AttendanceCrudRepo crudRepo, AttendanceMapper mapper) {
        this.crudRepo = crudRepo;
        this.mapper = mapper;
    }

    @Override
    public List<AttendanceDomain> findAll() {
        return mapper.toDomains(crudRepo.findAll());
    }

    @Override
    public AttendanceDomain findById(Integer integer) {
        Optional<Attendance> attendance = this.crudRepo.findById(integer);
        return attendance.map(mapper::toDomain).orElse(null);
    }

    @Override
    public AttendanceDomain save(AttendanceDomain attendanceDomain) {
        Attendance attendance = mapper.toEntity(attendanceDomain);
        Attendance savedAttendance = this.crudRepo.save(attendance);
        return mapper.toDomain(savedAttendance);
    }

    @Override
    public AttendanceDomain update(Integer integer, AttendanceDomain attendanceDomain) {
        try{
            Optional<Attendance> existingAttendance = crudRepo.findById(integer);
            if(existingAttendance.isPresent()){
                existingAttendance.get().setStudent(attendanceDomain.getStudent());
                existingAttendance.get().setSchedule(attendanceDomain.getSchedule());
                existingAttendance.get().setAttendanceDate(attendanceDomain.getAttendanceDate());
                existingAttendance.get().setStatus(attendanceDomain.getStatus());
                existingAttendance.get().setRecordedAt(attendanceDomain.getRecordedAt());
            }
            return mapper.toDomain(crudRepo.save(existingAttendance.get()));
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Attendance with ID " + integer + " Not found!");
        }
    }

    @Override
    public HttpStatus delete(Integer integer) {
        return null;
    }
}
