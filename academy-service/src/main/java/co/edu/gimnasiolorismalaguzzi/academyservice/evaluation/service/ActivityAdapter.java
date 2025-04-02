package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.ActivityGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.CreateActivityFront;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.ActivityGroup;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.mapper.ActivityGroupMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.repository.ActivityGroupCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.persistence.PersistenceActivityPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.ActivityDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.Activity;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.mapper.ActivityMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.repository.ActivityCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.entity.AchievementGroup;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class ActivityAdapter implements PersistenceActivityPort {
    @Autowired
    private ActivityGroupCrudRepo activityGroupCrudRepo;

    private final ActivityCrudRepo activityCrudRepo;

    @Autowired
    private final ActivityMapper activityMapper;

    @Autowired
    private ActivityGroupMapper activityGroupMapper;

    public ActivityAdapter(ActivityCrudRepo activityCrudRepo,
                           ActivityMapper activityMapper,
                           ActivityGroupCrudRepo activityGroupCrudRepo) {
        this.activityCrudRepo = activityCrudRepo;
        this.activityMapper = activityMapper;
        this.activityGroupCrudRepo = activityGroupCrudRepo;
    }

    @Override
    public List<ActivityDomain> findAll() {
        return this.activityMapper.toDomains(this.activityCrudRepo.findAll());
    }

    @Override
    public ActivityDomain findById(Integer integer) {
        Optional<Activity> activityOptional = this.activityCrudRepo.findById(integer);

        // Imprime los datos para verificar
        activityOptional.ifPresent(activity -> {
            System.out.println("Activity ID: " + activity.getId());
            System.out.println("AchievementGroup ID: " + activity.getAchievementGroup().getId());
            System.out.println("SubjectKnowledge: " + activity.getAchievementGroup().getSubjectKnowledge());
        });

        return activityOptional.map(activityMapper::toDomain).orElse(null);
    }

    @Override
    public ActivityDomain save(ActivityDomain entity) {
        entity.setStatus("A");
        Activity activity = activityMapper.toEntity(entity);
        Activity savedActivity = this.activityCrudRepo.save(activity);

        return activityMapper.toDomain(savedActivity);
    }

    @Transactional
    @Override
    public ActivityDomain createActivityAndGroup(CreateActivityFront activityFront) {
        try {
            // 1. Crear y mapear la Activity desde el CreateActivityFront
            Activity activity = Activity.builder()
                    .activityName(activityFront.getActivityName())
                    .description(activityFront.getDescription())
                    .achievementGroup(activityMapper.toEntity(
                        ActivityDomain.builder()
                            .achievementGroup(activityFront.getAchievementGroup())
                            .build()
                    ).getAchievementGroup())
                    .status("A")
                    .build();
            // Guardar la actividad
            Activity savedActivity = activityCrudRepo.save(activity);

            // 2. Crear y guardar el ActivityGroup
            ActivityGroup activityGroup = new ActivityGroup();
            activityGroup.setActivity(savedActivity);
            GroupsDomain group = GroupsDomain.builder().id(activityFront.getGroup().getId()).build();

            activityGroup.setGroup(activityGroupMapper.toEntity(
                ActivityGroupDomain.builder()
                    .group(group)
                    .build()
            ).getGroup());
            activityGroup.setStartDate(activityFront.getStartDate());
            activityGroup.setEndDate(activityFront.getEndDate());

            // Guardar el ActivityGroup
            ActivityGroup savedActivityGroup = activityGroupCrudRepo.save(activityGroup);

            // 3. Retornar la actividad creada como Domain
            return activityMapper.toDomain(savedActivity);

        } catch (Exception e) {
            log.error("Error creating activity and group: ", e);
            throw new AppException("Error creating activity and group: " + e.getMessage(),
                                 HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Transactional
    @Override
    public ActivityDomain updateActivityAndGroup(Integer id, CreateActivityFront activityDomainFront) {
        try {
            // 1. Buscar la actividad existente
            Activity existingActivity = activityCrudRepo.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Activity not found with id: " + id));

            // 2. Actualizar los campos de Activity
            existingActivity.setActivityName(activityDomainFront.getActivityName());
            existingActivity.setDescription(activityDomainFront.getDescription());
            existingActivity.setAchievementGroup(activityMapper.toEntity(
                    ActivityDomain.builder()
                            .achievementGroup(activityDomainFront.getAchievementGroup())
                            .build()
            ).getAchievementGroup());
            existingActivity.setStatus(activityDomainFront.getStatus());

            // Guardar los cambios de Activity
            Activity savedActivity = activityCrudRepo.save(existingActivity);

            // 3. Buscar y actualizar el ActivityGroup asociado
            ActivityGroup existingActivityGroup = activityGroupCrudRepo.findFirstByActivity_Id(id);
            if (existingActivityGroup == null) {
                throw new EntityNotFoundException("ActivityGroup not found for activity id: " + id);
            }

            // 4. Actualizar solo las fechas del ActivityGroup
            existingActivityGroup.setStartDate(activityDomainFront.getStartDate());
            existingActivityGroup.setEndDate(activityDomainFront.getEndDate());

            // 5. Guardar los cambios del ActivityGroup
            ActivityGroup savedActivityGroup = activityGroupCrudRepo.save(existingActivityGroup);

            // 6. Retornar la actividad actualizada como Domain
            return activityMapper.toDomain(savedActivity);

        } catch (EntityNotFoundException e) {
            log.error("Entity not found: ", e);
            throw new AppException("Entity not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error updating activity and group: ", e);
            throw new AppException("Error updating activity and group: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ActivityDomain updateActivityKnowledgeId(Integer activityId, Integer knowledgeId) {
        try{
            Optional<Activity> existingActivity = activityCrudRepo.findById(activityId);

            if (existingActivity.isPresent()){
                AchievementGroup achievementGroup = new AchievementGroup();
                achievementGroup.setId(knowledgeId);
                existingActivity.get().setAchievementGroup(achievementGroup);
            }

            return activityMapper.toDomain(activityCrudRepo.save(existingActivity.get()));

        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException("UserDetail with ID " + activityId + " not found");
        }
    }

    @Override
    public ActivityDomain update(Integer integer, ActivityDomain domain) {
        try{
            Optional<Activity> existingActivity = activityCrudRepo.findById(integer);

            if (existingActivity.isPresent()){
                existingActivity.get().setActivityName(domain.getActivityName());
                existingActivity.get().setDescription(domain.getDescription());
                existingActivity.get().setAchievementGroup(activityMapper.toEntity(domain).getAchievementGroup());
                existingActivity.get().setStatus(domain.getStatus());
            }

            return activityMapper.toDomain(activityCrudRepo.save(existingActivity.get()));

        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException("UserDetail with ID " + integer + " not found");
        }
    }

    @Override
    public HttpStatus delete(Integer integer) {
        try{
            if (this.activityCrudRepo.existsById(integer)) {
                activityCrudRepo.updateStatusById("I",integer);
                return HttpStatus.OK;
            } else {
                throw new AppException("Activity ID doesnt exist", HttpStatus.NOT_FOUND);
            }
        }catch(Exception e){
            throw new AppException("INTERN ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<ActivityDomain> getAllActivitiesWithKnowledgesAchievements(Integer id) {
        return this.activityMapper.toDomains(this.activityCrudRepo.findAll());
    }

    @Override
    public List<ActivityDomain> getAllActivitiesByAchievementGroupId(Integer id) {
        return this.activityMapper.toDomains(activityCrudRepo.findByAchievementGroup_Id(id));
    }




}
