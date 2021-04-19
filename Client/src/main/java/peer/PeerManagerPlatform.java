package peer;

import config.CommonConfig;
import peer.service.ChokeManagementService;
import peer.service.FileManagementService;
import peer.message.Handshake;
import util.LoggerUtil;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;


public class PeerManagerPlatform extends Peer {

    private final CommonConfig cConfig;
    public Map<Integer, Peer> peers;
    public static Peer my_self;
    private ServerSocket socket;
    
    ChokeManagementService myCM = new ChokeManagementService();

    public PeerManagerPlatform(Peer mySelf, Map<Integer, Peer> remotePeers, CommonConfig cConfig) {
        super(mySelf.getPeerId(), mySelf.getAddress(), mySelf.getPort(), mySelf.isHasFile());
        this.cConfig = cConfig;
        this.peers = remotePeers;
        this.my_self = mySelf;
    }

    public void init() {
        System.out.println("Starting peer " + this.getPeerId());

        new FileManagementService(this.getPeerId(), this.isHasFile());

        try {
            this.setBitField(FileManagementService.getBitField());
            socket = new ServerSocket(this.getPort());
            System.out.println("Created server for " + this.getAddress() + ":" + this.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.initServer();
        // myCM.choke( new ArrayList<Peer>(peers.values()));
        this.initClient();
        System.out.println("Starting timers for choking || 1: "+CommonConfig.getUnchokingInterval()+"||2 :"+CommonConfig.getOptimisticUnchokingInterval());

        (new Thread() {
            @Override
            public void run() {
                while (true) {
                    myCM.choke( new ArrayList<Peer>(peers.values()));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        (new Thread() {
            @Override
            public void run() {
                while (true) {
                    myCM.chokeOpt( new ArrayList<Peer>(peers.values()));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
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
//                        e.printStackTrace();
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
                new PeerConnectionHandler(socket.accept(), peers, (Peer)this).start();
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
