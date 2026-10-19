package mg.grh.dao;

import mg.grh.model.HistoriqueContratEmploye;
import org.springframework.stereotype.Repository;

@Repository
public class HistoriqueContratEmployeDao extends GenericDao<HistoriqueContratEmploye> {
    public HistoriqueContratEmployeDao() {
        super(HistoriqueContratEmploye.class);
    }
}
