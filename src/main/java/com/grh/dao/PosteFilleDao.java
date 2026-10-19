package com.grh.dao;

import com.grh.model.PosteFille;
import com.grh.repository.PosteFilleRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PosteFilleDao extends GenericDao<PosteFille> {

    public PosteFilleDao(PosteFilleRepository repository) {
        super(repository, "PosteFille");
    }
}
