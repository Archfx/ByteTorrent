package peer;

import java.util.List;

/**
 * Peer is the peer related to this specific state manager
 */
public class PeerStateManger extends Peer {

    private List<Peer> kNeighborPeers;
    private ChokeManager chokeManager;

    public PeerStateManger(Peer thisPeer, List<Peer> kNeighborPeers) {
        super(thisPeer);
        this.kNeighborPeers = kNeighborPeers;
        init();

    }

    private void init() {
        chokeManager = new ChokeManager();
    }

    public void start(){
        //TODO -: initializing connection and continue all the steps
//        chokeManager.choke();
    }
}
