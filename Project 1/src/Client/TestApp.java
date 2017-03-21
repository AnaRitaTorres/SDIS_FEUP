package Client; /**
 * Created by iamgroot on 18/03/17.
 */

import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TestApp {

    public static void main(String[] args){
        if(args.length > 4 || args.length < 2) {
            System.out.println("Wrong Number of Arguments");
            return;
        }

        String peer_ap = args[0]; //host
        String sub_protocol = args[1];

        /*
            TODO: Change switch statement when doing enhancements
         */

        //toUpperCase is used to avoid errors
        switch(sub_protocol.toUpperCase()){
            case "BACKUP":
                if(args.length != 4) {
                    System.out.println("Usage: java Client.TestApp <peer_ap> BACKUP <opnd_1> <opnd_2>");
                    return;
                }

                String path = args[2];
                File file = new File(path);

                //check if file exists and is a file
                if(!(file.exists() && file.isFile())) {
                    System.out.println("The File does not exist");
                    return;
                }

                Integer rep_degree = Integer.parseInt(args[3]);
                break;

            case "RESTORE":
                if(args.length != 3) {
                    System.out.println("Usage: java Client.TestApp <peer_ap> RESTORE <opnd_1>");
                    return;
                }

                path = args[2];
                file = new File(path);

                //check if file exists and is a file
                if(!(file.exists() && file.isFile())) {
                    System.out.println("The File does not exist");
                    return;
                }
                break;

            case "DELETE":
                if(args.length != 3) {
                    System.out.println("Usage: java Client.TestApp <peer_ap> DELETE <opnd_1>");
                    return;
                }

                path = args[2];
                file = new File(path);

                //check if file exists and is a file
                if(!(file.exists() && file.isFile())) {
                    System.out.println("The File does not exist");
                    return;
                }
                break;

            case "RECLAIM":
                if(args.length != 3) {
                    System.out.println("Usage: java Client.TestApp <peer_ap> RECLAIM <amount_of_space(KByte)>");
                    return;
                }
                Integer reclaimed_space =  Integer.parseInt(args[2]);
                break;
            case "STATE":
                //no opnds
                if(args.length != 2){
                    System.out.println("Usage: java Client.TestApp <peer_ap> STATE");
                    return;
                }

            default:
                System.out.println("The Subprotocol does not exist");
                break;
        }


        //Connect - RMI
        try {
            Registry registry = LocateRegistry.getRegistry();
            Interface stub = (Interface) registry.lookup(peer_ap);
            String response = stub.backup();
            System.out.println("response: " + response);
        } catch (Exception e) {
            System.err.println("Client.TestApp exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
