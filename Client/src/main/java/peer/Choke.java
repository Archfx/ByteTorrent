package peer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Choke implements Runnable {
    //Choke algorithm to select neighbours interested in data based on the download rates of them.

    Peer P;
    private Thread t = null;

    //	get IDs of connected peers
    int[] getPeerId(float[] peerID) {
        int l = peerID.length;
        float[] sortedPeerID = peerID.clone();
        Arrays.sort(sortedPeerID);
        int[] indices = new int[l];
        for (int index = 0; index < l; index++) {
            indices[index] = Arrays.binarySearch(sortedPeerID, peerID[index]);
        }
        return indices;
    }

    @Override
    public void run() {
        // multithreading interface
        List<Integer> list = new ArrayList<Integer>();
        int num_peer = P.psM.allPeerID.length;//port;//.length;
        int ind = -1;
        for (int i = 0; i < num_peer; i++) {
            list.add(i);
        }
        while (P.choked) {
            synchronized (P.psM.lockMyNeighbors) { //lock the object

                if (P.psM.neighbors == null) {
                    continue;
                }

                int[] cn = P.psM.neighbors.clone();  //current neighbor.
                Arrays.sort(cn);
//				Randmly selecting neighbors
                if (P.psM.isMyFile || P.psM.dlFinished) {
//					shuffle the list
                    java.util.Collections.shuffle(list);
                    int cnt = 0;
                    for (int i = 0; i < num_peer && cnt < P.psM.numNeighbors; i++) {
                        ind = list.get(i); //index of the peer to be selected.
                        if (P.psM.isInterested[ind] && P.psM.allPeerSockets[ind] != null) {
                            P.psM.neighbors[cnt++] = P.psM.allPeerID[ind];
                            //send unchoke message to previously choked peer.
                            if (Arrays.binarySearch(cn, P.allPeerID[ind]) < 0) {  ///not int the previous neighbor list.
//								Message msg = new Message();
//								msg.length = 0; //no payload.
//								msg.type = 1; //unchoke.
//								msg.payload = null; //no payload.
//								P.print("Unchoke " + P.allPeerID[ind] + " randomly.");
//								P.send(P.allPeerID[ind], msg);
                            }
                        }
                    }

                } else {
                    //selecting based on the download speeds.
                    int[] order = getPeerId(P.dlSpeed);
//					selected
                    int cnt = 0;
                    for (int i = 0; i < num_peer && cnt < P.psM.numNeighbors; i++) {
                        ind = order[i];
                        if (P.psM.isInterested[ind] && P.psM.allPeerID[ind] != P.id) {
                            P.psM.neighbors[cnt++] = P.psM.allPeerID[ind];
//							sending the unchoke msg to previously choked peer
                            if (Arrays.binarySearch(cn, P.psM.allPeerID[ind]) < 0) {
//								Message msg = new Message();
//								msg.length = 0; //no payload.
//								msg.type = 1; //unchoke.
//								msg.payload = null; //no payload.
//								P.send(P.allPeerID[ind], msg);
//								P.print("Based on downloading rate peer " + P.allPeerID[ind] + " unchoked.");
                            }
                        }
                    }

                }
//				sending the choke message to neighbors based on their history
                Arrays.sort(Arrays.copyOfRange(P.psM.neighbors, 0, P.psM.numNeighbors));
                for (int i = 0; i < P.psM.numNeighbors; i++) {
                    if (Arrays.binarySearch(P.psM.neighbors, cn[i]) < 0) {
//						Message msg = new Message();
//						msg.length = 0; //no payload.
//						msg.type = 0; //choke.
//						msg.payload = null; //no payload.
//						P.send(P.allPeerID[ind], msg);
                    }
                }
            }
//			//print log
//			if(P.neighbors[0]!=-1){
//				String neighbor_list = "";
//				for(int i = 0;i<P.neighbors.length-1;i++) neighbor_list += P.neighbors[i]+" ";
//				P.log("Peer ["+P.id+"] has the preferred neighbors [ "+neighbor_list+"].");
//			}

            try {
                Thread.sleep(1000 * P.psM.unchokeInterval);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void start(Peer p) { // starting the thread
        P = p;
        if (t == null) {
            t = new Thread(this, "choke thread for peer : " + P.id);
            t.start();
        }
    }
}

