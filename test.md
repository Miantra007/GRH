Ouvre http://localhost:8080/candidats/1/propositions/1. Le formulaire affiche le salaire (1 200 000 Ar), l’horaire, le responsable, le travail et la date de prise de poste, tous lus dans besoin_poste.
Mets « Oui » partout : seul Valider est actif. Mets un « Non » : seul Refuser est actif. Mets un « Autre » : un champ apparaît et seul Attendre est actif.
Après Attendre, ouvre http://localhost:8080/rh/6/notifications, puis « Examiner la réponse », puis Valider ou Refuser.
Ouvre http://localhost:8080/candidats/1/notifications pour voir le message envoyé au candidat.



UPDATE proposition_embauche
SET id_statut_proposition = (SELECT id_statut_proposition FROM statut_proposition WHERE libelle = 'EN_ATTENTE_CANDIDAT'),
    id_reponse_candidat = NULL, contre_proposition = NULL, salaire_demande = NULL,
    date_reponse_candidat = NULL, id_decision_rh = NULL, commentaire_rh = NULL,
    date_decision_rh = NULL, salaire_final = NULL
WHERE id_proposition = 1;

UPDATE critere_proposition
SET id_reponse_critere = NULL, contre_proposition = NULL, date_reponse = NULL
WHERE id_proposition = 1;

DELETE FROM notification WHERE id_proposition = 1;