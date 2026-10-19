package mg.grh.dao;

import mg.grh.model.NiveauCompetence;
import org.springframework.stereotype.Repository;

@Repository
public class NiveauCompetenceDao extends GenericDao<NiveauCompetence> {
    public NiveauCompetenceDao() {
        super(NiveauCompetence.class);
    }
}
