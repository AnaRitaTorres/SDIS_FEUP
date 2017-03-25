package channels;

import protocols.BackupProtocol;

import java.io.File;
import java.net.DatagramPacket;
import java.net.InetAddress;

/**
 * Created by iamgroot on 25/03/17.
 */
public class MDB extends BackupProtocol{


    //TODO:start thread - deve ser algo deste género, basicamente a thread tem de chamar um método runnable
    public void MDBstart(){

        Thread t = new Thread(new BackupProtocol());
        t.start();
    }


    //TODO:save putchunks



}
