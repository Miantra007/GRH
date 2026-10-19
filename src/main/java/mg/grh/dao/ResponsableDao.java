package mg.grh.dao;

import mg.grh.model.Responsable;
import org.springframework.stereotype.Repository;

@Repository
public class ResponsableDao extends GenericDao<Responsable> {
    public ResponsableDao() {
        super(Responsable.class);
    }
}
