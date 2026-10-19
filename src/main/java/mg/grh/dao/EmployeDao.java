package mg.grh.dao;

import mg.grh.model.Employe;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeDao extends GenericDao<Employe> {
    public EmployeDao() {
        super(Employe.class);
    }
}
