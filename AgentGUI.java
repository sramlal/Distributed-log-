package PracThirdYear;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

//import za.ac.ukzn.cs.distributedlog.Activity;

public class AgentGUI extends JFrame {

    private static final long serialVersionUID = -1539474531866048021L;

    private JTextField txtDescription;

    private JButton btnRecordActivity;

    private JTextField addressDescription;

    public static void main(String[] args) {
        new AgentGUI();
    }

    public AgentGUI() {
        super("Activity Recording Agent");
        initialiseGui();
    }

    private void initialiseGui() {

        RecordActivity recActivity = new RecordActivity();

        getContentPane().setLayout(new BorderLayout());
        addressDescription = new JTextField();
        txtDescription = new JTextField();
        txtDescription.setColumns(30);
        txtDescription.addActionListener(recActivity);
        JPanel pnlDesc = new JPanel();
        pnlDesc.add(txtDescription);

        getContentPane().add(pnlDesc, BorderLayout.CENTER);
        btnRecordActivity = new JButton("Record Activity");
        btnRecordActivity.addActionListener(recActivity);
        JPanel pnlButton = new JPanel();
        pnlButton.add(btnRecordActivity);
        getContentPane().add(pnlButton, BorderLayout.SOUTH);

        this.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        shutdown();
                    }
                }
        );

        setBounds(100, 100, 480, 100);
        setVisible(true);
    }

    private void shutdown() {
        System.exit(0);
    }

    private void handleThrowable(String message, Throwable t) {
        t.printStackTrace();
        JOptionPane.showMessageDialog(
                AgentGUI.this,
                message,
                t.getClass().getName(),
                JOptionPane.ERROR_MESSAGE
        );
    }

    private class RecordActivity implements ActionListener {
        private static final int SERVER_PORT = 9999;
        public void actionPerformed(ActionEvent e) {
            String description = txtDescription.getText();
            String address = addressDescription.getText();
            if (description.length() > 0) {
                Activity activity = new Activity();
                activity.setDescription(description);
                try {
                    Socket logConnection = new Socket(address, SERVER_PORT);
                    activity.setLocation(
                            "IP: " + logConnection.getLocalAddress().toString() +
                                    " PORT: " + logConnection.getLocalPort());
                    ObjectOutputStream eventOutput = new ObjectOutputStream(
                            logConnection.getOutputStream()
                    );
                    eventOutput.writeObject(activity);
                    eventOutput.flush();
                    eventOutput.close();
                    logConnection.close();
                } catch (UnknownHostException ex) {
                    handleThrowable("Failed to locate log server", ex);
                } catch (IOException ex) {
                    handleThrowable("Failed to write object", ex);
                }
            }
            txtDescription.setText("");
        }
    }
}

