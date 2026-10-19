package mg.grh.dao;

import mg.grh.model.TexteLegal;
import org.springframework.stereotype.Repository;

@Repository
public class TexteLegalDao extends GenericDao<TexteLegal> {
    public TexteLegalDao() {
        super(TexteLegal.class);
    }
}
