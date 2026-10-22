# Relie le deroulement de notre projet qui doit se passer comme suit

- Le directeur de plusieur poste (DSI) peut demander un besoin pour plusieurs postes chez lui 
  ex: poste pour dev , administrateur reseau 
- Le rh recoit ce besoin puis il remplit les formulaires de recrutement pour chaque poste , 1 poste = 1 formulaire de recrutement
- Puis le responsable financier doit valider ce que le rh a fournit notamment le salaire pour chaque poste ,il peut soit valider soit refuser , si il refuse alors le Rh doit refaire le processur pour le salaire du poste refuser , si il valide alors la direction generele doit valider en dernier

### ne pas faire la partie ce dessus (on utilisera les donnees de test c'est encore a implementer par une autre personne)

- Apres validation du dg , le Rh pourra alors publier les offres d'emploi de chaque poste present dans le besoin initial vers le cote clients
- Du cote client le candidat verra les offre d'emploi il pourra postuler mais doit s'inscrir d'abord , il remplira le cv et ainsi que la lettre de motivation
- Le Rh recevra alors les demandes de candidature pour chaque poste , il fera le selection de dossier 
- Un notification sera envoyer au candidat retenu apres la selection de dossier et avec des messages lui disant la date du test ainsi que l'entretient
- Il y aura des changement de statut tout au long de cela
- Puis le Rh decide finalement si le candidat est retenu ou rejete , si rejete une notification sera envoyer au candidat en lui disant qu'il a ete rejete,
  si retenu une notification sera envoyer au candidat en lui disant qu'il a ete retenu puis avec cela un bouton disant de remplir le formulaire d'accepation (ceci n'est pas encore relier peux tu le faire!)
- Le candidat remplit le formulaire et la suite est deja implementer dans ce code juste a part les session qu'il doit utiliser et tout (corrige cela maintenant pour que ca marche vraiment du cote rh et candidat)
- Puis apres les etapes ,une notification sera envoyer au candidat que le formulaire d'accepatation est valide (apres les plusieurs scene passe) et avec sela il doit cliquer sur un bouton choisir le contrat
  (ceci n'est pas encore relier peux tu le faire!)
- Puis apres le contrat changera de statut qui est deja implementer dedans et quand le contrat sera signe par le candidat ,le rh devra la valider alors apres une notification sera envoyer au candidat , ainsi que les choses qu'il devra faire et en meme temps voici ce qui soit se passer
- 
### ne pas faire la partie ce dessous (c'est encore a implementer par une autre personne)
  
- Verifier le type de besoin :
    - si CREATION on inserera juste dans la table employe le candidat recrute
    - si REMPLACEMENT la personne qui a occupe le poste avant changera de statut -> en suspendu puis sera inserer dans la table historique_employer et le candidat recrute remplacera sa place dans la table employe

*En gros on voudra un projet synchroniser avec les fonctionnalite deja implementer comme cela les personne qui on encore du travail pour tout de suite utiliser ce projet  