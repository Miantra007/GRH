package mg.grh.dao;

import mg.grh.model.TestTechnique;
import org.springframework.stereotype.Repository;

@Repository
public class TestTechniqueDao extends GenericDao<TestTechnique> {
    public TestTechniqueDao() {
        super(TestTechnique.class);
    }
}
