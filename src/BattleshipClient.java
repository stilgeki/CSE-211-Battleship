/**
 * Kendall Stilgenbauer & Nathan LeVasseur
 * Instructor: Dr. Kiper
 * CSE 211, Section A
 * 
 * This class implements the client functionality for a networked
 * Battleship game.  This includes connecting to the server and
 * handling a game through to completion.
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class BattleshipClient {

	Socket socket = null;
	DataOutputStream dos = null;
	DataInputStream dis = null;
	Scanner readFromConsole = null;
	InetAddress serverIp = null;
	static final int SERVER_PORT = 32100;
	boolean gameStatus = false;
	String protocolMessage;
	int playerID;
	String playerName = "Player";
	int guessRow;
	int guessColumn;
	
	
	public static void main(String[] args) {
		new BattleshipClient();
	} // end main
	
	
	/**
	 * Constructor for the BattleshipClient.  It calls methods
	 * necessary to instantiate a client and interact with a server
	 * to play the game.
	 * 
	 * Requires: Nothing.  This is the beginning of things happening.
	 * 
	 * Effects: A game is started and played.
	 * 
	 * Modifies: Nothing.
	 */
	public BattleshipClient() {
		startGame();
		
		// start main game loop
		do {
			printBoard();
			readGuess();
			receiveMessages();
		} while (gameStatus);
		
		closeSocketAndStreams();
	} // end BattleshipClient
	
	
	/**
	 * Performs operations and calls methods necessary to start the
	 * game of Battleship for the player.
	 * 
	 * Requires: Nothing.
	 * 
	 * Effects: Creates the server connection and output/input streams
	 * 			for playing of the game.
	 * 
	 * Modifies: Variable gameStatus (boolean).
	 */
	protected void startGame() {
		System.out.println("Welcome to BATTLESHIP!");
		getPlayerName();
		getServerAddress();
		createSocket();
		createStreams();
		//createBoard();
		
		gameStatus = true;
	} // end startGame
	
	
	/**
	 * Gets the name by which the player wishes to be identified when
	 * playing the game.
	 * 
	 * Requires: Nothing.
	 * 
	 * Effects: Retrieves the player name from the player.
	 * 
	 * Modifies: Variable playerName (String).
	 */
	protected void getPlayerName() {
		readFromConsole = new Scanner(System.in);
		
		System.out.println("Enter your name or press enter to "
							+ "accept default: ");
		
		playerName = readFromConsole.nextLine();
		
		System.out.println("Okay, I will call you " + playerName + ".");
	}
	
	
	/**
	 * Asks the player for the IP address of the server that will
	 * be used to host the game.  If nothing is provided, the client
	 * will assume the local host is the server.
	 * 
	 * Requires: Nothing.
	 * 
	 * Effects: Retrieves the server's IP address from the player.
	 * 
	 * Modifies: Variable serverIp (InetAddres).
	 */
	protected void getServerAddress() {
		readFromConsole = new Scanner(System.in);
		
		System.out.println("Enter the IP address of the server or "
							+ "hit enter for local host: ");
		
		String serverIpString = "";
		serverIpString = readFromConsole.nextLine();
		
		if(serverIpString.isEmpty()) {
			serverIpString = "127.0.0.1";
		}
		
		System.out.println("\nServer IP is " + serverIpString);
		
		try {
			serverIp = InetAddress.getByName(serverIpString);
		} catch (UnknownHostException e) {
			System.err.println("Could not convert String to InetAddress");
			e.printStackTrace();
		}
	} // end getServerAddress
	
	
	/**
	 * Creates the socket for communication with the server.
	 * 
	 * Requires: the server's IP address and port number (port number
	 * 			 is hard-coded as a specific value).
	 * 
	 * Effects: Creates a socket for server communication.
	 * 
	 * Modifies: Variable socket (Socket).
	 */
	protected void createSocket() {
		try {
			socket = new Socket(serverIp, SERVER_PORT);
		} catch (UnknownHostException e) {
			System.err.println("Could not get server IP address.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Could not create socket.");
			e.printStackTrace();
		}
	} // end createSocket
	
	
	/**
	 * Creates data input and output streams for sending messages to
	 * and receiving messages from the server.
	 * 
	 * Requires: A created socket connection with the server.
	 * 
	 * Effects: Data input and output streams are created for server
	 * 			communication.
	 * 
	 * Modifies: Variables dos (DataOutputStream) and dis (DataInputStream).
	 */
	protected void createStreams() {
		try {
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			System.err.println("Could not create output stream.");
			e.printStackTrace();
		}
		
		try {
			dis = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			System.err.println("Could not create input stream.");
			e.printStackTrace();
		}
	} // end createStreams
	
	
	/**
	 * Receives messages from the server.  Based on the protocol message
	 * received, a new method is called that handles the functionality.
	 * 
	 * Requires: The socket and input/output streams must be operational.
	 * 
	 * Effects: Receives a message, checks it against the protocol,
	 * 			then offloads the work to a new method.
	 * 
	 * Modifies: Variables dis (DataInputStream) and dos (DataOutputStream).
	 */
	protected void receiveMessages() {
		protocolMessage = "";
		try {
			protocolMessage = dis.readUTF();
		} catch (IOException e) {
			System.err.println("Could not receive message code.");
			e.printStackTrace();
		}
		
		if (protocolMessage.contains("getName")) {
			try {
				dos.writeBytes("sendName " + playerName);
			} catch (IOException e) {
				System.err.println("Could not send player name.");
				e.printStackTrace();
			}
		} else if (protocolMessage.contains("getShips")) {
			//sendShips();
		} else if (protocolMessage.contains("yourTurn")) {
			readGuess();
		} else if (protocolMessage.contains("hit")) {
			try {
				playerID = dis.readInt();
			} catch (IOException e) {
				System.err.println("Could not get player ID.");
				e.printStackTrace();
			}
			
			//updateBoard(playerID, 1); // playerID tells updateBoard which
								   	  // board to update, 1 is hit code
		} else if (protocolMessage.contains("miss")) {
			try {
				playerID = dis.readInt();
			} catch (IOException e) {
				System.err.println("Could not get player ID.");
				e.printStackTrace();
			}
			
			//updateBoard(playerID, 0); // 0 is miss code
			
		} else if (protocolMessage.contains("sunk")) {
			try {
				playerID = dis.readInt();
			} catch (IOException e) {
				System.err.println("Could not get player ID.");
				e.printStackTrace();
			}
			
			// deal with sunk ship somehow
			//updateBoard(playerID, );
		}
	}
	

	
	/**
	 * Gets the guess from the player with a Scanner object.
	 * 
	 * Requires: The Scanner object.
	 * 
	 * Effects: Retrieves the player's guess.
	 * 
	 * Modifies: Nothing.
	 */
	public void readGuess(){
		int guessX, guessY = -1;
		System.out.print("Enter your guess row (0-9): ");
		guessX = readFromConsole.nextInt();
		System.out.print("Enter your guess column (0-9): ");
		guessY = readFromConsole.nextInt();
		//Send guess to server for checkGuess(otherplayersboard, guessX, guessY)
	}
	
	
	/**
	 * Prints the boards for the player.
	 * 
	 * Requires: The two boards, the player's and the opponent's.
	 * 
	 * Effects: Prints the boards.
	 * 
	 * Modifies: Nothing.
	 */
	public void printBoard(){
		int[][] myBoard = new int[10][10]; // 
		//get player's board from server
		int[][] oppBoard = new int[10][10];//
		//get opponent's board from server
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++){
				if(myBoard[i][j] == 0){
					System.out.print("-");
				}else{
					System.out.print(myBoard[i][j]);
				}
			}
			System.out.println("");
		}
		myBoard = new int[10][10]; // 
		//get player's board from server
		oppBoard = new int[10][10];//
		//get opponent's board from server
		for(int x = 0; x < 10; x++){
			for(int y = 0; y < 10; y++){
				if(oppBoard[x][y] >= 0){
					System.out.print("-");
				}else if(oppBoard[x][y] == -6){
					System.out.print("X");
				}else{
					System.out.print(oppBoard[x][y] + (oppBoard[x][y]*2));
				}
			}
			System.out.println("");
		}
	}

	/**
	 * Closes the communication socket and streams when the game is over.
	 * 
	 * Requires: The game has ended.  Socket and streams were created in
	 * 			 the first place.
	 * 
	 * Effects: Closes the socket and streams.
	 * 
	 * Modifies: Variables socket (Socket), dos (DataOutputStream), and
	 * 			 dis (DataInputStream).
	 */
	protected void closeSocketAndStreams() {
		try {
			socket.close();
		} catch (IOException e) {
			System.err.println("Could not close socket.");
			e.printStackTrace();
		}
		
		try {
			dos.close();
			dis.close();
		} catch (IOException e) {
			System.err.println("Could not close streams.");
			e.printStackTrace();
		}
	} // end closeSocketAndStreams

}
