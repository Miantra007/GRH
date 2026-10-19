package mg.grh.dao;

import mg.grh.model.Entretien;
import org.springframework.stereotype.Repository;

@Repository
public class EntretienDao extends GenericDao<Entretien> {
    public EntretienDao() {
        super(Entretien.class);
    }
}
