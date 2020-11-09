import java.io.*; 
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Date;
import java.util.ArrayList;
  
class UDPServer { 

  static class User{
    private String name;
    private String secsAttached;
    private String timeConnected;
    private ArrayList<String> queries;
    User(String name){
      this.name = name;
      queries = new ArrayList<String>();

      //constructor gets current time and converts it into string format when the user is created 
      Date date = new Date();
      DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
      this.timeConnected = dateFormat.format(date);
    }
  
    public String getName(){
      return this.name;
    }
  
    public String getSecs(){
      return this.secsAttached;
    }
  
    public String getTime(){
      return this.timeConnected;
    }

    public void setTime(String seconds){
      this.secsAttached = seconds;
    }

    public void addQuery(String query){
      this.queries.add(query);
    }

    public int getQuerySize(){
      return this.queries.size();
    }

    public String getQuery(int index){
      return this.queries.get(index);
    }
  }

  public static void main(String args[]) throws Exception 
    { 
  
      DatagramSocket serverSocket = new DatagramSocket(9876); 
      ArrayList<User> userLogs = new ArrayList<User>();
      byte[] receiveData = new byte[1024]; 
      byte[] sendData  = new byte[1024]; 

      //File where logs will be written to
      File logFile = new File("logFile.txt");
      logFile.createNewFile();
      
  
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

           //check to see if the connected user is new 
           boolean existingUser = false;
           for(int i = 0; i < userLogs.size(); i++){
             if(userLogs.get(i).getName().equals(name))
                existingUser = true;
           }

           //if connected user is new, add to arrayList
           if(existingUser == false){
              User newUser = new User(name);
              userLogs.add(newUser);
           }
           

           InetAddress IPAddress = receivePacket.getAddress(); 
           int port = receivePacket.getPort(); 
   
           //Script Engine allows javascript eval function to evaluate the string equation automatically
           ScriptEngineManager mgr = new ScriptEngineManager();
           ScriptEngine engine = mgr.getEngineByName("JavaScript");

           

           //logs the user on exit by creating a user object and storing it in the user log arraylist
           if(equation.equals("exit") || equation.equals("Exit"))
           {
              FileWriter writer = new FileWriter(logFile, true);
              int userIndex = 0;
              String logOffTime = "";
              System.out.println("Disconnected");
              //set the seconds attached of the logged off user
              for(int i = 0; i < userLogs.size(); i++){
                if(userLogs.get(i).getName().equals(name)){
                   userIndex = i;
                   userLogs.get(i).setTime(secsAttached);
                   logOffTime = userLogs.get(i).getTime().toString();
                }
              }

              System.out.println("User " + name + " has logged off after " 
              + secsAttached + " seconds. Initially connected at " + logOffTime);

              //write the name, secs attached, connection time, and all logged queries in text file
              writer.write(name + ", ");
              writer.write(secsAttached + " seconds attached, ");
              writer.write("connected at " + logOffTime + ", queries: [");

              //loop through logged queries to write each one in log file
              for(int i = 0; i < userLogs.get(userIndex).getQuerySize(); i++)
              {
                  if(i == userLogs.get(userIndex).getQuerySize() - 1)
                    writer.write(userLogs.get(userIndex).getQuery(i));
                  else
                    writer.write(userLogs.get(userIndex).getQuery(i) + ",");
              }

              //create new line to write the next user log
              writer.write("] \n");
              writer.flush();
              writer.close();
           }
           else {
             try {
              //use the Script Engine Manager to evaluate the equation using the Javascript eval function
              String result = engine.eval(equation).toString();
              sendData = result.getBytes(); 
              //add the query to the list of user's queries
              for(int i = 0; i < userLogs.size(); i++){
                if(userLogs.get(i).getName().equals(name)){
                  userLogs.get(i).addQuery(equation);
                  System.out.println("Added " + equation+ " query for user " + name);
                }

              }
              //throw an exception if the received message from the client cannot be evaluated i.e. user sends words
             } catch (ScriptException e) {
               System.out.println("Exception " + e);
               String exceptionMessage = "Error: Please enter a valid mathematical expression";
               sendData =  exceptionMessage.getBytes();
             }
           }
   
           //send equation answer back to the client
           DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port); 
           serverSocket.send(sendPacket); 
         } 
     } 
 }  
