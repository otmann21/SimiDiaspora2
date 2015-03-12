package taches;

import org.simgrid.msg.Task;

/**
 * La classe Message doit pouvoir représenter tous les échanges que des Process ont besoin de faire.
 * Elle est générique.
 *  
 * @author otmann
 *
 */

public class Message<T> extends Task{
	
	
	/**
	 * Le type est une chaine de caractere qui va définir la fonction qu'a le message.
	 * Il faut enumerer tous les types utilisés ici, pour en avoir une liste
	 */
	private String type;
	private String expediteur;
	private String destinataire;
	
	/**
	 * Certains messages peuvent se separer en une action, et le nom du peer pour lequel on fait cette action.
	 * Dans ce cas on peut ce servir de l'attribut peerConcerne. Sinon on ne s'en sert pas
	 */	
	private String peerConcerne ="";
	
	/**
	 * Lorsque l'on repond a une requete, on precisera le hash du l'objet Message auquel on repond.
	 * Le hash en question sera celui donné par la méthode hashCode() de la classe Object.
	 * Par défaut cet attribut vaut 0, dans le cas où ce n'est pas une réponse, mais le début de la communication.
	 */
	private int hashCodeMessagePrecedent = 0;
	
	/**
	 * La payload du message.
	 */
	private T message;
	
	public Message(){
		super();
	}
	
	public Message(T message){
		super();
		this.message=message;
	}	
	
	/**
	 * Ce constructeur permet de directement indiquer que le message est une reponse au message 'precedent'.
	 * @param message
	 * @param precedent
	 */
	public Message(T message, Message precedent){
		super();
		this.message=message;
		this.hashCodeMessagePrecedent = precedent.hashCode();
	}

	public String getExpediteur() {
		return expediteur;
	}

	public void setExpediteur(String expediteur) {
		this.expediteur = expediteur;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDestinataire() {
		return destinataire;
	}

	public void setDestinataire(String destinataire) {
		this.destinataire = destinataire;
	}

	public String getPeerConcerne() {
		return peerConcerne;
	}

	public void setPeerConcerne(String peerConcerne) {
		this.peerConcerne = peerConcerne;
	}

	public int getHashCodeMessagePrecedent() {
		return hashCodeMessagePrecedent;
	}

	public void setHashCodeMessagePrecedent(int hashCodeMessagePrecedent) {
		this.hashCodeMessagePrecedent = hashCodeMessagePrecedent;
	}

	public T getMessage() {
		return message;
	}

	public void setMessage(T message) {
		this.message = message;
	}	
}