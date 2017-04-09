package protocols;

import chunk.Chunk;
import messages.ComposeMessage;
import messages.MessageType;
import server.Peer;
import server.PeerInformation;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Created by catarina on 23-03-2017.
 */

//NOTE:it's better to implement Runnable instead of extending Thread,
// because you can implement many interfaces but extend only from a single class
public class BackupProtocol {

    private static int NUM_ATTEMPTS = 5;

    public static void sendPutchunkMessage(Chunk chunk) throws IOException, InterruptedException {

        ComposeMessage messageTest = new ComposeMessage(MessageType.PUTCHUNK, Peer.getVersion(), Peer.getServerId(), chunk.getFileId(), chunk.getChunkNo(), chunk.getReplicationDeg(), chunk.getData());

        byte[] buf = messageTest.convertPutchunkMessageToByteArray();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, Peer.getMdb().getAddress(), Peer.getMdb().getPort_number());

        //Adds <fileId, chunkNo, replicationDeg> to Peer.informationStored;
        if(!Peer.getDatabase().containsKeyValue(chunk.getFileId(), chunk.getChunkNo())) {
            Peer.getDatabase().addToInformationStored(chunk.getFileId(), chunk.getChunkNo(), chunk.getReplicationDeg());
        }

        int num_attempts = 0;
        int random = ThreadLocalRandom.current().nextInt(0, 400);
        boolean successful = false;

        while(num_attempts < NUM_ATTEMPTS) {

            //Se o grau de réplica for superior, sai do ciclo
            if (Peer.getDatabase().getInformationStored().get(new PeerInformation(chunk.getFileId(), chunk.getChunkNo())) >= chunk.getReplicationDeg()){
                successful= true;
                System.out.println("Backup done with replication degree equal or greater than the desired replication degree!");
                break;
            }

            //Manda mensagem para canal de backup
            Peer.getMdb().getSocket().send(packet);

            //Espera x milisegundos
            TimeUnit.MILLISECONDS.sleep(random);

            num_attempts++;
            random *= 2;
        }

        if (!successful){
            System.out.println("Backup done with replication degree lesser than the desired replication degree!");
        }

        System.out.println("contem" + Peer.getDatabase().getInformationStored());
    }

    public static void sendStoredMessage(String fileId, int chunkNo) throws IOException {

        ComposeMessage messageTest = new ComposeMessage(MessageType.STORED, Peer.getVersion(), Peer.getServerId(), fileId, chunkNo);

        String message = messageTest.convertMessageToStringWithoutBody();

        byte[] buf = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, Peer.getMc().getAddress(), Peer.getMc().getPort_number());

        Peer.getMc().getSocket().send(packet);
    }
}
