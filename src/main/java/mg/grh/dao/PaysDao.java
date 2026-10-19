package mg.grh.dao;

import mg.grh.model.Pays;
import org.springframework.stereotype.Repository;

@Repository
public class PaysDao extends GenericDao<Pays> {
    public PaysDao() {
        super(Pays.class);
    }
}
