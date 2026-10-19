package mg.grh.dao;

import mg.grh.model.StatutPoste;
import org.springframework.stereotype.Repository;

@Repository
public class StatutPosteDao extends GenericDao<StatutPoste> {
    public StatutPosteDao() {
        super(StatutPoste.class);
    }
}
