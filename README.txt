

Controller (Server)
	Send Socket Connection
	Store Ship Locations
	Check Guesses
	Store Player Name
	Store Player Statistics (Score)
	End Game
	
Player (Client)
	Connect to Socket
	Display Grid
	Allow Hiding
	Allow Guessing
	Name Entry
	Allow Quitting
	
	
Order of Events:

Clients 1 and 2 send IPA to Server
Server acknowledges and sends a socket connection to Clients
Clients accept socket connection
Server initiates game
Clients collect users player names and send to Server
Server stores player names
Clients collect users ship locations and send to Server
Server stores ship locations
Server selects player to go first and sends move request to that player's Client
Client collects move from user and sends move to Server
Server checks move against other Client's ship locations
Server sends move result to both Clients
Server updates statistics
Server sends move request to other Client 
{Repeat until termination.}

Tasklist
	Grammar
	Basic Networking Code
		Send and Recieve Messages
		Client
		Server
	Basic Battleship Game Logic
		Hide and Guess
	Integration of Networking and Game code
	Polishing of game code (completing all features)
	GUI code and implementation
	Final Testing

Deadline Goals
	Grammar (Sunday)
	Text based network ready prototype (Tuesday)
	GUI Final Version (Thursday)
	
Meetings
	Sunday 5/3/2015 - 1PM Benton Hall Basement Common Area
	TBD
	
Coding Guidelines
	Remember prelog comments
	Test-first (using J-Unit)