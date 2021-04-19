package edu.ufl.cise.bytetorrent.util;

import edu.ufl.cise.bytetorrent.model.Peer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;


import java.util.ArrayList;

public class LoggerUtil {

    private static Peer myPeer;
    private static String message;
//    private static SimpleDateFormat formatter;
//    private static Date date;


    private LoggerUtil(){

    }

    public static void initialize(){
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();

        builder.setStatusLevel(Level.INFO);
        builder.setConfigurationName("DefaultLogger");
        AppenderComponentBuilder appenderBuilder = builder.newAppender("Console", "CONSOLE")
                .addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);
        appenderBuilder.add(builder.newLayout("PatternLayout")
                .addAttribute("pattern", "%d %p %c [%t] %m%n"));
        RootLoggerComponentBuilder rootLogger = builder.newRootLogger(Level.DEBUG);
        rootLogger.add(builder.newAppenderRef("Console"));

        builder.add(appenderBuilder);
        builder.add(rootLogger);
        Configurator.initialize(builder.build());
//        formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
    }

    public static void LogInfoMessage(String message){
        LogManager.getLogger().info(message);
    }

    public static void LogErrorMessage(String message){
        LogManager.getLogger().info(message);
    }

    public static void LogMakeTcpConnection(String peerID) {
//        date = new Date(System.currentTimeMillis());
//        String message = "["+date + "]: ";
        message += "Peer "+ myPeer.getPeerId()+ " makes a connection to Peer "+peerID+".";
        LogInfoMessage(message);
    }

    public static void LogConnectedMsg(String peerID) {
//        date = new Date(System.currentTimeMillis());
//        String message = "["+date + "]: ";
        message += "Peer "+ myPeer.getPeerId()+ " is connected from Peer "+peerID+".";
        LogInfoMessage(message);
    }

    public static void LogChangeNeighbors(ArrayList<Peer> neighbors) {
//        date = new Date(System.currentTimeMillis());
//        String message = "["+date + "]: ";
        message += "Peer "+ myPeer.getPeerId()+ " has the preferred neighbors ";
        for (Peer p : neighbors) {
            message += p.getPeerId()+",";
        }
        LogInfoMessage(message);
    }

    public static void LogOptUnchokeNeighbor(String peerID) {
//        date = new Date(System.currentTimeMillis());
//        String message = "["+date + "]: ";
        message += "Peer "+ myPeer.getPeerId()+ " has optimistically unchoked neighbor "+peerID+".";
        LogInfoMessage(message);
    }

    public static void LogReceivedUnchokingMsg(String peerID) {
//        date = new Date(System.currentTimeMillis());
//        String message = "["+date + "]: ";
        message += "Peer "+ myPeer.getPeerId()+ " is unchoked by "+peerID+".";
        LogInfoMessage(message);
    }

    public static void LogReceivedChokingMsg(String peerID) {
//        date = new Date(System.currentTimeMillis());
//        String message = "["+date + "]: ";
        message += "Peer "+ myPeer.getPeerId()+ " is choked by "+peerID+".";
        LogInfoMessage(message);
    }

    public static void LogReceivedHaveMsg(String peerID, int piece_index) {
//        date = new Date(System.currentTimeMillis());
//        String message = "["+date + "]: ";
        message += "Peer "+ myPeer.getPeerId()+ " received the ‘have’ message from "+peerID+" for the piece "+piece_index+".";
        LogInfoMessage(message);
    }

    public static void LogReceivedInterestedMsg(String peerID) {
//        date = new Date(System.currentTimeMillis());
//        String message = "["+date + "]: ";
        message += "Peer "+ myPeer.getPeerId()+ " received the ‘interested’message from "+peerID+".";
        LogInfoMessage(message);
    }

    public static void LogReceivedNotInterestedMsg(String peerID) {
//        date = new Date(System.currentTimeMillis());
//        String message = "["+date + "]: ";
        message += "Peer "+ myPeer.getPeerId()+ " received the ‘not interested’message from "+peerID+".";
        LogInfoMessage(message);
    }

    public static void LogDownloadingPiece(String peerID, int piece_index, int number_of_piece) {
//        date = new Date(System.currentTimeMillis());
//        String message = "["+date + "]: ";
        message += "Peer "+ myPeer.getPeerId()+ " has downloaded the piece "+piece_index+" from "+peerID+". " +
                "Now the number of pieces it has is "+number_of_piece+".";
        LogInfoMessage(message);
    }

    public static void LogCompleteDownload() {
//        date = new Date(System.currentTimeMillis());
//        String message = "["+date + "]: ";
        message += "Peer "+ myPeer.getPeerId()+ " as downloaded the complete file.";
        LogInfoMessage(message);
    }

    public static void setMyPeer(Peer myPeer) {
        LoggerUtil.myPeer = myPeer;
    }
}
