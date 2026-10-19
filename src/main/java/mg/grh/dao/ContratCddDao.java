package mg.grh.dao;

import mg.grh.model.ContratCdd;
import org.springframework.stereotype.Repository;

@Repository
public class ContratCddDao extends GenericDao<ContratCdd> {
    public ContratCddDao() {
        super(ContratCdd.class);
    }
}
