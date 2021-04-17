package peer;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PeerStateManagerPlatform {

    private List<Peer> kNeighborPeers;

    private ExecutorService executorService ;

    public static Peer my_node;

    public PeerStateManagerPlatform(Peer my_node, ArrayList<Peer> remotePeers) {
        my_node = my_node;
        executorService = Executors.newFixedThreadPool(remotePeers.size());
		//TODO  -: convert remote peers and initialize kNeighborPeers list
	}


    public void init() {
        kNeighborPeers.forEach(neighborPeer-> {
            executorService.submit(() -> {
                System.out.println(" Starting thread for peer :" + neighborPeer.getId());
                PeerStateManger peerStateManager = new PeerStateManger(neighborPeer, kNeighborPeers);
                peerStateManager.start();
            });
        });
    }
}

