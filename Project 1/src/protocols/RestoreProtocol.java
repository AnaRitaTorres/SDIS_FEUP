package protocols;

import chunk.Chunk;
import fileManager.FileManager;
import fileManager.FileToRestore;
import messages.ComposeMessage;
import messages.MessageType;
import server.Peer;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Created by catarina on 06-04-2017.
 */
public class RestoreProtocol {

    public void sendGetchunkMessage(File file) throws IOException {

        int numChunks = FileManager.getNumChunks(file);
        String filename = file.getName();

        for (int chunkNo=1; chunkNo<=numChunks; chunkNo++){

            String fileId = FileManager.generateFileId(file);

            if (chunkNo==1){
                FileToRestore fileToRestore = new FileToRestore(fileId, filename, numChunks);

                if(!Peer.getDatabase().getFilesToRestore().contains(fileToRestore))
                    Peer.getDatabase().getFilesToRestore().add(fileToRestore);

            }

            ComposeMessage messageTest = new ComposeMessage(MessageType.GETCHUNK, Peer.getVersion(), Peer.getServerId(), fileId, chunkNo);

            String message = messageTest.convertMessageToStringWithoutBody();

            byte[] buf = message.getBytes();

            DatagramPacket packet = new DatagramPacket(buf, buf.length, Peer.getMc().getAddress(), Peer.getMc().getPort_number());

            //Manda mensagem para canal de controlo
            Peer.getMc().getSocket().send(packet);
        }
    }

    public static void sendChunkMessage(String fileId, int chunkNo, byte[] body) throws IOException {

        int random = ThreadLocalRandom.current().nextInt(0, 400);

        ComposeMessage messageTest = new ComposeMessage(MessageType.CHUNK, Peer.getVersion(), Peer.getServerId(), fileId, chunkNo, body);

        byte[] buf = messageTest.convertMessageToByteArray();

        DatagramPacket packet = new DatagramPacket(buf, buf.length, Peer.getMdr().getAddress(), Peer.getMdr().getPort_number());

        try {
            //Espera x milisegundos
            TimeUnit.MILLISECONDS.sleep(random);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Manda mensagem para canal de restore
        Peer.getMdb().getSocket().send(packet);
    }

}
