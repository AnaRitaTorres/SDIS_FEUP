package channels;

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
    //TODO:start thread - deve ser algo deste género, basicamente a thread tem de chamar um método runnable
    public void MDBstart(){

        Thread t = new Thread();
        t.start();
    }

    //TODO:save putchunks



}
