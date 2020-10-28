import java.io.*; 
import java.net.*; 
import java.util.Date;




class TCPClient { 

    public static void main(String argv[]) throws Exception 
    { 
        String sentence, welcomeMessage; 
        String mathResult; 

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in)); 

        Socket clientSocket = new Socket("127.0.0.1", 6789); 

        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream()); 
        
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 

        welcomeMessage = inFromServer.readLine();

        System.out.println(welcomeMessage);

        sentence = inFromUser.readLine();
        if(sentence.equals("exit")){
          clientSocket.close();
        }
        
        outToServer.writeBytes(sentence + '\n'); 

        mathResult = inFromServer.readLine(); 

        System.out.println("FROM SERVER: " + mathResult); 
                         
          } 
      }
