package mg.grh.dao;

import mg.grh.model.HistoriqueStatutContrat;
import org.springframework.stereotype.Repository;

@Repository
public class HistoriqueStatutContratDao extends GenericDao<HistoriqueStatutContrat> {
    public HistoriqueStatutContratDao() {
        super(HistoriqueStatutContrat.class);
    }
}
