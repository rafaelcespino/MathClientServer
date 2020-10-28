import java.io.*; 
import java.net.*; 
import java.util.Date;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

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
           
           if(clientSentence.contains("exit")){

           }
           else{
            ScriptEngineManager mgr = new ScriptEngineManager();
            ScriptEngine engine = mgr.getEngineByName("JavaScript");
            String result = "";
            try{
              result = engine.eval(clientSentence).toString();  
            }catch(ScriptException e){
              result = e.getMessage();
            }
            outToClient.writeBytes(result); 
           }
        } 
    } 
} 
 

           
