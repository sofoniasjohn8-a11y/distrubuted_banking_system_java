import javax.swing.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public Client(String host, int port) {
        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            BankManager bankManager = (BankManager) registry.lookup("BankManagerService");
            System.out.println("Connected to server at " + host + ":" + port);
            SwingUtilities.invokeLater(() -> new LoginGUI(bankManager));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Error connecting to server at " + host + ":" + port + "\n" + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int    port = args.length > 1 ? Integer.parseInt(args[1]) : 1099;
        new Client(host, port);
    }
}
