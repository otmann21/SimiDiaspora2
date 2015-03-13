package processPeer;

import org.simgrid.msg.Host;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Process;

/**
 * Le peer envoie une publication a son spContenu, et notifie son spMur.
 * Puis il exit.
 * 
 * @author otmann
 *
 */
public class Publier extends Process {

	private int offest;
	private String publication;
	private String spContenu;
	private String spMur;

	public Publier(Host host, String name, String[]args){
		super(host,name,args);
		
		//Les attributs devront être initialisées a partir 
		//d'argument passés dans le deployment.xml.

	}
	public void main(String[] arg0) throws MsgException {
		// TODO Auto-generated method stub

	}

	public void envoiPublication(){

	}

	public void actualiseMur(){

	}

}
