package com.grh.dao;

import com.grh.model.CentreInteret;
import com.grh.repository.CentreInteretRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CentreInteretDao extends GenericDao<CentreInteret> {

    public CentreInteretDao(CentreInteretRepository repository) {
        super(repository, "CentreInteret");
    }
}
