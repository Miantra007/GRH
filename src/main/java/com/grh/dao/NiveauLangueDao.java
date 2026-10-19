package com.grh.dao;

import com.grh.model.NiveauLangue;
import com.grh.repository.NiveauLangueRepository;
import org.springframework.stereotype.Repository;

@Repository
public class NiveauLangueDao extends GenericDao<NiveauLangue> {

    public NiveauLangueDao(NiveauLangueRepository repository) {
        super(repository, "NiveauLangue");
    }
}
