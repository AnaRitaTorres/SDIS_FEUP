package channels;

import messages.DecomposeMessage;

import java.net.InetAddress;

public class MDR extends Channel {

    public MDR(int port_number, InetAddress address){
        super(port_number,address);
    }
}
