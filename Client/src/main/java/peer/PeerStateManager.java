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

    //	get IDs of connected peers sorted in speed
    // int[] getPeerId(ArrayList<Float> speeds) {

    //     float[] sortedNeighbors = (float[]) speeds.clone();
    //     Arrays.sort(sortedNeighbors);
    //     int[] indices = new int[speeds.size()];
    //     for (int i = 0; i < speeds.size(); i++) {
    //         indices[i] = Arrays.binarySearch(sortedNeighbors, speeds.get(i));
    //     }
    //     return indices;
    // }

//    k = prefered number of peers to unchoke
//    p = unchoking interval
//    m = optimally unchoke interval

    public List<Peer> choke (List<Peer> kNeighborPeers, int k){

        List<Peer> unchokeList = new ArrayList<Peer>();
        // List<Integer> peers = new ArrayList<Integer>();

        int num_peer = kNeighborPeers.size();
        // int ind = -1;
        // for (int i = 0; i < num_peer; i++) {
        //     peers.add(i);
        // }

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
                        // ind = peers.get(i); //index of the peer to be selected, selected randomly.
                        if ((kNeighborPeers.get(i)).getInterestedPeers() && (kNeighborPeers.get(i)).getPeerSockets() != null) {
                            unchokeList.add(kNeighborPeers.get(i));
                            nP++;
                        }
                    }

                } else {
                    //selecting based on the download speeds when file download is not complete.
                    // ArrayList<Float> dlSpeedOrdered = (ArrayList<Float>) kNeighborPeers.stream().map(Peer::getDlSpeed);
                    kNeighborPeers.sort(Comparator.comparing(Peer::getDlSpeed));
                    // selecting the peers
                    float nP = 0; // numbers of peers selected
                    for (int i = 0; i < num_peer && nP < k; i++) {
                        // ind = kNeighborPeers.get(i);
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
        // List<Integer> peers = new ArrayList<Integer>();

        int num_peer = kNeighborPeers.size();
        // int ind = -1;
        // for (int i = 0; i < num_peer; i++) {
        //     peers.add(i);
        // }
        while (chokeThreadRunning) {
            synchronized (lockMyNeighbors) { //lock the object

                if (kNeighborPeers == null) {
                    continue;
                }

//                int[] copy = (int[]) peers.clone();
//                Arrays.sort(copy);
                // Randomly selecting neighbors when download of file completed.
//				shuffle the list
                Collections.shuffle(kNeighborPeers);
                int nP = 0; // numbers of peers  to be selected selected
                for (int i = 0; i < num_peer && nP < 1; i++) {
                    // ind = peers.get(i); //index of the peer to be selected, selected randomly.
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

