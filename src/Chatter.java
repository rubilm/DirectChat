/**
 * Created by rubil94 on 06.03.2017.
 */
public class Chatter {
    public static String username = null;

    public static void main(String[] main) {
        Sender sender = new Sender();
        sender.start();

        ListenerDiscoveryPort ListenerDiscoveryPort = new ListenerDiscoveryPort();
        ListenerServicePort ListenerServicePort = new ListenerServicePort();
    }
}
