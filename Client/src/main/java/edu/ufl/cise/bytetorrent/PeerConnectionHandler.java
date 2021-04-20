package edu.ufl.cise.bytetorrent;

import edu.ufl.cise.bytetorrent.model.Peer;
import edu.ufl.cise.bytetorrent.service.FileManagementService;
import edu.ufl.cise.bytetorrent.util.FileUtil;
import edu.ufl.cise.bytetorrent.model.message.Handshake;
import edu.ufl.cise.bytetorrent.model.message.Message;
import edu.ufl.cise.bytetorrent.model.message.MessageGenerator;
import edu.ufl.cise.bytetorrent.model.message.payload.*;
import edu.ufl.cise.bytetorrent.util.LoggerUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;

/**
 * A handler thread class.  Handlers are spawned from the listening
 * loop and are responsible for dealing with a single client's requests.
 */
public class PeerConnectionHandler extends Thread {
    private final Map<Integer, Peer> peers;
    private final Socket connection;
    private ObjectOutputStream out;
    private ObjectInputStream thisPeerInputStream;
    private Peer connectingPeer;
    private boolean isMeChocked;
    private Peer selfPeer;
    private float downloadSpeed = 0;

    public PeerConnectionHandler(Socket connection, Map<Integer, Peer> peers, Peer selfPeer) {
        this.connection = connection;
        this.peers = peers;
        this.selfPeer = selfPeer;
    }

    public void run() {
        try {
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(connection.getInputStream());

            Handshake handshake = new Handshake(-1);
            try {
                handshake = (Handshake) in.readObject();
            } catch (ClassNotFoundException e) {
                // TODO -: close connection ??
                e.printStackTrace();
            }

            Peer thisPeer = peers.get(handshake.getID());
            LoggerUtil.LogConnectedMsg(String.valueOf(thisPeer.getPeerId()));
            thisPeer.setConnectionHandler(this);

            if (peers.get(handshake.getID()) == null) {
                System.out.println("Error performing Handshake : PeerId unknown");
                // TODO -: close connection ??
            } else {
                System.out.println("Received Handshake Message : " + handshake.getID());
                this.connectingPeer = thisPeer;
                if (thisPeer.getSocket() == null) {
                    try {
                        // waiting for the client connection to be established
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Message bitFieldMessage = MessageGenerator.bitfield(FileManagementService.getBitField());
                sendMessage(bitFieldMessage);
                System.out.println("Sent bit field message of length" + bitFieldMessage.getMessageLength());
                while (thisPeer.getSocket() == null) {
                    Thread.sleep(1000);
                }
                this.thisPeerInputStream = new ObjectInputStream(thisPeer.getSocket().getInputStream());
                new Thread(this::listenToMessages).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Disconnect with client ");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Disconnect with Client ");
        }
    }

    public void sendMessage(Message msg) {
        try {
            out.writeObject(msg);
            out.flush();
            System.out.println("Send message: " + msg.getMessageType() + " to " + connectingPeer.getPeerId());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void listenToMessages() {
        while (!selfPeer.isCompletedDownloading()) {
            Message message = null;
            try {
                message = (Message) thisPeerInputStream.readObject();
                System.out.println("Receive message: " + message.getMessageType() + " from " + connectingPeer.getPeerId());

                switch (message.getMessageType()) {
                    case CHOKE:
                        isMeChocked = true;
                        LoggerUtil.LogReceivedChokingMsg(String.valueOf(connectingPeer.getPeerId()));
                        break;
                    case UNCHOKE:
                        isMeChocked = false;
                        LoggerUtil.LogReceivedUnchokingMsg(String.valueOf(connectingPeer.getPeerId()));
                        sendRequestMessage();
                        break;
                    case INTERESTED:
                        connectingPeer.setInterested(true);
                        LoggerUtil.LogReceivedInterestedMsg(String.valueOf(connectingPeer.getPeerId()));
                        break;
                    case NOT_INTERESTED:
                        connectingPeer.setInterested(false);
                        LoggerUtil.LogReceivedNotInterestedMsg(String.valueOf(connectingPeer.getPeerId()));
                        break;
                    case HAVE:
                        HavePayLoad haveIndex = (HavePayLoad) message.getPayload();
                        if (connectingPeer.getBitField() != null) {
                            connectingPeer.setBitField(FileUtil.updateBitfield(haveIndex.getIndex(), connectingPeer.getBitField()));
                            if (FileManagementService.getNumFilePieces() == connectingPeer.incrementAndGetNoOfPieces()) {
                                connectingPeer.setHasFile(true);
                                System.out.println("Peer completed Downloading :" + connectingPeer.getPeerId());
                                LoggerUtil.LogCompleteDownload(connectingPeer);
                                checkAllDownloaded();
                            }
                            LoggerUtil.LogReceivedHaveMsg(String.valueOf(connectingPeer.getPeerId()), haveIndex.getIndex());
                        }
                        break;
                    case BITFIELD:
                        BitFieldPayLoad bitFieldPayLoad = (BitFieldPayLoad) message.getPayload();
                        connectingPeer.setBitField(bitFieldPayLoad.getBitfield());
                        connectingPeer.setNoOfPiecesOwned(FileUtil.bitCount(bitFieldPayLoad.getBitfield()));
                        if (!FileManagementService.compareBitfields(bitFieldPayLoad.getBitfield(), selfPeer.getBitField())) {
                            System.out.println("Peer " + connectingPeer.getPeerId() + " have no any interesting pieces");
                            sendMessage(MessageGenerator.notInterested());
                        } else {
                            System.out.println("Peer " + connectingPeer.getPeerId() + " has interesting pieces");
                            sendMessage(MessageGenerator.interested());
                        }
                        break;
                    case REQUEST:
                        RequestPayLoad requestPayLoad = (RequestPayLoad) message.getPayload();
                        byte[] pieceContent = FileManagementService.getFilePart(requestPayLoad.getIndex());
                        sendMessage(MessageGenerator.piece(requestPayLoad.getIndex(), pieceContent));
                        connectingPeer.setDlSpeed(downloadSpeed++);
                        break;
                    case PIECE:
                        PiecePayLoad piece = (PiecePayLoad) message.getPayload();
                        try {
                            FileManagementService.store(piece.getContent(), piece.getIndex());
                            selfPeer.setBitField(FileManagementService.getBitField());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        peers.values().stream().filter(peer -> peer.getConnectionHandler() != null).forEach(peer -> peer.getConnectionHandler().sendMessage(MessageGenerator.have(piece.getIndex())));
                        if (!isMeChocked)
                            sendRequestMessage();
                        LoggerUtil.LogDownloadingPiece(String.valueOf(connectingPeer.getPeerId()), piece.getIndex(), connectingPeer.incrementAndGetNoOfPieces());
                        if (FileManagementService.hasCompleteFile()) {
                            System.out.println("My self completed Downloading :" + selfPeer.getPeerId());
                            selfPeer.setHasFile(true);
                            checkAllDownloaded();
                        }
                        break;
                }

            } catch (SocketException e){
                e.printStackTrace();
                break;
            }catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Exit listen messages for " + connectingPeer.getPeerId());
        cleanUp();
    }

    private void cleanUp(){
        try {
            out.close();
            thisPeerInputStream.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkAllDownloaded() {
        if (selfPeer.isHasFile() && checkAllPeersDownloaded()) {
            selfPeer.setCompletedDownloading(true);
        }
    }

    private boolean checkAllPeersDownloaded() {
        return peers.values().stream().filter(Peer::isHasFile).count() == peers.size();
    }

    private void sendRequestMessage() {
        int pieceIdx = FileManagementService.requestPiece(connectingPeer.getBitField(), selfPeer.getBitField(), connectingPeer.getPeerId());
        if (pieceIdx == -1) {
            System.out.println("No more interesting pieces to request from peer " + connectingPeer.getPeerId());
            sendMessage(MessageGenerator.notInterested());
        } else {
            sendMessage(MessageGenerator.request(pieceIdx));
        }
    }

}
