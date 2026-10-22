package mg.grh.dao;

import mg.grh.model.HistoriqueEmploye;
import org.springframework.stereotype.Repository;

@Repository
public class HistoriqueEmployeDao extends GenericDao<HistoriqueEmploye> {
    public HistoriqueEmployeDao() {
        super(HistoriqueEmploye.class);
    }
}
