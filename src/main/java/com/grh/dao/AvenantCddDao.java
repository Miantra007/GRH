package com.grh.dao;

import com.grh.model.AvenantCdd;
import com.grh.repository.AvenantCddRepository;
import org.springframework.stereotype.Repository;

@Repository
public class AvenantCddDao extends GenericDao<AvenantCdd> {

    public AvenantCddDao(AvenantCddRepository repository) {
        super(repository, "AvenantCdd");
    }
}
