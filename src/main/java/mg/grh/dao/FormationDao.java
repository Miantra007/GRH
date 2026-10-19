package mg.grh.dao;

import mg.grh.model.Formation;
import org.springframework.stereotype.Repository;

@Repository
public class FormationDao extends GenericDao<Formation> {
    public FormationDao() {
        super(Formation.class);
    }
}
