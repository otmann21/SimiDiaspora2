package processPeer;

import java.util.ArrayList;

import org.simgrid.msg.Host;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Process;

public class Consulter extends Process{

	int offset ; //decalage permettant la bonne synchronisation.
	
	String SPAmi ; 
/**on a dit que le process liensAmis tournait sur un SP "central". Il faut
son nom (i e son adresse), pour aller le trouver et faire les vérif.*/
	
	ArrayList<String[]> annuaire ;
	
	String peer; //c'est de ce pair qu'on va consulter le mur.
	
	/**
	 * C'est un annuaire. 
	 * attribut contenant la liste des couples (peers,SPWall) de tout le réseau.
	 * Comment l'initialiser ? Il faut utiliser les fichiers xml de simgrid.
	 * Le peer_i est en position i dans la liste.
	 */

	/**récupère l'arraylist contenant le haché et le SPWall host
	 * de chaque publication.
	 * Quand un peer demande à voir le mur de peer0 à GestionMur, 
	 * celui-ci appelle la méthode recupere mur.
	*/
	
	public Consulter(Host host, String name, String[]args){
		super(host,name,args);
		offset=Integer.parseInt(args[0]);
		peer = args[1];
		
	}
	public ArrayList <String[]> recupereMur(String peer, String SPWall){

		ArrayList <String[]> liste = new ArrayList();
		// on demande ensuite la liste au process gestion mur.
		
		// cette méthode envoie un message au process gestion mur.
		
		return liste ;
	}
	
	public String recuperePubli(String hache, String SPContenu){
		String publi = "publication par défaut";
		return publi ;
	}
	
	public ArrayList <String> consulterMur(String peer){
		ArrayList<String> liste = new ArrayList() ;
		
		// on parcourt toutes les publications et on remplit la liste, 
		// c'est à dire que l'on fait une boucle en lançant à chaque fois la méthode
		//récuperePubli.
		
		return liste ;
	}
	
	public void main(String[] args) throws MsgException{
		// TODO Auto-generated method stub

	}

}
