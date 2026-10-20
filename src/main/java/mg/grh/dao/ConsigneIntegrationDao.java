package mg.grh.dao;

import mg.grh.model.ConsigneIntegration;
import org.springframework.stereotype.Repository;

@Repository
public class ConsigneIntegrationDao extends GenericDao<ConsigneIntegration> {
    public ConsigneIntegrationDao() {
        super(ConsigneIntegration.class);
    }

    /** Nombre de consignes encore à faire par un candidat. */
    public long countAFaire(Integer candidatId) {
        return em.createQuery("select count(c) from ConsigneIntegration c "
                        + "where c.candidat.id = :id and c.dateRealisation is null", Long.class)
                .setParameter("id", candidatId)
                .getSingleResult();
    }
}
