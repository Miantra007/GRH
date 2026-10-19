package com.grh.dao;

import com.grh.model.Civilite;
import com.grh.repository.CiviliteRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CiviliteDao extends GenericDao<Civilite> {

    public CiviliteDao(CiviliteRepository repository) {
        super(repository, "Civilite");
    }
}
