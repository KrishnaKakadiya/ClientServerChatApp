package com.connectedService.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private static final int PORT = 2422;

	public static void main(String[] args) {
		int clientCount = -1;
		try {
			// create welcoming socket at port 2422
			ServerSocket serverSocket = new ServerSocket(PORT);

			Socket socket = null;

			System.out.println("Server Started...");
			System.out.println("Listening on Port: " + PORT);
			while (true) {
				try {
					// wait for a client to connect server
					System.out.println("Waiting for new client... ");
					socket = serverSocket.accept();
					System.out.println("Connected to a client... " + (++clientCount));
					
					// service the client request using a new thread
					serviceClient(socket);
				} catch (IOException e) {
					System.out.println("I/O error: " + e);
				}
			}
		}
		catch (Exception ex) {
			System.out.println("Error: " + ex);
		}
	}

	public static void serviceClient(Socket socket) {
		try {
			// new thread for a client
			new ServerThread(socket).start();
		
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
