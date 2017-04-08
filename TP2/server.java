import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.io.IOException;

class server{

     public static void main(String[] args) throws UnknownHostException,IOException{

         //check num args
         if(args.length != 3){
            System.out.println("USAGE: java server <srvc_port> <mcast_addr> <mcast_port> ");
            return;
         }

       
        byte[] buffer = new byte[256];
        Integer srvc_port = Integer.valueOf(args[0]);
        String mcast_addr = args[1];
        Integer mcast_port = Integer.valueOf(args[2]);

        //create datagram socket
        DatagramSocket dSocket = new DatagramSocket(srvc_port);
        InetAddress group = InetAddress.getByName(mcast_addr);

        //create multicast socket
        MulticastSocket mSocket = new MulticastSocket(mcast_port);

        //Set the default time-to-live for multicast packets sent out on this 
        //MulticastSocket in order to control the scope of the multicasts.)
        mSocket.setTimeToLive(1);

        //create multicast datagram packet
        DatagramPacket dPacket = new DatagramPacket(buffer, buffer.length, group, mcast_port);

        while(true){
            mSocket.send(dPacket);
            System.out.println("multicast:<" + mcast_addr + "><" + mcast_port + ">:<srvc_addr><" + srvc_port + ">");

            //a call to receive() for this DatagramSocket will block for only this amount of time.
            dSocket.setSoTimeout(1000);

            //request
            //falta fazer isto!!!
        }

        mSocket.close();
        dSocket.close();
       
       
     }
}