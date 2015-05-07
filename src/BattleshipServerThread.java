/**
 /**
 * Kendall Stilgenbauer & Nathan LeVasseur
 * Instructor: Dr. Kiper
 * CSE 211, Section A
 * 
 * This class implements the server thread functionality for a
 * networked Battleship game.  This includes primarily the game logic
 * code, because each thread handles one instance of a game.  The
 * actual networking code responsible for establishing connections
 * with clients is in the BattleshipServer class.
 */

import java.io.*;
import java.net.*;

public class BattleshipServerThread extends Thread {
	
	protected Socket socket;
	int[][] player1Board, player2Board;
	String playername1, playername2;
	
	/**
	 * Constructor for the BattleshipServerThread.  After creation,
	 * the BattleshipServerThread handles two clients playing a game
	 * of Battleship.
	 * 
	 * Requires: Socket for clients.
	 * 
	 * Effects: Allows for playing of game.
	 * 
	 * Modifies: Nothing.
	 */
	public BattleshipServerThread(Socket socket) {
		super();
		
		this.socket = socket;
	} // end BattleshipServerThread
	
	/**
	 * Run method for the thread.  It calls methods necessary to run
	 * one game of Battleship.
	 * 
	 * Requires: Nothing.
	 * 
	 * Effects: Runs a game of Battleship.
	 * 
	 * Modifies: Nothing.
	 */
	public void run(){
		newGame();
		//get names from clients setName (int playerID, string name)
		//get ship locations from players hideShip() <- threading issues?
		//start game loop
		//send P1 guess request message
		//recieve guess checkGuess()
		//send guess result
		//send updated boards to both players
		//checkIfWin() if yes, exit loop, end game
		//send P2 guess request message, loop
		//end loop
		
	}
	
	
	/**
	 * Requires: Nothing.
	 * 
	 * Effects: sets up the boards to 10x10 arrays with all values 0 (empty) and instantiates the playernames to empty strings
	 * 
	 * Modifies: both boards and player name variables
	 */
	public void newGame(){
		player1Board = new int[10][10];
		player2Board = new int[10][10];
		for(int i = 0; i < 10; i ++){
			for(int j = 0; j < 10; j++){
				player1Board[i][j] = 0;
				player2Board[i][j] = 0;
			}
		}
		playername1 = "";
		playername2 = "";
	}

	
	/**
	 * Requires: A player's gameboard as a 2D int array, 
	 * a guess of two ints (x, y), 
	 * a char representing ship orientation 
	 * (h for horizontal, v for vertical), 
	 * and a number representing the ship to place.
	 * Shipnum 1 = Aircraft Carrier (5 spaces)
	 * Shipnum 2 = Battleship (4 spaces)
	 * Shipnum 3 = Submarine (3 spaces)
	 * Shipnum 4 = Destroyer (3 spaces)
	 * Shipnum 5 = Patrol Boat (2 spaces)
	 * 
	 * effects: adds the indicated ship starting at location x,y to the gameboard.
	 * returns an error if the ship doesn't fit, overlaps another ship, or has been placed already.
	 * returns an error if a value isn't specified.
	 * 
	 * modifies: playerboard by changing the values where the ship is placed.
	 * 
	 */
	public void placeShip(int[][] playerboard, int x, int y, char orient, int shipNum){
		int shipSize = 0;
		if(shipNum == 1){
			shipSize = 5;
		}else if(shipNum == 2){
			shipSize = 4;
		}else if(shipNum == 3 || shipNum == 4){
			shipSize = 3;
		}else if(shipNum == 5){
			shipSize = 2;
		}
		boolean taken = false;
		if((orient == 'h' && (shipSize + y > 9)) || (orient == 'v' && (shipSize + x > 9))){
			//Entry is invalid as it would go off of board
		}else{
			if(orient == 'h'){
				for(int i = y; i <= y + shipSize; i++){
					if(playerboard[x][i] != 0){
						//Ship is already hidden in a space
						taken = true;
					}
				}
				if(taken = false){
					for(int j = y; j <= y + shipSize; j++){
						playerboard[x][j] = shipNum;
					}
				}
			}else if(orient == 'v'){
				for(int i = x; i <= x + shipSize; i++){
					if(playerboard[i][y] != 0){
						//Ship is already hidden in a space
						taken = true;
					}
				}
				if(taken = false){
					for(int j = x; j <= x + shipSize; j++){
						playerboard[j][y] = shipNum;
					}
				}
			}else{
				//orient is invalid
			}
		}
	}

	
	/**
	 * Requires: a player board, and a two int guess (x,y)
	 * 
	 * Effects: Delivers a message to the client with notification of the status of the guess, either hit (the guess was a ship),
	 * miss (the guess was an empty space), or invalid (the guess was already attempted earlier in the game), the square is then set to a
	 * number depending on what was hidden there.
	 * 
	 * Modifies: The space guessed (board[x][y]) is set to -6 if empty or -shipNum if hiding a ship.
	 */
	public void checkGuess(int [][] board, int x, int y){
		if(board[x][y] == 0){
			//Deliver miss message
			board[x][y] = -6;
		}else if (board[x][y] < 0){
			//Deliver repeat guess message
		}else{
			//Deliver hit message
			board[x][y] -= 2*(board[x][y]);
			if(checkIfSunk(board, board[x][y])){
				//Deliver sunk message
			}
		}
	}
	
	
	/**
	 * Checks if a hit ship has been sunk.
	 * 
	 * Requires: A board and a shipNum identifier.
	 * 
	 * Effects: Returns true if the ship was sunk, false if not.
	 * 
	 * Modifies: Nothing.
	 */
	protected boolean checkIfSunk(int[][] board, int shipNum){
		int count = 0;
		int shipSize = 0;
		if(shipNum == 1){
			shipSize = 5;
		}else if(shipNum == 2){
			shipSize = 4;
		}else if(shipNum == 3 || shipNum == 4){
			shipSize = 3;
		}else if(shipNum == 5){
			shipSize = 2;
		}
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++){
				if(board[i][j] == shipNum){
					count ++;
				}
			}
		}
		if(count == shipSize){
			return true;
		}
		return false;
	}
	
	
	/**
	 * Checks if the player has won after sinking a ship.
	 * 
	 * Requires: A board.
	 * 
	 * Effects: Returns true if the player has won, false if not.
	 * 
	 * Modifies: Nothing.
	 */
	protected boolean checkIfWin(int[][] board){
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++){
				if(board[i][j] > 0){
					return false;
				}
			}
		}
		return true;
	}
	
	
	/**
	 * Sets the player's name on the server side.
	 * 
	 * Requires: playerID, which identifies the player, and name.
	 * 
	 * Effects: Sets the player's name.
	 * 
	 * Modifies: Variables playername1 or playername2 (String).
	 */
	protected void setName (int playerID, String name){
		if(playerID == 1){
			playername1 = name;
		}else if(playerID == 2){
			playername2 = name;
		}else{
			//deliver invalid player name message
		}
	}
}