package requests;

import fileManager.FileManager;
import messages.DecomposeHeader;
import messages.DecomposeMessage;
import protocols.BackupProtocol;
import protocols.RestoreProtocol;
import server.Peer;
import server.PeerInformation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Created by catarina on 31-03-2017.
 */
public class Handler {

    Queue<DecomposeMessage> requestsToHandle;

    public Handler(){
        requestsToHandle = new LinkedList<>();
    }

    public void addRequest(DecomposeMessage message){
        requestsToHandle.add(message);
    }

    public void removeRequest(){
        if (!requestsToHandle.isEmpty()){
            requestsToHandle.remove();
        }
    }

    public void handleRequests() throws IOException {

        if(requestsToHandle.isEmpty())
            return;

        DecomposeMessage messageToHandle = requestsToHandle.peek();
        DecomposeHeader header = new DecomposeHeader(messageToHandle.getHeader());

        //Peer ignores its own requests
        if (header.getSenderId() != Peer.getServerId()){

            //TODO: completar requests
            switch(header.getMessageType()){
                case PUTCHUNK:
                    handlePutchunk(messageToHandle);
                    break;
                case STORED:
                    handleStored(messageToHandle);
                    break;
                case DELETE:
                    handleDelete(messageToHandle);
                    break;
                case GETCHUNK:
                    handleGetchunk(messageToHandle);
                    break;
                case CHUNK:
                    handleChunk(messageToHandle);
                    break;
                case REMOVED:
                    handleRemoved(messageToHandle);
                    break;
            }
            removeRequest();
        }
        else{
            //Removes own request to avoid useless loop
            removeRequest();
        }
    }

    public void handlePutchunk(DecomposeMessage messageToHandle) throws IOException {

        //TODO: alterar este cabrão
        byte[] body = messageToHandle.getBody();
        DecomposeHeader header = new DecomposeHeader(messageToHandle.getHeader());

        String fileId = header.getFileId();
        int chunkNo = header.getChunkNo();
        int replicationDeg = header.getReplicationDeg();
        int random = ThreadLocalRandom.current().nextInt(0, 400 + 1);

        if (Peer.getOccupiedSize() + body.length < Peer.getMaxSizeToSave()) {

            PeerInformation peer = new PeerInformation(fileId, chunkNo, replicationDeg);
            
            if (Peer.useEnhancements()){

                if (Peer.getDatabase().addToStoredChunks(fileId, chunkNo, replicationDeg)) {

                    try {
                        TimeUnit.MILLISECONDS.sleep(random);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    int realDeg = Peer.getDatabase().getStoredChunks().get(peer);

                    if (realDeg < peer.getReplicationDeg()) {

                        FileManager.saveFile(body, fileId, chunkNo);
                        Peer.getDatabase().incrementsStoredChunks(fileId, chunkNo);
                        BackupProtocol.sendStoredMessage(fileId, chunkNo);
                    }
                }
            }
            else{
                if (!Peer.getDatabase().addToStoredChunks(fileId, chunkNo, replicationDeg)){

                    String path = fileId + "/" + chunkNo;
                    FileManager.deleteFile(path);
                }
                else{
                    random = ThreadLocalRandom.current().nextInt(0, 400+1);
                    try {
                        TimeUnit.MILLISECONDS.sleep(random);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    BackupProtocol.sendStoredMessage(fileId, chunkNo);
                    Peer.getDatabase().incrementsStoredChunks(fileId, chunkNo);
                }
                FileManager.saveFile(body, fileId, chunkNo);
            }
        }
    }

    public void handleStored(DecomposeMessage messageToHandle) throws IOException{

        DecomposeHeader header = new DecomposeHeader(messageToHandle.getHeader());
        String fileId = header.getFileId();
        int chunkNo = header.getChunkNo();

        //Se for o Peer initiator, tem no hashmap o par <fileId, chunkNo>
        if(Peer.getDatabase().containsKeyValue(fileId, chunkNo)) {
            Peer.getDatabase().incrementsReplicationDegree(fileId, chunkNo);
        }

        PeerInformation peer = new PeerInformation(fileId, chunkNo);
        if (Peer.getDatabase().getStoredChunks().containsKey(peer)){
            Peer.getDatabase().incrementsStoredChunks(fileId, chunkNo);
        }
    }

    public void handleDelete(DecomposeMessage messageToHandle) throws  IOException{

        DecomposeHeader header = new DecomposeHeader(messageToHandle.getHeader());

        String fileId = header.getFileId();
        FileManager.deleteDirectory(fileId);
    }

    public void handleGetchunk(DecomposeMessage messageToHandle) throws IOException {

        DecomposeHeader header = new DecomposeHeader(messageToHandle.getHeader());

        String fileId = header.getFileId();
        int chunkNo = header.getChunkNo();

        String pathString = Peer.getPath() + fileId + "/" + chunkNo;

        File file = new File(pathString);
        Path path = Paths.get(pathString);

        if (file.exists()) {
            byte[] body = Files.readAllBytes(path);
            int random = ThreadLocalRandom.current().nextInt(0, 400 + 1);
            try {
                TimeUnit.MILLISECONDS.sleep(random);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            RestoreProtocol.sendChunkMessage(fileId, chunkNo, body);
        }
        else{
            System.err.println("Não existe ficheiro");;
        }
    }

    public void handleChunk(DecomposeMessage messageToHandle) throws IOException {

        DecomposeHeader header = new DecomposeHeader(messageToHandle.getHeader());

        String fileId = header.getFileId();
        int chunkNo = header.getChunkNo();
        byte[] body = messageToHandle.getBody();

        int position = hasElement(fileId);

        if (position != -1) {
            if (!Peer.getDatabase().getFilesToRestore().contains(body))
                Peer.getDatabase().getFilesToRestore().elementAt(position).changePositionInVector(body, chunkNo-1);

            //Se vector estiver cheio
            if (Peer.getDatabase().getFilesToRestore().elementAt(position).filledVector()){
                Peer.getDatabase().getFilesToRestore().elementAt(position).saveFile();
            }
        }
    }

    public void handleRemoved(DecomposeMessage messageToHandle) throws IOException {

        DecomposeHeader header = new DecomposeHeader(messageToHandle.getHeader());
        String fileId = header.getFileId();
        int chunkNo = header.getChunkNo();

        PeerInformation peerInfo = new PeerInformation(fileId, chunkNo);

        Peer.getDatabase().decreasesReplicationDegree(fileId, chunkNo);
        Peer.getDatabase().decreasesStoredChunks(fileId, chunkNo);

        int desiredReplicationDeg = Peer.getDatabase().getDesiredReplicationDeg(fileId, chunkNo, Peer.getDatabase().getInformationStored());

        if (desiredReplicationDeg == -1){
            desiredReplicationDeg = Peer.getDatabase().getDesiredReplicationDeg(fileId, chunkNo, Peer.getDatabase().getStoredChunks());
        }

        //Se o valor real for inferior ao desejado
        if (Peer.getDatabase().getStoredChunks().get(peerInfo) < desiredReplicationDeg){
            BackupProtocol.sendStoredMessage(fileId, chunkNo);
        }
    }

    public int hasElement(String fileId){
        for (int i=0; i<Peer.getDatabase().getFilesToRestore().size(); i++){
            //Se existir
            if (Peer.getDatabase().getFilesToRestore().elementAt(i).getFileId().equals(fileId))
                return i;
        }
        return -1;
    }
}
