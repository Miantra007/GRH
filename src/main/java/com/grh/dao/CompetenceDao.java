package com.grh.dao;

import com.grh.model.Competence;
import com.grh.repository.CompetenceRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CompetenceDao extends GenericDao<Competence> {

    public CompetenceDao(CompetenceRepository repository) {
        super(repository, "Competence");
    }
}
