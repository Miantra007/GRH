package mg.grh.repository;

import mg.grh.model.Besoin;
import mg.grh.model.BesoinPoste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BesoinPosteRepository extends JpaRepository<BesoinPoste, Integer> {
    List<BesoinPoste> findByBesoinIdOrderByIdAsc(Integer besoinId);

    @Query("select distinct bp.besoin from BesoinPoste bp "
            + "where bp.decisionFinance = 'ACCEPTEE' and bp.decisionDg is null "
            + "order by bp.besoin.dateDemande asc")
    List<Besoin> findBesoinsAvecPosteEnAttenteDg();
}
