package mg.grh.dao;

import mg.grh.model.SituationFamille;
import org.springframework.stereotype.Repository;

@Repository
public class SituationFamilleDao extends GenericDao<SituationFamille> {
    public SituationFamilleDao() {
        super(SituationFamille.class);
    }
}
