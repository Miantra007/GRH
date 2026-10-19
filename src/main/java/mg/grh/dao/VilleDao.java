package mg.grh.dao;

import mg.grh.model.Ville;
import org.springframework.stereotype.Repository;

@Repository
public class VilleDao extends GenericDao<Ville> {
    public VilleDao() {
        super(Ville.class);
    }
}
