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

public class LiensAmis {

	/** Ce process gère les liens d'amitiés du réseau entier.
	 * C'est le seul process  complètement centralisé.
	 * 
	 * @author hugo
	 */
	
	boolean [][] topo;
	
	/**
	 * Cet attribut est un tableau de booleens.
	 * Si N est le nombre de peers,il possède 
	 * N lignes et N collones. topo[i][j] est vrai si i et j sont
	 * amis, faux sinon.
	 * 
	 */
	
	
	public ArrayList<String[]> listeAmis(String peer){
		ArrayList<String[]> liste = new ArrayList();
		return liste;
	}
	
	/**
	 * La méthode liste amis retourne la liste des (amis, SPWall)
	 *  du pair passé en argument. Elle retourne donc 
	 *  une liste de couple.
	 */
	
	public boolean sontAmis(String peer1, String peer2){
		return topo[0][0] ;
	}
	
	/**
	 * la méthode sontAmis(peer1, peer2), très explicitement, 
	 * renvoie le booléen indiquant si oui ou non ils sont amis.
	 * 
	 * Pour pouvoir accéder à ce booleen avec des entiers,
	 * on cree une methode entierPeer, renvoyant l'entier du pair.
	 * Tous les pairs ont pour nom "peer" + entierPeer.
	 * @param args
	 */
	
	public int entierPeer(String peer){
		return 2;
	}
	
	/**
	 * Comme expliqué, cette méthode renvoie l'entier correspondant
	 * à un peer donné.
	 * @param args
	 */
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
