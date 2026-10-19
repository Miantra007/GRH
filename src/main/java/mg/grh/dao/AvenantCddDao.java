package mg.grh.dao;

import mg.grh.model.AvenantCdd;
import org.springframework.stereotype.Repository;

@Repository
public class AvenantCddDao extends GenericDao<AvenantCdd> {
    public AvenantCddDao() {
        super(AvenantCdd.class);
    }
}
