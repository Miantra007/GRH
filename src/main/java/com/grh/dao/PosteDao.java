package com.grh.dao;

import com.grh.model.Poste;
import com.grh.repository.PosteRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PosteDao extends GenericDao<Poste> {

    public PosteDao(PosteRepository repository) {
        super(repository, "Poste");
    }
}
