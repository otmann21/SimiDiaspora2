package taches;

/**
 * Tous les types utilisés par Message vont être créés ici.
 * Ajouter des commentaires pour préciser leur rôle.
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
	 * peerConcerne : le peer qui l'a publiée
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
	 * Lorqu'un utilisateur poste qqch sur son mur, il doit prevenir son spGestionMur 
	 * pour qu'il l'ajoute dans la liste de publications
	 */
	maj_mur, //que signifie ce nom, maj ?
	
	/**
	 * Le champ message n'est pas utilisé
	 * Un peer demande le mur d'un utilisateur a un spGestionMur
	 * Le peer dont on veut le mur est passé dans peerConcerne 
	 */
	requete_mur,
	
	/**
	 * Message<ArrayList<String[]>>
	 * Le spGestionMur renvoie la liste des publi de l'utilisateur qu'on a demandé
	 */
	reponseMur,
	
	verif_ami,
	
	/**
	 * on utilise le message verif_ami lorsque l'on demande si i et j sont bien amis
	 * au process SP qui gère les liens d'amitié.
	 */
	
	liste_ami,
	
	/**
	 * on utilise le type de message liste_ami quand on veut connaître la liste
	 * des amis d'un pair donné.
	 */
	
	reponse_sontAmis,
	
	reponse_listeAmis

}
