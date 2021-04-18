package peer;

import peer.message.Handshake;

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
    private String message;    //message received from the client
    private String MESSAGE;    //uppercase message send to the client
    private Socket connection;
    private ObjectInputStream in;	//stream read from the socket
    private ObjectOutputStream out;    //stream write to the socket
    private ObjectInputStream thisPeerInputStream;
    private int no;		//The index number of the client
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
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                this.thisPeerInputStream = new ObjectInputStream(thisPeer.getSocket().getInputStream());
                peerMangerService = new PeerMangerService( new ArrayList<>());
                try{
                    while(true)
                    {

                        // TODO -: Listen to Messages

                        //receive the message sent from the client
                        message = (String)thisPeerInputStream.readObject();
                        //show the message to the user
                        System.out.println("Receive message: " + message + " from client " + no);
                        //Capitalize all letters in the message
                        MESSAGE = message.toUpperCase();
                        //send MESSAGE back to the client
                        sendMessage(MESSAGE);
                    }
                }
                catch(ClassNotFoundException classnot){
                    System.err.println("Data received in unknown format");
                }
            }

        }
        catch(IOException ioException){
            System.out.println("Disconnect with Client " + no);
        }
        finally{
            //Close connections
            try{
                in.close();
                out.close();
                connection.close();
            }
            catch(IOException ioException){
                System.out.println("Disconnect with Client " + no);
            }
        }
    }

    //send a message to the output stream
    public void sendMessage(String msg)
    {
        try{
            out.writeObject(msg);
            out.flush();
            System.out.println("Send message: " + msg + " to Client " + no);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

}
