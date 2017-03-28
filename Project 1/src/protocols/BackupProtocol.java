package protocols;

import chunk.Chunk;
import server.Peer;

import java.io.IOException;
import java.net.DatagramPacket;

/**
 * Created by catarina on 23-03-2017.
 */

//NOTE:it's better to implement Runnable instead of extending Thread,
// because you can implement many interfaces but extend only from a single class
public class BackupProtocol{

    //TODO: Acrescentar argumento Chunk chunk
    public static void sendPutchunkMessage() throws IOException {

        String message = "teste";
        //String message = createPutchunkMessage(chunk);
        byte[] buf = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, Peer.getMdb().getAddress(), Peer.getMdb().getPort_number());

        Peer.getMdb().getSocket().send(packet);
    }

    //TODO: Criar funcao para criar mensagem a ser enviada em sendPutchunkMessage
    public static String createPutchunkMessage(Chunk chunk){
            return "";
    }

}
