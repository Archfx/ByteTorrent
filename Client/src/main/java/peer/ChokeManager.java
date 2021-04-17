package peer;

import java.util.ArrayList;
import java.util.List;

public class ChokeManager {

    private boolean chokeThreadRunning ;
    private Object lockMyNeighbors;

    peer munode = PeerStateManagerPlatform.my_node;
    // TODO -: choking unckoking logic goes here

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
}
