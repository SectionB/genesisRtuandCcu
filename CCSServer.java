import java.io.*;
import java.net.*;

public class CCSServer {

    private static final int PORT = 5000;
    //GENISYS SERIAL COMMUNICATION PROTOCOL
    public static byte header = 0b00010000;
    public static byte address = 0b00010000;
    public static byte data = 0b00010000;
    public static byte checksum = 0b00010000;
    public static byte terminator = 0b00010000;

    public final byte mack = (byte) 0b11111010;
    public final byte mpol = (byte) 0b11111011;
    public final byte mcon = (byte) 0b11111100;
    public final byte mrec = (byte) 0b11111101;
    public final byte mexe = (byte) 0b11111110;

    public final byte sack = (byte) 0b11110001;
    public final byte sind = (byte) 0b11110010;
    public final byte sche = (byte) 0b11110011;

    DataInputStream in = null;
    DataOutputStream out = null;

    public CCS ccsGUI;

    public void setCCSGUI(CCS ccsGUI){
        this.ccsGUI = ccsGUI;
    }

    public KKSystem cssKKSystem = new KKSystem();

    public ServerSocket createServerSocket() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("SERVER online");
        } catch (IOException e) {
            System.out.println("SERVER: Error creating network connection");
        }
        return serverSocket;
    }

    public void handleRequests(ServerSocket serverSocket, CCSServer ccsServer) {

        Socket socket = null;
        try {
            socket = serverSocket.accept();
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            System.out.println("Client is connected");
            sendIndicationlMessage();
            PollingHandler pollingHandler = new PollingHandler(ccsServer);
            pollingHandler.start();

            byte[] cmassege;

            while(true) {

                cmassege = new byte[5];
                in.readFully(cmassege, 0, cmassege.length);

                header = cmassege[0];
                address = cmassege[1];
                data = cmassege[2];
                checksum = cmassege[3];
                terminator = cmassege[4];

                switch (header) {
                    case sack:
                        handleack();
                        break;
                    case sind:
                        handleind();
                        break;
                    case sche:
                        handleche();
                        break;
                    default:
                        hdefault();
                }
            }


            }
        catch (IOException e) {
            System.out.println("SERVER: Error connecting to client");
            System.exit(-1);
        }
    }

    //Send Server Messages
    public boolean sendControlMessage(byte data){
        System.out.println("Send Control Message");

        try {
            CCSServer.data = data;
            byte[] protocolMessage = {mcon, address, CCSServer.data, checksum, terminator};

            out.write(protocolMessage, 0, protocolMessage.length);
            out.flush();
            return true;

        } catch (Exception e) {
            System.out.println("SERVER: Error communicating with client");
            return false;
        }
    }
    public boolean sendPollingMessage(){
        try {
            data = 0b00000000;
            byte[] protocolMessage = {mpol, address, data, checksum, terminator};

            out.write(protocolMessage, 0, protocolMessage.length);
            out.flush();
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
    public boolean sendIndicationlMessage(){
        try {

            data = 0b00000000;
            byte[] protocolMessage = {mrec, address, data, checksum, terminator};

            out.write(protocolMessage, 0, protocolMessage.length);
            out.flush();

            byte[] response = new byte[5];
            in.readFully(response, 0, response.length);

            if(response[0] == sind){
                byte indi_data = response[3];
                cssKKSystem.setSystemStates(indi_data);
                ccsGUI.serverGUIUpdate(cssKKSystem.getSystemStatus());
                return true;
            }

            else {
                return false;
            }
        }
        catch (Exception e){
            return false;
        }
    }
    public void send_acknowledge() throws IOException {
        data = 0b00000000;
        byte[] responseMessage = {mack, address, data, checksum, terminator};
        out.write(responseMessage, 0, responseMessage.length);
        out.flush();
    }

    //Handle Client Responces
    public void handleack(){
        System.out.println("Client Acknowledgement Recieved - "+ data);
        cssKKSystem.setSystemStates(data);
        ccsGUI.serverGUIUpdate(cssKKSystem.getSystemStatus());
    }
    public void handleind() {
        System.out.println("Client INIDICATION Recieved - "+ data);
        cssKKSystem.setSystemStates(data);
        ccsGUI.serverGUIUpdate(cssKKSystem.getSystemStatus());
    }
    public void handleche() throws IOException {
        System.out.println("CHECKBACK_ message recieved!");
        send_acknowledge();
    }
    public void hdefault() {
        System.out.println("Client Flag Not Detected!");
    }
}
