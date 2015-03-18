package processSuperPeer;

import java.util.ArrayList;
import java.util.HashMap;

import org.simgrid.msg.Msg;
import org.simgrid.msg.Process;
import org.simgrid.msg.Host;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Task;

import taches.Message;
import taches.typeMessage;


/**
 * Ce process va stocker les walls d'utilisateurs.
 * Pour l'instant, ne verifie pas que les utilisateurs sont amis avant de repondre.
 * 
 * @author otmann
 *
 */

public class GestionMur extends Process{

	/**
	 * Dans la HashMap, les cle sont les noms des utilisateurs dont on stocke les murs.
	 * Un mur est une liste de doublet (hash de la publi, nom du superpeer_gestioncontenu qui la stock)
	 */
	HashMap<String, ArrayList<String[]>> donnees;

	/**
	 * La boite aux lettres sur laquelle le sp va écouter les requetes.
	 * 'nomHost' + '_GestionMur'
	 */
	private String mbox;
	/**
	 * c'est quoi au juste une boite au lettres, on en avait pas vraiment parlé. J'en 
	 * ai besoin moi aussi dans mes process ?
	 * @param host
	 * @param name
	 * @param args
	 */
	
	public GestionMur(Host host, String name, String[]args) {
		super(host,name,args);
		
		this.donnees = new HashMap<String, ArrayList<String[]>>();
		this.mbox = host.getName()+"_GestionMur";
	}

	public void main(String[] arg0) throws MsgException {
		while(true){
			
			if(Task.listen(this.mbox)){

				Message msg = (Message) Task.receive(this.mbox);

				switch(msg.getType()){
				case requete_mur:
					// il reste a verifier avec LiensAmis que les deux peers sont bien potes.
					Message<String> requete = msg;
					ArrayList<String[]> mur = reponseMur(requete.getPeerConcerne());
					Message<ArrayList<String[]>> reponse = new Message<ArrayList<String[]>>(mur, requete);
					//on renvoit donc les couples (hache, SPContenu).
					reponse.setType(typeMessage.reponseMur);
					
					reponse.isend(requete.getMboxReponse());
					break;
				case maj_mur:
					Message<String> maj = msg;
					ajoutPublicationDansMur(maj.getExpediteur(), maj.getMessage(), maj.getSuperPeerConcerne());
					Message<Boolean> confirmation = new Message<Boolean>(true, maj);
					confirmation.setType(typeMessage.confirmation);
					confirmation.isend(maj.getMboxReponse());

					break;
				}
			}
			Process.sleep(100);
		}
	}
	
	public void ajoutPublicationDansMur(String expediteur, String hash, String superpeer_contenu){
		
		//si on n'a pas encore de donnees sur le mur du gars, on ajoute une entree
		if(!donnees.containsKey(expediteur)){
			donnees.put(expediteur, new ArrayList<String[]>());
		}
		
		//on ajoute la donnee dans la liste de l'expediteur
		donnees.get(expediteur).add(new String[]{hash, superpeer_contenu});
	}
	
	public ArrayList<String[]> reponseMur(String utilisateur){
		
		ArrayList<String[]> reponse = null;
		
		if(donnees.containsKey(utilisateur)){
			reponse = donnees.get(utilisateur);
		}
		
		return reponse;
	}
}