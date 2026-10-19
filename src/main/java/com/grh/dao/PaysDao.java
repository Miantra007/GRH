package com.grh.dao;

import com.grh.model.Pays;
import com.grh.repository.PaysRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PaysDao extends GenericDao<Pays> {

    public PaysDao(PaysRepository repository) {
        super(repository, "Pays");
    }
}
