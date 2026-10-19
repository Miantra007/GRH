package mg.grh.dao;

import mg.grh.model.TypeRupture;
import org.springframework.stereotype.Repository;

@Repository
public class TypeRuptureDao extends GenericDao<TypeRupture> {
    public TypeRuptureDao() {
        super(TypeRupture.class);
    }
}
