package com.grh.dao;

import com.grh.model.ExperienceProfessionnelle;
import com.grh.repository.ExperienceProfessionnelleRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ExperienceProfessionnelleDao extends GenericDao<ExperienceProfessionnelle> {

    public ExperienceProfessionnelleDao(ExperienceProfessionnelleRepository repository) {
        super(repository, "ExperienceProfessionnelle");
    }
}
