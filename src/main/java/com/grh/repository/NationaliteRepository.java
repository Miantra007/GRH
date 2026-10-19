package com.grh.repository;

import com.grh.model.Nationalite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NationaliteRepository extends JpaRepository<Nationalite, Integer> {

    Optional<Nationalite> findByLibelleIgnoreCase(String libelle);
}
