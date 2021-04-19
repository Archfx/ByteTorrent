package peer;

import peer.file.FileController;
import peer.file.FileUtils;
import peer.message.Handshake;
import peer.message.Message;
import peer.message.MessageGenerator;
import peer.message.payload.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

/**
 * A handler thread class.  Handlers are spawned from the listening
 * loop and are responsible for dealing with a single client's requests.
 */
public class PeerConnectionHandler extends Thread {
    private final Map<Integer, Peer> peers;
    private final Socket connection;
    private ObjectInputStream in;    //stream read from the socket
    private ObjectOutputStream out;    //stream write to the socket
    private ObjectInputStream thisPeerInputStream;
    private Peer connectingPeer;
    private boolean isMeChocked;
    private Peer selfPeer;

    public PeerConnectionHandler(Socket connection, Map<Integer, Peer> peers, Peer selfPeer) {
        this.connection = connection;
        this.peers = peers;
        this.selfPeer = selfPeer;
    }

    public void run() {
        try {
            //initialize Input and Output streams
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());

            Handshake handshake = new Handshake(-1);
            try {
                handshake = (Handshake) in.readObject();
            } catch (ClassNotFoundException e) {
                // TODO -: close connection
                e.printStackTrace();
            }

            Peer thisPeer = peers.get(handshake.getID());
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

                Message bitFieldMessage = MessageGenerator.bitfield(FileController.getBitField());
                sendMessage(bitFieldMessage);
                System.out.println("Sent bit field message of length" + bitFieldMessage.getMessageLength());

                this.thisPeerInputStream = new ObjectInputStream(thisPeer.getSocket().getInputStream());
                new Thread(this::listenToMessages).start();
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
            System.out.println("Disconnect with Client ");
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
            while (true) {
                Message message = null;
                try {
                    message = (Message) thisPeerInputStream.readObject();
                    System.out.println("Receive message: " + message.getMessageType() + " from " + connectingPeer.getPeerId());

                    switch (message.getMessageType()) {
                        case CHOKE:
                            isMeChocked = true;
                            break;
                        case UNCHOKE:
                            isMeChocked = false;
                            sendRequestMessage();
                            break;
                        case INTERESTED:
                            connectingPeer.setInterested(true);
                            break;
                        case NOT_INTERESTED:
                            connectingPeer.setInterested(false);
                            break;
                        case HAVE:
                            HavePayLoad haveIndex = (HavePayLoad) message.getPayload();
                            FileUtils.updateBitfield(haveIndex.getIndex(), connectingPeer.getBitField());
                            if (!FileController.isInteresting(haveIndex.getIndex())) {
                                System.out.println("Peer " + connectingPeer.getPeerId() + " has interesting pieces");
                                ;
                                sendMessage(MessageGenerator.interested());
                            }
                            break;
                        case BITFIELD:
                            BitFieldPayLoad bitFieldPayLoad = (BitFieldPayLoad) message.getPayload();
                            connectingPeer.setBitField(bitFieldPayLoad.getBitfield());
                            if (!FileController.compareBitfields(bitFieldPayLoad.getBitfield(), selfPeer.getBitField())) {
                                System.out.println("Peer " + connectingPeer.getPeerId() + " have no any interesting pieces");
                                sendMessage(MessageGenerator.notInterested());
                            } else {
                                System.out.println("Peer " + connectingPeer.getPeerId() + " has interesting pieces");
                                sendMessage(MessageGenerator.interested());
                            }
                            break;
                        case REQUEST:
                            RequestPayLoad requestPayLoad = (RequestPayLoad) message.getPayload();
                            byte[] pieceContent = FileController.getFilePart(requestPayLoad.getIndex());
                            // TODO -: check this ??? request index is same as file index
                            sendMessage(MessageGenerator.piece(requestPayLoad.getIndex(), pieceContent));
                            break;
                        case PIECE:
                            PiecePayLoad piece = (PiecePayLoad) message.getPayload();
                            try {
                                FileController.store(piece.getContent(), piece.getIndex());
                            } catch (Exception e) {
                                // TODO: handle exception
                                e.printStackTrace();
                            }

                            try {
                                selfPeer.setBitField(FileController.getBitField());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            peers.values().stream().filter(Peer::isInterested).forEach(peer -> peer.getConnectionHandler().sendMessage(MessageGenerator.have(piece.getIndex())));
//                        piecesDownloaded++;

                            if (!isMeChocked)
                                sendRequestMessage();
                            break;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }

    }

    private void sendRequestMessage() {
        int pieceIdx = FileController.requestPiece(connectingPeer.getBitField(), selfPeer.getBitField(), connectingPeer.getPeerId());
        if (pieceIdx == -1) {
            System.out.println("No more interesting pieces to request from peer " + connectingPeer.getPeerId());
            sendMessage(MessageGenerator.notInterested());
        } else {
            sendMessage(MessageGenerator.request(pieceIdx));
        }
    }

}
