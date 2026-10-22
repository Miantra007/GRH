package mg.grh.dao;

import mg.grh.model.TypeBesoin;
import org.springframework.stereotype.Repository;

@Repository
public class TypeBesoinDao extends GenericDao<TypeBesoin> {
    public TypeBesoinDao() {
        super(TypeBesoin.class);
    }
}
