package mg.grh;

import jakarta.persistence.Table;
import mg.grh.model.Besoin;
import mg.grh.model.BesoinPoste;
import mg.grh.model.StatutBesoin;
import mg.grh.model.TypeBesoin;
import mg.grh.model.TypeContrat;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ModelMappingTest {

    @Test
    void modelClassesShouldExist() {
        assertNotNull(Besoin.class.getAnnotation(Table.class));
        assertNotNull(BesoinPoste.class.getAnnotation(Table.class));
        assertNotNull(TypeBesoin.class.getAnnotation(Table.class));
        assertNotNull(StatutBesoin.class.getAnnotation(Table.class));
        assertNotNull(TypeContrat.class.getAnnotation(Table.class));
    }
}
