package mg.grh.dao;

import mg.grh.model.Candidat;
import org.springframework.stereotype.Repository;

@Repository
public class CandidatDao extends GenericDao<Candidat> {
    public CandidatDao() {
        super(Candidat.class);
    }
}
