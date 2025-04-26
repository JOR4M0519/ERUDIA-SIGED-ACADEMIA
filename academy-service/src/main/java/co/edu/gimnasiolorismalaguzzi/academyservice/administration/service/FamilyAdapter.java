package co.edu.gimnasiolorismalaguzzi.academyservice.administration.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.FamilyDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.Family;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper.FamilyMapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository.FamilyCrudRepo;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceFamilyPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistenceAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@Slf4j
public class FamilyAdapter implements PersistenceFamilyPort {

    private final FamilyCrudRepo familyCrudRepo;

    @Autowired
    private FamilyMapper familyMapper;

    public FamilyAdapter(FamilyCrudRepo familyCrudRepo) {
        this.familyCrudRepo = familyCrudRepo;
    }

    @Override
    public List<FamilyDomain> findAll() {
        return this.familyMapper.toDomains(this.familyCrudRepo.findAll());
    }

    @Override
    public FamilyDomain findById(Integer integer) {
        Optional<Family> family = this.familyCrudRepo.findById(integer);
        return family.map(familyMapper::toDomain).orElse(null);
    }

    @Override
    public FamilyDomain save(FamilyDomain domain) {
        Family familyEntity = familyMapper.toEntity(domain);
        Family savedFamily = this.familyCrudRepo.save(familyEntity);
        return familyMapper.toDomain(savedFamily);
    }

    @Override
    public FamilyDomain update(Integer integer, FamilyDomain domain) {
        try{
            Optional<Family> existingFamily = familyCrudRepo.findById(integer);

            /*if(existingFamily.isPresent()){
                existingFamily.get().setStudent(domain.getStudent());
                existingFamily.get().setUser(entity.getUser());
                existingFamily.get().setRelationship(entity.getRelationship());
            }*/
            return familyMapper.toDomain(familyCrudRepo.save(existingFamily.get()));
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Family relation with ID " + integer + "Not found!");
        }
    }

    @Override
    public HttpStatus delete(Integer integer) {
        try{
            if(this.familyCrudRepo.existsById(integer)){
                familyCrudRepo.delete(this.familyCrudRepo.getReferenceById(integer));
                return HttpStatus.OK;
            } else {
                throw new AppException("Family relation ID doesnt exist!", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            throw new AppException("Intern Error!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<FamilyDomain> findRelativesByStudent(Integer id) {
        List<Family> relatives = familyCrudRepo.findByStudent_Id(id);
        //findRelativesByStudent()
        return familyMapper.toDomains(relatives);
    }

    /**
     * Devuelve la información del usuario de los estudiantes del familiar
     * @param relativeId
     * @return
     */
    @Override
    public List<FamilyDomain> findStudentsByRelativeId(Integer relativeId) {
        List<Family> relatives = familyCrudRepo.findByUser_Id(relativeId);
        return familyMapper.toDomains(relatives);
    }

    @Override
    public FamilyDomain saveById(Integer id, FamilyDomain familyDomain) {

        UserDomain user = UserDomain.builder().id(id).build();
        FamilyDomain familyToSave = new FamilyDomain(null,user,familyDomain.getUser(),familyDomain.getRelationship());

        Family familyEntity = familyMapper.toEntity( familyToSave);
        Family savedFamily = this.familyCrudRepo.save(familyEntity);
        return familyMapper.toDomain(savedFamily);
    }
}
