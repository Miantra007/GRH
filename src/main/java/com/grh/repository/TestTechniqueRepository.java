package com.grh.repository;

import com.grh.model.TestTechnique;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestTechniqueRepository extends JpaRepository<TestTechnique, Integer> {
}
