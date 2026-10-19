package com.grh.repository;

import com.grh.model.StatutContrat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatutContratRepository extends JpaRepository<StatutContrat, Integer> {

    Optional<StatutContrat> findByLibelle(String libelle);
}
