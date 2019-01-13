import java.util.ArrayList;
import java.util.Arrays;

public class CheckersAction {
	
	private ArrayList<CheckersMove> moveSequenceList;
	
	public CheckersAction(CheckersState state, CheckersMove move) {
		moveSequenceList = new ArrayList<CheckersMove>();
		//CheckersData data = new CheckersData(state.getBoardPieces());
		moveSequenceList.add(move);
	}
	
	public ArrayList<CheckersMove> getMoveSequenceList() {
		return moveSequenceList;
	}
	
	public void addMove(CheckersMove move) {
		moveSequenceList.add(move);
	}
	
	//checks for possible double and triple jumps beginning with move
	public static ArrayList<CheckersAction> getActionsFrom(CheckersState state, CheckersMove move){
		//System.out.println("Entered action.getActionsFrom ");
		//System.out.println(Arrays.deepToString((state.getBoardPieces())));
		CheckersState stateCopy = state.deepCopyBoard();
		int[][] stateBoardCopy = stateCopy.getBoardPieces();
		CheckersData data = new CheckersData(stateBoardCopy); //need to copy so that state is not changed
		data.makeMove(move);
		//System.out.println(Arrays.deepToString((state.getBoardPieces())));
		//System.out.println("After data.makeMove " + move.toString());
		ArrayList<CheckersAction> actionList = new ArrayList<CheckersAction>();
		CheckersAction a = new CheckersAction(state, move);
		//actionList.add(a); //moved to bottom, only add if no jumps
		//System.out.println(Arrays.deepToString((state.getBoardPieces())));
		if (move.isJump()) {
			CheckersMove[] legalJumps = data.getLegalJumpsFrom(state.getPlayerTurn().getColorRedorBlack(), move.toRow, move.toCol);
			// System.out.println(Arrays.deepToString((state.getBoardPieces())));
			if (legalJumps != null) {
				for (int i = 0; i < legalJumps.length; i++) {
					CheckersAction action1j = new CheckersAction(state, move);
					action1j.addMove(legalJumps[i]);
					// actionList.add(action);
					data.makeMove(legalJumps[i]);
					CheckersMove[] legalDoubleJumps = data.getLegalJumpsFrom(state.getPlayerTurn().getColorRedorBlack(),
							legalJumps[i].toRow, legalJumps[i].toCol);
					if (legalDoubleJumps != null) {
						for (int j = 0; j < legalDoubleJumps.length; j++) {
							CheckersAction action2j = new CheckersAction(state, move);
							action2j.addMove(legalJumps[i]);
							action2j.addMove(legalDoubleJumps[j]);
							actionList.add(action2j);
						}
					} else {
						actionList.add(action1j);
					}
					data.makeMove(legalJumps[i].inverseMove());
				}
			}
			else {
				actionList.add(a);
			}
		} else {
			actionList.add(a);
		}
		//System.out.println(Arrays.deepToString((state.getBoardPieces())));
		return actionList;
	}
	
	@Override
	public String toString() {
		String str = "";
		for (int i = 0; i < moveSequenceList.size(); i++) {
			str = "move (" +moveSequenceList.get(i).fromRow + ", "
					+ moveSequenceList.get(i).fromCol + ", " 
					+ moveSequenceList.get(i).toRow + ", "
					+ moveSequenceList.get(i).toCol + ") ";
		}
		return str;
		
	}

}
