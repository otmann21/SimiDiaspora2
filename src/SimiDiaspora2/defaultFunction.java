/*
 * Generated function Class.
 */
package SimiDiaspora2;
import org.simgrid.msg.Host;
import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Task;
import org.simgrid.msg.Process;

public class defaultFunction extends Process {
   public defaultFunction(Host host, String name, String[]args) {
		super(host,name,args);
   } 
    
   public void main(String[] args) throws MsgException {
        Msg.info("defaultFunction started");
        //TODO INSERT YOUR FUNCTION CODE HERE            
        Msg.info("defaultFunction exit");
    }
}
