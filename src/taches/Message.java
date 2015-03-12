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
	 * Le type va définir la fonction qu'a le message.
	 * Tous les types utlisés sont listés dans l'enum typeMessage.
	 */
	private typeMessage type;
	private String expediteur;
	private String destinataire;
	
	/**
	 * Certains messages peuvent se separer en une action, et le nom du peer pour lequel on fait cette action.
	 * Dans ce cas on peut ce servir de l'attribut peerConcerne. Sinon on ne s'en sert pas
	 */	
	private String peerConcerne =null;
	
	/**
	 * Lorsque l'on repond a une requete, on precisera le hash du l'objet Message auquel on repond.
	 * Le hash en question sera celui donné par la méthode hashCode() de la classe Object.
	 * Par défaut cet attribut vaut 0, dans le cas où ce n'est pas une réponse, mais le début de la communication.
	 */
	private int hashCodeMessagePrecedent = 0;
	
	/**
	 * Peut servir a indiquer sur quelle mbox renvoyer la réponse, notamment lors des requetes aux superpeers.
	 */
	private String mboxReponse=null;
	
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

	public typeMessage getType() {
		return type;
	}

	public void setType(typeMessage type) {
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

	public String getMboxReponse() {
		return mboxReponse;
	}

	public void setMboxReponse(String mboxReponse) {
		this.mboxReponse = mboxReponse;
	}	
}