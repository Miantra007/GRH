package mg.grh.dao;

import mg.grh.model.CentreInteret;
import org.springframework.stereotype.Repository;

@Repository
public class CentreInteretDao extends GenericDao<CentreInteret> {
    public CentreInteretDao() {
        super(CentreInteret.class);
    }
}
