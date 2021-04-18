package peer;

import config.CommonConfig;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;


public class PeerManagerPlatform extends Peer {
    private final CommonConfig cConfig;
    private static Map<Integer, Peer> peers;
    private ServerSocket socket;

    public PeerManagerPlatform(Peer mySelf, CommonConfig cConfig) {
        super(mySelf.getId(), mySelf.getAddress(), mySelf.getPort(), mySelf.isHasFile());
        this.cConfig = cConfig;
    }

    public void init() {
        System.out.println("Starting peer " + this.getId());

        // TODO: handle file status

        try {
            socket = new ServerSocket(this.getPort());
            System.out.println("Created server for " + this.getAddress());
        } catch (IOException e) {
        }

        this.initServer();
        this.initClient();
    }


    public void initServer() {
        (new Thread() {
            @Override
            public void run() {
                while (!socket.isClosed()) {
                    startListening();
                }
            }
        }).start();

    }

    public void initClient() {
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        for (Peer p : peers.values()) {
                            Socket s = new Socket(p.getAddress(), p.getPort());
                            // TODO: send handshake message

                            p.setSocket(s);
                            p.setUp(true);
                        }
                        // Sleep to not spam
                        Thread.sleep(10000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            ;
        };

    }

    private void startListening() {
        int clientNum = 1;
        try {
            try {
                new PeerConnectionHandler(socket.accept(), peers).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Client " + clientNum + " is connected!");
            clientNum++;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
