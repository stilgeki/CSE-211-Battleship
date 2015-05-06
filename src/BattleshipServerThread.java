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

public class BattleshipServerThread extends Thread {
	int[][] player1Board, player2Board;
	String playername1, playername2;
	
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
		
	}

}
