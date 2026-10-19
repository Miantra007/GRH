package com.grh.dao;

import com.grh.model.ElementSalaireCdd;
import com.grh.repository.ElementSalaireCddRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ElementSalaireCddDao extends GenericDao<ElementSalaireCdd> {

    public ElementSalaireCddDao(ElementSalaireCddRepository repository) {
        super(repository, "ElementSalaireCdd");
    }
}
