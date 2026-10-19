package com.grh.dao;

import com.grh.model.Entretien;
import com.grh.repository.EntretienRepository;
import org.springframework.stereotype.Repository;

@Repository
public class EntretienDao extends GenericDao<Entretien> {

    public EntretienDao(EntretienRepository repository) {
        super(repository, "Entretien");
    }
}
