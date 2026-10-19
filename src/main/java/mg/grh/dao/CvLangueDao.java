package mg.grh.dao;

import mg.grh.model.CvLangue;
import org.springframework.stereotype.Repository;

@Repository
public class CvLangueDao extends GenericDao<CvLangue> {
    public CvLangueDao() {
        super(CvLangue.class);
    }
}
