package mg.grh.dao;

import mg.grh.model.Cv;
import org.springframework.stereotype.Repository;

@Repository
public class CvDao extends GenericDao<Cv> {
    public CvDao() {
        super(Cv.class);
    }
}
