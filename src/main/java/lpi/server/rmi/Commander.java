package lpi.server.rmi;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;


public class Commander {
    IServer proxy;
    private String SESSION_ID;

    public Commander(IServer proxy, String sessionId) {
        this.proxy = proxy;
        this.SESSION_ID = sessionId;
    }

    public void execute(String[] command) {
        if (command[0] == null) {
            System.out.println("No command specified!");
            return;
        }
        switch (command[0]) {
            case "login":
                if(command.length<3){
                    System.out.println("Invalid login command");
                    return;
                }
                login(command[1], command[2]);
                break;
            case "echo":
                String message = stream(Arrays.copyOfRange(command, 1, command.length)).collect(Collectors.joining(" "));
                echo(message);
                break;
            case "ping":
                ping();
                break;
            case "list":
                String[] users = list();
                System.out.println("Currently logged users are : " + stream(users).collect(Collectors.joining(" ")));
                break;
            case "msg":
                String msgContent = stream(Arrays.copyOfRange(command, 2, command.length)).collect(Collectors.joining(" "));
                message(command[1], msgContent);
                break;
            case "exit":
                exit();
                break;
            default:
                System.out.println("Command not found!");
        }
    }

    public String[] list() {
        String[] users = new String[10];
        try {
            users = proxy.listUsers(SESSION_ID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void message(String receiver, String content) {
        try {
            IServer.Message message = new IServer.Message(receiver, content);
            proxy.sendMessage(SESSION_ID, message);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
    }

    public void login(String login, String password) {
        try {
            String sessionId = proxy.login(login, password);
            System.out.println("You have been logged in, your sessing id is :" + sessionId);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
    }

    public void receive() {
        try {
            IServer.Message massege = proxy.receiveMessage(SESSION_ID);
            if (massege != null) {
                System.out.println("Your received a message from " + massege.getSender() + " stating: " + massege.getMessage());
            }
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
            System.exit(1); //exit server is not available
        }
    }


    private void echo(String text) {
        try {
            System.out.println(proxy.echo(text));
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
    }

    private void ping() {
        try {
            proxy.ping();
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
    }

    private void exit() {
        System.exit(0);
    }

}
