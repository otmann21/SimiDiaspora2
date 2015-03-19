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

import java.lang.Math;
public class LiensAmis extends Process{
	
	/** Ce process gère les liens d'amitiés du réseau entier.
	 * C'est le seul process complètement centralisé.
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
	
	/**
	 * mbox est la boite aux lettres sur laquelle on peut joindre le Super Peer LiensAmis.
	 * Chaque peer a une boite aux lettres.
	 */
	private String mbox;
	
	/**
	 * Nombre de peer du reseau.
	 */
	
	int nbPeers ;
	/**
	 * Le constructeur prend d'abord en argument l'hote et le nom.
	 * Puis le tableau args, contenant : 
	 * args 0 : nombre de peer.
	 * args 1 : mode de remplissage.
	 * Si on choisi le mode de remplissage numéro 1, on précise la proba dans l'argument d'apres.
	 *
	 * Les deux modes de remplissage sont les suivants :
	 * 0 - Le pair 0 est ami avec tout le monde. On entre 0 pour cela.
	 * 1 - Tous les pairs ont une probabilite p d'etre amis avec un autre pair.
	 * Dans ce second mode, on passe la probabilite apres l'argument "1".
	 * 
	 * @param peer
	 * @return
	 */

	
	public LiensAmis(Host host, String name, String[]args){
		
		super(host, name, args);
		mbox = host.getName()+"_LiensAmis";
		//La ligne de gros CHEAT
		annuaire.put("peer0", "superpeer0");
		int n = Integer.parseInt(args[0]);
		//On doit donc donner en premier argument de args le nombre de pairs du réseau.

		this.nbPeers = n ;

		int modeRemplissage = Integer.parseInt(args[1]);
		//Cet entier, qui vaut 0 pour le remplissage 'facile', peut prendre d'autres valeurs.
		//Si l'on rentre 1, on obtient un mode de remplissage un peu plus aléatoire et crédible.

		this.topo = new boolean [n][n] ;

		//Pré-remplissage.
		
		for(int i=0 ; i<n;i++){ // on met tout le monde a false.
			for(int j=0 ; j<n;j++){
				topo[i][j]=false;
			}
		}
		
		for(int i=0 ; i<n;i++){
			topo[i][i] = true; //on est par defaut ami avec soi-meme.
		}

		if (modeRemplissage==0){ //Premier type de remplissage. Le peer0 est ami avec tout le monde.

			for(int i=0 ; i<n;i++){ //le peer0 est ami avec tout le monde.
				topo[0][i] = true;
				topo[i][0] = true;
			}
		}

		if(modeRemplissage==1){ 
			//chaque pair a une probabilité p d'être ami avec un autre pair.
			//p est passé en paramètres juste après le 1.
			
			double proba = new Double(args[2]);
			
			for(int i=0 ; i<n;i++){
				
				for(int j=0 ; j<n;j++){
					if (Math.random()<proba){
						topo[i][j] = true;
					}
				}
			}
		}


	}
	public String consulterAnnuaire(String peer){
		String spMur="";
		if(this.annuaire.containsKey(peer)) spMur=this.annuaire.get(peer);
		return spMur;
	}
	
	/**
	 * La methode liste amis retourne la liste des (amis, SPWall)
	 * du pair passe en argument. Elle retourne donc
	 * une liste de couple.
	 */
	
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
	 * La methode nbAmis renvoie le nombre d'amis du Peer passe en parametres.
	 * @param peer
	 * @return
	 */
	public int nbAmis(String peer){
		int nbAmis = 0;

		int pair = this.entierPeer(peer);
		//on parcours la pair-ieme ligne et on regarde ses amis.
		for (int j = 0 ; j < this.nbPeers ; j++){
			if (topo[pair][j]){
				nbAmis++;
			}
		}
		return nbAmis;
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
	public boolean sontAmis(String peer1, String peer2){
		return topo[this.entierPeer(peer1)][this.entierPeer(peer2)] ;
	}
	
	public int entierPeer(String peer){ //Convertit 
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
		
		System.out.println(1 / 5);
		
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
					String peer = requete2.getExpediteur();
					ArrayList<String[]> rep2 = listeAmis(peer);
					//création et envoi du message au SP.
					Message<ArrayList<String[]>> reponse2 = new Message<ArrayList<String[]>>(rep2, requete2);
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
			Process.sleep(1);
		}
	}
}
