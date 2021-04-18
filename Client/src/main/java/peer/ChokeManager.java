package peer;

import config.CommonConfig;
import peer.file.FileController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChokeManager {

    private static boolean chokeThreadRunning = true ;
    // private Object lockMyNeighbors;

//    Peer myNode = PeerManagerPlatform.my_node;

    

    // TODO -: choking unckoking logic goes here

    //    k = prefered number of peers to unchoke
    //    p = unchoking interval
    //    m = optimally unchoke interval

    // public List<Peer> choke (List<Peer> allPeers){
    public static void choke (List<Peer> allPeers){

        List<Peer> unchokeList = new ArrayList<Peer>();

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

