package mg.grh.repository;

import mg.grh.model.Besoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BesoinRepository extends JpaRepository<Besoin, Integer> {
    List<Besoin> findByStatutBesoinLibelleOrderByDateDemandeAsc(String libelle);
}
