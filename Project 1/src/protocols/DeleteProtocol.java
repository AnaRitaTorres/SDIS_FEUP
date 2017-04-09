package protocols;

import messages.ComposeMessage;
import messages.MessageType;
import server.Peer;
import server.PeerInformation;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by iamgroot on 03/04/17.
 */
public class DeleteProtocol {

    public static void sendDeleteMessage(String fileId) throws IOException{

        ComposeMessage messageTest = new ComposeMessage(MessageType.DELETE, Peer.getVersion(), Peer.getServerId(), fileId);

        String message = messageTest.convertMessageToStringWithoutBody();

        byte[] buf = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, Peer.getMc().getAddress(), Peer.getMc().getPort_number());

        Peer.getMc().getSocket().send(packet);

        Set<PeerInformation> set = new HashSet<>();

        for (PeerInformation peer: Peer.getDatabase().getInformationStored().keySet()){
            if (peer.getFileId().equals(fileId)) {
                set.add(peer);
            }
        }
        Peer.getDatabase().getInformationStored().keySet().removeAll(set);
    }
}
