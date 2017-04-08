import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.Random;
import java.util.zip.GZIPOutputStream;

/**
 * Created by rubil94 on 06.03.2017.
 */
public class Sender extends Thread {

    public boolean shutdown = false;

    public void run(){
        sendDiscovery();
    }
    /**
     * send a udp packet
     * @param ia   receiver address
     * @param port receiver port
     * @param txt  messag to send
     * @return status of send
     */
    public static boolean send(InetAddress ia, int port, String txt) {
        try {
            DatagramSocket ds = new DatagramSocket();
            ds.setBroadcast(true);
            DatagramPacket dp = new DatagramPacket(txt.getBytes(), txt.length(), ia, port);
            ds.send(dp);
            ds.close();
        } catch (Exception ie) {
            ie.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * send comressed udp chat packet
     * @param ia
     * @param port
     * @param txt
     * @return
     */
    public static boolean sendChat(InetAddress ia, int port, String txt) {
        try {
            DatagramSocket ds = new DatagramSocket();
            ds.setBroadcast(true);
            DatagramPacket dp = new DatagramPacket(compressChatMessage(txt), compressChatMessage(txt).length, ia, port);
            ds.send(dp);
            ds.close();
        } catch (Exception ie) {
            ie.printStackTrace();
            return false;
        }
        return true;
    }

    /***
     * create the discovery packet
     * transmit it to the send thread
     */
    public void sendDiscovery() {
        while(!shutdown) {
            try {
                InetAddress discoveryIP = InetAddress.getByName("255.255.255.255");
                Random rn = new Random();
                int Id = rn.nextInt(99 - 10 + 1) + 10;
                send(discoveryIP, ListenerDiscoveryPort.DEFAULT_PORT, "HelloFromTheOtherSide"+Id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /***
     * compress the chat message
     * @param message
     * @return
     * @throws IOException
     */
    public static byte[] compressChatMessage(String message) throws IOException {
        ByteArrayOutputStream bOutStream = new ByteArrayOutputStream(message.length());
        GZIPOutputStream gOutStream = new GZIPOutputStream(bOutStream);

        gOutStream.write(message.getBytes());
        gOutStream.close();

        byte[] compressedMsg = bOutStream.toByteArray();
        bOutStream.close();

        return compressedMsg;
    }
}
