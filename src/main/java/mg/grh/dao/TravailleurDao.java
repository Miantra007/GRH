package mg.grh.dao;

import mg.grh.model.Travailleur;
import org.springframework.stereotype.Repository;

@Repository
public class TravailleurDao extends GenericDao<Travailleur> {
    public TravailleurDao() {
        super(Travailleur.class);
    }
}
