package channels;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by iamgroot on 27/03/17.
 */
public class Channel implements Runnable{

    public static final int PACKET_MAX_SIZE = 65000;

    public MulticastSocket socket;
    public DatagramPacket packet;
    public InetAddress address;
    public int port_number;


    public Channel(int port_number, InetAddress address){
        this.port_number = port_number;
        this.address = address;
    }

    public void run(){

        //opens socket
        try {
            socket = new MulticastSocket(port_number);
            //TODO: verificar se vale a pena usar socket.setTimeToLive(1);
            socket.joinGroup(address);
        }
        catch(IOException e){
           e.printStackTrace();
        }

        //buffer for packet
        byte[] buffer = new byte[PACKET_MAX_SIZE];

        while(true){

            try {
                //creates packet for reception
                packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                //responds
                InetAddress address = packet.getAddress();
                packet = new DatagramPacket(buffer, buffer.length, address, port_number);
                socket.send(packet);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        //TODO:close socket(se der) - para diminuir gastos de mem


    }
}
