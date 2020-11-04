import java.io.*; 
import java.net.*; 
import java.time.Duration;
import java.time.Instant;
  
class UDPClient { 
    public static void main(String args[]) throws Exception 
    { 
      //creates buffered reader for user input and UDP socket for the datagram
      BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in)); 
      DatagramSocket clientSocket = new DatagramSocket(); 

      //create the start time for connection duration
      Instant start = Instant.now();
  
      //stores client IP Address in IPAddress variable
      InetAddress IPAddress = InetAddress.getByName("127.0.0.1"); 
  
      byte[] sendData = new byte[1024]; 
      byte[] receiveData = new byte[1024]; 

      System.out.println("Please enter your name: ");
      String name = inFromUser.readLine();
      System.out.println("Name: " + name);
      //gets String equation from user and sends as Bytes

      while(true){
      //get equation input from user as string
      String equation = inFromUser.readLine(); 

      //if the user types exit, send dataCSV with the total connection time before closing the socket and ending the program
      if(equation.equals("Exit") || equation.equals("exit")){
        //calculate total time attached to server and send it to the server for logging
        Instant end = Instant.now();
        Duration connectionTime = Duration.between(start, end);
        String dataCSV = name+ "," + "Exit" + "," + connectionTime.toSeconds();
        sendData = dataCSV.getBytes();         
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
        clientSocket.send(sendPacket);
        clientSocket.close(); 
        break;
      }

      else{
      //puts all data together as a CSV in order of name, equation, and time elapsed
      String dataCSV = name+ "," + equation + ",0"; //time elapsed is zero since the connection hasn't been ended 
      sendData = dataCSV.getBytes();         
      //Datagram contains the equation, length, IP address, and port number
      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876); 
      clientSocket.send(sendPacket); 
      }
      
      //receives the answer and stores into answer variable
    	DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length); 
    	clientSocket.receive(receivePacket); 
    	String answer = new String(receivePacket.getData(), 0, receivePacket.getLength()); 
        
      //print answer to the user before restarting the loop
      System.out.println("FROM SERVER:" + answer); 
      
      }

      
  } 
}
