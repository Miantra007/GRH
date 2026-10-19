package mg.grh.dao;

import mg.grh.model.MotifCdd;
import org.springframework.stereotype.Repository;

@Repository
public class MotifCddDao extends GenericDao<MotifCdd> {
    public MotifCddDao() {
        super(MotifCdd.class);
    }
}
