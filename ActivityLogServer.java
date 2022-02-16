package PracThirdYear;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

//import za.ac.ukzn.cs.distributedlog.Activity;

public final class ActivityLogServer {

    private static final int SERVER_PORT = 9999;

    private File myLogFile = null;

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            new ActivityLogServer();
        } catch (IOException e) {
            System.err.println("Activity log server crashed!!!");
            e.printStackTrace();
        }
    }

    public ActivityLogServer() throws IOException {
        myLogFile = new File("Activity.log");
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(SERVER_PORT);
            processActivities(ss);
        } catch (ClassNotFoundException e) {
            System.err.println("Where is the Activity class?");
            e.printStackTrace();
        } finally {
            ss.close();
        }
        System.out.println("...bye!");
    }

    private void processActivities(ServerSocket ss)
            throws IOException, ClassNotFoundException {
        while (true) {
            System.out.println("Listening for activities...");
            Socket logConnection = ss.accept();
            ObjectInputStream activityInput = new ObjectInputStream(
                    logConnection.getInputStream()
            );
            Activity activity = (Activity)activityInput.readObject();
            BufferedWriter logWriter = new BufferedWriter(
                    new FileWriter(myLogFile, true)
            );
            logWriter.write(activity.toString());
            logWriter.flush();
            logWriter.close();
            logConnection.close();
        }
    }
}

