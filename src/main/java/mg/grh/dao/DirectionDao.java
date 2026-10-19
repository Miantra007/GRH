package mg.grh.dao;

import mg.grh.model.Direction;
import org.springframework.stereotype.Repository;

@Repository
public class DirectionDao extends GenericDao<Direction> {
    public DirectionDao() {
        super(Direction.class);
    }
}
