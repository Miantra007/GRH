package com.grh.dao;

import com.grh.model.SignatureCdi;
import com.grh.repository.SignatureCdiRepository;
import org.springframework.stereotype.Repository;

@Repository
public class SignatureCdiDao extends GenericDao<SignatureCdi> {

    public SignatureCdiDao(SignatureCdiRepository repository) {
        super(repository, "SignatureCdi");
    }
}
