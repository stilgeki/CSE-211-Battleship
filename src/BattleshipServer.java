/**
 * Kendall Stilgenbauer & Nathan LeVasseur
 * Instructor: Dr. Kiper
 * CSE 211, Section A
 * 
 * This class implements the server functionality for a networked
 * Battleship game.  This includes exclusively the networking code
 * for the server.  Individual games are handled by a server thread,
 * and therefore game logic code is in the BattleshipServerThread
 * class.
 */


import java.io.*;
import java.net.*;


public class BattleshipServer {
	
	static final int SERVER_PORT = 32100;
	ServerSocket serverSocket = null;
	Socket clientSocket = null;
	DataOutputStream dos = null;
	DataInputStream dis = null;

	public static void main(String[] args) {
		new BattleshipServer();
	} // end main
	
	
	/**
	 * Constructor for the BattleshipServer class.  Once instantiated,
	 * it calls methods necessary for the creation of an instance of
	 * the server.
	 * 
	 * Requires: Nothing.  This is the beginning of the program.
	 * 
	 * Effects: A server is started and run.
	 * 
	 * Modifies: Nothing.
	 */
	public BattleshipServer() {
		createServerSocket();
		displayContactInfo();
		listenForClients();
		closeServer();
	} // end BattleshipServer
	
	
	/**
	 * Creates the server socket.  It prints an error message if the
	 * server socket could not be created.
	 * 
	 * Requires: The server port number.
	 * 
	 * Effects: Creates the server socket.
	 * 
	 * Modifies: Variable serverSocket (ServerSocket).
	 */
	protected void createServerSocket() {
		try {
			serverSocket = new ServerSocket(SERVER_PORT);
		} catch (IOException e) {
			System.err.println("Server socket could not be created.");
			e.printStackTrace();
		}
	} // end createServerSocket
	
	
	/**
	 * Displays contact info of the server, such as IP address and
	 * port number.  The purpose of this is so that if the server is
	 * on a different computer than the clients, the owner of the
	 * server can easily give their IP info to the client(s).
	 * 
	 * Requires: Server socket and IP address.
	 * 
	 * Effects: Displays server contact info.
	 * 
	 * Modifies: Nothing.
	 */
	protected void displayContactInfo() {
		try {
			//Display contact information
			System.out.println("Battleship Server standing by to "
					+ "accept Clients:"
					+ "\nIP : " + InetAddress.getLocalHost()
					+ "\nPort : " + serverSocket.getLocalPort()
					+ "\n\n");
		} catch (UnknownHostException e) {
			System.err.println("Could not find host information.");
			e.printStackTrace();
		}
	} // end displayContactInfo
	
	
	/**
	 * Listens for clients, and when one contacts the server it creates
	 * a thread to handle that client.  The do-while loop allows the
	 * server to handle multiple clients simultaneously by creating a
	 * new thread for each.
	 * 
	 * Requires: Server socket.
	 * 
	 * Effects: Connects to clients and spawns a new thread for them
	 * 			to play a game.
	 * 
	 * Modifies: Nothing.
	 */
	protected void listenForClients() {
		do {
			try {
				new BattleshipServerThread(serverSocket.accept()).start();
			} catch (IOException e) {
				System.err.println("Could not create thread.");
				e.printStackTrace();
			}
		} while (true);
	} // end listenForClients
	
	
	/**
	 * Closes the server when the server is ready to shut down.
	 * 
	 * Requires: Server socket.
	 * 
	 * Effects: Closes the server socket.
	 * 
	 * Modifies: Variable serverSocket (ServerSocket).
	 */
	protected void closeServer() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			System.err.println("Could not close server socket.");
			e.printStackTrace();
		}
	} // end closeServer

}
