import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientThread extends Thread {
	private int TalkingPort;																	// create an int for your listening port											
	public PrintWriter networkOutput;															// Declare networkOutput
	public ServerSocket clientSocket;															// Declare clientSocket
	public ClientThread(int port) {																// Constructor
		this.TalkingPort = port;
	}
	public void run() {   
    		   	try{
    		   				this.clientSocket =	new ServerSocket(TalkingPort);						// create  the Socket
    		   				Socket socket = clientSocket.accept();									// accept the connection
	        				this.networkOutput =			new PrintWriter(socket.getOutputStream(), true);    	// Creating outputstream               
	        				BufferedReader networkIn = 			new BufferedReader(new InputStreamReader(socket.getInputStream()));  // defined inputstream
	        				System.out.println("You have a request from a peer. Accept?"); 			// asks client to connect or not
	        				String inputLine;														// String to store other peer message 
	        				while(!(inputLine = networkIn.readLine()).equals("LEAVE")){				// compare the message wheather its leave or not if its leave then branch
	            					System.out.println(inputLine);									// prints the peer message
	        				}
	        				System.out.println("You Are disconnected");								// tells user that its disconnected
	        				socket.close();															// close the socket
    		   	}catch(Exception x){																// Throws exception if any
    		   		System.out.println(x.getMessage());
    		   	}
    	         
    	}

}