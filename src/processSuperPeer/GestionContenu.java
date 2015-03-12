package processSuperPeer;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import org.simgrid.msg.Msg;
import org.simgrid.msg.Process;
import org.simgrid.msg.Host;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Task;

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
	 * Dans la Hashmap de haut niveau, les clés sont les noms des utilisateurs.
	 * Dans les petites Hashmaps, les clés sont des hash de publications, et les valeurs sont les publications.
	 */
	HashMap<String, HashMap<String, String>> donnees;

	/**
	 * La boite aux lettres sur laquelle le sp va écouter les requetes.
	 * 'nomHost' + '_GestionContenu'
	 */
	private String mbox;

	public GestionContenu(Host host, String name, String[]args) {
		super(host,name,args);

		this.donnees = new HashMap<String, HashMap<String, String>>();
		this.mbox = host.getName()+"_GestionContenu";
	}

	public void main(String[] args) throws MsgException {
		while(true){
			if(Task.listen(this.mbox)){
				Message msg = (Message) Task.receive(this.mbox);
				switch(msg.getType()){
				case requete_publication:
					Message<String> requete = msg;
					String publication = reponseContenu(requete.getPeerConcerne(), requete.getMessage());
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
		}
	}

	public void ajoutContenu(String publication, String expediteur){

		//On vérifie si on a déjà une entrée pour le peer qui veut poster, sinon on en crée une
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

	String reponseContenu(String posteur, String hash){
		
		String reponse=null;
		
		if(donnees.containsKey(posteur)){
			if(donnees.containsKey(hash)){
				reponse = donnees.get(posteur).get(hash);
			}
		}
		
		return reponse;
	}

}