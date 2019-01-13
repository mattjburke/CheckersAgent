# CheckersAgent
An agent uses alpha beta search to determine its next move in the game of checkers. Implemented using an existing GUI so that a person can play against the agent.

To run this program, run the Checkers.java file. A GUI of a checkers game will pop up in  a new window, 
with the user as red and the computer as black. The pieces you can select to move will be highlighted in
blue, and when you click on a movable piece, your options of where to move will be highlighted in green. 
The computer will then take its turn.

Acknowledgements: 
The GUI code containing Checkers, CheckersMove, and CheckersData was downloaded from 
http://math.hws.edu/eck/cs124/javanotes5/source/Checkers.java.
The method computerTakeTurn() (called on line 347) was added to these files, as well as some minor methods 
like updateGui() and toString() for debugging purposes. If the method computerTakeTurn() is commented out, 
two humans can play against each other using the GUI.

AlphaBetaSearch, Game, AdversarialSearch, and Metrics were imported from a textbook's online repository 
at https://github.com/aimacode/aima-java/tree/AIMA3e/aima-core/src/main/java/aima/core/search/adversarial.

I created CheckersGame, CheckersState, CheckersAction, and CheckersPlayer by myself.
