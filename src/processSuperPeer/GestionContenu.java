package processSuperPeer;

import java.util.HashMap;

import org.simgrid.msg.Msg;
import org.simgrid.msg.Process;
import org.simgrid.msg.Host;
import org.simgrid.msg.MsgException;

/**
 * Ce process va stocker les publications des utilisateurs.
 * 
 * @author otmann
 *
 */

public class GestionContenu extends Process{

	/**
	 * Dans la Hashmap de haut niveau, les clés sont les noms des utilisateurs.
	 * Dans les petites Hashmaps, les clés sont des hash de publications, et les valeurs sont les publications.
	 */
	HashMap<String, HashMap<String, String>> contenu;

	/**
	 * La boite aux lettres sur laquelle le sp va écouter les requetes.
	 * 'nomHost' + '_GestionContenu'
	 */
	private String mbox;

	public GestionContenu(Host host, String name, String[]args) {
		super(host,name,args);

		this.mbox = host.getName()+"_GestionContenu";


	}

	public void main(String[] args) throws MsgException {


	}

	public void ajoutContenu(String Contenu, String expediteur){

	}

	String reponseContenu(String posteur, String hash){

		return null;
	}

}