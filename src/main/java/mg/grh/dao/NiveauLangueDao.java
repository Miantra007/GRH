package mg.grh.dao;

import mg.grh.model.NiveauLangue;
import org.springframework.stereotype.Repository;

@Repository
public class NiveauLangueDao extends GenericDao<NiveauLangue> {
    public NiveauLangueDao() {
        super(NiveauLangue.class);
    }
}
