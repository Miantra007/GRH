package com.grh.dao;

import com.grh.model.Langue;
import com.grh.repository.LangueRepository;
import org.springframework.stereotype.Repository;

@Repository
public class LangueDao extends GenericDao<Langue> {

    public LangueDao(LangueRepository repository) {
        super(repository, "Langue");
    }
}
