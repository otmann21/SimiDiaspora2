package processSuperPeer;

import java.util.ArrayList;
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
 
public class LiensAmis extends Process{

	/** Ce process gère les liens d'amitiés du réseau entier.
	 * C'est le seul process  complètement centralisé.
	 * 
	 * @author hugo
	 */

	boolean [][] topo;

	/**
	 * Cet attribut est une matrice symétrique de booleens.
	 * Si N est le nombre de peers,il possède 
	 * N lignes et N collones. topo[i][j] est vrai si i et j sont
	 * amis, faux sinon.
	 * 
	 */
	private String mbox;
	int nbPeers ; 

	public LiensAmis(Host host, String name, String[]args){
		super(host, name, args);
		
		int n = Integer.parseInt(args[0]);
		/**
		 * On doit donner en premier argument de args le nombre de pairs du réseau.
		 */
		this.nbPeers = n ;
		this.topo = new boolean [n][n] ;

		for(int i=0 ; i<n;i++){ // on met tout le monde à false.
			for(int j=0 ; j<n;j++){
				topo[i][j]=false;
			}
		}

		for(int i=0 ; i<n;i++){
			topo[i][i] = true; //on est par defaut ami avec soi-même.
		}

		for(int i=0 ; i<n;i++){ //le peer0 est ami avec tt le monde.
			topo[0][i] = true;
			topo[i][0] = true;
		}
	}

	/**
	 * Le constructeur prend seulement en argument le nombre de 
	 * peers du réseau.
	 * 
	 * On rempli ensuite le tableau topo n * n de façon raisonnable,
	 * plausible et un peu aléatoire. Par exemple en générant un nombre
	 * d'amis aléatoire.
	 * @param peer
	 * @return
	 */
	
	public ArrayList<String[]> listeAmis(String peer){
		ArrayList<String[]> liste = new ArrayList();
		
		int nbAmis = 0;
		int pair = this.entierPeer(peer);
			//on parcours la ième ligne et on regarde les amis de peeri.
			for (int j = 0 ; j < this.nbPeers ; j++){
				if (topo[pair][j]){
					String[] ami = {"peer" + j, "SPWall"};
					liste.add(nbAmis, ami);
					nbAmis++;
				}
		}
		return liste;
	}

	/**
	 * La méthode liste amis retourne la liste des (amis, SPWall)
	 *  du pair passé en argument. Elle retourne donc 
	 *  une liste de couple.
	 */

	public boolean sontAmis(String peer1, String peer2){
		return topo[this.entierPeer(peer1)][this.entierPeer(peer2)] ;
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
		String str = peer.substring(4);
		int entier = Integer.parseInt(str);		
		return entier;
	}

	/**
	 * Comme expliqué, cette méthode renvoie l'entier correspondant
	 * à un peer donné.
	 * @param args
	 */

	public void main(String[] args) throws MsgException{
		// TODO Auto-generated method stub
		while(true){
			if (Task.listen(this.mbox)){
				Message msg = (Message) Task.receive(this.mbox);
				switch(msg.getType()){
				case verif_ami:
					break;
				case liste_ami:
					break;
				}
			}
		}
	}
}
