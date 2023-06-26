package net.guides.springboot2.springboot2jpacrudexample.repository;

import net.guides.springboot2.springboot2jpacrudexample.model.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface FormationRepository extends JpaRepository<Formation, Long>{
    boolean existsByNom(String nom);
    Optional<Formation> findByNom(String nom);
    Optional<Formation> findById(Long id) ;


}
