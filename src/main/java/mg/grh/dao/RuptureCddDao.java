package mg.grh.dao;

import mg.grh.model.RuptureCdd;
import org.springframework.stereotype.Repository;

@Repository
public class RuptureCddDao extends GenericDao<RuptureCdd> {
    public RuptureCddDao() {
        super(RuptureCdd.class);
    }
}
