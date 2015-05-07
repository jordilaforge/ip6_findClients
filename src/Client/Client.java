package Client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class Client extends Thread{
	
	
    private static final int DEFAULT_PORT = 1234;
	private static final int size=15000;
    private DatagramSocket socket;
    DatagramPacket packet;
    private String ownIP;
    public String serverIP;
    
    public Client(){
    	try {
			this.ownIP =  InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
    	System.out.println("IP-Client: "+this.ownIP);
    }

    
    
    
    public void run()
    {
    	System.out.println("Start Discovery....");
    	try {
    		 
    		  //Open a random port to send the package
    		  socket = new DatagramSocket();
    		  socket.setBroadcast(true);

    		  byte[] sendData = "DISCOVER_FUIFSERVER_REQUEST".getBytes();

    		  //Try the 255.255.255.255 first
    		  try {
    		    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), DEFAULT_PORT);
    		    socket.send(sendPacket);
    		    System.out.println(getClass().getName() + ">>> Request packet sent to: 255.255.255.255 (DEFAULT)");
    		  } catch (Exception e) {
    		  }

    		  // Broadcast the message over all the network interfaces
    		  Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
    		  while (interfaces.hasMoreElements()) {
    		    NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();

    		    if (networkInterface.isLoopback() || !networkInterface.isUp()) {
    		      continue; // Don't want to broadcast to the loopback interface
    		    }

    		    for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
    		      InetAddress broadcast = interfaceAddress.getBroadcast();
    		      if (broadcast == null) {
    		        continue;
    		      }

    		      // Send the broadcast package!
    		      try {
    		        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, 8888);
    		        socket.send(sendPacket);
    		      } catch (Exception e) {
    		      }

    		      System.out.println(getClass().getName() + ">>> Request packet sent to: " + broadcast.getHostAddress() + "; Interface: " + networkInterface.getDisplayName());
    		    }
    		  }

    		  System.out.println(getClass().getName() + ">>> Done looping over all network interfaces. Now waiting for a reply!");

    		  //Wait for a response
    		  byte[] recvBuf = new byte[size];
    		  DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
    		  socket.receive(receivePacket);

    		  //We have a response
    		  System.out.println(getClass().getName() + ">>> Broadcast response from server: " + receivePacket.getAddress().getHostAddress());

    		  //Check if the message is correct
    		  String message = new String(receivePacket.getData()).trim();
    		  if (message.equals("DISCOVER_FUIFSERVER_RESPONSE")) {
    		    //DO SOMETHING WITH THE SERVER'S IP (for example, store it in your controller)
    			serverIP = receivePacket.getAddress().toString().replace("/", "");
    		    System.out.println(serverIP);
    		    writeServerIP(serverIP);
    		  }

    		  //Close the port!
    		  socket.close();
    		} catch (IOException ex) {
    		 	ex.printStackTrace();
    		}
    }




	private void writeServerIP(String serverIP) {
		try {
			PrintWriter out = new PrintWriter("serverIP.txt");
			out.println(serverIP);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
    
    
}
