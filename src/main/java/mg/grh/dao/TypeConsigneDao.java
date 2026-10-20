package mg.grh.dao;

import mg.grh.model.TypeConsigne;
import org.springframework.stereotype.Repository;

@Repository
public class TypeConsigneDao extends GenericDao<TypeConsigne> {
    public TypeConsigneDao() {
        super(TypeConsigne.class);
    }
}
