package com.connectedService.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerThread extends Thread {
	
	private final String KEY_GET = "get";
	private final String KEY_PUT = "put";

	protected Socket socket;

	public ServerThread(Socket clientSocket) {
		this.socket = clientSocket;
	}

	public void run() {
		System.out.println("Servicing Client...");
		DataInputStream in = null;
		DataOutputStream out = null;

		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());

			String inputLine;
			while (true) {
				try {
					
					// read command from socket
					inputLine = in.readUTF();
					
					// if command is quit, then close the socket
					if ((inputLine == null) || inputLine.equalsIgnoreCase("QUIT")) {
						socket.close();
						System.out.println("One Client Disconnected");
						return;
					} 
					else {
						
						int spacePosition = inputLine.indexOf(' ');
						String inputCommand = inputLine.substring(0,spacePosition);
						
						if(spacePosition<=0)
						{
							System.out.println("Illegal command " + inputLine);
							out.writeUTF("Illegal command");
							out.flush();
						}
						// if command is valid proceed
						else {
							if (inputCommand.equalsIgnoreCase(KEY_GET)) {
								String fileName = inputLine.substring(spacePosition + 1);
								String fileData = FileHandler.readFile(fileName);
								if(fileData == null || fileData.equals(""))
								{
									System.out.println("File not found or is blank");
								}
								else{
									out.writeUTF(fileData);
								}
								out.flush();
								System.out.println("File Sent to Client: " + fileName);
							}
							// if put command specified, then get the file from client 
							// and store it on server
							else if (inputCommand.equalsIgnoreCase(KEY_PUT)) {
								String fileName = inputLine.substring(spacePosition + 1);
								String fileData = in.readUTF();
								if(FileHandler.saveFile(fileName, fileData)){
									System.out.println("File Downloaded on Server: " + fileName);
								}
								else
								{
									System.out.println("Error Occoured while Downloading File");
								}
								out.flush();
							}
						}

					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		} catch (IOException e) {
			return;
		}
	}
}
