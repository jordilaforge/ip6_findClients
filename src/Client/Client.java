package Client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Client {
	
	
	private String hostname= "localhost";
    private int port=1234;
    private InetAddress host;
    private DatagramSocket socket;
    DatagramPacket packet;
    private String ownIP;
    
    public Client(){
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
            host = InetAddress.getByName(hostname);
            socket = new DatagramSocket (null);
            packet=new DatagramPacket (new byte[100], 0,host, port);
            socket.send (packet);
            packet.setLength(100);
            socket.receive (packet);
            socket.close ();
            byte[] data = packet.getData ();
            String time=new String(data);  // convert byte array data into string
            System.out.println(time);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
}
