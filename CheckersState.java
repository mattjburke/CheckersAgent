import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckersState {

	private int[][] boardPieces;
	private CheckersState parent;
	private ArrayList<CheckersState> children;
	private int evalScore;
	private CheckersPlayer playerTurn;
	
	public CheckersState(int[][] boardPieces, int player) {
		this.boardPieces = boardPieces;
		this.playerTurn = new CheckersPlayer(player);
	}
	
	public CheckersState deepCopyBoard() {
		int[][] newBoard = new int[8][8];
		CheckersState duplicate = new CheckersState(newBoard, playerTurn.getColorRedorBlack());
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				newBoard[i][j] = boardPieces[i][j];
			}
		}
		return duplicate;	
	}
	
	
	public int[][] getBoardPieces() {
		return boardPieces;
	}
	public void setBoardPieces(int[][] boardPieces) {
		this.boardPieces = boardPieces;
	}


	public CheckersState getParent() {
		return parent;
	}


	public void setParent(CheckersState parent) {
		this.parent = parent;
	}


	public ArrayList<CheckersState> getChildren() {
		return children;
	}


	public void setChildren(ArrayList<CheckersState> children) {
		this.children = children;
	}
	
	public void addChild(CheckersState child) {
		children.add(child);
	}


	public int getEvalScore() {
		return evalScore;
	}


	public void setEvalScore(int evalScore) {
		this.evalScore = evalScore;
	}
	
	public static int evaluateScore(int[][] boardPieces) {
		return 0;
	}
	
	public static int evaluateScore(CheckersState state) {
		return evaluateScore(state.getBoardPieces());
	}
	
	public static CheckersState getChild(CheckersState state, CheckersMove move) {
		return null;
	}


	public CheckersPlayer getPlayerTurn() {
		return playerTurn;
	}


	public void setPlayerTurn(CheckersPlayer playerTurn) {
		this.playerTurn = playerTurn;
	}
	
	public ArrayList<CheckersAction> getActions(CheckersPlayer player) {
		CheckersState stateCopy = this.deepCopyBoard();
		CheckersData data = new CheckersData(stateCopy.getBoardPieces()); //cloned
		CheckersMove[] moves = data.getLegalMoves(playerTurn.getColorRedorBlack());
		ArrayList<CheckersAction> actions = new ArrayList<CheckersAction>();
		if (moves != null) {
			for (int i = 0; i < moves.length; i++) {
				CheckersState stateCopy2 = this.deepCopyBoard();
				actions.addAll(CheckersAction.getActionsFrom(stateCopy2, moves[i]));
			}
			return actions;
		}
		else {
			return null;
		}
		
	}
	
}
