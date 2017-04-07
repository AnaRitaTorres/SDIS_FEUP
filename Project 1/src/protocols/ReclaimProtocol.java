package protocols;

import messages.ComposeMessage;
import messages.MessageType;
import server.Peer;

import java.io.IOException;
import java.net.DatagramPacket;

/**
 * Created by iamgroot on 07/04/17.
 */
public class ReclaimProtocol {

    public static void sendRemovedMessage(String FileId, int chunkNo) throws IOException{

        ComposeMessage messageTest = new ComposeMessage(MessageType.REMOVED, Peer.getVersion(), Peer.getServerId(), FileId, chunkNo);

        byte[] message = messageTest.convertMessageWithoutBodyToByteArray();

        DatagramPacket packet = new DatagramPacket(message, message.length,Peer.getMc().getAddress(),Peer.getMc().getPort_number());

        Peer.getMc().getSocket().send(packet);
    }
}
