import java.util.ArrayList;
import java.util.List;

public class CheckersGame implements Game<CheckersState, CheckersAction, CheckersPlayer> {

	private CheckersPlayer[] players;
	
	public CheckersGame() {
		//currentState = new CheckersState(new int[8][8], CheckersData.RED);
		players = new CheckersPlayer[2];
		players[0] = new CheckersPlayer(CheckersData.RED);
		players[1] = new CheckersPlayer(CheckersData.BLACK);
	}
	

	@Override
	public CheckersState getInitialState() {
		return new CheckersState(new int[8][8], CheckersData.RED);
	}

	@Override
	public CheckersPlayer[] getPlayers() {
		return players;
	}

	@Override
	public CheckersPlayer getPlayer(CheckersState state) {
		return state.getPlayerTurn();
	}

	@Override
	public List<CheckersAction> getActions(CheckersState state) {
		CheckersState stateCopy = state.deepCopyBoard();
		ArrayList<CheckersAction> ret = stateCopy.getActions(state.getPlayerTurn());
		return ret;
	}

	@Override
	public CheckersState getResult(CheckersState state, CheckersAction action) {
		ArrayList<CheckersMove> moveSequenceList = action.getMoveSequenceList();
		CheckersState stateCopy = state.deepCopyBoard();
		CheckersData data = new CheckersData(stateCopy.getBoardPieces()); //cloned
		for (int i = 0; i<moveSequenceList.size(); i++) {
			data.makeMove(moveSequenceList.get(i));
		}
		CheckersState newState = new CheckersState(data.getBoardIntArr(), players[0].getColorRedorBlack());
		if (state.getPlayerTurn().getColorRedorBlack() == players[0].getColorRedorBlack()) {
			newState.setPlayerTurn(players[1]); 
		}
		return newState;
	}

	@Override
	public boolean isTerminal(CheckersState state) {
		if (state.getActions(state.getPlayerTurn()).equals(null)) {
			return false;
		}
		return true;
	}

	@Override
	public double getUtility(CheckersState state, CheckersPlayer player) {
		return getUtility4(state, player);
	}
	
	//simple score based on the number of pieces on the board
	public double getUtility1(CheckersState state, CheckersPlayer player) {
		//first identify the pieces
		int color = player.getColorRedorBlack();
		int colorKing = color + 1;
		int opponent = players[0].getColorRedorBlack();
		if (player.equals(players[0])) {
			opponent = players[1].getColorRedorBlack();
		}
		int oppKing = opponent + 1;
		
		int numColor = 0;
		int numColorKing = 0;
		int numOpp = 0;
		int numOppKing = 0;
		
		//loop through board
		int[][] board = state.getBoardPieces();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] == color)
					numColor++;
				if (board[i][j] == colorKing)
					numColorKing++;
				if (board[i][j] == opponent)
					numOpp++;
				if (board[i][j] == oppKing)
					numOppKing++;
			}
		}
		int score = numColor + numColorKing*2 - numOpp - numOppKing*2;
		return score;
	}
	
	//pieces further down the board weighted higher
	public double getUtility2(CheckersState state, CheckersPlayer player) {
		//first identify the pieces
		int color = player.getColorRedorBlack();
		int colorKing = color + 1;
		int opponent = players[0].getColorRedorBlack();
		if (player.equals(players[0])) {
			opponent = players[1].getColorRedorBlack();
		}
		int oppKing = opponent + 1;
		
		double numColor = 0;
		double numColorKing = 0;
		double numOpp = 0;
		double numOppKing = 0;
		
		//loop through board
		int[][] board = state.getBoardPieces();
		double k = 2.0;
		double kingVal = 8;
		if (color == CheckersData.BLACK) {
			for (int i = 1; i <= 8; i++) {
				for (int j = 1; j <= 8; j++) {
					if (board[i-1][j-1] == color)
						numColor += i/k;
					if (board[i-1][j-1] == colorKing)
						numColorKing += kingVal;
					if (board[i-1][j-1] == opponent)
						numOpp += (9-i)/k;
					if (board[i-1][j-1] == oppKing)
						numOppKing += kingVal;
				}
			}
		}
		else {
			for (int i = 1; i <= 8; i++) {
				for (int j = 1; j <= 8; j++) {
					if (board[i-1][j-1] == color)
						numColor += (9-i)/k;
					if (board[i-1][j-1] == colorKing)
						numColorKing += kingVal;
					if (board[i-1][j-1] == opponent)
						numOpp += i/k;
					if (board[i-1][j-1] == oppKing)
						numOppKing += kingVal;
				}
			}
		}
		//kings anywhere get score higher than max score of pawn
		double score = numColor + numColorKing - numOpp - numOppKing;
		return score;
	}
	
	//since pieces on the edge of the board cannot be jumped, they are in a desirable position. They get a bonus to their score
	public double getUtility3(CheckersState state, CheckersPlayer player) {
		//first identify the pieces
		int color = player.getColorRedorBlack();
		int colorKing = color + 1;
		int opponent = players[0].getColorRedorBlack();
		if (player.equals(players[0])) {
			opponent = players[1].getColorRedorBlack();
		}
		int oppKing = opponent + 1;
		
		double numColor = 0;
		double numColorKing = 0;
		double numOpp = 0;
		double numOppKing = 0;
		
		//loop through board
		int[][] board = state.getBoardPieces();
		double k = 2;
		double kingVal = 8;
		double edgeVal = 3;
		if (color == CheckersData.BLACK) {
			for (int i = 1; i <= 8; i++) {
				for (int j = 1; j <= 8; j++) {
					if (board[i-1][j-1] == color) {
						numColor += i/k;
						if (i == 0 || i == 7 || j == 0 || j == 7) {
							numColor += edgeVal;
						}
					}
					if (board[i-1][j-1] == colorKing) {
						numColorKing += kingVal;
						if (i == 0 || i == 7 || j == 0 || j == 7) {
							numColorKing += edgeVal;
						}
					}
					if (board[i-1][j-1] == opponent) {
						numOpp += (9-i)/k;
						if (i == 0 || i == 7 || j == 0 || j == 7) {
							numOpp += edgeVal;
						}
					}
					if (board[i-1][j-1] == oppKing) {
						numOppKing += kingVal;
						if (i == 0 || i == 7 || j == 0 || j == 7) {
							numOppKing += edgeVal;
						}
					}
				}
			}
		}
		else {
			for (int i = 1; i <= 8; i++) {
				for (int j = 1; j <= 8; j++) {
					if (board[i-1][j-1] == color) {
						numColor += (9-i)/k;
						if (i == 0 || i == 7 || j == 0 || j == 7) {
							numColor += edgeVal;
						}
					}
					if (board[i-1][j-1] == colorKing) {
						numColorKing += 10;
						if (i == 0 || i == 7 || j == 0 || j == 7) {
							numColorKing += edgeVal;
						}
					}
					if (board[i-1][j-1] == opponent) {
						numOpp += i/k;
						if (i == 0 || i == 7 || j == 0 || j == 7) {
							numOpp += edgeVal;
						}
					}
					if (board[i-1][j-1] == oppKing) {
						numOppKing += 10;
						if (i == 0 || i == 7 || j == 0 || j == 7) {
							numOppKing += edgeVal;
						}
					}
				}
			}
		}
		//kings anywhere get score of 10, higher than max score of pawn
		double score = numColor + numColorKing - numOpp - numOppKing;
		return score;
	}
	
	public double getUtility4(CheckersState state, CheckersPlayer player) {
		//first identify the pieces
		int color = player.getColorRedorBlack();
		int colorKing = color + 1;
		int opponent = players[0].getColorRedorBlack();
		if (player.equals(players[0])) {
			opponent = players[1].getColorRedorBlack();
		}
		int oppKing = opponent + 1;
		
		int numColor = 0;
		int numColorKing = 0;
		int numOpp = 0;
		int numOppKing = 0;
		
		double score = getUtility3(state, player);
		
		//loop through board
		int[][] board = state.getBoardPieces();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] == color)
					numColor++;
				if (board[i][j] == colorKing)
					numColorKing++;
				if (board[i][j] == opponent)
					numOpp++;
				if (board[i][j] == oppKing)
					numOppKing++;
			}
		}
		//if only kings left
		if (numColor == 0) {
			score = numColorKing*14 - numOppKing*20 - numOpp*10;
		}
		return score;
	}



}
