package processSuperPeer;

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
 * Ce process va stocker les walls d'utilisateurs.
 * Chaque peer a un Super Peer associe qui gere son mur.
 * 
 * @author otmann
 *
 */

public class GestionMur extends Process{

	/**
	 * Dans la HashMap, les cle sont les noms des utilisateurs dont on stocke les murs.
	 * Un mur est une liste de doublet (hash de la publi, nom du superpeer_gestioncontenu qui la stocke)
	 */
	HashMap<String, ArrayList<String[]>> donnees;

	/**
	 * La boite aux lettres sur laquelle le sp va ecouter les requetes.
	 * 'nomHost' + '_GestionMur'
	 */
	private String mbox;
	
	/**
	 * Le nomdu spLiensAmis a qui le spMur dira de quels peers il s'occupe.
	 */
	private String spLiensAmis;
	
	/**
	 * Constructeur de la classe. 
	 * @param host
	 * @param name
	 * @param args
	 */
	public GestionMur(Host host, String name, String[]args) {
		super(host,name,args);
		
		this.donnees = new HashMap<String, ArrayList<String[]>>();
		this.mbox = host.getName()+"_GestionMur";
		this.spLiensAmis = args[0];
	}
	
/**
 * Methode main de la classe.
 */
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
			Process.sleep(1);
		}
	}
	
	/**
	 * Cette methode ajoute une publication au Super Peer contenu qui l'a a sa charge.
	 * @param expediteur
	 * @param hash
	 * @param superpeer_contenu
	 * @throws TransferFailureException
	 * @throws HostFailureException
	 * @throws TimeoutException
	 */
	public void ajoutPublicationDansMur(String expediteur, String hash, String superpeer_contenu) throws TransferFailureException, HostFailureException, TimeoutException{
		
		//si on n'a pas encore de donnees sur le mur du gars, on ajoute une entree
		if(!donnees.containsKey(expediteur)){
			donnees.put(expediteur, new ArrayList<String[]>());
			Message<String> notif= new Message<String>();
			notif.setType(typeMessage.notif_spMur);
			notif.setPeerConcerne(expediteur);
			notif.isend(spLiensAmis+"_LiensAmis");
		}
		
		//on ajoute la donnee dans la liste de l'expediteur
		donnees.get(expediteur).add(new String[]{hash, superpeer_contenu});
	}
	
	/**
	 * La methode reponseMur renvoie le mur chiffre du peer dont on veut consulter le mur.
	 * @param utilisateur
	 * @return
	 */
	public ArrayList<String[]> reponseMur(String utilisateur){
		
		ArrayList<String[]> reponse = null;
		
		if(donnees.containsKey(utilisateur)){
			reponse = donnees.get(utilisateur);
		}
		
		return reponse;
	}
}