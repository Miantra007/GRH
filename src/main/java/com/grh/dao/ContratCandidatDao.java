package com.grh.dao;

import com.grh.model.ContratCandidat;
import com.grh.repository.ContratCandidatRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ContratCandidatDao extends GenericDao<ContratCandidat> {

    public ContratCandidatDao(ContratCandidatRepository repository) {
        super(repository, "ContratCandidat");
    }
}
