package com.grh.dao;

import com.grh.model.ContratCe;
import com.grh.repository.ContratCeRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ContratCeDao extends GenericDao<ContratCe> {

    public ContratCeDao(ContratCeRepository repository) {
        super(repository, "ContratCe");
    }
}
