import java.util.ArrayList;
import java.util.Scanner;

public class Game {
	
	/** AI name. */
	private static final String AI_NAME = "AI Pentagoku";
	
	/** Game tree. */
	private GameTree myGameTree;
	
	/** Player name. */
	private String myPlayerName;
	
	/** Player token color. */
	private char myPlayerColor;
	
	/** AI token color. */
	private char myAIColor;
	
	/** Who goes first. */
	private boolean IsMinFirst;
	
	/** List for made moves. */
	ArrayList<String> myMoveList = new ArrayList<String>();
	
	
	/**
	 * Constructor.
	 */
	public Game() {
		getUserInfo();
	}
	
	/**
	 * Get player information and game settings.
	 */
	private void getUserInfo() {
		Scanner scan = new Scanner(System.in);
		System.out.println("What is your name? ");
		this.myPlayerName = scan.nextLine();
		System.out.print(myPlayerName + ", choose your token color (b for black or w for white): ");
		String color = scan.nextLine().toLowerCase();
		while (!color.equals("b") && !color.equals("w")) {
			System.out.print("Invalid input, choose your token color (b for black or w for white): ");
			color = scan.nextLine().toLowerCase();
		}
		this.myPlayerColor = color.charAt(0);
		if (myPlayerColor == 'w') {
			this.myAIColor = 'b';
		} else {
			this.myAIColor = 'w';
		}
		System.out.print("You will be challenging " + AI_NAME + ", choose a player to move first "
				+ "(1 for AI to move first or 2 for you to move first): ");
		String moveFirst = scan.nextLine();
		while (!moveFirst.equals("1") && !moveFirst.equals("2")) {
			System.out.print("Invalid input, choose a player to move first (1 for AI to"
					+ " move first or 2 for you to move first): ");
			moveFirst = scan.nextLine();
		}
		if (moveFirst.equals("1")) {
			this.IsMinFirst = true;
		} else {
			this.IsMinFirst = false;
		}
		// create game tree
		PentagoBoard initialBoard = new PentagoBoard(myAIColor, myPlayerColor);
		System.out.println("Initial Board:");
		initialBoard.displayBoard();
		this.myGameTree = new GameTree(initialBoard, this.IsMinFirst);

		//scan.close();
	}

	/**
	 * Start a Pentago game util theres a win or tie.
	 */
	public void startGame() {
		Scanner scan = new Scanner(System.in);
		String move;
		int blockNum = 0;
		int position = 0;
		int rotateNum = 0;
		String slash = null;
		String space = null;
		String direction = null;
		while(!myGameTree.getCurrentState().getMyBoard().isWin(myAIColor) &&
				!myGameTree.getCurrentState().getMyBoard().isWin(myPlayerColor) 
				&& !myGameTree.getCurrentState().getMyBoard().isTie()) {
			if (this.IsMinFirst) {
				// AI move
				String AImove = myGameTree.miniMaxDecision();
				System.out.println("AI move: " + AImove);
				myGameTree.playerAction(AImove);
				myMoveList.add(AImove);
				//printTurnSummary("2");
				if (myGameTree.getCurrentState().getMyBoard().isWin(myAIColor) 
						|| myGameTree.getCurrentState().getMyBoard().isWin(myPlayerColor)
						|| myGameTree.getCurrentState().getMyBoard().isTie()) {
						break;
				}
			}
			System.out.print("Make your move ([Block Number]/[Position] [Block Number][Rotation]): ");
			move = scan.nextLine().toUpperCase();
			if (move.length() == 6) {
				blockNum = Integer.parseInt(move.substring(0, 1));
				slash = move.substring(1, 2);
				position = Integer.parseInt(move.substring(2, 3));
				space = move.substring(3, 4);
				rotateNum = Integer.parseInt(move.substring(4, 5));
				direction = move.substring(5, 6);
			}	
			while(blockNum <= 0 || blockNum > 4 || position <= 0 || position > 9 
					|| rotateNum <= 0 || rotateNum > 4 || !slash.equals("/") || !space.equals(" ")
					|| (!direction.equals("L") && !direction.equals("R")) 
					|| !myGameTree.getCurrentState().getMyBoard().isEmpty(blockNum, position)) {
				System.out.print("Invalid input, make your move ([Block Number]/[Position] [Block Number][Rotation]): ");
				move = scan.nextLine().toUpperCase();
				if (move.length() == 6) {
					blockNum = Integer.parseInt(move.substring(0, 1));
					slash = move.substring(1, 2);
					position = Integer.parseInt(move.substring(2, 3));
					space = move.substring(3, 4);
					rotateNum = Integer.parseInt(move.substring(4, 5));
					direction = move.substring(5, 6);
				}	
			}
			// player move
			myGameTree.playerAction(move);
			myMoveList.add(move);
			if (this.IsMinFirst) {
				//printTurnSummary("1");
			} else {
				//printTurnSummary("2");
			}
			if (myGameTree.getCurrentState().getMyBoard().isWin(myAIColor) 
			    || myGameTree.getCurrentState().getMyBoard().isWin(myPlayerColor) 
				|| myGameTree.getCurrentState().getMyBoard().isTie()) {
				break;
			}
			if (!this.IsMinFirst) {
				// AI move
				String AImove = myGameTree.miniMaxDecision();
				System.out.println("AI move: " + AImove);
				myGameTree.playerAction(AImove);
				myMoveList.add(AImove);
				//printTurnSummary("1");
			}
		}
		myGameTree.displayCurrentState();
		PentagoBoard finalBoard = myGameTree.getCurrentState().getMyBoard();
		if (finalBoard.isTie()) {
			System.out.println("It's a tie.");
		} else if (finalBoard.isWin(myPlayerColor)) {
			System.out.println(myPlayerName + " wins!");
		} else if (finalBoard.isWin(myAIColor)){
			System.out.println("AI wins!");
		}
		scan.close();
	}

	private void printTurnSummary(String nextTurn) {
		System.out.println("Turn summary: ");
		if(this.IsMinFirst) {
			System.out.println(AI_NAME);
			System.out.println(this.myPlayerName);
			System.out.println(Character.toString(this.myAIColor).toUpperCase());
			System.out.println(Character.toString(this.myPlayerColor).toUpperCase());
		} else {
			System.out.println(this.myPlayerName);
			System.out.println(AI_NAME);
			System.out.println(Character.toString(this.myPlayerColor).toUpperCase());
			System.out.println(Character.toString(this.myAIColor).toUpperCase());
		}
		System.out.println(nextTurn);
		System.out.println(this.myGameTree.getCurrentState().getMyBoard().toString());
		for(int i = 0; i < this.myMoveList.size(); i++) {
			System.out.println(this.myMoveList.get(i));
		}
		System.out.println();
	}
}
