import java.util.*;
import java.net.*;
import java.io.*;

class Server{
    
    Vector<Vehicle> vehicles = new Vector<Vehicle>();

    public static void main(String[] args) throws IOException{
        
       Server server = new Server();
       server.run(args);

    }

    public int register(String plate_number, String owner_name){

        for (int i=0; i < vehicles.size(); i++){
            if (vehicles.get(i).plate_number.equals(plate_number)){
                return -1;
            }
        }

        Vehicle v = new Vehicle(plate_number, owner_name);
        vehicles.add(v);

        return vehicles.size();
    
    }

     public String lookup(String plate_number){

        for (int i=0; i < vehicles.size(); i++){
            if (vehicles.get(i).plate_number.equals(plate_number)){
                return "oioioi";
            }
        }

        return "NOT_FOUND";
        
    }

    public String processRequest(String request){

        System.out.println("Request received: " + request);

        request.trim();

        String[] parsedString = request.split(" ");

        String message =new String();

        if (parsedString[0].equals("register")){

            System.out.println("x"+parsedString[1]+"x");
            message = Integer.toString(register(parsedString[1], parsedString[2]));
        }

        else if(parsedString[0].equals("lookup")){

            message = lookup(parsedString[1]);
        
        }

        return message;

    }

    public void run(String[] args) throws IOException{

        if (args.length != 1){
                    System.out.println("Usage: java server <port_number>");
                    return;
                }

                int port_number = Integer.parseInt(args[0]); //converte string para o tipo primitivo int

                //Verificar valor da porta
                if(port_number < 1024){
                    System.out.println("Port_number must be a value higher or equal to 1024");
                    return;
                }

                //Passos para Comunicação com Sockets UDP

                // Primeiro - Criar um Socket (atribui-se um nome ao socket UDP criado se se pretender receber mensagens de sockets remotos a que não se enviou mensagens previamente)
                DatagramSocket serverSocket = new DatagramSocket(port_number);
                
                //Receção de pedidos do cliente

                //cria um array de bytes usado para criar o DatagramPacket
            

                DatagramPacket packet = null;

                // Segundo - Transferir informação (DatagramPackets)
                while(true){
                    
                    byte[] buf = new byte[1024];

                    //Cria-se o DatagramPacket(byte[] buf, int length, InetAddress address, int port);
                    //O primeiro argumento contém a informação do cliente, o segundo o comprimento dessa mesma informação. Os terceiro e quarto argumentos são necessários para o servidor conseguir responder para o cliente
                    packet = new DatagramPacket(buf, buf.length);

                    serverSocket.receive(packet);

                    // Pedido Recebido
                    // public byte[] getData() - DatagramPacket function
                    // String(byte[] bytes)
                    String request = new String(packet.getData());

                    //Construção da resposta do servidor
                    String response = processRequest(request);
                    System.out.println(response);
                    buf = response.getBytes();

                    //Envio da resposta
                    InetAddress address = packet.getAddress();
                    int port = packet.getPort();
                    packet = new DatagramPacket(buf, buf.length, address, port);

                    serverSocket.send(packet);
            
        }

        //serverSocket.close();

    }

}