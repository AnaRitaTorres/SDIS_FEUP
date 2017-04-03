package requests;

import fileManager.FileManager;
import messages.DecomposeHeader;
import messages.DecomposeMessage;
import protocols.BackupProtocol;
import server.Peer;
import java.io.IOException;
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
        System.out.println("Message: " + header.getChunkNo());

        //Peer ignores its own requests
        if (header.getSenderId() != Peer.getServerId()){

            //TODO: completar handler
            switch(header.getMessageType()){
                case PUTCHUNK:
                    handlePutchunk(messageToHandle);
                    break;
                //TODO: guardar o grau de replica algures...
                case STORED:
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

        //TODO: ter o ciclo
        byte[] body = messageToHandle.getBody();
        DecomposeHeader header = new DecomposeHeader(messageToHandle.getHeader());

        //Se ainda tiver espaço para guardar chunks
        if (Peer.getOccupiedSize() + body.length < Peer.getMaxSizeToSave()){

            String fileId = header.getFileId();
            int chunkNo = header.getChunkNo();

            //updtes occupied size
            Peer.updateOccupiedSize(body.length);

            //saves file
            FileManager.saveFile(body, chunkNo, Peer.getPath());

            BackupProtocol.sendStoredMessage(fileId, chunkNo);

        }
    }
}
