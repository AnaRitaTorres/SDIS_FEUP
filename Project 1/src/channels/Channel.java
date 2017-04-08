package channels;

import messages.DecomposeMessage;
import requests.Handler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by iamgroot on 27/03/17.
 */
public class Channel implements Runnable{

    protected static final int PACKET_MAX_SIZE = 65000;

    private MulticastSocket socket;
    protected DatagramPacket packet;

    private InetAddress address;
    private int port_number;

    private Handler handler;

    public Channel(int port_number, InetAddress address){
        this.port_number = port_number;
        this.address = address;
        handler = new Handler();
    }

    public void run(){

        //opens socket
        try {
            socket = new MulticastSocket(port_number);
            socket.setTimeToLive(1);
            socket.joinGroup(address);
        }
        catch(IOException e){
           e.printStackTrace();
        }

        //buffer for packet
        byte[] buf = new byte[PACKET_MAX_SIZE];

        while(true){

            try {
                handler.handleRequests();
                
                //creates packet for reception
                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                DecomposeMessage message = new DecomposeMessage(packet);

                handler.addRequest(message);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        //TODO:close socket(se der) - para diminuir gastos de mem
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort_number() {
        return port_number;
    }

    public MulticastSocket getSocket() {
        return socket;
    }

}
