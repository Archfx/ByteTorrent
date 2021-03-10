import java.io.ObjectOutputStream;
import java.sql.ClientInfoStatus;

public class Peer implements Runnable{

    String hostIP = "";
    private Thread t = null;

    ObjectOutputStream[] tcpOut; //Stream to communicate between peers
    byte[] bitField;
    byte[][] neighborBitField;

    @Override
    public void run() {

    }

    // havePiece function

    // setBitField function

    // getBitField function

    // clearBitField function

    // sendRequest function

    // getPeerInfo

    // getConfig

    // handshake

    // getClientID() // get client ID by direction


    public Client getClientID(PeerDirection direction) {
        switch (direction){
            case Downloader:
                return myClient;
            case Uploader:
                return peerCleint;
        }
    }

    public void choke(PeerDirection direction, boolean choke){

        Client C = getClientID(direction);

        if (choke) C.choke();
        else C.unchoke();

        if (direction == PeerDirection.uploader){
            if (choke) C.sendMessage(new ChokeMsg());
            else C.sendMessage(new UnchokeMsg());
        }


    }


}
