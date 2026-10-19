package mg.grh.dao;

import mg.grh.model.Langue;
import org.springframework.stereotype.Repository;

@Repository
public class LangueDao extends GenericDao<Langue> {
    public LangueDao() {
        super(Langue.class);
    }
}
