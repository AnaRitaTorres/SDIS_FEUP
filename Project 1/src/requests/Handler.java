package requests;

import fileManager.FileManager;
import fileManager.FileToRestore;
import messages.DecomposeHeader;
import messages.DecomposeMessage;
import protocols.BackupProtocol;
import protocols.RestoreProtocol;
import server.Peer;
import server.PeerDatabase;

import javax.xml.bind.DatatypeConverter;
import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
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

            //TODO: completar handler
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

            //updates occupied size
            Peer.updateOccupiedSize(body.length);

            //saves file
            FileManager.saveFile(body, fileId, chunkNo);

            BackupProtocol.sendStoredMessage(fileId, chunkNo);

        }
    }

    public void handleStored(DecomposeMessage messageToHandle) throws IOException{
        byte[] body = messageToHandle.getBody();
        DecomposeHeader header = new DecomposeHeader(messageToHandle.getHeader());
        String fileId = header.getFileId();
        int chunkNo = header.getChunkNo();

        //Se for o Peer initiator, tem no hashmap o par <fileId, chunkNo>
        if(Peer.containsKeyValue(fileId, chunkNo))
            Peer.incrementsReplicationDegree(fileId, chunkNo);
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
        Path path = Paths.get(pathString);

        byte[] body = Files.readAllBytes(path);

        RestoreProtocol.sendChunkMessage(fileId, chunkNo, body);
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
            System.out.println("uiaa" + Peer.getFilesToRestore().elementAt(position));
            if (Peer.getFilesToRestore().elementAt(position) == null) {
                Peer.getFilesToRestore().elementAt(position).addToVector(body, chunkNo);
                System.out.println("Adicionei");
            }
        }

        //Se vector estiver cheio
        System.out.println("Vou verificar se estou cheio");
        if (Peer.getFilesToRestore().elementAt(position).filledVector()){
            System.out.println("Estou gordinho");
            Peer.getFilesToRestore().elementAt(position).saveFile();
            System.out.println("guardei gordinho");
        }
    }

    public int hasElement(String fileId){
        for (int i=0; i<Peer.getFilesToRestore().size(); i++){
            //Se existir
            if (Peer.getFilesToRestore().elementAt(i).getFileId().equals(fileId))
                return i;
        }
        return -1;
    }
}
