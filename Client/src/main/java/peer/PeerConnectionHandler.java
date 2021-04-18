package peer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * A handler thread class.  Handlers are spawned from the listening
 * loop and are responsible for dealing with a single client's requests.
 */
public class PeerConnectionHandler extends Thread {
    private String message;    //message received from the client
    private String MESSAGE;    //uppercase message send to the client
    private Socket connection;
    private ObjectInputStream in;	//stream read from the socket
    private ObjectOutputStream out;    //stream write to the socket
    private int no;		//The index number of the client

    public PeerConnectionHandler(Socket connection, int no) {
        this.connection = connection;
        this.no = no;
    }

    public void run() {
        try{
            //initialize Input and Output streams
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());
            try{
                while(true)
                {
                    //receive the message sent from the client
                    message = (String)in.readObject();
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
