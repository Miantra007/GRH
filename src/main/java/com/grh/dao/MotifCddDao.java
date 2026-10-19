package com.grh.dao;

import com.grh.model.MotifCdd;
import com.grh.repository.MotifCddRepository;
import org.springframework.stereotype.Repository;

@Repository
public class MotifCddDao extends GenericDao<MotifCdd> {

    public MotifCddDao(MotifCddRepository repository) {
        super(repository, "MotifCdd");
    }
}
