import java.io.*; 
import java.net.*; 
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Date;
import java.util.ArrayList;
  
class UDPServer { 

  static class User{
    private String name;
    private String secsAttached;
    private Date timeConnected;
  
    User(String name, String secs){
      this.name = name;
      this.secsAttached = secs;
      timeConnected = new Date();
    }
  
    public String getName(){
      return this.name;
    }
  
    public String getSecs(){
      return this.secsAttached;
    }
  
    public Date getTime(){
      return this.timeConnected;
    }
  }

  public static void main(String args[]) throws Exception 
    { 
  
      DatagramSocket serverSocket = new DatagramSocket(9876); 
      ArrayList<User> userLogs = new ArrayList<User>();
      byte[] receiveData = new byte[1024]; 
      byte[] sendData  = new byte[1024]; 
  
      while(true) 
        { 
  
          DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length); 
           serverSocket.receive(receivePacket); 

           //converts message into string from byte array 
           String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength()); 

           //splits comma separated string into array 
           String[] separatedMessage = receivedMessage.split(",");
           String name = separatedMessage[0]; //first item in array is the name of the sender
           String equation = separatedMessage[1]; //second item in array is the equation to be evaluated
           String secsAttached = separatedMessage[2];

           InetAddress IPAddress = receivePacket.getAddress(); 
           int port = receivePacket.getPort(); 
   
           //Script Engine allows javascript eval function to evaluate the string equation automatically
           ScriptEngineManager mgr = new ScriptEngineManager();
           ScriptEngine engine = mgr.getEngineByName("JavaScript");

           //logs the user on exit by creating a user object and storing it in the user log arraylist
           if(equation.contains("exit"))
           {
              
              User newUser = new User(name, secsAttached);
           }
           else {
             try {
              System.out.println(name);
              String result = engine.eval(equation).toString();
              sendData = result.getBytes(); 
             } catch (ScriptException e) {
               System.out.println("Exception " + e);
               String exceptionMessage = "Error: Please enter a valid mathematical expression";
               sendData =  exceptionMessage.getBytes();
             }
           }
   
           DatagramPacket sendPacket = 
              new DatagramPacket(sendData, sendData.length, IPAddress, 
                                port); 
   
           serverSocket.send(sendPacket); 
         } 
     } 
 }  
