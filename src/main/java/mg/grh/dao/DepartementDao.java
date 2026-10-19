package mg.grh.dao;

import mg.grh.model.Departement;
import org.springframework.stereotype.Repository;

@Repository
public class DepartementDao extends GenericDao<Departement> {
    public DepartementDao() {
        super(Departement.class);
    }
}
