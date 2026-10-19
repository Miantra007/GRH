package com.grh.repository;

import com.grh.model.Travailleur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravailleurRepository extends JpaRepository<Travailleur, Integer> {
}
