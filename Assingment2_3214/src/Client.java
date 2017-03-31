/*
 * Copyright (c) 1995, 2013, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 
import java.io.*;
import java.net.*;
import java.util.Scanner;
public class Client {	
	private static int portNumber = 21453;												//pre- declaring port number for server
	private static String ipAddress = "127.0.0.1";										// predeclaring ip address as local
	private static int TalkingPort;														// variable to store my listening port
	private static String empty = "Finish";												// just a string for comparsion
	public static void main(String[] args) throws IOException, InterruptedException {	
		Scanner sc = new Scanner(System.in);											// Initializing Scanner sc
		System.out.print("USAGE:- JOIN == Connect \n	LEAVE == Exit \n	LIST == Lists all user Connected \n	CONNECT == For  chatting with a peer \n"
				+ "	ACCEPT ==  to Accept Connection from peer. \n");					// Just printing the Usage of programe
		System.out.print("Please Enter Server Ip-Address: -");							// Asks for server ip
		ipAddress = sc.nextLine();														// store it in ipAddress variable
		try (	Socket echoSocket = 	new Socket(ipAddress, portNumber);				// Initializing Socket 	
				PrintWriter out = 		new PrintWriter(echoSocket.getOutputStream(), true);	// declaring out stream
				BufferedReader in =		new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));) {	// declaring in stream
			out.println(ipAddress);														// Send it server
			System.out.print("Please Enter your Listening Port: -");					// Enter your listening port
			TalkingPort = Integer.parseInt(sc.nextLine());
			out.println(TalkingPort); 													// send it server
			ClientThread a = new ClientThread(TalkingPort);
			a.start();
			String InputLine = sc.nextLine();
			
			while (!(InputLine.equals("LEAVE"))){ 
				if(InputLine.equals("JOIN")){
					out.println(InputLine);
					System.out.println(in.readLine());
				}
				if(InputLine.equals("LIST")){
					out.println(InputLine);
					String temp = in.readLine();
					while(!(temp.equals(empty))){
						System.out.println(temp);
						temp = in.readLine();
					}
				}
					//--------------
					//Part B
					//--------------
				if (InputLine.equals("ACCEPT")){
					while (!(InputLine = sc.nextLine()).equals("CLOSE")){
						a.networkOutput.println(TalkingPort+" >"+InputLine);
					}
					a.clientSocket.close();
				}

				if (InputLine.equals("CONNECT")){
					//Create new ClientServer(listing port)
					System.out.print("Please Enter your friends portNumber: -");
					int portT = Integer.parseInt(sc.nextLine());
					System.out.print("Please Enter your friends Ip Address: -");
					String RemoteIp = sc.nextLine();
					Socket Psocket = new Socket(RemoteIp,portT); // Connect to another peer


					//--------------
					//Part A
					//--------------
					PrintWriter talkout = new PrintWriter(Psocket.getOutputStream(), true);
					BufferedReader talkin = new BufferedReader(new InputStreamReader(Psocket.getInputStream()));

					//Read from console
					String into = sc.nextLine();

					while(!(into.equals("LEAVE"))){
						talkout.println(TalkingPort+" >"+into);// Send message to another peer
						//Thread.sleep(4000);
						System.out.println(talkin.readLine());

						//Read next message from the console
						into = sc.nextLine();
					}
					System.out.println("You are disconnected");
					Psocket.close();
				}	
				InputLine = sc.nextLine();
			}
			if (InputLine.equals("LEAVE")){
				out.println(InputLine);
				System.out.print(in.readLine());
				System.exit(1);	
			}

		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + ipAddress);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " + ipAddress);
			System.exit(1);
		} 
	}
}