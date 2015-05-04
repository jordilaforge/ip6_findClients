package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Server {

	public static final int DEFAULT_PORT = 1234;
    private DatagramSocket socket;
    private DatagramPacket packet;
    private String ownIP;
    private String ownSubnet;
    
    public Server(){
    	try {
			this.ownIP =  InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println("IP-Server: "+this.ownIP);
    }

    public void run()
    {
        try
        {
            socket = new DatagramSocket(DEFAULT_PORT);
            System.out.println("Socket created on port: "+DEFAULT_PORT);
        }
        catch( Exception ex )
        {
            System.out.println("Problem creating socket on port: " + DEFAULT_PORT );
        }

        packet = new DatagramPacket (new byte[1], 1);

        while (true)
        {
            try
            {
            	System.out.println("Waiting for Client.....");
                socket.receive (packet);
                System.out.println("Received from: " + packet.getAddress () + ":" +
                                   packet.getPort ());
                byte[] outBuffer = ownIP.getBytes();
                packet.setData (outBuffer);
                packet.setLength (outBuffer.length);
                socket.send (packet);
            }
            catch (IOException ie)
            {
                ie.printStackTrace();
            }
        }
    }
}
