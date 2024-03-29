package client; 


import java.io.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TestApp {

    private String peer_ap;
    private String sub_protocol;
    private int replicationDeg;
    private int reclaimed_space;
    private File file;
    private static boolean use_enhancements;

    public static void main(String[] args) throws RemoteException {
        TestApp testApp = new TestApp();
        testApp.run(args);
    }

    public void run(String[] args) throws RemoteException {

        if (!validArguments(args))
            return;

        Registry registry;
        Interface rmi = null;

        try{
            registry = LocateRegistry.getRegistry();
            rmi = (Interface) registry.lookup(peer_ap);

            switch(sub_protocol){
                case "BACKUP":
                    rmi.backup(file, replicationDeg, false);
                    break;
                case "BACKUPENH":
                    rmi.backup(file, replicationDeg, true);
                    break;

                case "RESTORE":
                    rmi.restore(file);
                    break;

                case "DELETE":
                    rmi.delete(file);
                    break;

                case "RECLAIM":
                    rmi.reclaim(reclaimed_space);
                    break;

            }
        }
        catch(Exception e){
            System.err.println("client exception: " + e.toString());
            e.printStackTrace();
        }
        if (rmi == null)
            rmi.exit();

    }

    public boolean validArguments(String[] args){

        peer_ap = args[0]; //remote object name
        sub_protocol = args[1];

        if(args.length > 4 || args.length < 2) {
            System.out.println("Wrong Number of Arguments");
            return false;
        }

        sub_protocol = args[1].toUpperCase();

        switch(sub_protocol){
            case "BACKUP":
            case "BACKUPENH":
                if(args.length != 4) {
                    System.out.println("Usage: java TestApp <peer_ap> BACKUP <opnd_1> <opnd_2>");
                    return false;
                }

                String path = args[2];
                file = new File(path);

                replicationDeg = Integer.parseInt(args[3]);

                return validFile(file);

            case "RESTORE":
                if(args.length != 3) {
                    System.out.println("Usage: java client.TestApp <peer_ap> RESTORE <opnd_1>");
                    return false;
                }

                path = args[2];
                file = new File(path);

                return validFile(file);

            case "DELETE":
                if(args.length != 3) {
                    System.out.println("Usage: java client.TestApp <peer_ap> DELETE <opnd_1>");
                    return false;
                }

                path = args[2];
                file = new File(path);

                return validFile(file);

            case "RECLAIM":
                if(args.length != 3) {
                    System.out.println("Usage: java client.TestApp <peer_ap> RECLAIM <amount_of_space(KByte)>");
                    return false;
                }
                reclaimed_space =  Integer.parseInt(args[2]);

                break;

            case "STATE":
                if(args.length != 2){
                    System.out.println("Usage: java client.TestApp <peer_ap> STATE");
                    return false;
                }
                break;

            default:
                System.out.println("The Subprotocol " + sub_protocol + " does not exist");
                return false;
        }
        return true;
    }

    public boolean validFile(File file){
        //check if File exists and is a File
        if(!(file.exists() && file.isFile())) {
            System.err.println("The File does not exist");
            return false;
        }
        return true;
    }
}

