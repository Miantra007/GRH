package com.grh.dao;

import com.grh.model.Candidat;
import com.grh.repository.CandidatRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CandidatDao extends GenericDao<Candidat> {

    public CandidatDao(CandidatRepository repository) {
        super(repository, "Candidat");
    }
}
