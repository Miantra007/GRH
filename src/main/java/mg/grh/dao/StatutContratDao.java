package mg.grh.dao;

import mg.grh.model.StatutContrat;
import org.springframework.stereotype.Repository;

@Repository
public class StatutContratDao extends GenericDao<StatutContrat> {
    public StatutContratDao() {
        super(StatutContrat.class);
    }
}
