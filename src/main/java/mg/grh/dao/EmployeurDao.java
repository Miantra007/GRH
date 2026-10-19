package mg.grh.dao;

import mg.grh.model.Employeur;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeurDao extends GenericDao<Employeur> {
    public EmployeurDao() {
        super(Employeur.class);
    }
}
