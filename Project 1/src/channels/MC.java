package channels;

import messages.DecomposeHeader;
import messages.DecomposeMessage;
import messages.MessageType;

import java.net.InetAddress;

/**
 * Created by iamgroot on 25/03/17.
 */
public class MC extends Channel{

    public MC(int port_number, InetAddress address){
        super(port_number,address);
    }

}
