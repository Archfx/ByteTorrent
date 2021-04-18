package peer;

import config.CommonConfig;
import java.util.HashMap;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectOutputStream;

import h.Handshake;

public class PeerProcessThread extends Peer implements Runnable {
    private final CommonConfig cConfig;
    private static HashMap<Integer, Peer> peers = new HashMap<Integer, Peer>();
    private ServerSocket socket;
    private final PeerStateManger peerManager;

    public PeerProcessThread(int id, String address, int port, boolean hasFile, CommonConfig cConfig) {
        super(id, address, port, hasFile);
        this.cConfig = cConfig;
    }

    @Override
    public void run() {
        System.out.println("Starting peer " + this.getId());
        
        // TODO: handle file status

        try {
            socket = new ServerSocket(this.getPort());
            System.out.println("Created server for " + this.getAddress());
        }
        catch (Exception e) {}

        this.startServer();
        this.startClient();

    }
    public void init() {
        Thread t = new Thread(this);
		t.setName("peerProcess-" + this.getId());
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
            Socket s = new Socket(p.getAddress(), p.getPort());
	    ObjectOutputStream o = new ObjectOutputStream(s.getOutputStream()); o.flush();
            o.writeObject(new Handshake(getId()));
            o.flush();
            o.reset();
            // TODO: Set socket and peer
        }
        }
        catch (Exception e) {}
    }

    ///
    /// Start the client friend that will continue sending handshake messages
    ///
    public void startClient() {
        new Thread {
            public void run() {
                while (true) {
                    try {
                        for (Peer p: peers.valus()) {
                            Socket s = new Socket(p.getAddress(), p.getPort());
                            // TODO: send handshake message

                            p.setSocket(s);
                            p.setUp(true);
                        }
                        // Sleep to not spam
                        Thread.sleep(60000);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    

                }

            }

        }.start();
    }
    public void startConnection() throws Exception {
        Socket ls = socket.accept();

        // Get the handshake message from socket 

        // TODO: parse the handshake from sender
        Handshake handshake = new Handshake(0);

        if (peers.get(handshake.getID()) == null) { throw new Exception(); }
    }

    public void stop() {
        // TODO: This should check if all peers have complete file and then stop
    }

}
