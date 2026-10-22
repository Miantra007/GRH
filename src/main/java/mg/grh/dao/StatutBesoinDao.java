package mg.grh.dao;

import mg.grh.model.StatutBesoin;
import org.springframework.stereotype.Repository;

@Repository
public class StatutBesoinDao extends GenericDao<StatutBesoin> {
    public StatutBesoinDao() {
        super(StatutBesoin.class);
    }
}
