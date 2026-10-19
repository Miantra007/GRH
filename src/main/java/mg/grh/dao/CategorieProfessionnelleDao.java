package mg.grh.dao;

import mg.grh.model.CategorieProfessionnelle;
import org.springframework.stereotype.Repository;

@Repository
public class CategorieProfessionnelleDao extends GenericDao<CategorieProfessionnelle> {
    public CategorieProfessionnelleDao() {
        super(CategorieProfessionnelle.class);
    }
}
