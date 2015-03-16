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
	
	/**
	 * C'est un annuaire qui contient la liste des couples (peers,SPWall) de tout le reseau.
	 * Le peer_i est en position i dans la liste.
	 */
	ArrayList<String[]> annuaire ;

	String peer; //c'est de ce pair qu'on va consulter le mur.
	String spwall; //spwall du peer en cours.
	String mbox; //baL.

	

	

	public Consulter(Host host, String name, String[]args){
		super(host,name,args);
		offset=Integer.parseInt(args[0]);
		peer = args[1];
	}
	
	/**recupere l'arraylist contenant le hache et le SPContenu de chaque publication.
	 * Quand un peer demande à voir le mur de peer0 à GestionMur, 
	 * celui-ci appelle la méthode recupere mur.
	 */
	public ArrayList <String[]> recupereMur(String peer, String spWall){
		// quand on recupere le mur, on obtient un ensemble de couple (hache, contenu)
// ON commence par demander à SP LiensAmis la liste de ses amis. puis il demande a consulter le mur 
		//du premier de ses amis.
		
	///////
		//// distribuer le bon code dans les méthodes. ICi on a juste a renvoyer listePubli.
		//////
		Message<String> reqWall = new Message();	//creation du message de requete au SPWall.
		reqWall.setType(typeMessage.requete_mur);
		reqWall.setPeerConcerne(peer);
		reqWall.isend(spWall); //envoi du message au spWall, puis attente de sa réponse.
		
		try {
			Process.sleep(100); //si ce temps n'est pas suffisant, on ajoutera du temps.
		} catch (HostFailureException e) {
			e.printStackTrace();
		}
		
		if(Task.listen(mbox)){
			try {
				Message msg= (Message) Task.receive(mbox);
				boolean resultat = (msg.getType()==typeMessage.reponseMur) && (msg.getHashCodeMessagePrecedent()==reqWall.hashCode());
				if (resultat){
					ArrayList<String[]> listePubli = new ArrayList();
					listePubli = (ArrayList<String[]>) msg.getMessage();
					
					//on prend ensuite tous les 2emes elements de la liste, qui represente la liste de SPContenus.
					Iterator ind = listePubli.iterator();
					ArrayList<String> listeSpContenu = new ArrayList();

					while (ind.hasNext()){
						String[] el = (String[]) ind.next();
						String spContenu = el[1];
						listeSpContenu.add(spContenu);
					}
					
					//on contact tous les spContenu de la liste et on demande les publications.
					Iterator indSp = l2.iterator();
					ArrayList<String> listeContenu = new ArrayList();

					while (indSp.hasNext()){
						
						Message<String> demandePubli = new Message<String>();
						demandePubli.setMessage("");
						demandePubli.setPeerConcerne("");
						demandePubli.setMboxReponse("");
						// ces attributs sont a renseigner.
						
						demandePubli.setType(typeMessage.requete_publication);
						demandePubli.isend((String) indSp.next());
						
						try {
							Message<String> publication= (Message) Task.receive(mbox);
							boolean res = (publication.getType()==typeMessage.reponse_publication) && (publication.getHashCodeMessagePrecedent()==demandePubli.hashCode());
							if (res){
								ArrayList<String> listePubli = new ArrayList();
								l2 = (ArrayList<String[]>) msg.getMessage();
							}
						}
						
						String[] el = (String[]) indSp.next();
						String spContenu = el[1];
						listeContenu.add(spContenu);
					}
				}
				
			} catch (TransferFailureException | HostFailureException
					| TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ArrayList <String[]> liste = new ArrayList();
		// on demande ensuite la liste au process gestion mur.

		// cette methode envoie un message au process gestion mur. NON.

		return listePubli ;
	}

	public String recuperePubli(String hache, String SPContenu){
		
		ArrayList mur2 = this.recupereMur(peer, this.spwall);
		
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

		ArrayList mur = this.recupereMur(peer, this.spwall);
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
