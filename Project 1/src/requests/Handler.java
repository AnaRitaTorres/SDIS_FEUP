package requests;

import fileManager.FileManager;
import messages.DecomposeHeader;
import messages.DecomposeMessage;
import protocols.BackupProtocol;
import protocols.ReclaimProtocol;
import server.Peer;
import server.PeerDatabase;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.lang.InterruptedException;

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

    public void handleRequests() throws IOException,InterruptedException {

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
        //TODO: eliminar ficheiro
        FileManager.deleteFile(fileId);

        //TODO:eliminar os chunks com este fileID dos stored
    }

    public void handleRemoved(DecomposeMessage messageToHandle) throws  IOException, InterruptedException{

        DecomposeHeader header = new DecomposeHeader(messageToHandle.getHeader());
        String fileId = header.getFileId();
        int chunkNo = header.getChunkNo();
        int random = ThreadLocalRandom.current().nextInt(0, 400);

        PeerDatabase database = new PeerDatabase(fileId,chunkNo);

        //verifica se existe, caso exista é removido
        if(Peer.getInfo().containsValue(database))
            Peer.getInfo().remove(database);

        //se a contagem for menor que o repDegre
        if(Peer.getInfo().size() < Peer.getReplication_degree()){
            //delay
            TimeUnit.MILLISECONDS.sleep(random);

            //TODO:don't knoww
            //a peer receives a PUTCHUNK message for the same file chunk, it should back off and restrain
            // from starting yet another backup subprotocol for that file chunk
        }

    }

}