import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;

/**
 * Created by rubil94 on 21.03.2017.
 */
public class ListenerDiscoveryPort extends Thread {
    public static int DEFAULT_PORT = 7016;
    private static int BUFFER_SIZE = 1024;

    public int port;
    public InetAddress addr;

    public boolean shutdown = false;
    public Boolean replyDiscover = false;

    /***
     * Constructor starting a listener on the default port.
     */
    public ListenerDiscoveryPort() {
        init(null, DEFAULT_PORT);
    }

    /***
     * Contructor helper for setting up a listener on a specific port
     * @param port Port for the listener to start
     */
    private void init(InetAddress addr, int port) {
        this.addr = addr;
        this.port = port;
        this.start();
    }

    /***
     * receive the udp discovery packet on the service port
     * send the discovery reply
     */
    public void run() {
        System.out.println("Starting Discovery Port");
        try {
            DatagramSocket ds = new DatagramSocket(DEFAULT_PORT);
            ds.setBroadcast(true);

            while (!shutdown) {
                try {
                    byte[] buf = new byte[BUFFER_SIZE];
                    DatagramPacket dp = new DatagramPacket(buf, BUFFER_SIZE);
                    ds.receive(dp);

                    String str = new String(dp.getData(), 0, dp.getLength());
                    // System.out.println("[Listener] " + str);
                    if (str.startsWith("HelloFromTheOtherSide")) {
                        String id = str.substring(Math.max(str.length() - 2, 0));

                        if (Chatter.username == null) {
                            Chatter.username = createUsername();
                        }

                        String discoveryResponse = "CCkvanc" + id + "captain_" + Chatter.username;
                        Sender.send(dp.getAddress(), DEFAULT_PORT, discoveryResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SocketException se) {
            se.printStackTrace();
        }
    }


    /***
     * create a username
     * @return username
     */
    public String createUsername() {
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder str = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 18; i++) {
            char c = chars[random.nextInt(chars.length)];
            str.append(c);
        }

        return str.toString();
    }
}
