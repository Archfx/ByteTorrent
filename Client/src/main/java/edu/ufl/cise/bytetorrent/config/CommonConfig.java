package edu.ufl.cise.bytetorrent.config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class CommonConfig {
    private static CommonConfig commonConfig = null;
    private static int NumberOfPreferredNeighbors;
    private static int UnchokingInterval;
    private static int OptimisticUnchokingInterval;
    private static String FileName ;
    private static int FileSize ;
    private static int PieceSize;

    private HashMap<String, String> info_list = new HashMap<>();
    private static final String FILE_NAME ="Common.cfg";

    private CommonConfig()
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
            String readLine = "";
            while ((readLine = reader.readLine()) != null) {
                String config[] = readLine.split(" ");
                info_list.put(config[0], config[1]);
            }
            reader.close();
            NumberOfPreferredNeighbors = Integer.parseInt(info_list.get("NumberOfPreferredNeighbors"));
            UnchokingInterval = Integer.parseInt(info_list.get("UnchokingInterval"));
            OptimisticUnchokingInterval = Integer.parseInt(info_list.get("OptimisticUnchokingInterval"));
            FileName = info_list.get("FileName");
            FileSize = Integer.parseInt(info_list.get("FileSize"));
            PieceSize = Integer.parseInt(info_list.get("PieceSize"));
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Cannot find Common.cfg file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CommonConfig initialize()
    {
        if (commonConfig == null)
            commonConfig = new CommonConfig();

        return commonConfig;
    }

    public static int getNumberOfdPreferredNeighbors() {
        return NumberOfPreferredNeighbors;
    }

    public static int getUnchokingInterval() {
        return UnchokingInterval;
    }

    public static int getOptimisticUnchokingInterval() {
        return OptimisticUnchokingInterval;
    }

    public static String getFileName() {
        return FileName;
    }

    public static int getFileSize() {
        return FileSize;
    }

    public static int getPieceSize() {
        return PieceSize;
    }

//    public static void main(String[] args) throws Exception {
//        CommonConfig edu.ufl.cise.bytetorrent.config = CommonConfig.getInstance();
//        System.out.println("Reading Common edu.ufl.cise.bytetorrent.config\n");
//        System.out.println("NumberOfPreferredNeighbors : " + edu.ufl.cise.bytetorrent.config.getNumberOfdPreferredNeighbors());
//        System.out.println("UnchokingInterval : " + edu.ufl.cise.bytetorrent.config.getUnchokingInterval());
//        System.out.println("OptimisticUnchokingInterval : " + edu.ufl.cise.bytetorrent.config.getOptimisticUnchokingInterval());
//        System.out.println("FileName : " + edu.ufl.cise.bytetorrent.config.getFileName());
//        System.out.println("FileSize : " + edu.ufl.cise.bytetorrent.config.getFileSize());
//        System.out.println("PieceSize : " + edu.ufl.cise.bytetorrent.config.getPieceSize());
//        System.out.println("\nAdding Peers to the System\n");
//        PeerInfoConfig remotePeerInfo = PeerInfoConfig.getInstance();
//        for (Peer peer : remotePeerInfo.getPeerInfoList()) {
//            System.out.println("Id : "+peer.getId()+", Address : "+peer.getAddress()+", Port : "+peer.getPort()+
//                    ", HasFile : "+peer.isHasFile());
//        }
//
//    }

}
