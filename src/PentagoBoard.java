
public class PentagoBoard {
	
	/** Utility value for win states. */
	private static final int WIN = 100;
	
	/** Utility value for lost states. */
	private static final int LOSE = 100;
	
	/** Utility value for tie states. */
	private static final int TIE = 9999;
	
	/** defualt utility value. */
	private static final int DEFAULT_UTILITY_VALUE = -9999;
	
	/** Block 1. */
	private char[] myBlock1;
	
	/** Block 2. */
	private char[] myBlock2;
	
	/** Block 3. */
	private char[] myBlock3;
	
	/** Block 4. */
	private char[] myBlock4;
	
	/** The utility value for this board. */
	private int myUtilityValue = DEFAULT_UTILITY_VALUE;
	
	/** The player token. */
	private char myMaxToken;
	
	/** The AI token. */
	private char myMinToken;
	
	/**
	 * Constructor
	 */
	public PentagoBoard(char theMin, char theMax) {
		this.myBlock1 = new char[9];
		this.myBlock2 = new char[9];
		this.myBlock3 = new char[9];
		this.myBlock4 = new char[9];
		for(int i = 0; i < 9; i++) {
			this.myBlock1[i] = '.';
			this.myBlock2[i] = '.';
			this.myBlock3[i] = '.';
			this.myBlock4[i] = '.';
		}
		this.setMinMaxToken(theMin, theMax);
	}
	
	/**
	 * Set the 4 blocks with given arrays.
	 */
	public void setBlock1(char[] theBlock1, char[] theBlock2, char[] theBlock3, char[] theBlock4) {
		this.myBlock1 = theBlock1;
		this.myBlock2 = theBlock2;
		this.myBlock3 = theBlock3;
		this.myBlock4 = theBlock4;
	}
	
	/**
	 * Setters for min max tokens.
	 */
	public void setMinMaxToken(char theMin, char theMax) {
		this.myMinToken = theMin;
		this.myMaxToken = theMax;
	}
	
	/**
	 * Getter for max tokens.
	 */
	public char getMyMaxToken() {
		return myMaxToken;
	}
	
	/**
	 * Getter for min token.
	 */
	public char getMyMinToken() {
		return myMinToken;
	}
	
	public PentagoBoard copyAndMove(String theMove, char theColor) {
		PentagoBoard copy = new PentagoBoard(myMinToken, myMaxToken);
		char[] copyBlock1 = new char [9];
		char[] copyBlock2 = new char [9];
		char[] copyBlock3 = new char [9];
		char[] copyBlock4 = new char [9];
		for(int i = 0; i < 9; i++) {
			copyBlock1[i] = myBlock1[i];
			copyBlock2[i] = myBlock2[i];
			copyBlock3[i] = myBlock3[i];
			copyBlock4[i] = myBlock4[i];
		}
		copy.setBlock1(copyBlock1, copyBlock2, copyBlock3, copyBlock4);
		copy.move(theMove, theColor);
		return copy;
	}
	
	/**
	 * Place a token in given position and rotate block in given direction.
	 * @ return -1 if move is unavailable, 1 is a successful move.
	 */
	public int move(String theMove, char theColor) {
		int blockNum = Integer.parseInt(theMove.substring(0, 1));
		int position = Integer.parseInt(theMove.substring(2, 3));
		int rotateNum = Integer.parseInt(theMove.substring(4, 5));
		String direction = theMove.substring(5, 6).toUpperCase();
		
		// place color in chosen position
		char[] block;
		if (blockNum == 1) {
			block = this.myBlock1;
		} else if (blockNum == 2) {
			block = this.myBlock2;
		} else if (blockNum == 3) {
			block = this.myBlock3;
		} else {
			block = this.myBlock4;
		}
		if (isEmpty(blockNum, position)) {
			block[position - 1] = theColor;
		} else {
			return -1;
		}
		// rotate block
		if (direction.equals("L")) {
			rotateLeft(rotateNum);
		} else {
			rotateRight(rotateNum);
		}
		return 1;
	}		
	
	public void updateUtilityValue() {
		int minUtilityValue = 0;
		int maxUtilityValue = 0;
		char[][] board = get2DArray();
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 6; j++) {
				if(board[i][j] == myMaxToken) {
					maxUtilityValue += getUtilityValue(board, i, j);
				} else if (board[i][j] == myMinToken) {
					minUtilityValue -= getUtilityValue(board, i, j);
				}
			}
		}
		int boardUtilityValue;
		if (isTie()) {
			boardUtilityValue = TIE;
		} else if (isWin(myMaxToken)) {
			boardUtilityValue = WIN;
		} else if (isWin(myMinToken)) {
			boardUtilityValue = LOSE;
		} else {
			boardUtilityValue = minUtilityValue + maxUtilityValue;
		}
		this.setMyUtilityValue(boardUtilityValue);
	}
	
	/**
	 * This mathod returns the utility value of a postion which is the total 
	 * possible winning conditions in that position.
	 * @return the utility value of a position
	 */
	private int getUtilityValue(char[][] theBoard, int theIIndex, int theJIndex) {
		int utility = 0;
		char color = theBoard[theIIndex][theJIndex];
		
		int countL = 0;
		int countR = 0;
		int countU = 0;
		int countD = 0;
		int countNE = 0;
		int countSE = 0;
		int countSW = 0;
		int countNW = 0;
		boolean L = true;
		boolean R = true;
		boolean U = true;
		boolean D = true;
		boolean NE = true;
		boolean SE = true;
		boolean SW = true;
		boolean NW = true;
		for(int i = 1; i < 5; i++) {
			// check left right up and down see if there is possible winning condition
			if (theJIndex - i >= 0 && L) {
				if (theBoard[theIIndex][theJIndex - i] == color || theBoard[theIIndex][theJIndex - i] == '.') {
					countL++;
				} else {
					L = false;
				}
			}
			if (theJIndex + i <= 5 && R) {
				if (theBoard[theIIndex][theJIndex + i] == color || theBoard[theIIndex][theJIndex + i] == '.') {
					countR++;
				} else {
					R = false;
				}
			}
			if (theIIndex - i >= 0 && U) {
				if (theBoard[theIIndex - i][theJIndex] == color || theBoard[theIIndex - i][theJIndex] == '.') {
					countU++;
				} else {
					U = false;
				}
			}
			if (theIIndex + i <= 5 && D) {
				if (theBoard[theIIndex + i][theJIndex] == color || theBoard[theIIndex + i][theJIndex] == '.') {
					countD++;
				} else {
					D = false;
				}
			}
			// check northeast southeast southwest and northwest
			if (theIIndex - i >= 0 && theJIndex - i >= 0 && NW) {
				if (theBoard[theIIndex - i][theJIndex - i] == color || theBoard[theIIndex - i][theJIndex - i] == '.') {
					countNW++;
				} else {
					NW = false;
				}
			}
			if (theIIndex + i <= 5 && theJIndex + i <= 5 && SE) {
				if (theBoard[theIIndex + i][theJIndex + i] == color || theBoard[theIIndex + i][theJIndex + i] == '.') {
					countSE++;
				} else {
					SE = false;
				}
			}
			if (theIIndex - i >= 0 && theJIndex + i <= 5 && NE) {
				if (theBoard[theIIndex - i][theJIndex + i] == color || theBoard[theIIndex - i][theJIndex + i] == '.') {
					countNE++;
				} else {
					NE = false;
				}
			}
			if (theIIndex + i <= 5 && theJIndex - i >= 0 && SW) {
				if (theBoard[theIIndex + i][theJIndex - i] == color || theBoard[theIIndex + i][theJIndex - i] == '.') {
					countSW++;
				} else {
					SW = false;
				}
			}
		}
		if (countL + countR == 5) {
			utility += 2;
		} else if (countL + countR == 4) {
			utility ++;
		} else if (countL == 4 || countR == 4) {
			if (countL == 4) {
				utility++;
			} 
			if (countR == 4) {
				utility++;
			}
		}
		if (countU + countD == 5) {
			utility += 2;
		}  else if (countU + countD == 4) {
			utility ++;
		} else if (countU == 4 || countD == 4) {
			if (countU == 4) {
				utility++;
			}
			if (countD == 4) {
				utility++;
			}
		}
		if (countNW + countSE == 5) {
			utility += 2;
		} else if (countNW + countSE == 4) {
			utility ++;
		} else if (countNW == 4 || countSE == 4) {
			if (countNW == 4) {
				utility++;
			}
			if (countSE == 4) {
				utility++;
			}
		}
		if (countNE + countSW == 5) {
			utility += 2;
		} else if (countNE + countSW == 4) {
			utility ++;
		} else if (countNE == 4 || countSW == 4) {
			if (countNE == 4) {
				utility++;
			}
			if (countSW == 4) {
				utility++;
			}
		}
		// for testing:
//		System.out.println(theIIndex + " " + theJIndex + " " + color);
//		System.out.println("countL: " + countL);
//		System.out.println("countR: " + countR);
//		System.out.println("countU: " + countU);
//		System.out.println("countD: " + countD);
//		System.out.println("countNW: " + countNW);
//		System.out.println("countSE: " + countSE);
//		System.out.println("countNE: " + countNE);
//		System.out.println("countSW: " + countSW);
//		System.out.println("Utility: " + utility);
		return utility;
	}
	
	/**
	 * Return whether or not the given color is in wining condition.
	 */
	public boolean isWin(char color) {
		boolean result = false;
		for(int i = 0; i < 3; i++) {
			result = 
					 // Columns:
						(myBlock1[i] == color && myBlock1[i + 3] == color && myBlock1[i + 6] == color 
						&& myBlock3[i] == color && myBlock3[i + 3] == color)
					 || (myBlock1[i + 3] == color && myBlock1[i + 6] == color && myBlock3[i] == color 
					 	&& myBlock3[i + 3] == color && myBlock3[i + 6] == color)
					 || (myBlock2[i] == color && myBlock2[i + 3] == color && myBlock2[i + 6] == color 
					 	&& myBlock4[i] == color && myBlock4[i + 3] == color)
					 || (myBlock4[i + 3] == color && myBlock4[i + 6] == color && myBlock4[i] == color 
					 	&& myBlock4[i + 3] == color && myBlock4[i + 6] == color)
					 // Rows:
					 || (myBlock1[i * 3] == color && myBlock1[i * 3 + 1] == color && myBlock1[i * 3 + 2] == color 
					 	&& myBlock2[i * 3] == color && myBlock2[i * 3 + 1] == color)
					 || (myBlock1[i * 3 + 1] == color && myBlock1[i * 3 + 2] == color && myBlock2[i * 3] == color 
					 	&& myBlock2[i * 3 + 1] == color && myBlock2[i * 3 + 2] == color)
					 || (myBlock3[i * 3] == color && myBlock3[i * 3 + 1] == color && myBlock3[i * 3 + 2] == color 
					 	&& myBlock4[i * 3] == color && myBlock4[i * 3 + 1] == color)
					 || (myBlock3[i * 3 + 1] == color && myBlock3[i * 3 + 2] == color && myBlock4[i * 3] == color 
					 	&& myBlock4[i * 3 + 1] == color && myBlock4[i * 3 + 2] == color);
			if (result) {
				return result;
			}
		}
		result =   (myBlock1[0] == color && myBlock1[4] == color && myBlock1[8] == color 
					&& myBlock4[0] == color && myBlock4[4] == color)
				|| (myBlock1[4] == color && myBlock1[8] == color && myBlock4[0] == color 
					&& myBlock4[4] == color && myBlock4[8] == color)
				|| (myBlock1[1] == color && myBlock1[5] == color && myBlock2[6] == color 
					&& myBlock4[1] == color && myBlock4[5] == color)
				|| (myBlock1[3] == color && myBlock1[7] == color && myBlock3[2] == color 
					&& myBlock4[3] == color && myBlock4[7] == color)
				
				|| (myBlock2[2] == color && myBlock2[4] == color && myBlock2[6] == color 
					&& myBlock3[2] == color && myBlock3[4] == color)
				|| (myBlock2[4] == color && myBlock2[6] == color && myBlock3[2] == color 
					&& myBlock3[4] == color && myBlock3[6] == color)
				|| (myBlock2[1] == color && myBlock2[3] == color && myBlock1[8] == color 
					&& myBlock3[1] == color && myBlock3[3] == color)
				|| (myBlock2[5] == color && myBlock2[7] == color && myBlock4[0] == color 
					&& myBlock3[5] == color && myBlock3[7] == color);
		return result;
	}
	
	/**
	 * Return whether this board result in a tie.
	 * @return whether this board result in a tie.
	 */
	public boolean isTie() {
		boolean isTie = true;
		for(int i = 0; i < 9; i++) {
			if (myBlock1[i] == '.' || myBlock2[i] == '.' 
					|| myBlock3[i] == '.' || myBlock4[i] == '.') {
				isTie = false;
			}
		}
		return isTie || (isWin(myMaxToken) && isWin(myMinToken));
	}
	
	/**
	 * Rotate the given number block 90 degree to the left.
	 * @param theNumber the number.
	 */
	public void rotateLeft(int theNumber) {
		char[] block;
		if(theNumber == 1) {
			block = this.myBlock1;
		} else if(theNumber == 2) {
			block = this.myBlock2;
		} else if(theNumber == 3) {
			block = this.myBlock3;
		} else {
			block = this.myBlock4;
		}
		// rotate left
		char temp = block[6];
		block[6] = block[0];
		char temp2 = block[8];
		block[8] = temp;
		temp = block[2];
		block[2] = temp2;
		block[0] = temp;
		
		temp = block[3];
		block[3] = block[1];
		temp2 = block[7];
		block[7] = temp;
		temp = block[5];
		block[5] = temp2;
		block[1] = temp;
	}
	
	/**
	 * Rotate the given number block 90 degree to the right.
	 * @param theNumber the number.
	 */
	public void rotateRight(int theNumber) {
		char[] block;
		if(theNumber == 1) {
			block = this.myBlock1;
		} else if(theNumber == 2) {
			block = this.myBlock2;
		} else if(theNumber == 3) {
			block = this.myBlock3;
		} else {
			block = this.myBlock4;
		}
		// rotate left
		char temp = block[2];
		block[2] = block[0];
		char temp2 = block[8];
		block[8] = temp;
		temp = block[6];
		block[6] = temp2;
		block[0] = temp;
		
		temp = block[5];
		block[5] = block[1];
		temp2 = block[7];
		block[7] = temp;
		temp = block[3];
		block[3] = temp2;
		block[1] = temp;
	}
	
	/**
	 * Check if position empty.
	 */
	public boolean isEmpty(int theBlockNum, int thePosition) {
		char[] block;
		if (theBlockNum <= 0 || theBlockNum > 4 || thePosition <= 0 || thePosition > 9) {
			return false;
		}
		if (theBlockNum == 1) {
			block = this.myBlock1;
		} else if (theBlockNum == 2) {
			block = this.myBlock2;
		} else if (theBlockNum == 3) {
			block = this.myBlock3;
		} else {
			block = this.myBlock4;
		}
		return block[thePosition - 1] == '.';
	}
	
	/**
	 * Display the board.
	 */
	public void displayBoard() {
		String[] board = new String[9];
		board[0] = "+-------+-------+";
		board[1] = "| " + myBlock1[0] + " " + myBlock1[1] + " " + myBlock1[2] + 
				   " | " + myBlock2[0] + " " + myBlock2[1] + " " + myBlock2[2] + " |";
		board[2] = "| " + myBlock1[3] + " " + myBlock1[4] + " " + myBlock1[5] + 
				   " | " + myBlock2[3] + " " + myBlock2[4] + " " + myBlock2[5] + " |";
		board[3] = "| " + myBlock1[6] + " " + myBlock1[7] + " " + myBlock1[8] + 
				   " | " + myBlock2[6] + " " + myBlock2[7] + " " + myBlock2[8] + " |";
		board[4] = "+-------+-------+";
		board[5] = "| " + myBlock3[0] + " " + myBlock3[1] + " " + myBlock3[2] + 
				   " | " + myBlock4[0] + " " + myBlock4[1] + " " + myBlock4[2] + " |";
		board[6] = "| " + myBlock3[3] + " " + myBlock3[4] + " " + myBlock3[5] + 
				   " | " + myBlock4[3] + " " + myBlock4[4] + " " + myBlock4[5] + " |";
		board[7] = "| " + myBlock3[6] + " " + myBlock3[7] + " " + myBlock3[8] + 
				   " | " + myBlock4[6] + " " + myBlock4[7] + " " + myBlock4[8] + " |";
		board[8] = "+-------+-------+";
		for(int i = 0; i < 9; i++) {
			System.out.println(board[i]);
		}
	}
	
	@Override
	public String toString() {
		return "" + myBlock1[0] + myBlock1[1] + myBlock1[2] + myBlock2[0] + myBlock2[1] + myBlock2[2] + "\n"
				+ myBlock1[3] + myBlock1[4] + myBlock1[5] + myBlock2[3] + myBlock2[4] + myBlock2[5] + "\n"
				+ myBlock1[6] + myBlock1[7] + myBlock1[8] + myBlock2[6] + myBlock2[7] + myBlock2[8] + "\n"
				+ myBlock3[0] + myBlock3[1] + myBlock3[2] + myBlock4[0] + myBlock4[1] + myBlock4[2] + "\n"
				+ myBlock3[3] + myBlock3[4] + myBlock3[5] + myBlock4[3] + myBlock4[4] + myBlock4[5] + "\n" 
				+ myBlock3[6] + myBlock3[7] + myBlock3[8] + myBlock4[6] + myBlock4[7] + myBlock4[8] + "";
	}
	
	/**
	 * @return return a 6x6 2D array for utility value calculating.
	 */
	public char[][] get2DArray(){
		char[][] board = new char[6][6];
		char[] block;
		int index;
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 6; j++) {
				if (i <= 2 && j <=2) {
					block = this.myBlock1;
					index = i * 3 + j;
				} else if (i <= 2 && j >= 3) {
					block = this.myBlock2;
					index = i * 3 + (j - 3);
				} else if (i >= 3 && j <= 2) {
					block = this.myBlock3;
					index = (i - 3) * 3 + j;
				} else {
					block = this.myBlock4;
					index = (i - 3) * 3 + (j - 3);
				}
				board[i][j] = block[index];
			}
		}
		return board;
	}
	
	/**
	 * Getter for myUtilityValue.
	 * @return myUtilityValue.
	 */
	public int getMyUtilityValue() {
		return myUtilityValue;
	}
	
	/**
	 * Setter for myUilityValue.
	 * @param myUtilityValue new UtilityValue.
	 */
	public void setMyUtilityValue(int myUtilityValue) {
		this.myUtilityValue = myUtilityValue;
	}
}
