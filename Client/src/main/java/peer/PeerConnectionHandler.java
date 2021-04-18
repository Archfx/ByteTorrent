package peer;

import peer.file.FileUtils;
import peer.message.Handshake;
import peer.message.Message;
import peer.message.MessageGenerator;
import peer.message.payload.HavePayLoad;
import peer.message.payload.PayLoad;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
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
    private PeerMangerService peerMangerService;
    private boolean isMeChocked;

    public PeerConnectionHandler(Socket connection, Map<Integer, Peer> peers) {
        this.connection = connection;
        this.peers = peers;
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

                // TODO -: use file manager to get bit field
                Message bitFieldMessage = MessageGenerator.bitfield(new byte[]{});
                sendMessage(bitFieldMessage);
                System.out.println("Sent bit field message of length" + bitFieldMessage.getMessageLength());

                this.thisPeerInputStream = new ObjectInputStream(thisPeer.getSocket().getInputStream());
                peerMangerService = new PeerMangerService(new ArrayList<>());
                new Thread(this::listenToMessages).start();
                new Thread(this::requestPieces).start();
                new Thread(this::sendChockeUnchoke).start();
            }

        } catch (IOException ioException) {
            System.out.println("Disconnect with Client ");
        } finally {
            //Close connections
            try {
                in.close();
                out.close();
                connection.close();
            } catch (IOException ioException) {
                System.out.println("Disconnect with Client ");
            }
        }
    }

    private void sendMessage(Message msg) {
        try {
            out.writeObject(msg);
            out.flush();
            System.out.println("Send message: " + msg.getMessageType() + " to Client ");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void listenToMessages() {
        try {
            while (true) {
                Message message = (Message) thisPeerInputStream.readObject();
                System.out.println("Receive message: " + message.getMessageType() + " from " + connectingPeer.getPeerId());

                switch (message.getMessageType()) {
                    case CHOKE:
                        isMeChocked = true;
                        break;
                    case UNCHOKE:
                        isMeChocked = false;
                        break;
                    case INTERESTED:
                        connectingPeer.setInterested(true);
                        break;
                    case NOT_INTERESTED:
                        connectingPeer.setInterested(false);
                        break;
                    case HAVE:
                        HavePayLoad haveIndex = (HavePayLoad)message.getPayload();
                        FileUtils.updateBitfield(haveIndex.getIndex(), connectingPeer.getBitField());
                        break;
                    case BITFIELD:
                        break;
                    case REQUEST:
                        break;
                    case PIECE:
                        break;
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            System.err.println("Data received in unknown format");
        }
    }

    private void requestPieces() {

    }

    private void sendChockeUnchoke() {

    }
}
