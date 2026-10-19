package com.grh.dao;

import com.grh.model.Ville;
import com.grh.repository.VilleRepository;
import org.springframework.stereotype.Repository;

@Repository
public class VilleDao extends GenericDao<Ville> {

    public VilleDao(VilleRepository repository) {
        super(repository, "Ville");
    }
}
