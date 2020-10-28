import java.io.*; 
import java.net.*; 
import java.util.Date;




class TCPClient { 

    public static void main(String argv[]) throws Exception 
    { 
        String sentence, welcomeMessage; 
        String modifiedSentence; 

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in)); 

        Socket clientSocket = new Socket("127.0.0.1", 6789); 

        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream()); 
        
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 

        welcomeMessage = inFromServer.readLine();

        System.out.println(welcomeMessage);

        sentence = inFromUser.readLine();
        
        outToServer.writeBytes(sentence + '\n'); 

        modifiedSentence = inFromServer.readLine(); 

        System.out.println("FROM SERVER: " + modifiedSentence); 

        clientSocket.close(); 
                         
          } 
      }
