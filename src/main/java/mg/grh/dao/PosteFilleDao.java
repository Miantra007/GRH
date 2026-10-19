package mg.grh.dao;

import mg.grh.model.PosteFille;
import org.springframework.stereotype.Repository;

@Repository
public class PosteFilleDao extends GenericDao<PosteFille> {
    public PosteFilleDao() {
        super(PosteFille.class);
    }
}
