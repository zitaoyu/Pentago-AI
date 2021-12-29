import java.util.ArrayList;

public class GameTree {
	
	/** Depth limit of the game tree. */
	private static final int DEPTH_LIMIT = 2;
	
	/** The gaem tree. */
	private ArrayList<Node> myList;
	
	/** Pointer to the current node. */
	private Node myCurrentState;
	
	/**
	 * Constructor.
	 * @param theInitialBoard the initial board.
	 * @param isMin whether or not the root node is a min's turn.
	 */
	public GameTree(PentagoBoard theInitialBoard, boolean isMin) {
		Node root = new Node(theInitialBoard, 0, isMin);
		this.myList = new ArrayList<Node>();
		this.myCurrentState = root;
		updateTree(root);
		//minMax();
		//minMaxPruning();
	}
	
	/**
	 * Add the given node to the game tree and generate node with the given node if possible.
	 * @param theNode the node.
	 */
	public void updateTree(Node theNode) {
		this.myList.add(theNode);
		// if not reached depth limit then expand
		if (!(theNode.getMyDepth() == DEPTH_LIMIT)) {
			PentagoBoard pb = theNode.getMyBoard();
			for(int i = 1; i <= 4; i++) {				// for each block
				for(int j = 1; j <= 9; j++) {			// for each position
					if (pb.isEmpty(i, j)) {
						for(int k = 1; k <=4; k++) {	// for each block
							int depth = theNode.getMyDepth() + 1;
							boolean isMin = !theNode.isMin();
							String move1 = String.valueOf(i) + "/" + String.valueOf(j) + " " + String.valueOf(k) + "L";
							String move2 = String.valueOf(i) + "/" + String.valueOf(j) + " " + String.valueOf(k) + "R";
							char token;
							if (theNode.isMin()) {
								token = pb.getMyMinToken();
							} else {
								token = pb.getMyMaxToken();
							}
							PentagoBoard newPb1 = pb.copyAndMove(move1, token);
							PentagoBoard newPb2 = pb.copyAndMove(move2, token);
							Node child1 = new Node(newPb1, depth, isMin, theNode, move1);
							Node child2 = new Node(newPb2, depth, isMin, theNode, move2);
							updateTree(child1);
							updateTree(child2);
						}
					}
				}
			}
		} else {
			theNode.updateUtilityValue();
		}
	}
	
	/**
	 * Perform MinMax algorithm on this game tree.
	 */
	private void minMax() {
		for(int i = myList.size() - 1; i >= 0; i--) {
			Node node = myList.get(i);
			if(node.getMyParent() != null) {
				Node parent = node.getMyParent();
				int parentUV = parent.getMyUtilityValue();
				int childUV = node.getMyUtilityValue();
				if (parent.isMin()) {
					if (parentUV > childUV || parentUV == -9999) {
						parent.setMyUtilityValue(childUV);
					}
				} else {
					if (parentUV < childUV || parentUV == -9999) {
						parent.setMyUtilityValue(childUV);
					}
				}
			}
		}
	}
	
	/**
	 * Perform MinMax algorithm with Apha-Beta Prunning on this game tree.
	 */
	private void minMaxPruning() {
		Node node;
		Node nextNode;
		Node parent;
		int i = 0;
		while(i < myList.size() - 1) {
			node = myList.get(i);
			nextNode = myList.get(i + 1);
			parent = node.getMyParent();
			
			// if next node is child of current node and its not vistied
			// , goes to current node's parent
			// , else, copy parents alpha and beta to child
			if (node == nextNode.getMyParent() && !nextNode.isVisited()) {
					nextNode.setMyAlpha(node.getMyAlpha());
					nextNode.setMyBeta(node.getMyBeta());
					i++;
			} else if (node.getMyDepth() == DEPTH_LIMIT
					|| (node == nextNode.getMyParent() && nextNode.isVisited() && parent != null)) {
				node.setVisited(true);
				int UV = node.getMyUtilityValue();
				int parentUV = parent.getMyUtilityValue();
				// update utility value, alpha, and beta to its parent
				if (parent.isMin()) {
					if (parentUV > UV || parentUV == -9999) {
						parent.setMyUtilityValue(UV);
					if (parent.getMyBeta() > UV || parent.getMyBeta() == 1000) {	// update beta
							parent.setMyBeta(UV);
						}
					}
				} else {
					if (parentUV < UV || parentUV == -9999) {
						parent.setMyUtilityValue(UV);
						if (parent.getMyAlpha() < UV || parent.getMyAlpha() == -1000) {	// update Alpha
							parent.setMyAlpha(UV);
						}
					}
				}
				// parent's alpha > beta: prun the remaining child nodes
				if (parent.getMyAlpha() > parent.getMyBeta()) {
					//System.out.println("Parent UV" + parent.getMyUtilityValue() + " Parent alpha and beta: " + parent.getMyAlpha() + " " + parent.getMyBeta());
					boolean isPrunable = false;
					int targetIndex = -1;
					for (int j = i + 1; j < myList.size(); j++) {
						if (parent == myList.get(j).getMyParent() && !myList.get(j).isVisited()) {
							isPrunable = true;
							targetIndex = j;
							break;
						}
					}
					while(isPrunable) {
						prune(myList.get(targetIndex));
						isPrunable = false;
						for (int k = i + 1; k < myList.size(); k++) {
							if (parent == myList.get(k).getMyParent() && !myList.get(k).isVisited()) {
								isPrunable = true;
								targetIndex = k;
								break;
							}
						}
					}
					if (i + 1 < myList.size()) {
						nextNode = myList.get(i + 1);
					}
				}
				// if next node dont have the same parent, goes to parent
				if (parent != nextNode.getMyParent()) {
					i = myList.indexOf(parent);
				} else {
					i++;
				}
			} else {
				int index = i + 1;
				while(myList.get(index).isVisited()) {
					index++;
				}
				myList.get(index).setMyAlpha(node.getMyAlpha());
				myList.get(index).setMyBeta(node.getMyBeta());
				i = index;
			}
		}
	}
	
	/**
	 * Prune a node from list and all of its child nodes.
	 * @param theTarget the node.
	 */
	private void prune(Node theTarget) {
		ArrayList<Node> prunedList = new ArrayList<Node>();
		prunedList.add(theTarget);
		myList.remove(theTarget);
		Node node;
		int i = 0;
		while(i < myList.size()){
			node = myList.get(i);
			if (prunedList.contains(node.getMyParent()) && !node.isVisited()) {
				prunedList.add(node);
				myList.remove(node);
				i--;
			}
			i++;
		}
	}
	
	
	/**
	 * Return the action for the current node that the pointer is pointing to.
	 * @return action as a String.
	 */
	public String miniMaxDecision() {
		int UV = myCurrentState.getMyUtilityValue();
		Node node;
		for(int i = 0; i < myList.size(); i++) {
			node = myList.get(i);
			if (node != null) {
				if (node.getMyParent() == myCurrentState 
						&& node != myCurrentState
						&& node.getMyDepth() == myCurrentState.getMyDepth() + 1
						&& UV == node.getMyUtilityValue()) {
					return node.getMyMove();
				}
			}
		}
		return null;
	}
	
	/**
	 * Perform the given action the the game tree.
	 * @param theAction the action.
	 */
	public void playerAction(String theAction) {
		Node node;
		for(int i = 0; i < myList.size(); i++) {
			node = myList.get(i);
			if (node.getMyDepth() == myCurrentState.getMyDepth() + 1 
					&& node.getMyParent() == myCurrentState
					&& node.getMyMove().equals(theAction)) {
				myCurrentState = node;
				break;
			}
		}
		displayCurrentState();
		if (myCurrentState.getMyDepth() == DEPTH_LIMIT) {
			regenerateTree();
		}
	}
	
	/**
	 * Update the game tree with the current node as the root node.
	 */
	public void regenerateTree() {
		this.myList = new ArrayList<Node>();
		myCurrentState.setMyUtilityValue(-9999);
		Node root = new Node(myCurrentState.getMyBoard(), 0, myCurrentState.isMin(), null, myCurrentState.getMyMove());
		this.myCurrentState = root;
		updateTree(root);
		minMax();
		//minMaxPruning();
	}
	
	/**
	 * Display the current board to the console.
	 */
	public void displayCurrentState() {
		this.myCurrentState.getMyBoard().displayBoard();
	}
	
	/**
	 * Get the current node.
	 * @return the current node.
	 */
	public Node getCurrentState() {
		return myCurrentState;
	}
	
	/**
	 * Get the game tree list.
	 * @return myList.
	 */
	public ArrayList<Node> getMyList(){
		return myList;
	}
}
