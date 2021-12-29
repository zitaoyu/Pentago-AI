# Pentagon-AI

INSTRUCTION:
This program is a Pentago game that you can play against a AI that is implemented 
with MinMax algorithm or MinMaxPruning algorithm.

How to run the program:
	- Start game by running "main.java".
	- Enter console input to interact with the console to set up a game
	  (require name, token color, and which player to move first).
	- Keep entering a move ([Block Number]/[Position] [Block Number][Rotation]) 
	  each turn until there is a win or tie.

---------------------------------------------------------------------------------------

Language: Java

Minmax Algorithm:
	Nodes expanded:80929
	depth level for look-ahead: 2
	Time complexity: O(b^m) where b is 288 in this case and m is 2
	Space complexity: O(bm) where b is 288 in this case and m is 2
	
Minmax with Alpha-Beta Prunning Algorithm:
	Nodes expanded: 20018
	depth level for look-ahead: 2
	Time complexity (worst case): O(b^m) where b is 288 in this case and m is 2
	Space complexity: O(bm) where b is 288 in this case and m is 2
	