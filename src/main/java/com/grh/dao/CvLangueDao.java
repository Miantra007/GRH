package com.grh.dao;

import com.grh.model.CvLangue;
import com.grh.repository.CvLangueRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CvLangueDao extends GenericDao<CvLangue> {

    public CvLangueDao(CvLangueRepository repository) {
        super(repository, "CvLangue");
    }
}
