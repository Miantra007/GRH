package mg.grh.dao;

import mg.grh.model.Role;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDao extends GenericDao<Role> {
    public RoleDao() {
        super(Role.class);
    }
}
