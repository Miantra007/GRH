package mg.grh.dao;

import mg.grh.model.SignatureCdi;
import org.springframework.stereotype.Repository;

@Repository
public class SignatureCdiDao extends GenericDao<SignatureCdi> {
    public SignatureCdiDao() {
        super(SignatureCdi.class);
    }
}
