package com.grh.dao;

import com.grh.model.StatutPoste;
import com.grh.repository.StatutPosteRepository;
import org.springframework.stereotype.Repository;

@Repository
public class StatutPosteDao extends GenericDao<StatutPoste> {

    public StatutPosteDao(StatutPosteRepository repository) {
        super(repository, "StatutPoste");
    }
}
