package lpi.server.rmi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Timer;

public class Main {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static IServer proxy;
    private static String SESSION_ID;

    static {
        try {
            proxy = (IServer) LocateRegistry.getRegistry("localhost", 4321).lookup(IServer.RMI_SERVER_NAME);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        logginUser();
        Commander commander = new Commander(proxy,SESSION_ID);
        Receiver receiver = new Receiver(commander);
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(receiver,0,200);

        while (true) {
            System.out.println("Enter your command...");
           commander.execute(reader.readLine().split(" "));
        }

    }

    private static void logginUser() throws IOException {
        System.out.println("Enter your name and password...");
        String[] creds = reader.readLine().split(" ");
        if (creds.length < 2) {
            System.out.println("You have to properly login before proceed!");
            logginUser();
        } else {
            SESSION_ID = proxy.login(creds[0], creds[1]);
        }
    }
}
