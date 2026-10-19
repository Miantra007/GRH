package mg.grh.dao;

import mg.grh.model.Competence;
import org.springframework.stereotype.Repository;

@Repository
public class CompetenceDao extends GenericDao<Competence> {
    public CompetenceDao() {
        super(Competence.class);
    }
}
