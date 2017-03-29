package protocols;

import chunk.Chunk;
import messages.MessageHeaderTest;
import messages.MessageTest;
import server.Peer;

import java.io.IOException;
import java.net.DatagramPacket;

/**
 * Created by catarina on 23-03-2017.
 */

//NOTE:it's better to implement Runnable instead of extending Thread,
// because you can implement many interfaces but extend only from a single class
public class BackupProtocol{

    public static void sendPutchunkMessage(Chunk chunk) throws IOException {

        MessageTest messageTest = new MessageTest(Peer.getVersion(), Peer.getServerId(), chunk.getFileId(), chunk.getChunkNo(), chunk.getReplicationDeg(), chunk.getData());
        messageTest.createPutchunkMessage();

        String message = messageTest.convertMessageToString();

        System.out.println(message);

        byte[] buf = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, Peer.getMdb().getAddress(), Peer.getMdb().getPort_number());

        Peer.getMdb().getSocket().send(packet);
    }

}
