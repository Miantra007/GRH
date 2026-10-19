package com.grh.dao;

import com.grh.model.ContratCdd;
import com.grh.repository.ContratCddRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ContratCddDao extends GenericDao<ContratCdd> {

    public ContratCddDao(ContratCddRepository repository) {
        super(repository, "ContratCdd");
    }
}
