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
            Vehicle vehicle = new Vehicle(owner_name, plate_number);
            vehicles.add(vehicle);
            return vehicles.size();
        }
    }

    public String processRequest(String receivedInfo){

        receivedInfo.trim(); //removes spaces
        String[] info = receivedInfo.split(" "); //splits string "receivedInfo" by its spaces

        String function = info[0].toUpperCase();
        String plate_number = info[1].toUpperCase();
        String owner_name = "";

        for (Integer i=2; i < info.length(); i++){
            owner_name += info[i].toUpperCase() + " ";
        }

        owner_name.trim();

        if (function == "REGISTER"){
            return String.valueOf(register(owner_name, plate_number));
        }
        else if (function == "LOOKUP"){
            return lookup(plate_number);
        }
    }

    public static void main(String[] args) throws Exception{

        int port_number = args[2];

        if (port_number < 1024){
            System.out.println("Port number must be higher than 1024");
        }

        //create datagram socket
        DatagramSocket serverSocket = new DatagramSocket(port_number); //porta >= 1024

        byte[] request = new byte[1024];
        byte[] answer = new byte[1024];


        while(true){
            
           //vessel for the received datagram
           DatagramPacket receivedDatagram = new DatagramPacket(request, request.length);

           //receives datagram
           serverSocket.receive(receivedDatagram);

           //convert byte[] to String
           String info = new String(receivedDatagram.getData());

           //process request
           String response = processRequest(info);

           answer = response.getBytes(); //getBytes from String Library

           //get client's IP address
           InetAddress ip = receivedDatagram.getAddress();

           //get client's port_number
           int port_number = receivedDatagram.getPort();

           //create the datagram to send
           DatagramPacket sentDatagram = new DatagramPacket(answer, answer.length, ip,port_number);

           //datagram to socket
           serverSocket.send(sentDatagram);

        }

    }

}