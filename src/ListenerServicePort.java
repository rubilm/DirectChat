import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.Timestamp;
import java.util.Scanner;

/**
 * Created by rubil94 on 21.03.2017.
 */
public class ListenerServicePort extends Thread {
    public static int DEFAULT_PORT = 7015;
    private static int BUFFER_SIZE = 1024;

    public int port;
    public InetAddress addr;

    public boolean shutdown = false;

    public Scanner scanner;

    /***
     * Constructor starting a listener on the default port.
     */
    public ListenerServicePort() {
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
     * receive udp chat packet on the service port
     * reply the chat packet
     */
    public void run() {
        try {
            DatagramSocket ds = new DatagramSocket(DEFAULT_PORT);
            ds.setBroadcast(true);

            while (!shutdown) {
                try {
                    byte[] buf = new byte[BUFFER_SIZE];
                    DatagramPacket dp = new DatagramPacket(buf, BUFFER_SIZE);
                    ds.receive(dp);
                    String str = new String(dp.getData(), 0, dp.getLength());
                    System.out.println("[Partner]: " + str);
                    String msg = constructMessage();
                    Sender.sendChat(dp.getAddress(), DEFAULT_PORT, msg); //compressed
                    //Sender.send(dp.getAddress(), DEFAULT_PORT, msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        catch (SocketException se) {
            se.printStackTrace();
        }
    }

    /***
     * create discovery reply message
     */
    public String constructMessage() {
        System.out.println("[You]: ");
        scanner = new Scanner(System.in);

        String message = scanner.nextLine();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        String responseMessage = "{\"from\":\"captain_" + Chatter.username + "\",\"timestamp\":\"" + timestamp + "\",\"message\":{\"text\":\"" + message + "\"}}";
        return responseMessage;
    }
}
