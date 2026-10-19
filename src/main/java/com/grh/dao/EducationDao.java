package com.grh.dao;

import com.grh.model.Education;
import com.grh.repository.EducationRepository;
import org.springframework.stereotype.Repository;

@Repository
public class EducationDao extends GenericDao<Education> {

    public EducationDao(EducationRepository repository) {
        super(repository, "Education");
    }
}
