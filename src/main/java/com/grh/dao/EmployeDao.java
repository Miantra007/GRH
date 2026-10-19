package com.grh.dao;

import com.grh.model.Employe;
import com.grh.repository.EmployeRepository;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeDao extends GenericDao<Employe> {

    public EmployeDao(EmployeRepository repository) {
        super(repository, "Employe");
    }
}
