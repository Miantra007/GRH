package com.grh.dao;

import com.grh.model.TypeAvenant;
import com.grh.repository.TypeAvenantRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TypeAvenantDao extends GenericDao<TypeAvenant> {

    public TypeAvenantDao(TypeAvenantRepository repository) {
        super(repository, "TypeAvenant");
    }
}
