package com.grh.dao;

import com.grh.model.NiveauCompetence;
import com.grh.repository.NiveauCompetenceRepository;
import org.springframework.stereotype.Repository;

@Repository
public class NiveauCompetenceDao extends GenericDao<NiveauCompetence> {

    public NiveauCompetenceDao(NiveauCompetenceRepository repository) {
        super(repository, "NiveauCompetence");
    }
}
