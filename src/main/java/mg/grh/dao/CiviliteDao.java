package mg.grh.dao;

import mg.grh.model.Civilite;
import org.springframework.stereotype.Repository;

@Repository
public class CiviliteDao extends GenericDao<Civilite> {
    public CiviliteDao() {
        super(Civilite.class);
    }
}
