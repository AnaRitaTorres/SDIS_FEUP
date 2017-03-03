import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Vector;

class client{

     public static void main(String[] args) throws UnknownHostException, IOException{


         //check num of args
         if(args.length < 3){
            System.out.println("Wrong Number of Arguments");
            System.out.println("USAGE: java client <mcast_addr> <mcast_port> <oper> <opnd> * ");
            return;
         }
         
        String mcast_addr = args[0];
        Integer mcast_port = Integer.valueOf(args[1]);
        String oper = args[2];
        Vector<String> opnd = new Vector<String>();

        //used to store the info sent by the server
        byte[] buffer = new byte[256];
        
        //save info given in the console
        for(int i=3; i < args.length; i++){
            opnd.addElement(args[i]);
        }

        InetAddress group = InetAddress.getByName(mcast_addr);
        MulticastSocket mSocketReceived = new MulticastSocket(mcast_port);

        //join the multicast group to learn the address of the socket
        mSocketReceived.joinGroup(group);
       
         //create datagram for the info sent by the server
         DatagramPacket dPacketReceived = new DatagramPacket(buffer,buffer.length);
         //receive the info
        mSocketReceived.receive(dPacketReceived);

        String receivedInfo = new String(buffer, 0, buffer.length).trim();		
		
        //service port 
		int srvc_port = Integer.parseInt(receivedInfo);

        buffer = new byte[256];
        //service address
        InetAddress srvc_addr = dPacketReceived.getAddress();

        //request
        DatagramSocket dSocketRequest = new DatagramSocket();
        DatagramPacket dPacketRequest = new DatagramPacket(buffer,buffer.length,srvc_addr,srvc_port);

        //send request
        dSocketRequest.send(dPacketRequest);

        buffer = new byte[256];
		dPacketRequest = new DatagramPacket(buffer, buffer.length);
		dSocketRequest.receive(dPacketRequest);
		String answer = new String(dPacketRequest.getData(), 0, dPacketRequest.getLength());
    
       //close sockets
       dSocketRequest.close();
       mSocketReceived.leaveGroup(group);
       mSocketReceived.close();
         
     }

}









//print this -> multicast: <mcast_addr> <mcast_port>: <srvc_addr> <srvc_port> 