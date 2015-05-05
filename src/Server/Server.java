package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Server extends Thread{

	public static final int DEFAULT_PORT = 1234;
    private DatagramSocket socket;
    public static final int size=15000;
    private String ownIP;
    
    public Server(){
    	try {
			this.ownIP =  InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
    	System.out.println("IP-Server: "+this.ownIP);
    }

    public void run()
    {
        try {
            //Keep a socket open to listen to all the UDP trafic that is destined for this port
            socket = new DatagramSocket(DEFAULT_PORT, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);

            while (true) {
              System.out.println(getClass().getName() + ">>>Ready to receive broadcast packets!");

              //Receive a packet
              byte[] recvBuf = new byte[size];
              DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
              socket.receive(packet);

              //Packet received
              System.out.println(getClass().getName() + ">>>Discovery packet received from: " + packet.getAddress().getHostAddress());
              System.out.println(getClass().getName() + ">>>Packet received; data: " + new String(packet.getData()));

              //See if the packet holds the right command (message)
              String message = new String(packet.getData()).trim();
              if (message.equals("DISCOVER_FUIFSERVER_REQUEST")) {
                byte[] sendData = "DISCOVER_FUIFSERVER_RESPONSE".getBytes();

                //Send a response
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
                socket.send(sendPacket);

                System.out.println(getClass().getName() + ">>>Sent packet to: " + sendPacket.getAddress().getHostAddress());
              }
            }
          } catch (IOException ex) {
            ex.printStackTrace();
          }
    }
}
