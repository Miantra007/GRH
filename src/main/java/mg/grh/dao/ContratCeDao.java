package mg.grh.dao;

import mg.grh.model.ContratCe;
import org.springframework.stereotype.Repository;

@Repository
public class ContratCeDao extends GenericDao<ContratCe> {
    public ContratCeDao() {
        super(ContratCe.class);
    }
}
