package com.grh.dao;

import com.grh.model.TexteLegal;
import com.grh.repository.TexteLegalRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TexteLegalDao extends GenericDao<TexteLegal> {

    public TexteLegalDao(TexteLegalRepository repository) {
        super(repository, "TexteLegal");
    }
}
