package mg.grh.dao;

import mg.grh.model.ExperienceProfessionnelle;
import org.springframework.stereotype.Repository;

@Repository
public class ExperienceProfessionnelleDao extends GenericDao<ExperienceProfessionnelle> {
    public ExperienceProfessionnelleDao() {
        super(ExperienceProfessionnelle.class);
    }
}
