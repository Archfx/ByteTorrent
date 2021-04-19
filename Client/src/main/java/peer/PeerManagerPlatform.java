package peer;

import config.CommonConfig;
import peer.file.FileController;
import peer.message.Handshake;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Comparator;
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

    private static boolean chokeThreadRunning = true ;

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
        System.out.println("Starting timers for choking");
        scheduledExecutorService.schedule(() -> choke((List<Peer>) peers.values()), CommonConfig.getUnchokingInterval(), TimeUnit.SECONDS);
        scheduledExecutorService.schedule(() -> chokeOpt((List<Peer>) peers.values()), CommonConfig.getOptimisticUnchokingInterval(), TimeUnit.SECONDS);
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

    public static void choke (List<Peer> allPeers){

        // List<Peer> unchokeList = new ArrayList<Peer>();

        int num_peer = allPeers.size();

        System.out.println(" Starting selecting k peers to send the file ");

        while (chokeThreadRunning) {
            synchronized (allPeers)
            { //lock the peerlist

                if (allPeers == null) {
                    continue;
                }

				// Randomly selecting neighbors when download of file completed.
                // if (myNode.isDoneDonwloading()) {
                if (FileController.hasCompleteFile()) {
//					shuffle the list
                    Collections.shuffle(allPeers);
                    int nP = 0; // numbers of peers selected
                    for (int i = 0; i < num_peer && nP < CommonConfig.getNumberOfdPreferredNeighbors(); i++) {
                        if ((allPeers.get(i)).isInterested() && (allPeers.get(i)).getPeerSockets() != null) {
                            // unchokeList.add(allPeers.get(i));
                            allPeers.get(i).setChoked(false);
                            nP++;
                        }

                    }
                }
                else {
                    // selecting based on the download speeds when file download is not complete.
                    allPeers.sort(Comparator.comparing(Peer::getDlSpeed));
                    // selecting the peers
                    float nP = 0; // numbers of peers selected
                    for (int i = 0; i < num_peer && nP < CommonConfig.getNumberOfdPreferredNeighbors(); i++) {
                        if ((allPeers.get(i)).isInterested() && (allPeers.get(i)).getAllPeerID() != null) {
                            // unchokeList.add(allPeers.get(i));
                            allPeers.get(i).setChoked(false);
                            nP++;
                        }
                        }
                    }

                }

            }

        // return unchokeList;
    }



    // public List<Peer> chokeOpt (List<Peer> allPeers){
    public static void chokeOpt (List<Peer> allPeers){


        // List<Peer> optUnchokeList = new ArrayList<Peer>();

        int num_peer = allPeers.size();

        System.out.println(" Starting optimum peer to send the file ");

        while (chokeThreadRunning) {
            // synchronized (lockMyNeighbors) { //lock the object
            synchronized (allPeers) {
                if (allPeers == null) {
                    continue;
                }

//				shuffle the list
                Collections.shuffle(allPeers);
                int nP = 0; // numbers of peers  to be selected selected
                for (int i = 0; i < num_peer && nP < 1; i++) {
                    if ((allPeers.get(i)).isInterested() && (allPeers.get(i)).getPeerSockets() != null) {
                        // optUnchokeList.add(allPeers.get(i));
                        allPeers.get(i).setChoked(false);
                        nP++;
                    }
                }
            }
        }

        // return optUnchokeList;
    }

}
