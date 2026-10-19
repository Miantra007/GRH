package com.grh.dao;

import com.grh.model.Formation;
import com.grh.repository.FormationRepository;
import org.springframework.stereotype.Repository;

@Repository
public class FormationDao extends GenericDao<Formation> {

    public FormationDao(FormationRepository repository) {
        super(repository, "Formation");
    }
}
