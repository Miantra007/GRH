package com.grh.dao;

import com.grh.model.Employeur;
import com.grh.repository.EmployeurRepository;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeurDao extends GenericDao<Employeur> {

    public EmployeurDao(EmployeurRepository repository) {
        super(repository, "Employeur");
    }
}
