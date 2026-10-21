package mg.grh.service;

import mg.grh.model.Candidat;
import mg.grh.repository.CandidatRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class CandidatService extends GenericService<Candidat> {
    private final CandidatRepository candidatRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public CandidatService(CandidatRepository repository) {
        super(repository);
        this.candidatRepository = repository;
    }

    public Candidat enregistrerCandidat(Candidat candidat, String motDePasse) {
        if (candidat.getNomCandidat() == null || candidat.getNomCandidat().isBlank()
                || candidat.getPrenomCandidat() == null || candidat.getPrenomCandidat().isBlank()) {
            throw new IllegalArgumentException("Le nom et le prénom sont obligatoires.");
        }
        if (candidat.getEmail() == null || !candidat.getEmail().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("Adresse email invalide.");
        }
        candidat.setNomCandidat(candidat.getNomCandidat().trim());
        candidat.setPrenomCandidat(candidat.getPrenomCandidat().trim());
        candidat.setEmail(candidat.getEmail().trim().toLowerCase(Locale.ROOT));
        if (candidat.getNomCandidat().length() > 50 || candidat.getPrenomCandidat().length() > 50
                || candidat.getEmail().length() > 150) {
            throw new IllegalArgumentException("Un des champs dépasse la longueur autorisée.");
        }
        candidatRepository.findByEmail(candidat.getEmail())
                .filter(existant -> !existant.getId().equals(candidat.getId()))
                .ifPresent(existant -> { throw new IllegalArgumentException("Un compte existe déjà avec cet email."); });
        if (motDePasse != null && !motDePasse.isBlank()) {
            if (motDePasse.length() < 8) throw new IllegalArgumentException("Le mot de passe doit contenir au moins 8 caractères.");
            candidat.setMotDePasse(passwordEncoder.encode(motDePasse));
        } else if (candidat.getId() == null) {
            throw new IllegalArgumentException("Le mot de passe est obligatoire.");
        }
        return candidatRepository.save(candidat);
    }

    public Candidat trouverParId(Integer id) {
        return candidatRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Compte candidat introuvable."));
    }
}
