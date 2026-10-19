package com.grh.dao;

import com.grh.model.CategorieProfessionnelle;
import com.grh.repository.CategorieProfessionnelleRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CategorieProfessionnelleDao extends GenericDao<CategorieProfessionnelle> {

    public CategorieProfessionnelleDao(CategorieProfessionnelleRepository repository) {
        super(repository, "CategorieProfessionnelle");
    }
}
