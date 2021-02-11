import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class RTSClient {

    private static final int PORT = 5000;
    //GENISYS SERIAL COMMUNICATION PROTOCOL
    public static byte header = 0b00010000;
    public static byte address = 0b00010000;
    public static byte data = 0b00010000;
    public static byte checksum = 0b00010000;
    public static byte terminator = 0b00010000;

    private static InetAddress host;
    //Basic GENISYS Protocol Master to Slave Messages
    public final byte mack = (byte) 0b11111010;
    public final byte mpol = (byte) 0b11111011;
    public final byte mcon = (byte) 0b11111100;
    public final byte mrec = (byte) 0b11111101;
    public final byte mexe = (byte) 0b11111110;


    //Basic GENISYS Protocol Slave to Master Messages
    public final byte sack = (byte) 0b11110001;
    public final byte sind = (byte) 0b11110010;
    public final byte sche = (byte) 0b11110011;


    DataInputStream in;
    DataOutputStream out;

    private final KKSystem rtsKKSystem = new KKSystem();

    private RTS rtsGUI;

    public void setRTSGUI(RTS rtuApp) {

        this.rtsGUI = rtuApp;
    }

    public Socket createClientSocket() {
        Socket socket = null;
        try {
            host = InetAddress.getLocalHost();
            socket = new Socket(host, PORT);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return socket;
    }

    public void handleRequests(Socket socket) {

        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            byte[] sMEssage;

            while (true) {

                sMEssage = new byte[5];
                in.readFully(sMEssage, 0, sMEssage.length);

                header = sMEssage[0];
                address = sMEssage[1];
                data = sMEssage[2];
                checksum = sMEssage[3];
                terminator = sMEssage[4];

                switch (header) {
                    case mack:
                        handleack();
                        break;
                    case mpol:
                        handlepol();
                        break;
                    case mcon:
                        handlecon(data);
                        break;
                    case mrec:
                        handlerec();
                        break;
                    case mexe:
                        handleex();
                        break;
                    default:
                        handleDef();
                }
                rtsGUI.clientGUIUpdate(rtsKKSystem.getSystemStatus());
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();

        } finally {
            try {
                System.out.println("\nClosing connection…");
                socket.close();
            } catch (IOException ex) {
                System.out.println("Unable to disconnect!");
                System.exit(1);
            }
        }
    }

    public void handleack() {
        System.out.println("Acknowledge Message Recieved");
    }

    public void handlepol() throws IOException {
        System.out.println("Polling Request Recieved");
        System.out.println("Send Polling Message");
        send_acknowledge();
    }

    public void handlecon(byte dataByte) throws InterruptedException, IOException {
        System.out.println("Control Message Recieved  - " + dataByte);
        rtsKKSystem.checkSystemUpdate(dataByte);
        send_checkBack();
    }

    public void handlerec() throws IOException {
        System.out.println("Indication Request Recieved");
        send_recall();
    }

    public void handleex() throws IOException {
        System.out.println("Control Execute Message Recieved");
        send_acknowledge();

    }

    public void handleDef() {
        System.out.println("Master Flag Not Detected!");
    }


    public void send_acknowledge() throws IOException {
        data = rtsKKSystem.getResponce();
        byte[] responseMessage = {sack, address, data, checksum, terminator};
        out.write(responseMessage, 0, responseMessage.length);
        out.flush();
    }

    public void send_recall() throws IOException {
        System.out.println("Recall Message Send");
        data = rtsKKSystem.getResponce();
        byte[] responseMessage = {sind, address, data, checksum, terminator};
        out.write(responseMessage, 0, responseMessage.length);
        out.flush();
    }

    public void send_checkBack() throws IOException {
        System.out.println("Recall Message Send");
        data = rtsKKSystem.getResponce();
        byte[] responseMessage = {sind, address, data, checksum, terminator};
        out.write(responseMessage, 0, responseMessage.length);
        out.flush();
    }

}
