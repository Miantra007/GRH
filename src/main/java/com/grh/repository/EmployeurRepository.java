package com.grh.repository;

import com.grh.model.Employeur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeurRepository extends JpaRepository<Employeur, Integer> {
}
