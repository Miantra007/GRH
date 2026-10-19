package com.grh.dao;

import com.grh.model.Cv;
import com.grh.repository.CvRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CvDao extends GenericDao<Cv> {

    public CvDao(CvRepository repository) {
        super(repository, "Cv");
    }
}
