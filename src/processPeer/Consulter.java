package processPeer;

import java.util.ArrayList;
import java.util.Iterator;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.simgrid.msg.Host;
import org.simgrid.msg.HostFailureException;
import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Process;
import org.simgrid.msg.Task;
import org.simgrid.msg.TimeoutException;
import org.simgrid.msg.TransferFailureException;

import taches.Message;
import taches.typeMessage;

public class Consulter extends Process{

	int offset ; //decalage permettant la bonne synchronisation.

	//On a dit que le process liensAmis tournait sur un SP "central". Il faut
	//son nom (i e son adresse), pour aller le trouver et faire les verif.

	String SPAmi ; 

	String peer; //c'est de ce pair de qui on va consulter le mur.
	String spWall; //spwall du peer en cours.
	String mbox; //baL.

	/**
	 * On passe donc en argument, apres l'host et le nom, l'offset, puis le peer qui veut consulter un mur.
	 * @param host
	 * @param name
	 * @param args
	 */

	public Consulter(Host host, String name, String[]args){
		super(host,name,args);
		offset=Integer.parseInt(args[0]);
		SPAmi = args[1];
		mbox = host.getName()+"_Consulter";
	}

	/**recupere l'arraylist contenant le hache et le SPContenu de chaque publication.
	 * Quand un peer demande à voir du contenu, on lui donne le contenu de son premier peer ami. 
	 * celui-ci appelle la méthode recupere mur.
	 * @throws TimeoutException 
	 * @throws HostFailureException 
	 * @throws TransferFailureException 
	 */
	public ArrayList <String[]> recupereMur() throws TransferFailureException, HostFailureException, TimeoutException{		

		// Quand on recupere le mur, on obtient un ensemble de couple (hache, contenu)
		// On commence par demander à SP LiensAmis la liste de ses amis.

		ArrayList<String[]> listePubli = new ArrayList<String[]>();

		Message<String> recupListeAmis = new Message<String>();
		recupListeAmis.setType(typeMessage.liste_ami);
		recupListeAmis.setMboxReponse(this.mbox);
		recupListeAmis.send(this.SPAmi+"_LiensAmis"); //envoi du message au spWall, puis attente de sa réponse.

		// Puis il recupere la reponse sous forme de liste, et choisi le premier pair ami.
		//		while(!Task.listen(mbox)){
		Message lAmis= (Message) Task.receive(mbox);
		boolean resultat = (lAmis.getType()==typeMessage.reponse_listeAmis) && (lAmis.getHashCodeMessagePrecedent()==recupListeAmis.hashCode());
		if (resultat){
			ArrayList<String[]> lAmis2 = new ArrayList<String[]>();
			lAmis2 = (ArrayList<String[]>) lAmis.getMessage();
			int n = lAmis2.size();
			int indice = (int) Math.round(n * Math.random());
			this.peer = lAmis2.get(indice)[0];
			this.spWall = lAmis2.get(indice)[1];
			//on choisi le premier peer ami a consulter.
		}

		//		}


		// Demande de consultation du mur du premier de ses amis.

		Message<String> reqWall = new Message<String>();	//creation du message de requete au SPWall.
		reqWall.setType(typeMessage.requete_mur);
		reqWall.setPeerConcerne(this.peer);
		reqWall.setMboxReponse(this.mbox);
		reqWall.send(spWall+"_GestionMur"); //envoi du message au spWall, puis attente de sa réponse.



		Message msg= (Message) Task.receive(mbox);

		boolean resultat1 = (msg.getType()==typeMessage.reponseMur) && (msg.getHashCodeMessagePrecedent()==reqWall.hashCode());
		if (resultat1){
			listePubli = (ArrayList<String[]>) msg.getMessage();

		}

		return listePubli;
	}

	public String recuperePubli(String hache, String SPContenu) throws TransferFailureException, HostFailureException, TimeoutException{ //sp contenu attribut inutile

		String publi = ""; //la publi que l'on va renvoyer.


		//creation et envoi du message à SPContenu
		Message<String> demandePubli = new Message<String>();
		demandePubli.setMessage(hache);
		demandePubli.setPeerConcerne(this.peer);
		demandePubli.setMboxReponse(this.mbox);
		demandePubli.setType(typeMessage.requete_publication);
		demandePubli.send(SPContenu+"_GestionContenu");


		Message<String> publication= (Message) Task.receive(mbox);
		boolean res = (publication.getType()==typeMessage.reponse_publication) && (publication.getHashCodeMessagePrecedent()==demandePubli.hashCode());
		if (res){
			publi = (String) publication.getMessage();
		}

		return publi ;
	}
	/**
	 * consulterMur retourne toutes les publications du mur.
	 * On appelle x fois la fonction recupere publi.
	 * On n'envoie pas le resultat avec un message.
	 * @param peer : le pair dont on consulte le mur.
	 * @return la liste de toutes les publications du mur.
	 * @throws TimeoutException 
	 * @throws HostFailureException 
	 * @throws TransferFailureException 
	 */
	public ArrayList <String> consulterMur(String peer) throws TransferFailureException, HostFailureException, TimeoutException{
		ArrayList<String> liste = new ArrayList<String>() ;

		ArrayList<String[]> mur = this.recupereMur();

		if(mur!=null&&!mur.isEmpty()){
			ArrayList<String[]> mur2 = (ArrayList<String[]>) mur.clone();

			for(String[] el: mur2){
				String contenu = recuperePubli(el[0],el[1]);

				liste.add(contenu);
			}
		}
		return liste ;
	}

	public void main(String[] args) throws MsgException{
		Process.sleep(offset);
		ArrayList<String> liste = this.consulterMur(this.peer);
		String out = host.getName()+" consulte le mur de " +peer+".";
		if(liste.isEmpty()){out+=" Il est vide.";}
		else{
			out+=" Il y a :";
			for(String s : liste){
				out+="\n'"+s+"'";
			}
		}
		Msg.info(out);
	}

}
