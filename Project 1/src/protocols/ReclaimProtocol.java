package protocols;

import messages.ComposeMessage;
import messages.MessageType;
import server.Peer;
import server.PeerInformation;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by catarina on 09-04-2017.
 */
public class ReclaimProtocol {

    int to_reclaim_space;

    public ReclaimProtocol(int to_reclaim_space){
        this.to_reclaim_space = to_reclaim_space;
    }

    public void reclaim() throws IOException {

        int deleted_space = 0;

        //Deletes from storedChunks, chunks that are not stored in this peer
        Peer.getDatabase().deleteTrashFromStoredChunks();

        System.out.println(Peer.getDatabase().getStoredChunks());

        while(to_reclaim_space > deleted_space){

            Set<PeerInformation> set = new HashSet<>();

            for (PeerInformation peer: Peer.getDatabase().getStoredChunks().keySet()){

                //Se o grau real for superior ao desejado
                if (Peer.getDatabase().getStoredChunks().get(peer) > peer.getReplicationDeg()){

                    String path = Peer.getPath() + peer.getFileId() + "/" + peer.getChunkNo();
                    System.out.println("Path: " + path);

                    File file = new File(path);
                    if (file.exists()){
                        deleted_space += file.length();
                        set.add(peer);
                        sendRemoved(peer.getFileId(), peer.getChunkNo());

                        if (deleted_space <= to_reclaim_space){
                            break;
                        }
                    }
                }
            }

            Peer.getDatabase().getStoredChunks().keySet().removeAll(set);

            if (deleted_space <= to_reclaim_space)
                break;

            //Caso não haja mais ficheiros com grau de réplica desejado superior ao grau real
            set = new HashSet<>();

            for (PeerInformation peer: Peer.getDatabase().getStoredChunks().keySet()){

                String path = Peer.getPath() + peer.getFileId() + "/" + peer.getChunkNo();
                System.out.println("Path: " + path);

                File file = new File(path);

                if (file.exists()){

                    deleted_space += file.length();
                    set.add(peer);
                    sendRemoved(peer.getFileId(), peer.getChunkNo());

                    if (deleted_space <= to_reclaim_space)
                        break;

                }
            }

            Peer.getDatabase().getStoredChunks().keySet().removeAll(set);

        }

    }

    public static void sendRemoved(String fileId, int chunkNo) throws IOException {

        ComposeMessage messageTest = new ComposeMessage(MessageType.REMOVED, Peer.getVersion(), Peer.getServerId(), fileId, chunkNo);

        String message = messageTest.convertMessageToStringWithoutBody();

        byte[] buf = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, Peer.getMc().getAddress(), Peer.getMc().getPort_number());

        Peer.getMc().getSocket().send(packet);
    }

}
