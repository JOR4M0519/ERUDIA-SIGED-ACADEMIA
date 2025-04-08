package co.edu.gimnasiolorismalaguzzi.academyservice.student.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceUserDetailPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupStudentsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.StudentPromotionDTO;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.GroupStudent;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.mapper.GroupStudentsMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.repository.GroupStudentsCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence.PersistenceGroupStudentPort;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@PersistenceAdapter
@Slf4j
public class GroupStudentsAdapter implements PersistenceGroupStudentPort {

    private final GroupStudentsCrudRepo groupStudentsCrudRepo;

    private GroupStudentsMapper groupStudentsMapper;


    private final PersistenceUserDetailPort persistenceUserDetailPort;

    public GroupStudentsAdapter(GroupStudentsCrudRepo groupStudentsCrudRepo, GroupStudentsMapper groupStudentsMapper, PersistenceUserDetailPort persistenceUserDetailPort) {
        this.groupStudentsCrudRepo = groupStudentsCrudRepo;
        this.groupStudentsMapper = groupStudentsMapper;
        this.persistenceUserDetailPort = persistenceUserDetailPort;
    }

    // Añadir este método a GroupStudentsAdapter.java
    public List<GroupStudentsDomain> getStudentsByGroupId(Integer groupId) {
        return groupStudentsCrudRepo.findByGroupId(groupId)
                .stream()
                .map(groupStudentsMapper::toDomain)
                .collect(Collectors.toList());
    }


    @Override
    public List<GroupStudentsDomain> findAll() {
        return this.groupStudentsMapper.toDomains(groupStudentsCrudRepo.findAll());
    }

    @Override
    public GroupStudentsDomain findById(Integer integer) {
        Optional<GroupStudent> groupStudent = groupStudentsCrudRepo.findById(integer);
        return groupStudent.map(groupStudentsMapper::toDomain).orElse(null);
    }

    @Override
    public List<GroupStudentsDomain> findByStudentId(Integer studentId) {
        return this.groupStudentsMapper.toDomains(groupStudentsCrudRepo.findByStudent_Id(studentId));
    }


    @Override
    public List<GroupStudentsDomain> getGroupsStudentById(int id,String status) {
        return groupStudentsMapper.toDomains(groupStudentsCrudRepo.findByStudent_IdAndGroup_Status(id,status));
    }

    @Override
    public List<GroupStudentsDomain> getGroupsStudentByGroupId(Integer groupId, String statusNotLike) {
        return this.groupStudentsMapper.toDomains(groupStudentsCrudRepo.findByGroup_IdAndGroup_StatusNotLike(groupId,statusNotLike));
    }

    @Override
    public List<GroupStudentsDomain> getListByMentorIdByYear(Integer mentorId, Integer year) {
        return groupStudentsMapper.toDomains(groupStudentsCrudRepo.findByGroup_Mentor_Id(mentorId));
    }

    @Override
    public List<GroupStudentsDomain> getGroupListByStatus(String status) {
        return this.groupStudentsMapper.toDomains(groupStudentsCrudRepo.findByStatus(status));
    }

    @Override
    public GroupStudentsDomain save(GroupStudentsDomain domain) {
        GroupStudent groupStudent = groupStudentsMapper.toEntity(domain);
        GroupStudent savedGroupStudent = this.groupStudentsCrudRepo.save(groupStudent);
        return groupStudentsMapper.toDomain(savedGroupStudent);
    }

    @Override
    public GroupStudentsDomain update(Integer integer, GroupStudentsDomain domain) {
        try{
            Optional<GroupStudent> existingGroupStudent = groupStudentsCrudRepo.findById(integer);
            if(existingGroupStudent.isPresent()){
                existingGroupStudent.get().setGroup(groupStudentsMapper.toEntity(domain).getGroup());
                existingGroupStudent.get().setStudent(groupStudentsMapper.toEntity(domain).getStudent());
                existingGroupStudent.get().setStatus(groupStudentsMapper.toEntity(domain).getStatus());
            }
            return groupStudentsMapper.toDomain(groupStudentsCrudRepo.save(existingGroupStudent.get()));
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("GroupStudent with id: " + integer + "not found!");
        }
    }

    @Override
    @Transactional
    public List<GroupStudentsDomain> promoteStudents(StudentPromotionDTO promotionDTO) {
        try {
            log.debug("Starting student promotion process for {} students to group {}",
                    promotionDTO.getStudentIds().size(), promotionDTO.getTargetGroupId());

            List<GroupStudentsDomain> promotedStudents = new ArrayList<>();

            for (Integer studentId : promotionDTO.getStudentIds()) {
                // 1. Verificar si el estudiante ya está en el grupo destino
                boolean existingAssignment = groupStudentsCrudRepo
                        .existsByStudent_IdAndGroup_Id(studentId, promotionDTO.getTargetGroupId());

                boolean isActiveToPromotion = persistenceUserDetailPort
                        .findByUser_Id(studentId).getUser().getPromotionStatus().equals("A");

                if (existingAssignment) {
                    log.warn("Student "+studentId +" already assigned to target group "+promotionDTO.getTargetGroupId(),
                            studentId, promotionDTO.getTargetGroupId());
                    throw new AppException("El estudiante "+studentId +" ya se encuentra registrado en el grupo "+promotionDTO.getTargetGroupId()
                            ,HttpStatus.CONFLICT);
                }
                if (!isActiveToPromotion) {
                    log.warn("Student "+studentId +" already assigned to target group "+promotionDTO.getTargetGroupId(),
                            studentId, promotionDTO.getTargetGroupId());
                    throw new AppException("El estudiante "+studentId +" no se encuentra activo para promover"
                            ,HttpStatus.UPGRADE_REQUIRED);
                }

                // 2. Crear nueva asignación de grupo
                GroupStudentsDomain newAssignment = GroupStudentsDomain.builder().build();

                // Configurar estudiante
                UserDomain student = new UserDomain(studentId);
                newAssignment.setStudent(student);

                // Configurar grupo destino
                GroupsDomain targetGroup = GroupsDomain.builder()
                        .id(promotionDTO.getTargetGroupId())
                        .build();
                newAssignment.setGroup(targetGroup);
                newAssignment.setStatus("A");

                // 3. Guardar la nueva asignación
                 List<GroupStudentsDomain> groupStudents =  findByStudentId(studentId);

                 for (GroupStudentsDomain group: groupStudents){
                     group.setStatus("I");
                     update(group.getId(),group);

                 }

                // 4. Guardar la nueva asignación
                GroupStudentsDomain savedAssignment = save(newAssignment);

                if (savedAssignment != null) {
                    promotedStudents.add(savedAssignment);
                    log.info("Successfully promoted student {} to group {}",
                            studentId, promotionDTO.getTargetGroupId());
                }
            }

            if (promotedStudents.isEmpty()) {
                log.warn("No students were promoted");
                throw new AppException("No students were promoted", HttpStatus.BAD_REQUEST);
            }

            return promotedStudents;

        } catch (Exception e) {
            log.error("Error during student promotion process", e);
            throw new AppException("Error promoting students: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public HttpStatus delete(Integer integer) {
        try{
            if(this.groupStudentsCrudRepo.existsById(integer)){
                groupStudentsCrudRepo.delete(this.groupStudentsCrudRepo.getReferenceById(integer));
                return HttpStatus.OK;
            } else {
                throw new AppException("GroupStudent ID doesnt exist!", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e){
            throw new AppException("Internal Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
