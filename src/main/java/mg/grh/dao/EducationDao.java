package mg.grh.dao;

import mg.grh.model.Education;
import org.springframework.stereotype.Repository;

@Repository
public class EducationDao extends GenericDao<Education> {
    public EducationDao() {
        super(Education.class);
    }
}
