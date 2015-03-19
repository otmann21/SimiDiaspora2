package taches;
/**
* Tous les types utilises par Message vont etre crees ici.
* Ajouter des commentaires pour preciser leur role.
*
*
* @author otmann
*
*/
public enum typeMessage {
/**
* Lorsque l'un peer demande a supeerpeer_GestionContenu une publication.
* Message<String>
* message : le hash de la publication
* peerConcerne : le peer qui l'a publiee, celui de qui on consulte le mur.
* mboxRponse à renseigner
*/
requete_publication,
/**
* Message<String>
* le spGestionContenu repond une publication
*/	
reponse_publication,
/**
* Lorsque l'un peer demande a supeerpeer_GestionContenu de s'ajouter une publication.
* Message<String>
* message : la publication
*/	
ajout_publication,
/**
* Message<String>
* Lorqu'un utilisateur poste qqch sur son mur, il doit prevenir son spGestionMur pour qu'il l'ajoute dans la liste de publications
* Utilise superPeerConcerne pour dire sur quel spContenu est la publi. Maj = mise a jour.
*/
maj_mur,
/**
* Le champ message n'est pas utilise.
* Un peer demande le mur d'un utilisateur a un spGestionMur
* Le peer dont on veut le mur est passe dans peerConcerne
*/
requete_mur,
/**
* Message<ArrayList<String[]>>
* Le spGestionMur renvoie la liste des publi de l'utilisateur qu'on a demande.
* C'est la liste des couples (hache du message, SPContenu).
*/
reponseMur,
/**
* Confirmation est un Message<Boolean>
* Un simple boolean pour dire si une requete a ete correctement traitee.
*/
confirmation,
/**
* on utilise le message verif_ami lorsque l'on demande si i et j sont bien amis
* au process SP qui gere les liens d'amitie.
*/
verif_ami,
/**
* on utilise le type de message liste_ami quand on veut connaitre la liste
* des amis d'un pair donne.
*/
liste_ami,
/**
* Le type de message reponse_sontAmis correspond a une reponse de la part du Process SP liensAmis,
* qui vient de verifier si deux peers sont amis.
* Le corps du message est le booleen.
*/
reponse_sontAmis,
/**
* Idem. Le Process SP listeAmis renvoie par ce message la liste des amis d'un peer.
*/
reponse_listeAmis,

/**
 * Quand le spGestionMur notifie le spLiensAmis que le peerConcerne fait partie de ses peers
 */
notif_spMur
}