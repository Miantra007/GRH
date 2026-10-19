package com.grh.dao;

import com.grh.model.Role;
import com.grh.repository.RoleRepository;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDao extends GenericDao<Role> {

    public RoleDao(RoleRepository repository) {
        super(repository, "Role");
    }
}
