package channels;

import messages.DecomposeHeader;
import messages.DecomposeMessage;
import protocols.BackupProtocol;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by iamgroot on 25/03/17.
 */
public class MDB extends Channel {

    public MDB(int port_number, InetAddress address){

        super(port_number,address);
    }

}
