package protocols;

import chunk.Chunk;
import messages.ComposeMessage;
import messages.MessageType;
import server.Peer;
import server.PeerDatabase;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Created by catarina on 23-03-2017.
 */

//NOTE:it's better to implement Runnable instead of extending Thread,
// because you can implement many interfaces but extend only from a single class
public class BackupProtocol{

    //TODO: tirar numeros magicos
    public static void sendPutchunkMessage(Chunk chunk) throws IOException, InterruptedException {

        ComposeMessage messageTest = new ComposeMessage(MessageType.PUTCHUNK, Peer.getVersion(), Peer.getServerId(), chunk.getFileId(), chunk.getChunkNo(), chunk.getReplicationDeg(), chunk.getData());

        byte[] buf = messageTest.convertPutchunkMessageToByteArray();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, Peer.getMdb().getAddress(), Peer.getMdb().getPort_number());

        //Adds pair <fileId, chunkNo> to Peer.informationStored;
        if(!Peer.containsKeyValue(chunk.getFileId(), chunk.getChunkNo()))
            Peer.addToInformationStored(chunk.getFileId(), chunk.getChunkNo());

        int num_attempts = 0;
        int random = ThreadLocalRandom.current().nextInt(0, 400);

        while(num_attempts < 5) {

            //Manda mensagem para canal de backup
            Peer.getMdb().getSocket().send(packet);

            //Espera x milisegundos
            TimeUnit.MILLISECONDS.sleep(random);

            //Se o grau de réplica for superior, sai do ciclo
            if (Peer.getInformationStored().get(new PeerDatabase(chunk.getFileId(), chunk.getChunkNo())) >= chunk.getReplicationDeg())
                break;

            num_attempts++;
            random *= 2;
        }

    }

    public static void sendStoredMessage(String fileId, int chunkNo) throws IOException {

        ComposeMessage messageTest = new ComposeMessage(MessageType.STORED, Peer.getVersion(), Peer.getServerId(), fileId, chunkNo);

        String message = messageTest.convertMessageToStringWithoutBody();

        byte[] buf = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, Peer.getMc().getAddress(), Peer.getMc().getPort_number());

        Peer.getMc().getSocket().send(packet);
    }

}
