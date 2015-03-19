package processSuperPeer;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import org.simgrid.msg.HostFailureException;
import org.simgrid.msg.Msg;
import org.simgrid.msg.Process;
import org.simgrid.msg.Host;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Task;
import org.simgrid.msg.TimeoutException;
import org.simgrid.msg.TransferFailureException;

import taches.Message;
import taches.typeMessage;

/**
 * Ce process va stocker les publications des utilisateurs.
 *  
 * @author otmann
 *
 */

public class GestionContenu extends Process{

	/**
	 * Dans la Hashmap de haut niveau, les cles sont les noms des utilisateurs.
	 * Dans les petites Hashmaps, les cles sont des hash de publications, 
	 * et les valeurs sont les publications.
	 */
	HashMap<String, HashMap<String, String>> donnees;

	/**
	 * La boite aux lettres sur laquelle le Super Peer va ecouter les requetes.
	 * 'nomHost' + '_GestionContenu'
	 */
	private String mbox;

	/**
	 * Adresse du SP Liens amis, que l'on va devoir contacter.
	 */
	private String SPLiensAmis; 

	/**
	 * Constructeur.
	 * @param host
	 * @param name
	 * @param args
	 */
	public GestionContenu(Host host, String name, String[]args) {
		super(host,name,args);

		this.donnees = new HashMap<String, HashMap<String, String>>();
		this.mbox = host.getName()+"_GestionContenu";
		this.SPLiensAmis = args[0];
	}

	public void main(String[] args) throws MsgException {
		while(true){
			if(Task.listen(this.mbox)){

				Message msg = (Message) Task.receive(this.mbox);				

				switch(msg.getType()){
				case requete_publication:
					Message<String> requete = msg;
					String publication;
					if(verif(requete.getPeerConcerne(), requete.getExpediteur())){
						publication = reponseContenu(requete.getPeerConcerne(), requete.getMessage());
					}
					else{
						publication = "ERR505 :Vous n'etes pas amis";
					}
					Message<String> reponse = new Message<String>(publication, requete);
					reponse.setType(typeMessage.reponse_publication);
					reponse.isend(requete.getMboxReponse());					
					break;
				case ajout_publication:
					Message<String> upload = msg;
					ajoutContenu(upload.getMessage(), upload.getExpediteur());
					Message<Boolean> confirmation = new Message<Boolean>(true, upload);
					confirmation.setType(typeMessage.confirmation);
					confirmation.isend(upload.getMboxReponse());
					break;
				}
			}
			Process.sleep(1);
		}
	}

	/**
	 * La methode ajoutContenu permet, lors d'une publication, d'ajouter le contenu de cette publication dans le
	 * Super Peer qui l'a en charge.
	 * @param publication
	 * @param expediteur
	 */
	public void ajoutContenu(String publication, String expediteur){

		//On vérifie si on a déjà une entrée pour le peer qui veut poster, sinon on en crée une.
		if(!donnees.containsKey(expediteur)){
			donnees.put(expediteur, new HashMap<String, String>());
		}

		//On calcule le hash de la publication
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-1");
			md.reset();
			md.update(publication.getBytes());
			String hash;

			try {
				hash = new String(md.digest(), "UTF-8");

				//on l'ajoute
				donnees.get(expediteur).put(hash, publication);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e1) {

			e1.printStackTrace();
		}
	}

	/**
	 * Cette methode permet le systeme d'acknowledgment mis en oeuvre dans les communications.
	 * @param posteur
	 * @param hash
	 * @return
	 */
	public String reponseContenu(String posteur, String hash){

		String reponse=null;

		if(donnees.containsKey(posteur)){
			if(donnees.get(posteur).containsKey(hash)){
				reponse = donnees.get(posteur).get(hash);
			}
		}

		return reponse;
	}
	
	/**
	 * Cette methode est chargee de demander a LiensAmis si les deux peer passes en arguments sont bien amis.
	 * @param peer1
	 * @param peer2
	 * @return pairAmi
	 * @throws TransferFailureException
	 * @throws HostFailureException
	 * @throws TimeoutException
	 */
	public boolean verif(String peer1, String peer2) throws TransferFailureException, HostFailureException, TimeoutException{
		boolean pairAmi = false ;


		Message<String> verifAm = new Message<String>();	//creation du message de requete à LiensAmi.
		verifAm.setType(typeMessage.verif_ami);
		verifAm.setPeerConcerne(peer1);
		verifAm.setPeerConcerne2(peer2);
		verifAm.setMboxReponse(this.mbox);
		verifAm.isend(SPLiensAmis+"_LiensAmis");


		//On regarde si l'on reçoie le message.

		Message msgVerif= (Message) Task.receive(mbox);
		boolean resVerif = (msgVerif.getType()==typeMessage.reponse_sontAmis) && (msgVerif.getHashCodeMessagePrecedent()==verifAm.hashCode());
		if ((resVerif)&&((boolean) msgVerif.getMessage())){
			pairAmi = true;
		}
		return pairAmi;
	}

}