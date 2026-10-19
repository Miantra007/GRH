package mg.grh.dao;

import mg.grh.model.LangueContrat;
import org.springframework.stereotype.Repository;

@Repository
public class LangueContratDao extends GenericDao<LangueContrat> {
    public LangueContratDao() {
        super(LangueContrat.class);
    }
}
