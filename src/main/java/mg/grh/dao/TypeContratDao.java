package mg.grh.dao;

import mg.grh.model.TypeContrat;
import org.springframework.stereotype.Repository;

@Repository
public class TypeContratDao extends GenericDao<TypeContrat> {
    public TypeContratDao() {
        super(TypeContrat.class);
    }
}
