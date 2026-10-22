package mg.grh.dao;

import mg.grh.model.StatutEmploye;
import org.springframework.stereotype.Repository;

@Repository
public class StatutEmployeDao extends GenericDao<StatutEmploye> {
    public StatutEmployeDao() {
        super(StatutEmploye.class);
    }
}
