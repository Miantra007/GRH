package mg.grh.dao;

import mg.grh.model.Besoin;
import org.springframework.stereotype.Repository;

@Repository
public class BesoinDao extends GenericDao<Besoin> {
    public BesoinDao() {
        super(Besoin.class);
    }
}
