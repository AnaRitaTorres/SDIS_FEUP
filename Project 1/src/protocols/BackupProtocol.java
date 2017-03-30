package protocols;

import chunk.Chunk;
import messages.ComposeMessage;
import messages.MessageType;
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

        ComposeMessage messageTest = new ComposeMessage(MessageType.PUTCHUNK, Peer.getVersion(), Peer.getServerId(), chunk.getFileId(), chunk.getChunkNo(), chunk.getReplicationDeg(), chunk.getData());

        String message = messageTest.convertPutchunkMessageToString();

        System.out.println(message);

        byte[] buf = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, Peer.getMdb().getAddress(), Peer.getMdb().getPort_number());

        Peer.getMdb().getSocket().send(packet);
    }

    public static void sendStoredMessage(String fileId, int chunkNo) throws IOException {

        ComposeMessage messageTest = new ComposeMessage(MessageType.STORED, Peer.getVersion(), Peer.getServerId(), fileId, chunkNo);

        String message = messageTest.convertMessageToString();

        byte[] buf = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, Peer.getMc().getAddress(), Peer.getMc().getPort_number());

        Peer.getMc().getSocket().send(packet);
    }

}
