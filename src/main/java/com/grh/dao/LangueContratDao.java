package com.grh.dao;

import com.grh.model.LangueContrat;
import com.grh.repository.LangueContratRepository;
import org.springframework.stereotype.Repository;

@Repository
public class LangueContratDao extends GenericDao<LangueContrat> {

    public LangueContratDao(LangueContratRepository repository) {
        super(repository, "LangueContrat");
    }
}
