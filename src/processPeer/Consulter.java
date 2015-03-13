package processPeer;

import java.util.ArrayList;

import org.simgrid.msg.Host;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Process;

public class Consulter {

	int offset ; //decalage permettant la bonne synchronisation.
	
	String SPAmi ; 
	// on a dit que le process liensAmis tournait sur un SP "central". Il faut
	// son nom (i e son adresse), pour aller le trouver et faire les vérif.
	
	ArrayList<String[]> annuaire ;
	
	/**
	 * C'est un annuaire. 
	 * attribut contenant la liste des couples (amis,SPWall)
	 * Comment l'initialiser ? Il faut utiliser les fichiers xml de simgrid.
	 */

	public ArrayList <String[]> recupereMur(String peer, String SPWall){
		ArrayList <String[]> liste = new ArrayList();
		return liste ;
	}
	
	public String recuperePubli(String hache, String SPContenu){
		String publi = "publication par défaut";
		return publi ;
	}
	
	public ArrayList <String> consulterMur(String peer){
		ArrayList<String> liste = new ArrayList() ;
		return liste ;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
