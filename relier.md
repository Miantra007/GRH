# Relie le deroulement de notre projet qui doit se passer comme suit

- Le directeur de plusieurs postes (ex:DSI) peut demander un besoin pour plusieurs postes chez lui 
  ex: poste pour dev , administrateur reseau 
- Le rh recoit ce besoin puis il remplit les formulaires de recrutement pour chaque poste , 1 poste = 1 formulaire de recrutement
- Puis le responsable financier doit valider ce que le rh a fournit notamment le salaire pour chaque poste ,il peut soit valider soit refuser , si il refuse alors le Rh doit refaire le processur pour le salaire du poste refuser , si il valide alors la direction generele doit valider en dernier


- Apres validation du dg , le Rh pourra alors publier les offres d'emploi de chaque poste present dans le besoin initial vers le cote clients
- Du cote client le candidat verra les offre d'emploi il pourra postuler mais doit s'inscrir d'abord , il remplira le cv et ainsi que la lettre de motivation
- Le Rh recevra alors les demandes de candidature pour chaque poste , il fera le selection de dossier 
- Un notification sera envoyer au candidat retenu apres la selection de dossier et avec des messages lui disant la date du test ainsi que l'entretient
- Il y aura des changement de statut tout au long de cela
- Puis le Rh decide finalement si le candidat est retenu ou rejete , si rejete une notification sera envoyer au candidat en lui disant qu'il a ete rejete,
- Le candidat remplit le formulaire et la suite est deja implementer dans ce code juste a part les session qu'il doit utiliser et tout (corrige cela maintenant pour que ca marche vraiment du cote rh et candidat)
- Puis apres les etapes ,une notification sera envoyer au candidat que le formulaire d'accepatation est valide (apres les plusieurs scene passe) et avec sela il doit cliquer sur un bouton choisir le contrat
- Puis apres le contrat changera de statut qui est deja implementer dedans et quand le contrat sera signe par le candidat ,le rh devra la valider alors apres une notification sera envoyer au candidat , ainsi que les choses qu'il devra faire et en meme temps voici ce qui soit se passer

  
- Verifier le type de besoin :
    - si CREATION on inserera juste dans la table employe le candidat recrute
    - si REMPLACEMENT la personne qui a occupe le poste avant changera de statut -> en suspendu puis sera inserer dans la table historique_employer et le candidat recrute remplacera sa place dans la table employe

*En gros on voudra un projet synchroniser

Implemente ce qui ne sont pas encore dans le projet , corrige les gestions des connexions et tout
puis ameliore le design , met le css dans un fichier style.css, le header et footer dans des fichier respectif

--------------------------------------------------------------------------------------------------------------


- Par defaut l'index doit tout de suite aller vers la liste des offres d'emploi pas besoin de s'authentifier

- lorsque le candidat clique sur postuler si il n'est pas encore inscrit alors ca va rediriger vers saisie-candidat.html pas login , corrige
- dans le cv langue doit etre dropdown venant de la base
