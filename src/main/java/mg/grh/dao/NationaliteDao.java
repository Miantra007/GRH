package mg.grh.dao;

import mg.grh.model.Nationalite;
import org.springframework.stereotype.Repository;

@Repository
public class NationaliteDao extends GenericDao<Nationalite> {
    public NationaliteDao() {
        super(Nationalite.class);
    }
}
