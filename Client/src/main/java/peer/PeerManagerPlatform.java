package peer;

import config.CommonConfig;
import peer.message.Handshake;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class PeerManagerPlatform extends Peer {
    private ScheduledExecutorService scheduledExecutorService;

    private final CommonConfig cConfig;
    private Map<Integer, Peer> peers;
    private ServerSocket socket;

    public PeerManagerPlatform(Peer mySelf, Map<Integer, Peer> remotePeers, CommonConfig cConfig) {
        super(mySelf.getPeerId(), mySelf.getAddress(), mySelf.getPort(), mySelf.isHasFile());
        this.scheduledExecutorService = Executors.newScheduledThreadPool(2);
        this.cConfig = cConfig;
        this.peers = remotePeers;
    }

    public void init() {
        System.out.println("Starting peer " + this.getPeerId());

        // TODO: handle file status

        try {
            socket = new ServerSocket(this.getPort());
            System.out.println("Created server for " + this.getAddress() + ":" + this.getPort());
        } catch (IOException e) {
        }

        this.initServer();
        this.initClient();
        scheduledExecutorService.schedule(() -> ChokeManager.choke((List<Peer>) peers.values()), 1, TimeUnit.SECONDS);
        scheduledExecutorService.schedule(() -> ChokeManager.chokeOpt((List<Peer>) peers.values()), 1, TimeUnit.SECONDS);
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
                System.out.println("Initializing client");
                while (true) {
                    try {
                        for (Peer peer : peers.values()) {
                            if (!peer.isUp()) {
                                Socket s = new Socket(peer.getAddress(), peer.getPort());
                                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                                out.flush();
                                out.writeObject(new Handshake(getPeerId()));
                                out.flush();
                                out.reset();
                                System.out.println("Handshake Message sent to peer " + peer.getPeerId() + " from" + getPeerId());
                                peer.setSocket(s);
                                peer.setUp(true);
                            }
                            // Sleep to not spam
                            Thread.sleep(10000);
                        }
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (ConnectException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
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
