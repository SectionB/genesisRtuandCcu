import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;

public class CCS {

    private JLabel CLIENTSTAUS;
    private JPanel CSSPanel;
    private JButton ONOFFButtonV1;
    private JButton ONOFFButtonV2;
    private JButton ONOFFButtonV3;
    private JButton ONOFFButtonP1;
    private JButton ONOFFButtonP2;
    private JButton ONOFFButtonM1;
    private JButton ONOFFButtonM2;
    private JLabel STATUSV1;
    private JLabel STATUSV2;
    private JLabel STATUSV3;
    private JLabel STATUSP1;
    private JLabel STATUSP2;
    private JLabel STATUSM1;
    private JLabel STATUSM2;
    private JLabel MESSEGE;
    private JButton SEND;

    private JLabel[] lableArray= {STATUSV1,STATUSV2,STATUSV3,STATUSP1,STATUSP2,STATUSM1,STATUSM2};

    private static CCSServer ccsServer;

    byte controlMessage =  0b00000000;

    public CCS() {
        ONOFFButtonV1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlMessage =  ONOFFButton(controlMessage,0);
            }
        });
        ONOFFButtonV2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlMessage =  ONOFFButton(controlMessage,1);
            }
        });
        ONOFFButtonV3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlMessage =  ONOFFButton(controlMessage,2);
            }
        });
        ONOFFButtonP1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlMessage =  ONOFFButton(controlMessage,3);
            }
        });
        ONOFFButtonP2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlMessage =  ONOFFButton(controlMessage,4);
            }
        });
        ONOFFButtonM1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlMessage =  ONOFFButton(controlMessage,5);
            }
        });
        ONOFFButtonM2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlMessage =  ONOFFButton(controlMessage,6);
            }
        });
        SEND.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean response = ccsServer.sendControlMessage(controlMessage);
                controlMessage = 0b00000000;
                if(response){
                    MESSEGE.setText("Success " + ccsServer.data);
                }
                else {
                    MESSEGE.setText("Fail");
                }

            }
        });


    }

    public void updateLableON(JLabel lable){
        lable.setText("ON");
        lable.setForeground(Color.GREEN);
    }

    public void updateLableOFF(JLabel lable){
        lable.setText("OFF");
        lable.setForeground(Color.red);
    }

    public void serverGUIUpdate(boolean [] status){
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

    public static void main(String[] args) throws IOException {

        CCS ccsUI = new CCS();
        JFrame frame = new JFrame("CSS");
        frame.setContentPane(ccsUI.CSSPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        StartServer(ccsUI);
    }

    public static void StartServer(CCS ccsUI){
        ccsServer= new CCSServer();

        ccsServer.setCCSGUI(ccsUI);
        ServerSocket serverSocket = ccsServer.createServerSocket();

        if (serverSocket != null) {
            ccsServer.handleRequests(serverSocket, ccsServer);
        }
    }

    static byte ONOFFButton(byte b, int n)
    {
        return (byte) (b ^ (1 << n));
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
