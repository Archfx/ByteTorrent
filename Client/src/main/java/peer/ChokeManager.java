package peer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.sound.midi.Soundbank;

public class ChokeManager {

    private boolean chokeThreadRunning ;
    private Object lockMyNeighbors;

    Peer myNode = PeerStateManagerPlatform.my_node;

    // TODO -: choking unckoking logic goes here

    //    k = prefered number of peers to unchoke
    //    p = unchoking interval
    //    m = optimally unchoke interval

    public List<Peer> choke (List<Peer> kNeighborPeers, int k){

        List<Peer> unchokeList = new ArrayList<Peer>();

        int num_peer = kNeighborPeers.size();

        System.out.println(" Starting choke ");

        while (chokeThreadRunning) {
            synchronized (lockMyNeighbors) { //lock the object

                if (kNeighborPeers == null) {
                    continue;
                }

				// Randomly selecting neighbors when download of file completed.
                if (myNode.isDoneDonwloading()) {
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
                    // selecting based on the download speeds when file download is not complete.
                    kNeighborPeers.sort(Comparator.comparing(Peer::getDlSpeed));
                    // selecting the peers
                    float nP = 0; // numbers of peers selected
                    for (int i = 0; i < num_peer && nP < k; i++) {
                        if ((kNeighborPeers.get(i)).getInterestedPeers() && (kNeighborPeers.get(i)).getAllPeerID() != null) { 
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
}

