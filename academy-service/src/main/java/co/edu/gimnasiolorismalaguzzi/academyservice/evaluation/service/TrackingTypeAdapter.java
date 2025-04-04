package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.TrackingTypeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.StudentTracking;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.TrackingType;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.mapper.TrackingTypeMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.repository.TrackingTypeCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.persistence.PersistenceTrackingTypePort;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class TrackingTypeAdapter implements PersistenceTrackingTypePort {

    private final TrackingTypeCrudRepo trackingTypeCrudRepo;

    @Autowired
    private TrackingTypeMapper trackingTypeMapper;

    public TrackingTypeAdapter(TrackingTypeCrudRepo trackingTypeCrudRepo) {
        this.trackingTypeCrudRepo = trackingTypeCrudRepo;
    }


    @Override
    public List<TrackingTypeDomain> findAll() {
        return trackingTypeMapper.toDomains(this.trackingTypeCrudRepo.findAll());
    }

    @Override
    public TrackingTypeDomain findById(Integer integer) {
        Optional<TrackingType> studentTracking = this.trackingTypeCrudRepo.findById(integer);
        return studentTracking.map(trackingTypeMapper::toDomain).orElse(null);
    }

    @Override
    public TrackingTypeDomain save(TrackingTypeDomain domain) {
        TrackingType trackingType = trackingTypeMapper.toEntity(domain);
        TrackingType savedTrackingType = trackingTypeCrudRepo.save(trackingType);
        return trackingTypeMapper.toDomain(savedTrackingType);
    }

    @Override
    public TrackingTypeDomain update(Integer integer, TrackingTypeDomain domain) {
        try{
            Optional<TrackingType> existingTrackingType = trackingTypeCrudRepo.findById(integer);
            existingTrackingType.ifPresent(trackingType -> trackingType.setType(trackingTypeMapper.toEntity(domain).getType()));

            return trackingTypeMapper.toDomain(trackingTypeCrudRepo.save(existingTrackingType.get()));
        } catch (Exception e){
            throw new EntityNotFoundException("TrackingType with ID " + integer + "not found!");
        }
    }

    @Override
    public HttpStatus delete(Integer integer) {
        return HttpStatus.I_AM_A_TEAPOT;
    }
}
