package taches;

import org.simgrid.msg.Host;
import org.simgrid.msg.Task;

/**
 * La classe Message doit pouvoir representer tous les echanges que des Process ont besoin de faire.
 * Elle est generique pour permettre souplesse et diversite dans les messages.
 *  
 * @author otmann
 *
 */

public class Message<T> extends Task{
	
	
	/**
	 * Le type va definir la fonction qu'a le message.
	 * Tous les types utlises sont listes dans l'enum typeMessage.
	 */
	private typeMessage type;
	/**
	 * Expediteur du message.
	 */
	private String expediteur;
	
	/**
	 * Destinataire du message.
	 */
	private String destinataire;
	
	/**
	 * Certains messages peuvent se separer en une action, et le nom du peer pour 
	 * lequel on fait cette action. Dans ce cas on peut se servir de l'attribut peerConcerne. Sinon on ne 
	 * s'en sert pas.
	 */	
	
	private String peerConcerne=null;
	
	private String peerConcerne2=null;
	
	/**
	 * Il peut y avoir deux peers concernes, comme dans verif_amis (peer1, peer2).
	 */
	
	private String superPeerConcerne=null;
	
	/**
	 * Lorsque l'on repond a une requete, on precisera le hash de l'objet Message auquel on repond.
	 * Le hash en question sera celui donne par la methode hashCode() de la classe Object.
	 * Par defaut cet attribut vaut 0, dans le cas ou ce n'est pas une reponse, mais le debut de la communication.
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
	
	/**
	 * Constructeur de la classe Message.
	 */
	
	public Message(){
		super();
		this.expediteur = Host.currentHost().getName();
	}
	
	
	/**
	 * Constructeur ou l'on precise directement la payload.
	 * @param message
	 */
	public Message(T message){
		super();
		this.message=message;
		this.expediteur = Host.currentHost().getName();

	}	
	
	/**
	 * Ce constructeur permet de directement indiquer que le message est une 
	 * reponse au message 'precedent'.
	 * @param message
	 * @param precedent
	 */
	public Message(T message, Message precedent){
		super();
		this.message=message;
		this.hashCodeMessagePrecedent = precedent.hashCode();
		this.expediteur = Host.currentHost().getName();

	}

	//Suivent tous les getters et les setters des attributs private.
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

	public String getSuperPeerConcerne() {
		return superPeerConcerne;
	}

	public void setSuperPeerConcerne(String superPeerConcerne) {
		this.superPeerConcerne = superPeerConcerne;
	}

	public String getPeerConcerne2() {
		return peerConcerne2;
	}

	public void setPeerConcerne2(String peerConcerne2) {
		this.peerConcerne2 = peerConcerne2;
	}	
}