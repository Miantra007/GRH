package mg.grh.dao;

import mg.grh.model.ContratCandidat;
import org.springframework.stereotype.Repository;

@Repository
public class ContratCandidatDao extends GenericDao<ContratCandidat> {
    public ContratCandidatDao() {
        super(ContratCandidat.class);
    }
}
