package lpi.server.rmi;

import java.util.*;

import static java.util.Arrays.asList;

public class Receiver extends TimerTask {
    private Commander commander;
    private List<String> userList;

    public Receiver(Commander commander) {
        this.commander = commander;
    }

    @Override
    public void run() {
        commander.receive();

        List<String> currentUsers = asList(commander.list());

        if (userList == null) {
            userList = currentUsers;
        }

        if (currentUsers.size() > userList.size()) {
            List newUsers = new ArrayList(currentUsers);
            newUsers.removeAll(userList);
            System.out.println("New users have been loggen in : " + newUsers.toString());
        }
        if (userList.size() > currentUsers.size()) {
            List loggedUsers = new ArrayList(userList);
            loggedUsers.removeAll(currentUsers);
            System.out.println("Users have been loged out :" + loggedUsers.toString());
        }
        userList = currentUsers;
    }
}
