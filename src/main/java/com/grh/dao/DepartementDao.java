package com.grh.dao;

import com.grh.model.Departement;
import com.grh.repository.DepartementRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DepartementDao extends GenericDao<Departement> {

    public DepartementDao(DepartementRepository repository) {
        super(repository, "Departement");
    }
}
