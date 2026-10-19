package mg.grh.dao;

import mg.grh.model.ConventionCollective;
import org.springframework.stereotype.Repository;

@Repository
public class ConventionCollectiveDao extends GenericDao<ConventionCollective> {
    public ConventionCollectiveDao() {
        super(ConventionCollective.class);
    }
}
