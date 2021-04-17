package peer;

import java.util.List;

public class PeerStateManger2 extends Peer{

    private List<Peer> kNeighborPeers;
    private ChokeManager chokeManager;

    public PeerStateManger2(Peer peer, List<Peer> kNeighborPeers) {
        super(peer);
        kNeighborPeers = kNeighborPeers;
        init();

    }

    private void init() {
        chokeManager = new ChokeManager();
    }

    public void start(){
        //TODO -: initializing connection and continue all the steps
        chokeManager.choke();
    }
}
