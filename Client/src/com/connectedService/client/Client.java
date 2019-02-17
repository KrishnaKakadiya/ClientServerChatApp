package com.connectedService.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
	private final static String KEY_GET = "get";
	private final static String KEY_PUT = "put";

	public static void main(String[] args) {
		String userInput = null;
		String host = null;
		int port = -1;
		try {
			
			BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
			
			// get server ip and port from user
			System.out.print("Enter Server IP Address: ");
			host = inFromUser.readLine();
			System.out.print("Enter Server Port: ");
			try{
				port = Integer.parseInt(inFromUser.readLine());
			}
			catch(Exception e)
			{
				System.out.println("Please enter proper port...Exiting...");
				return;
			}
			
			Socket clientSocket = new Socket(host, port);

			DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
			DataInputStream in = new DataInputStream(clientSocket.getInputStream());

			// print menu for user
			printMenu();
			while (true) {
				try {
					System.out.print(">");
					// read command from user
					userInput = inFromUser.readLine();
					
					// if input is quit, then exit socket and quit program
					if ((userInput == null) || userInput.equalsIgnoreCase("QUIT")) {
						out.writeUTF(userInput);
						clientSocket.close();
						return;
					} 
					// if command other then quit
					else {
						
						int spacePosition = userInput.indexOf(' ');
						String inputCommand = userInput.substring(0,spacePosition);
						
						if(spacePosition<=0){
							System.out.println("Illegal command " + userInput);
							out.writeUTF("Illegal command");
							out.flush();
						} 
						else {
							// if get command is passed by user 
							// and download the file from server
							if (inputCommand.equalsIgnoreCase(KEY_GET)) {
								String fileName = userInput.substring(spacePosition + 1);
								out.writeUTF(userInput);
								
								String fileData = in.readUTF();
								if(FileHandler.saveFile(fileName, fileData)){
									System.out.println("File Downloaded on Client: " + fileName);
								}
								else
								{
									System.out.println("Error Occoured while Downloading File");
								}
								out.flush();
							}
							// if put command is passed, then pass the command 
							// and upload the file to server
							if (inputCommand.equalsIgnoreCase(KEY_PUT)) {
								String fileName = userInput.substring(spacePosition + 1);
								String fileData = FileHandler.readFile(fileName);
								out.writeUTF(userInput);
								
								if(fileData == null || fileData.equals(""))
								{
									System.out.println("File NotFound or is Blank");	
								}
								else{
									out.writeUTF(fileData);
								}
								out.flush();
								System.out.println("File Uploaded on Server: " + fileName);
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}
	}

	private static void printMenu() {
		System.out.println("Syntax : ");
		System.out.println("a) get <FileName> ");
		System.out.println("b) put <FileName> ");
		System.out.println("c) quit ");
		System.out.println();
	}
}
