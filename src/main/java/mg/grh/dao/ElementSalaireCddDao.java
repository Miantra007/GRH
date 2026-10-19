package mg.grh.dao;

import mg.grh.model.ElementSalaireCdd;
import org.springframework.stereotype.Repository;

@Repository
public class ElementSalaireCddDao extends GenericDao<ElementSalaireCdd> {
    public ElementSalaireCddDao() {
        super(ElementSalaireCdd.class);
    }
}
