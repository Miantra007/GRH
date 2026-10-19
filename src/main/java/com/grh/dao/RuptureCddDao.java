package com.grh.dao;

import com.grh.model.RuptureCdd;
import com.grh.repository.RuptureCddRepository;
import org.springframework.stereotype.Repository;

@Repository
public class RuptureCddDao extends GenericDao<RuptureCdd> {

    public RuptureCddDao(RuptureCddRepository repository) {
        super(repository, "RuptureCdd");
    }
}
