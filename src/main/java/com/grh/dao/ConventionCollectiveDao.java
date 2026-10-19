package com.grh.dao;

import com.grh.model.ConventionCollective;
import com.grh.repository.ConventionCollectiveRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ConventionCollectiveDao extends GenericDao<ConventionCollective> {

    public ConventionCollectiveDao(ConventionCollectiveRepository repository) {
        super(repository, "ConventionCollective");
    }
}
