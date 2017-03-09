import java.util.*;
import java.net.*;
import java.io.*;
import java.lang.String;

class Client{

    public static void main(String[] args) throws UnknownHostException, IOException{

        if (args.length < 4){
            System.out.println("Usage: java Client <host_name> <port_number> <oper> <opnd>*");
            return;
        }

        //Guardar e verificar dados recebidos
        String host_name = args[0];

        //port_number
        int port_number = Integer.parseInt(args[1]);
        if (port_number < 1024){
            System.out.println("<port_number> must be a value higher or equal to 1024");
            return;
        }

        //register or lookup
        String oper = args[2].toLowerCase();

        if (!oper.equals("register") && !oper.equals("lookup")){
            System.out.println("<oper> must be 'register' or 'lookup'");
            return;
        }

        String message = new String();

        //register
        if (oper.equals("register")){
            
            if (args.length != 5){
                System.out.println("Wrong number of arguments for 'register' function"); 
                return;
            }
            
            message = new String("register " + args[3] + " " + args[4]);
        }

        //lookup
        if (oper.equals("lookup")){
            if (args.length != 4){
                System.out.println("Wrong number of arguments for 'lookup' function"); 
                return;
            }

            message = new String("lookup " + args[3]);
        }

        

        // Cria-se o socket
        // O construtor não precisa de ter argumentos, pois o construtor liga o socket a uma porta disponível. O datagramPacket contém a informação do endereço. O servidor consegue obter o número da porta através do packet recebido.
        DatagramSocket clientSocket = new DatagramSocket();


        // Manda-se o pedido para o servidor
        byte[] buf = message.getBytes();

        //public static InetAddress getByName(String host)
        InetAddress address = InetAddress.getByName(host_name);

        //DatagramPacket(byte[] buf, int length, InetAddress address, int port) - construtor
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port_number);

        clientSocket.send(packet);

        //Recebe-se o pedido do servidor
       

    }

}