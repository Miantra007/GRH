package mg.grh.dao;

import mg.grh.model.ContratCdi;
import org.springframework.stereotype.Repository;

@Repository
public class ContratCdiDao extends GenericDao<ContratCdi> {
    public ContratCdiDao() {
        super(ContratCdi.class);
    }
}
