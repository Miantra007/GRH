package com.grh.dao;

import com.grh.model.Travailleur;
import com.grh.repository.TravailleurRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TravailleurDao extends GenericDao<Travailleur> {

    public TravailleurDao(TravailleurRepository repository) {
        super(repository, "Travailleur");
    }
}
