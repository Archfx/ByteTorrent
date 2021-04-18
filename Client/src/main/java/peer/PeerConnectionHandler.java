package peer;

import peer.message.Handshake;
import peer.message.Message;
import peer.message.MessageGenerator;

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
    private Message message;    //message received from the client
    private Socket connection;
    private ObjectInputStream in;	//stream read from the socket
    private ObjectOutputStream out;    //stream write to the socket
    private ObjectInputStream thisPeerInputStream;
    private Peer thisPeer;
    private PeerMangerService peerMangerService;

    public PeerConnectionHandler(Socket connection, Map<Integer, Peer> peers) {
        this.connection = connection;
        this.peers = peers;
    }

    public void run() {
        try{
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
            }
            else {
                System.out.println("Received Handshake Message : " + handshake.getID());
                this.thisPeer = thisPeer;
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
                System.out.println("Sent bit field message of length" + bitFieldMessage.getMessageLength() );

                this.thisPeerInputStream = new ObjectInputStream(thisPeer.getSocket().getInputStream());
                peerMangerService = new PeerMangerService( new ArrayList<>());
                try{
                    while(true)
                    {

                        // TODO -: Listen to Messages

                        //receive the message sent from the client
                        message = (Message)thisPeerInputStream.readObject();
                        //show the message to the user
                        System.out.println("Receive message: " + message.getMessageType());
                    }
                }
                catch(ClassNotFoundException classnot){
                    System.err.println("Data received in unknown format");
                }
            }

        }
        catch(IOException ioException){
            System.out.println("Disconnect with Client ");
        }
        finally{
            //Close connections
            try{
                in.close();
                out.close();
                connection.close();
            }
            catch(IOException ioException){
                System.out.println("Disconnect with Client ");
            }
        }
    }

    //send a message to the output stream
    public void sendMessage(Message msg)
    {
        try{
            out.writeObject(msg);
            out.flush();
            System.out.println("Send message: " + msg.getMessageType() + " to Client ");
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

}
