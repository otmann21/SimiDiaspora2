package processPeer;

import java.util.ArrayList;
import java.util.Iterator;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.simgrid.msg.Host;
import org.simgrid.msg.HostFailureException;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Process;
import org.simgrid.msg.Task;
import org.simgrid.msg.TimeoutException;
import org.simgrid.msg.TransferFailureException;

import taches.Message;
import taches.typeMessage;

public class Consulter extends Process{

	int offset ; //decalage permettant la bonne synchronisation.

	/**on a dit que le process liensAmis tournait sur un SP "central". Il faut
	son nom (i e son adresse), pour aller le trouver et faire les verif.*/

	String SPAmi ; 

	String peer; //c'est de ce pair de qui on va consulter le mur.
	String spwall; //spwall du peer en cours.
	String mbox; //baL.

	/**
	 * On passe donc en argument, après l'host et le nom, l'offset, puis le peer qui veut consulter un mur.
	 * @param host
	 * @param name
	 * @param args
	 */

	public Consulter(Host host, String name, String[]args){
		super(host,name,args);
		offset=Integer.parseInt(args[0]);
		peer = args[1];
	}

	/**recupere l'arraylist contenant le hache et le SPContenu de chaque publication.
	 * Quand un peer demande à voir du contenu, on lui donne le contenu de son premier peer ami. 
	 * celui-ci appelle la méthode recupere mur.
	 */
	public ArrayList <String[]> recupereMur(String spWall){		
		
		// Quand on recupere le mur, on obtient un ensemble de couple (hache, contenu)
		// On commence par demander à SP LiensAmis la liste de ses amis.

		Message<String> recupListeAmis;
		recupListeAmis.setType(typeMessage.liste_ami);
		//recupListeAmis.setPeerConcerne(peer);
		recupListeAmis.isend(this.SPAmi); //envoi du message au spWall, puis attente de sa réponse.

		try {
			Process.sleep(100); //si ce temps n'est pas suffisant, on ajoutera du temps.
		} catch (HostFailureException e) {
			e.printStackTrace();
		}

		// Puis il recupere la reponse sous forme de liste, et choisi le premier pair ami.
		if (Task.listen(mbox)){
			Message lAmis= (Message) Task.receive(mbox);
			boolean resultat = (lAmis.getType()==typeMessage.reponse_listeAmis) && (lAmis.getHashCodeMessagePrecedent()==recupListeAmis.hashCode());
			if (resultat){
				ArrayList<String> lAmis2 = new ArrayList<String>();
				lAmis2 = (ArrayList<String>) lAmis.getMessage(); 
				this.peer = lAmis2.get(0); 
				//on a choisi le peer a consulter, le peer 0, qui est ami avec tout le monde.
			}	
		}		
		
		//recherche du SPWall qui lui correspond.
		Message<String> demande;
		demande.setType(typeMessage.demandeSPWall);
		demande.setExpediteur(this.mbox);
		demande.isend(this.SPAmi);
		
		//reception du SPWall
		
		//affectation du SPWall
		this.spwall = spWall;
		
		
		// et demande a consulter le mur du premier de ses amis.
		ArrayList<String[]> listePubli = new ArrayList();

		Message<String> reqWall = new Message();	//creation du message de requete au SPWall.
		reqWall.setType(typeMessage.requete_mur);
		reqWall.setPeerConcerne(this.peer);
		reqWall.isend(spWall); //envoi du message au spWall, puis attente de sa réponse.

		try {
			Process.sleep(100); //si ce temps n'est pas suffisant, on ajoutera du temps.
		} catch (HostFailureException e) {
			e.printStackTrace();
		}

		if(Task.listen(mbox)){
			Message msg= (Message) Task.receive(mbox);
			boolean resultat = (msg.getType()==typeMessage.reponseMur) && (msg.getHashCodeMessagePrecedent()==reqWall.hashCode());
			if (resultat){
				listePubli = (ArrayList<String[]>) msg.getMessage();
			}

			return listePubli ;
		}

		public String recuperePubli(String hache){ //sp contenu attribut inutile

			String SPContenu;
			//On recupere le mur grace a la fonction precedente.
			//liste (hache, SPContenu).
			
			ArrayList mur2 = this.recupereMur(this.spwall);
			String[] couple = recupereMur(this.spwall).get(mur2.indexOf(hache));
			//on prend l'element de mur2 associé au message 'hache'

			Message<String> demandePubli = new Message<String>();
			demandePubli.setMessage("");
			demandePubli.setPeerConcerne("");
			demandePubli.setMboxReponse("");
			// ces attributs sont a renseigner.

			// on demande ensuite la liste au process gestion mur.
			demandePubli.setType(typeMessage.requete_publication);
			demandePubli.isend((String) indSp.next());

			try {
				Message<String> publication= (Message) Task.receive(mbox);
				boolean res = (publication.getType()==typeMessage.reponse_publication) && (publication.getHashCodeMessagePrecedent()==demandePubli.hashCode());
				if (res){
					listePubli = (ArrayList<String[]>) msg.getMessage();
				}


				String[] el = (String[]) indSp.next();
				String spContenu = el[1];
				listeContenu.add(spContenu);
			}

		} catch (TransferFailureException | HostFailureException
				| TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//recherche du hache de l'élement dans le mur, on cherche son indice.
		Iterator i = mur2.iterator();
		ArrayList listeHache = new ArrayList();

		while (i.hasNext()){
			String[] el = (String[]) i.next();
			String elHach = el[0];
			listeHache.add(elHach);
		}

		int indice = listeHache.indexOf(hache); //calcul de l'indice, en supposant qu'il est unique.
		String[] couple = (String[]) listeHache.get(indice);
		String publi = couple[1];
		return publi ;
	}

	/**
	 * consulterMur retourne toutes les publications du mur.
	 * On appelle x fois la fonction recupere publi.
	 * On n'envoie pas le resultat avec un message.
	 * @param peer : le pair dont on consulte le mur.
	 * @return la liste de toutes les publications du mur.
	 */
	public ArrayList <String> consulterMur(String peer){
		ArrayList<String> liste = new ArrayList() ;

		ArrayList mur = this.recupereMur(this.spwall);
		Iterator it = mur.iterator();

		while (it.hasNext()){
			String[] el = (String[]) it.next();
			String contenu = el[1];
			liste.add(contenu);
		}
		return liste ;
	}

	public void main(String[] args) throws MsgException{
		// TODO Auto-generated method stub

	}

}
