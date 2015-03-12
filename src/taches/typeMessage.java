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
	 */
	requete_publication

}
