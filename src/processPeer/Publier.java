package processPeer;

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

/**
 * Le peer envoie une publication a son spContenu, et si spContenu lui repond ok, il notifie son spMur.
 * PB : Les commandes sleep ne finissent pas.
 * Puis il exit.
 * 
 * @author otmann
 *
 */
public class Publier extends Process {

	private int offset;
	private String publication;
	private String spContenu;
	private String spMur;
	private String mbox;

	public Publier(Host host, String name, String[]args){
		super(host,name,args);

		//Les attributs sont initialisées a partir d'argument passés dans le deployment.xml		
		offset=Integer.parseInt(args[0]);
		publication = args[1];
		spContenu = args[2];
		spMur = args[3];
		mbox = host.getName()+"_Publier";
	}

	public void main(String[] arg0) throws MsgException {
		Process.sleep(offset);
		if(envoiPublication()) actualiseMur();
		//		Task t = new Task();
		//		t.send(spContenu+"_GestionContenu");
	}

	public boolean envoiPublication() throws TransferFailureException, HostFailureException, TimeoutException{

		boolean resultat=false;
		Message<String> requetePublication = new Message<String>(publication);
		requetePublication.setType(typeMessage.ajout_publication);
		requetePublication.setMboxReponse(this.mbox);
		requetePublication.send(spContenu+"_GestionContenu");

		try {
			Message msg= (Message) Task.receive(mbox);
			resultat = (msg.getType()==typeMessage.confirmation) && (msg.getHashCodeMessagePrecedent()==requetePublication.hashCode()) && ((Boolean) msg.getMessage());

		} catch (TransferFailureException | HostFailureException
				| TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultat;

	}

	public boolean actualiseMur() throws TransferFailureException, HostFailureException, TimeoutException{

		boolean resultat=false;

		MessageDigest md;
		String hash = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
			md.reset();
			md.update(publication.getBytes());
			try {
				hash = new String(md.digest(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}

		Message<String> majMur = new Message<String>(hash);
		majMur.setType(typeMessage.maj_mur);
		majMur.setMboxReponse(mbox);
		majMur.setSuperPeerConcerne(spContenu);
		majMur.send(spMur+"_GestionMur");


		try {
			Message msg= (Message) Task.receive(mbox);
			resultat = (msg.getType()==typeMessage.confirmation) && (msg.getHashCodeMessagePrecedent()==majMur.hashCode()) && ((Boolean) msg.getMessage());
		} catch (TransferFailureException | HostFailureException
				| TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(resultat) Msg.info(this.host.getName()+ " a publié '"+ this.publication+"'.");
		return resultat;
	}

}
