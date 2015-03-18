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

	/**
	 * Cet attribut est une matrice symetrique de booleens.
	 * Si N est le nombre de peers,il possede N lignes et N collones. 
	 * topo[i][j] est vrai si i et j sont amis, faux sinon.
	 * 
	 */
	boolean [][] topo;

	/**
	 * C'est un annuaire qui contient la liste des couples (peers,SPWall) de tout le reseau.
	 * Le peer_i est en position i dans la liste.
	 */
	HashMap<String, String> annuaire=new HashMap<String, String>();
	
	
	private String mbox;
	int nbPeers ;

	/**
	 * Le constructeur prend en argument l'hote, le nom et le nombre de peers du reseau.
	 * 
	 * On rempli ensuite le tableau topo n * n de facon raisonnable,
	 * plausible et un peu aleatoire. Par exemple en generant un nombre
	 * d'amis aleatoire.On a choisi en premier lieu de mettre tout le monde ami avec le peer0.
	 * @param peer
	 * @return
	 */
	
	public LiensAmis(Host host, String name, String[]args){
		super(host, name, args);
		mbox = host.getName()+"_LiensAmis";

		int n = Integer.parseInt(args[0]);
		//On doit donc donner en premier argument de args le nombre de pairs du réseau.
		
		this.nbPeers = n ;
		
		//remplissage du tableau des liens d'amitie. Le peer0 est ami avec tout le monde.
		this.topo = new boolean [n][n] ;

		for(int i=0 ; i<n;i++){ // on met tout le monde a false.
			for(int j=0 ; j<n;j++){
				topo[i][j]=false;
			}
		}

		for(int i=0 ; i<n;i++){
			topo[i][i] = true; //on est par defaut ami avec soi-meme.
		}

		for(int i=0 ; i<n;i++){ //le peer0 est ami avec tout le monde.
			topo[0][i] = true;
			topo[i][0] = true;
		}
	}

	public String consulterAnnuaire(String peer){
		String spMur="";
		if(this.annuaire.containsKey(peer)) spMur=this.annuaire.get(peer);
		return spMur;
	}


	public ArrayList<String[]> listeAmis(String peer){
		ArrayList<String[]> liste = new ArrayList<String[]>();

		int nbAmis = 0;
		int pair = this.entierPeer(peer);
		//on parcours la ième ligne et on regarde les amis de peeri.
		for (int j = 0 ; j < this.nbPeers ; j++){
			if (topo[pair][j]){
				String[] ami = {"peer" + j, consulterAnnuaire("peer"+j)};
				liste.add(nbAmis, ami);
				nbAmis++;
			}
		}
		return liste;
	}

	/**
	 * La methode liste amis retourne la liste des (amis, SPWall)
	 *  du pair passe en argument. Elle retourne donc 
	 *  une liste de couple.
	 */

	public boolean sontAmis(String peer1, String peer2){
		return topo[this.entierPeer(peer1)][this.entierPeer(peer2)] ;
	}

	/**
	 * la methode sontAmis(peer1, peer2), tres explicitement, 
	 * renvoie le booleen indiquant si oui ou non ils sont amis.
	 * 
	 * Pour pouvoir acceder à ce booleen avec des entiers,
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
	 * Comme explique, cette methode renvoie l'entier correspondant
	 * a un peer donne.
	 * @param args
	 */

	public void main(String[] args) throws MsgException{
		// TODO Auto-generated method stub
		while(true){
			if (Task.listen(this.mbox)){
				Message msg = (Message) Task.receive(this.mbox);
				switch(msg.getType()){
				case verif_ami:
					//récupération des arguments et appel de la méthode sontAmis.
					Message<String> requete = msg;
					String peer1 = requete.getPeerConcerne();
					String peer2 = requete.getPeerConcerne2();
					boolean rep = sontAmis(peer1, peer2);

					//création et envoi du message au SP.
					Message reponse = new Message(rep);
					reponse.setType(typeMessage.reponse_sontAmis);
					reponse.isend(requete.getMboxReponse());					
					break;
				case liste_ami:
					//récupération des arguments et appel de la méthode sontAmis.
					Message<String> requete2 = msg;
					String peer = requete2.getPeerConcerne();
					ArrayList<String[]> rep2 = listeAmis(peer);

					//création et envoi du message au SP.
					Message<ArrayList<String[]>> reponse2 = new Message<ArrayList<String[]>>(rep2);
					reponse2.setType(typeMessage.reponse_listeAmis);
					reponse2.isend(requete2.getMboxReponse());	
					break;
				case demandeSPWall:
					//récupération des arguments et appel de la méthode consulterAnnuaire.
					String peerAChercher =(String) msg.getMessage();
					String SPWallduPeer = this.consulterAnnuaire(peerAChercher);

					//création et envoi du message au process consulter.
					Message<String> repSP = new Message<String>(SPWallduPeer);
					repSP.setType(typeMessage.reponseSPWall);
					repSP.isend(msg.getExpediteur());
					break;
				}
			}
		}
	}
}
