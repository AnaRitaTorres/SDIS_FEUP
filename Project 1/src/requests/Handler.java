package requests;

import fileManager.FileManager;
import messages.DecomposeHeader;
import messages.DecomposeMessage;
import protocols.BackupProtocol;
import protocols.RestoreProtocol;
import server.Peer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;

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
            }
            removeRequest();
        }
        else{
            //Removes own request to avoid useless loop
            removeRequest();
        }
    }

    public void handlePutchunk(DecomposeMessage messageToHandle) throws IOException {

        byte[] body = messageToHandle.getBody();
        DecomposeHeader header = new DecomposeHeader(messageToHandle.getHeader());

        //Se ainda tiver espaço para guardar chunks
        if (Peer.getOccupiedSize() + body.length < Peer.getMaxSizeToSave()){

            String fileId = header.getFileId();
            int chunkNo = header.getChunkNo();
            int replicationDeg = header.getReplicationDeg();

            //updates occupied size
            Peer.updateOccupiedSize(body.length);

            //saves file
            FileManager.saveFile(body, fileId, chunkNo);

            if (!Peer.getDatabase().addToStoredChunks(fileId, chunkNo, replicationDeg)){
                Peer.getDatabase().incrementsStoredChunks(fileId, chunkNo, replicationDeg);
            }

            BackupProtocol.sendStoredMessage(fileId, chunkNo);
        }
    }

    public void handleStored(DecomposeMessage messageToHandle) throws IOException{

        DecomposeHeader header = new DecomposeHeader(messageToHandle.getHeader());
        String fileId = header.getFileId();
        int chunkNo = header.getChunkNo();

        //Se for o Peer initiator, tem no hashmap o par <fileId, chunkNo>
        if(Peer.getDatabase().containsKeyValue(fileId, chunkNo))
            Peer.getDatabase().incrementsReplicationDegree(fileId, chunkNo);

        if (!Peer.getDatabase().addToStoredChunks(fileId, chunkNo)){
            Peer.getDatabase().incrementsStoredChunks(fileId, chunkNo);
        }
    }

    public void handleDelete(DecomposeMessage messageToHandle) throws  IOException{

        DecomposeHeader header = new DecomposeHeader(messageToHandle.getHeader());

        String fileId = header.getFileId();
        FileManager.deleteFile(fileId);
    }

    public void handleGetchunk(DecomposeMessage messageToHandle) throws IOException {

        DecomposeHeader header = new DecomposeHeader(messageToHandle.getHeader());

        String fileId = header.getFileId();
        int chunkNo = header.getChunkNo();

        String pathString = Peer.getPath() + fileId + "/" + chunkNo;
        System.out.println(pathString);

        File file = new File(pathString);
        Path path = Paths.get(pathString);

        if (file.exists()) {
            byte[] body = Files.readAllBytes(path);
            RestoreProtocol.sendChunkMessage(fileId, chunkNo, body);
        }
        else{
            System.err.println("Não existe ficheiro");;
        }
    }

    //TODO: falta juntar depois os ficheiros recebidos no handleChunk
    public void handleChunk(DecomposeMessage messageToHandle) throws IOException {

        DecomposeHeader header = new DecomposeHeader(messageToHandle.getHeader());

        //TODO: Ao reverter ficheiro, espera-se o nome original --> linha de comandos
        String fileId = header.getFileId();
        int chunkNo = header.getChunkNo();
        byte[] body = messageToHandle.getBody();

        int position = hasElement(fileId);

        if (position != -1) {
            if (!Peer.getDatabase().getFilesToRestore().contains(body))
                Peer.getDatabase().getFilesToRestore().elementAt(position).changePositionInVector(body, chunkNo-1);

        }

        //Se vector estiver cheio
        if (Peer.getDatabase().getFilesToRestore().elementAt(position).filledVector()){
            Peer.getDatabase().getFilesToRestore().elementAt(position).saveFile();
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
