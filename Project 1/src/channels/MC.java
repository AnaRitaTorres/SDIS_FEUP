package channels;

import messages.DecomposeHeader;
import messages.DecomposeMessage;
import messages.MessageType;

import java.net.InetAddress;

public class MC extends Channel{

    public MC(int port_number, InetAddress address){
        super(port_number,address);
    }

}
