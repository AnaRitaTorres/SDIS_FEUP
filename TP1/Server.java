import java.lang.*;
import java.net.*;
import java.io.*;
import java.util.Vector;

class Server{

    public Vector<Vehicle> vehicles;

    //retorna o nome do owner associado à matrícula, caso exista, caso contrário, retorna "NOT_FOUND";

    public Integer searchPlateNumber(String plate_number){

        for (Integer i=0; i<vehicles.size(); i++){
            if (vehicles.get(i).getPlateNumber() == plate_number){
                return i;
            }
        }

        return -1;

    }

    public String lookup(String plate_number){

        Integer number = searchPlateNumber(plate_number);
        if (number != -1)
            return vehicles.get(number).getOwnerName();
        else
            return "NOT_FOUND";
    }

    public Integer register(String owner_name, String plate_number){

        Integer number = searchPlateNumber(plate_number);
        if (number != -1){
            return -1;
        }
        else{
            return vehicles.size();
        }
    }

    public static void main(String[] args) throws Exception{

        int port_number = args[2];

        if (port_number < 1024){
            System.out.println("Port number must be higher than 1024");
        }

        DatagramSocket serverSocket = new DatagramSocket(port_number); //porta >= 1024

        byte[] request = new byte[1024];
        byte[] answer = new byte[1024];


        while(true){
        
           DatagramPacket receivedDatagram = new DatagramPacket(request, request.length);

           serverSocket.receive(receivedDatagram);

           //convert byte[] to String
           String info = new String(receivedDatagram.getData());

           //processar pedido
           //get IP address
           InetAddress ip = receivedDatagram.getAddress();


        }

    }

}