package com.grh.dao;

import com.grh.model.Responsable;
import com.grh.repository.ResponsableRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ResponsableDao extends GenericDao<Responsable> {

    public ResponsableDao(ResponsableRepository repository) {
        super(repository, "Responsable");
    }
}
