package template.chat;

// Server program to handle multiple 
// Clients with socket connections 
import java.io.*; 
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList; 
  
public class ChatServer {     
    String username;
    private static CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>(); 
  
    public static void ChatServer(String username, int port) {
        try {
            int connectCount=0;
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server is running and waiting for connections.."); 
            
            // Accept incoming connections 
            while (true) { 
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket); 
  
                // Create a new client handler for the connected client 
                ClientHandler clientHandler = new ClientHandler(clientSocket); 
                clients.add(clientHandler);
                new Thread(clientHandler).start();
                
                connectCount+=1;

                if (connectCount>=3){
                    serverSocket.close();
                }
                
                
            } 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 

        
    } 
  
    // Broadcast a message to all clients except the sender 
    public static void broadcast(String message, ClientHandler sender)  
    { 
        for (ClientHandler client : clients) { 
            if (client != sender) { 
                client.sendMessage(message); 
            } 
        } 
    } 
  
    // Internal class to handle client connections 
    private static class ClientHandler implements Runnable { 
        private Socket clientSocket; 
        private PrintWriter out; 
        private BufferedReader in; 
  
        // Constructor 
        public ClientHandler(Socket socket) { 
            this.clientSocket = socket; 
  
            try { 
                // Create input and output streams for communication 
                out = new PrintWriter(clientSocket.getOutputStream(), true); 
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
            } catch (IOException e) { 
                e.printStackTrace(); 
            } 
        } 
  
        // Run method to handle client communication 
        @Override
        public void run() { 
            try { 
                // Get the username from the client
                String username = getUsername();
                System.out.println("User " + username + " connected."); 
  
                out.println("Welcome to the chat, " + username + "!");
                out.println("Type Your Message");
                String inputLine;
  
                // Continue receiving messages from the client 
                while ((inputLine = in.readLine()) != null) { 
                    System.out.println("[" + username + "]: " + inputLine); 
  
                    // Broadcast the message to all clients 
                    broadcast("[" + username + "]: " + inputLine, this); 
                } 
  
                // Remove the client handler from the list 
                clients.remove(this); 
  
                // Close the input and output streams and the client socket 
                in.close(); 
                out.close(); 
                clientSocket.close(); 
            } catch (IOException e) { 
                e.printStackTrace(); 
            } 
        } 
  
        // Get the username from the client 
        private String getUsername() throws IOException { 
            out.println("Enter your username:"); 
            return in.readLine();
        } 
  
        
        public void sendMessage(String message) { 
            out.println(message); 
            out.println("Type Your Message"); 
        } 
    } 
} 