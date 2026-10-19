package mg.grh.dao;

import mg.grh.model.Poste;
import org.springframework.stereotype.Repository;

@Repository
public class PosteDao extends GenericDao<Poste> {
    public PosteDao() {
        super(Poste.class);
    }
}
