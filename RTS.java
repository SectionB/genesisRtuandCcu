import javax.swing.*;
import java.awt.*;
import java.net.Socket;

public class RTS {
    private JLabel message;
    private JPanel RTSPanel;
    private JLabel STATUSV1;
    private JLabel STATUSV2;
    private JLabel STATUSV3;
    private JLabel STATUSP1;
    private JLabel STATUSP2;
    private JLabel STATUSM1;
    private JLabel STATUSM2;
    private static RTSClient rtsClient;

    private JLabel[] lableArray= {STATUSV1,STATUSV2,STATUSV3,STATUSP1,STATUSP2,STATUSM1,STATUSM2};

    public RTS() {    }

    public void updateLableON(JLabel lable){
        lable.setText("ON");
        lable.setForeground(Color.GREEN);
    }

    public void updateLableOFF(JLabel lable){
        lable.setText("OFF");
        lable.setForeground(Color.red);
    }

    public void clientGUIUpdate(boolean [] status){
        int count =0;
        for (boolean i:status
             ) {
            if(i){
                updateLableON(lableArray[count]);
            }
            else {
                updateLableOFF(lableArray[count]);
            }
            count++;
        }
    }

    public static void main(String[] args){
        RTS rtsUI = new RTS();
        JFrame frame = new JFrame("RTS");
        frame.setContentPane(rtsUI.RTSPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        StartClient(rtsUI);
    }

    public static void StartClient(RTS rtsUI){
        rtsClient = new RTSClient();

        rtsClient.setRTSGUI(rtsUI);
        Socket socket = rtsClient.createClientSocket();
        if (socket != null) {
            rtsClient.handleRequests(socket);
        }
    }
}