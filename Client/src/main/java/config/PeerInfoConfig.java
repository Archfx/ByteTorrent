package config;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;

public class PeerInfoConfig {
    public static final String FILE_NAME = "PeerInfo.cfg";
    private ArrayList<RemotePeerInfo> peerInfoList = new ArrayList<RemotePeerInfo>();
    private static PeerInfoConfig peerInfoConfig = null;


    private PeerInfoConfig() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
            String readLine = "";
            while ((readLine = reader.readLine()) != null) {
                String config[] = readLine.split(" ");
                int id = Integer.parseInt(config[0]);
                String address = config[1];
                int port = Integer.parseInt(config[2]);
                boolean hasFile = false;
                if ("1".equals(config[3])) {
                    hasFile = true;
                }
                peerInfoList.add(new RemotePeerInfo(id, address, port, hasFile));

            }
            reader.close();
            }
        catch (Exception e){
            System.out.println("ERROR: Cannot find PeerInfo.cfg file");
        }

//        // Hardcode for now
//        peerInfoList.add(new RemotePeerInfo(1001, "lin114-00.cise.ufl.edu", 6001, true));
//        peerInfoList.add(new RemotePeerInfo(1002, "lin114-01.cise.ufl.edu", 6001, false));
//        peerInfoList.add(new RemotePeerInfo(1003, "lin114-02.cise.ufl.edu", 6001, false));
//        peerInfoList.add(new RemotePeerInfo(1004, "lin114-03.cise.ufl.edu", 6001, false));
//        peerInfoList.add(new RemotePeerInfo(1005, "lin114-04.cise.ufl.edu", 6001, false));
//        peerInfoList.add(new RemotePeerInfo(1006, "lin114-05.cise.ufl.edu", 6001, true));
//        peerInfoList.add(new RemotePeerInfo(1007, "lin114-06.cise.ufl.edu", 6001, false));
//        peerInfoList.add(new RemotePeerInfo(1008, "lin114-07.cise.ufl.edu", 6001, false));
//        peerInfoList.add(new RemotePeerInfo(1009, "lin114-08.cise.ufl.edu", 6001, false));
    }

    public static PeerInfoConfig getInstance()
    {
        if (peerInfoConfig == null)
            peerInfoConfig = new PeerInfoConfig();

        return peerInfoConfig;
    }

    public ArrayList<RemotePeerInfo> getPeerInfoList() {
        return peerInfoList;
    }
}