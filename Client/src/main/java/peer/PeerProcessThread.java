package peer;

import config.CommonConfig;
import peer.Peer;
import java.util.HashMap;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import h.Handshake;

@Deprecated
public class PeerProcessThread extends Peer implements Runnable {
    private final CommonConfig cConfig;
    private static HashMap<Integer, Peer> peers = new HashMap<Integer, Peer>();
    private ServerSocket socket;

    public PeerProcessThread(int id, String address, int port, boolean hasFile, CommonConfig cConfig) {
        super(id, address, port, hasFile);
        this.cConfig = cConfig;
    }

    @Override
    public void run() {
        System.out.println("Starting peer " + this.id);
        // TODO: Start file sharing 
    }
    public void init() {
        Thread t = new Thread(this);
		t.setName("peerProcess-" + this.id);
		t.start();
    }

    public void startServer() {
        (new Thread() {
	    @Override
	        public void run() {
		    while (!socket.isClosed()) {
			try { startConnection();}
                        catch (Exception e) {}
		    }
		}
	}).start();

    }
    public void startBroadcast() {
        try {
        // TODO: This should send handshake message to neighboring peers and check bitfields
        for (Peer p : peers.values()) {
            Socket s = new Socket(p.address, p.port);
	    ObjectOutputStream o = new ObjectOutputStream(s.getOutputStream()); o.flush();
            o.writeObject(new Handshake(id));
            o.flush();
            o.reset();
            // TODO: Set socket and peer
        }
        }
        catch (Exception e) {}
    }

    public void startConnection() {
        // TODO: This should create a connection between host and neighboring peers
    }

    public void stop() {
        // TODO: This should check if all peers have complete file and then stop 
    }

}
