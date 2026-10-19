package com.grh.dao;

import com.grh.model.TypeRupture;
import com.grh.repository.TypeRuptureRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TypeRuptureDao extends GenericDao<TypeRupture> {

    public TypeRuptureDao(TypeRuptureRepository repository) {
        super(repository, "TypeRupture");
    }
}
