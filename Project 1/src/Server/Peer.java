package Server;

/**
 * Created by catarina on 21-03-2017.
 */
import Client.Interface;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Peer implements Interface{

    public String backup(){
        return "oi";
    }
    public String delete(){
        return "oi";
    }
    public String restore(){
        return "oi";
    }
    public String reclaim(){
        return "oi";
    }

    public static void main(String args[]){

        if (args.length != 1){
            System.out.println("Wrong number of arguments");
            return;
        }

        String remote_object_name = args[0];

        try{

            //The main method of the server needs to create the remote object that provides the service. Additionally, the remote object must be exported to the Java RMI runtime so that it may receive incoming remote calls.
            Peer server = new Peer();
            Interface stub = (Interface) UnicastRemoteObject.exportObject(server, 0);
            //The exportObject exports the supplied remote object to receive incoming remote method invocations on an anonymous TCP port and returns the stub for the remote object to pass to clients.
            //As a result of the exportObject call, the runtime may begin to listen on a new server socket or may use a shared server socket to accept incoming remote calls for the remote object.

            //The returned stub implements the same set of remote interfaces as the remote object's class and contains the host name and port over which the remote object can be contacted.

            ///////////////////////////////////////////////////////////////////////////////

            //For a caller (client, peer, or applet) to be able to invoke a method on a remote object, that caller must first obtain a stub for the remote object.
            //Java RMI provides a registry API for applications to bind a name to a remote object's stub and for clients to look up remote objects by name in order to obtain their stubs.
            Registry registry = LocateRegistry.getRegistry();
            registry.bind(remote_object_name, stub);

            //Once a remote object is registered on the server, callers can look up the object by name, obtain a remote object reference, and then invoke remote methods on the object.
        }
        catch (Exception e){

            //System.err.println()  prints the standard error (in red xD)
            System.err.println("Peer exception: " + e.toString());
            e.printStackTrace();

        }

    }
}