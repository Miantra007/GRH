package mg.grh.dao;

import mg.grh.model.TypeAvenant;
import org.springframework.stereotype.Repository;

@Repository
public class TypeAvenantDao extends GenericDao<TypeAvenant> {
    public TypeAvenantDao() {
        super(TypeAvenant.class);
    }
}
