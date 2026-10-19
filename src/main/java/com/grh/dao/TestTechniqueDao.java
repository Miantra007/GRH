package com.grh.dao;

import com.grh.model.TestTechnique;
import com.grh.repository.TestTechniqueRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TestTechniqueDao extends GenericDao<TestTechnique> {

    public TestTechniqueDao(TestTechniqueRepository repository) {
        super(repository, "TestTechnique");
    }
}
