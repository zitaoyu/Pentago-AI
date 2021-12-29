
public class Node {
	
	/** PentagoBoard for this node. */
	private PentagoBoard myBoard;
	
	/** PentagoBoard for this node. */
	private Node myParent;
	
	/** defualt utility value. */
	private static final int DEFAULT_ALPHA = -1000;
	
	/** defualt utility value. */
	private static final int DEFAULT_BETA = 1000;
	
	/** defualt utility value. */
	private static final int DEFAULT_UTILITY_VALUE = -9999;
	
	/** Alpha for this node. */
	private int myAlpha = DEFAULT_ALPHA;
	
	/** Beta value for this node. */
	private int myBeta = DEFAULT_BETA;
	
	/** Utility value for this node. */
	private int myUtilityValue = DEFAULT_UTILITY_VALUE;

	/** Depth of the node. */
	private int myDepth;
	
	/** Whether or not this node is visited. */
	private boolean isVisited = false;
	
	/** Indecate whether or not this is a min's turn. */
	private boolean isMin;
	
	/** The action of this node in String form. */
	private String myMove;
	
	/**
	 * Construtor.
	 */
	public Node(PentagoBoard theBoard, int theDepth, boolean isMin) {
		this.myBoard = theBoard;
		this.myDepth = theDepth;
		this.myParent = null;
		this.isMin = isMin;
		this.myMove = null;
	}
	
	/**
	 * Construtor.
	 */
	public Node(PentagoBoard theBoard, int theDepth, boolean isMin, Node theParent, String theMove) {
		this.myBoard = theBoard;
		this.myDepth = theDepth;
		this.myParent = theParent;
		this.isMin = isMin;
		this.myMove = theMove;
	}
	
	public void updateUtilityValue() {
		myBoard.updateUtilityValue();
		setMyUtilityValue(myBoard.getMyUtilityValue());
	}
	
	public void setMyUtilityValue(int theUV) {
		this.myBoard.setMyUtilityValue(theUV);
		this.myUtilityValue = this.getMyBoard().getMyUtilityValue();
	}
	
	public int getMyUtilityValue() {
		return this.myUtilityValue;
	}
	
	// Getters and setters:
	public PentagoBoard getMyBoard() {
		return myBoard;
	}

	public void setMyBoard(PentagoBoard myBoard) {
		this.myBoard = myBoard;
	}

	public Node getMyParent() {
		return myParent;
	}

	public void setMyParent(Node myParent) {
		this.myParent = myParent;
	}

	public int getMyDepth() {
		return myDepth;
	}

	public void setMyDepth(int myDepth) {
		this.myDepth = myDepth;
	}

	public String getMyMove() {
		return myMove;
	}

	public void setMyMove(String myMove) {
		this.myMove = myMove;
	}

	public boolean isMin() {
		return isMin;
	}

	public void setMin(boolean isMin) {
		this.isMin = isMin;
	}

	public int getMyAlpha() {
		return myAlpha;
	}

	public void setMyAlpha(int myAlpha) {
		this.myAlpha = myAlpha;
	}

	public int getMyBeta() {
		return myBeta;
	}

	public void setMyBeta(int myBeta) {
		this.myBeta = myBeta;
	}

	public boolean isVisited() {
		return isVisited;
	}

	public void setVisited(boolean isVisited) {
		this.isVisited = isVisited;
	}
}
