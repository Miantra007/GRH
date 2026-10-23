package mg.grh;

import mg.grh.dto.BesoinCreationForm;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BesoinCreationFormTest {

    @Test
    void accepteUneLigneDePosteValide() {
        BesoinCreationForm.LignePoste ligne = new BesoinCreationForm.LignePoste();
        ligne.setPosteId(1);
        ligne.setNombrePostes(2);

        assertDoesNotThrow(ligne::valider);
    }

    @Test
    void refuseUneLigneSansNombreDePostes() {
        BesoinCreationForm.LignePoste ligne = new BesoinCreationForm.LignePoste();
        ligne.setPosteId(1);
        ligne.setNombrePostes(null);

        assertThrows(IllegalArgumentException.class, ligne::valider);
    }
}
