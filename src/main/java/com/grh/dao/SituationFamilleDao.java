package com.grh.dao;

import com.grh.model.SituationFamille;
import com.grh.repository.SituationFamilleRepository;
import org.springframework.stereotype.Repository;

@Repository
public class SituationFamilleDao extends GenericDao<SituationFamille> {

    public SituationFamilleDao(SituationFamilleRepository repository) {
        super(repository, "SituationFamille");
    }
}
