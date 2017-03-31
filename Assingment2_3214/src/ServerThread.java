import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread{
    	private Socket socket;
    	private static String Join = "JOIN", Leave = "LEAVE", List = "LIST";
    	private ClientObject a = new ClientObject();
    	public ServerThread(Socket socket){
    		this.socket = socket;
    	}

		public void run(){
    		   try (	
    	        		PrintWriter out =				new PrintWriter(socket.getOutputStream(), true);                   
    	        		BufferedReader in = 			new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
    	            	String inputLine =				in.readLine();
    	            	a.ipAddress = inputLine;
    	            	//Server.ipAddress.add(inputLine);
    	            	inputLine =						in.readLine();
    	            	a.portNumebr = Integer.parseInt(inputLine);
    	            	int pt = Integer.parseInt(inputLine);
    	            	//Server.PortNumberUser.add(inputLine);
    	            	Server.clientList.add(a);
    	            	while ((inputLine = in.readLine()) != null) {
    	            		System.out.println("Waiting for Connection.......");
    	            		System.out.print(inputLine);
    	            		if(inputLine.equals(Join)){
    	            			out.println("Connected");
    	            		}else if (inputLine.equals(Leave)){
    	            			for(int i =0; i< Server.clientList.size(); i++){
    	            				if(pt ==(Server.clientList.get(i).portNumebr)){
    	            					Server.clientList.remove(i);
    	            					//Server.ipAddress.remove(i);
    	            					out.println("You Are Disconnected");
    	            					socket.close();
    	            					if(Server.clientList.size() ==0){
    	            						Server.serverSocket.close();
    	            						System.exit(1);
    	            					}
    	            				}
    	            			}
    	            		}else if(inputLine.equals(List)){
    	            			String s;
    	            			for(int i =0; i<Server.clientList.size(); i++){
    	            				s = ("Ip Address:- "+Server.clientList.get(i).ipAddress+" PortNumber:- "+ Server.clientList.get(i).portNumebr);
    	            					out.println(s);
    	            				}
    	            				out.println("Finish");
    	            		}
    	            	}
    	        } catch (IOException e) {
    	            System.out.println("Exception caught when trying to listen on port "
    	                + Server.portNumber + " or listening for a connection");
    	            System.out.println(e.getMessage());
    	        }
    	}
    	
    	
    }