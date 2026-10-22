package mg.grh.dao;

import mg.grh.model.BesoinPoste;
import org.springframework.stereotype.Repository;

@Repository
public class BesoinPosteDao extends GenericDao<BesoinPoste> {
    public BesoinPosteDao() {
        super(BesoinPoste.class);
    }
}
