package template.chat;

import java.io.*; 
import java.net.*; 
import java.util.Scanner; 
  
public class ChatClient {
	Socket socket;
  
    public ChatClient(String username,String server_add, int port) { 
        try { 
        	
            this.socket = new Socket(server_add, port);
  
            // Setting up input and output streams 
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true); 
            BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream())); 
  
            // Start a thread to handle incoming messages 
            new Thread(() -> { 
                try { 
                    String serverResponse; 
                    while ((serverResponse = in.readLine()) != null) { 
                        System.out.println(serverResponse); 
                    } 
                } catch (IOException e) { 
                    e.printStackTrace(); 
                } 
            }).start(); 
  
            // Read messages from the console and send to the server 
            Scanner scanner = new Scanner(System.in); 
            String userInput; 
            while (true) { 
                userInput = scanner.nextLine(); 
                out.println(userInput); 
            } 
             
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
    } 
} 