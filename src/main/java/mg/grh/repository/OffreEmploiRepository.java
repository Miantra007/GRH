package mg.grh.repository;

import mg.grh.model.OffreEmploi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface OffreEmploiRepository extends JpaRepository<OffreEmploi, Integer> {
    @Query("select o from OffreEmploi o join o.statutOffre s where o.visibleClient = true and s.libelle = 'PUBLIEE' and (o.dateLimite is null or o.dateLimite >= CURRENT_DATE) order by o.datePublication desc")
    List<OffreEmploi> findOffresPubliees();

    @Query("select o from OffreEmploi o join o.statutOffre s where o.id = :id and o.visibleClient = true and s.libelle = 'PUBLIEE' and (o.dateLimite is null or o.dateLimite >= CURRENT_DATE)")
    Optional<OffreEmploi> findOffrePubliee(@Param("id") Integer id);
}
