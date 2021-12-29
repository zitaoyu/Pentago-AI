import java.util.ArrayList;

public class Test {
	public static void main(String[] args) {
		PentagoBoard initialBoard = new PentagoBoard('b', 'w');
		initialBoard.displayBoard();
		GameTree myGameTree = new GameTree(initialBoard, false);

		ArrayList<Node> a = myGameTree.getMyList();
		System.out.println(a.size());
		int depth = 0;
		System.out.print("[");
		for(int i = 0; i < 2000; i++) {
			if(a.get(i).getMyDepth() == 0) {
				System.out.println(a.get(i).getMyUtilityValue());
			}
		}
		for(int i = 0; i < 2000; i++) {
			if(a.get(i).getMyDepth() == 1) {
				System.out.print("[" + a.get(i).getMyUtilityValue() + "], ");
			}
		}
		System.out.println("\n");
		for(int i = 2; i < 2000; i++) {
			if (a.get(i - 1).getMyParent() != a.get(i).getMyParent()) {
				System.out.print("{");
			}
			if(a.get(i).getMyDepth() == 2) {
				System.out.print("[" + a.get(i).getMyUtilityValue() + "], ");
			}
			if (a.get(i + 1).getMyParent() != a.get(i).getMyParent()) {
				System.out.print("}");
			}
			if(i % 50 == 0) {
				System.out.print("\n");
			}
		}
	}

}
