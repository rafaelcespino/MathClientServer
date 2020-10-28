import java.io.*; 
import java.net.*; 
import java.util.Date;

class User{
  private String name;
  private int secsConnected;
  private Date connectTime;

  User(String s){
      this.name = s;
      this.connectTime = new Date();
  }

  public String getName(){
    return this.name;
  }

  public Date connectTime(){
    return connectTime;
  }
  
} 

class TCPServer { 

  public static void main(String argv[]) throws Exception 
    { 
      String clientSentence; 
      String capitalizedSentence; 

      ServerSocket welcomeSocket = new ServerSocket(6789); 
  
      while(true) { 
  
            Socket connectionSocket = welcomeSocket.accept(); 

           BufferedReader inFromClient = 
              new BufferedReader(new
              InputStreamReader(connectionSocket.getInputStream())); 


           DataOutputStream  outToClient = 
             new DataOutputStream(connectionSocket.getOutputStream()); 

           outToClient.writeBytes("Connection successful \n");

           clientSentence = inFromClient.readLine(); 

           capitalizedSentence = clientSentence.toUpperCase() + '\n'; 

           outToClient.writeBytes(capitalizedSentence); 
        } 
    } 
} 
 

           
