import java.io.*; 
import java.net.*; 
  
class UDPClient { 
    public static void main(String args[]) throws Exception 
    { 
      //creates buffered reader for user input and UDP socket for the datagram
      BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in)); 
      DatagramSocket clientSocket = new DatagramSocket(); 
  
      //stores client IP Address in IPAddress variable
      InetAddress IPAddress = InetAddress.getByName("127.0.0.1"); 
  
      byte[] sendData = new byte[1024]; 
      byte[] receiveData = new byte[1024]; 

      System.out.println("Please enter your name: ");
      String name = inFromUser.readLine();
      System.out.println("Name: " + name);
      //gets String equation from user and sends as Bytes
      String equation = inFromUser.readLine(); 
      sendData = equation.getBytes();         
      //Datagram contains the equation, length, IP address, and port number
      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876); 
    	clientSocket.send(sendPacket); 
      
      //receives the answer and stores into answer variable
    	DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length); 
    	  
    	clientSocket.receive(receivePacket); 
    	  
    	String answer = new String(receivePacket.getData()); 
    	  
    	System.out.println("FROM SERVER:" + answer); 
    	clientSocket.close(); 
  } 
}
