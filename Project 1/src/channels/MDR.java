package channels;

import messages.DecomposeMessage;

import java.net.InetAddress;

/**
 * Created by iamgroot on 25/03/17.
 */
public class MDR extends Channel {

    public MDR(int port_number, InetAddress address){
        super(port_number,address);
    }
}
