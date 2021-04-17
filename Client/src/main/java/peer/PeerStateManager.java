package peer;

import peer.message.Message;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Deprecated
public class PeerStateManager {
//    private static ScheduledExecutorService scheduler;
    private ServerSocket ssocket;
    private Peer hostPeer;
    private HashMap<Integer, Peer> peers;

    private static ArrayList<Peer> kNeighborPeers;
    // private static ArrayList<Boolean> interestedPeers;
    // private static ArrayList<Peer> unChoked;

    // private static ArrayList<Float> dlSpeed;
    // private static ArrayList<Integer> allPeerID;
    // private static ArrayList<Socket> peerSockets;

    private Object lockMyNeighbors;

    private boolean isMyFile;
    private boolean dlFinished;
    private int unchokeInterval;
    private int unchokeOptInterval;

    private boolean chokeThreadRunning ;

    // private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

//    public int[] allPeerID;
//    public Socket[] peerSockets;

    public PeerStateManager(ServerSocket ssocket, Peer hostPeer, HashMap<Integer, Peer> peers) {
		this.ssocket = ssocket;
		this.peers = peers;
		this.hostPeer = hostPeer;
	}



    //    k = prefered number of peers to unchoke
    //    p = unchoking interval
    //    m = optimally unchoke interval

    public List<Peer> choke (List<Peer> kNeighborPeers, int k){

        List<Peer> unchokeList = new ArrayList<Peer>();

        int num_peer = kNeighborPeers.size();


        while (chokeThreadRunning) {
            synchronized (lockMyNeighbors) { //lock the object

                if (kNeighborPeers == null) {
                    continue;
                }

				// Randomly selecting neighbors when download of file completed.
                if (isMyFile || dlFinished) {
//					shuffle the list
                    Collections.shuffle(kNeighborPeers);
                    int nP = 0; // numbers of peers selected
                    for (int i = 0; i < num_peer && nP < k; i++) {
                        if ((kNeighborPeers.get(i)).getInterestedPeers() && (kNeighborPeers.get(i)).getPeerSockets() != null) {
                            unchokeList.add(kNeighborPeers.get(i));
                            nP++;
                        }
                    }

                } else {
                    //selecting based on the download speeds when file download is not complete.
                    kNeighborPeers.sort(Comparator.comparing(Peer::getDlSpeed));
                    // selecting the peers
                    float nP = 0; // numbers of peers selected
                    for (int i = 0; i < num_peer && nP < k; i++) {
                        if ((kNeighborPeers.get(i)).getInterestedPeers() && (kNeighborPeers.get(i)).getAllPeerID() != null) { //change this after merge
                            unchokeList.add(kNeighborPeers.get(i));
                            nP++;
                        }
                        }
                    }

                }

            }

        return unchokeList;
    }



    public List<Peer> chokeOpt (List<Peer> kNeighborPeers, int k){

        List<Peer> optUnchokeList = new ArrayList<Peer>();

        int num_peer = kNeighborPeers.size();

        while (chokeThreadRunning) {
            synchronized (lockMyNeighbors) { //lock the object

                if (kNeighborPeers == null) {
                    continue;
                }

//				shuffle the list
                Collections.shuffle(kNeighborPeers);
                int nP = 0; // numbers of peers  to be selected selected
                for (int i = 0; i < num_peer && nP < 1; i++) {
                    if ((kNeighborPeers.get(i)).getInterestedPeers() && (kNeighborPeers.get(i)).getPeerSockets() != null) {
                        optUnchokeList.add(kNeighborPeers.get(i));
                        nP++;
                    }
                }

            }

        }

        return optUnchokeList;
    }


    public List<Peer> requestPiece (List<Peer> neighbors){
        return null;
    }

    public List<Peer> handshake (List<Peer> neighbors){
        return null;
    }

    public List<Peer> bitField (List<Peer> neighbors){
        return null;
    }

    public List<Peer> isInterested (List<Peer> neighbors){
        return null;
    }

    private void sendMessage(Message message, Peer peer){

    }

    public void main(String[] args) {
        // scheduler.scheduleAtFixedRate((Runnable) choke( kNeighborPeers,4), 0, unchokeInterval, TimeUnit.SECONDS);
        // scheduler.scheduleAtFixedRate((Runnable) chokeOpt( kNeighborPeers,4), 0, unchokeOptInterval, TimeUnit.SECONDS);

    }
}

