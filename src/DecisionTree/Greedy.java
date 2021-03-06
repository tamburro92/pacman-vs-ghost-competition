package DecisionTree;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedList;

import org.apache.commons.lang3.ArrayUtils;

import pacman.EvaluationHeuristic;
import pacman.controllers.Controller;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class Greedy extends Controller<MOVE>
{
	static private int DEPTH = 9;
	EnumMap<GHOST, MOVE> ghostMoves = new EnumMap<GHOST, MOVE>(GHOST.class);
	static private LinkedList<Integer> values;
	private Tree tree;
	public Greedy(){
		tree = new Tree(DEPTH);
		values = new LinkedList<Integer>();
	}
	
	public int dfsSearch(Node node, int life){

		Game gameState = node.getPredecessor().getGameState().copy();
		if (!isValidMove(gameState.getPossibleMoves(gameState.getPacmanCurrentNodeIndex()), node.getMove()))
			return Integer.MIN_VALUE;
		
		gameState.advanceGame(node.getMove(), ghostMoves);
		node.setGameState(gameState);
			
		ArrayList<Node> neighbors = node.getNeighbors();

		if (neighbors == null || node.getGameState().gameOver()|| node.getGameState().getPacmanNumberOfLivesRemaining() < life) 
			return EvaluationHeuristic.evaluateGameState(node.getGameState()); // end of branch return heuristic

		int bestValue = Integer.MIN_VALUE;
		for (Node neighbor : neighbors) {
			int value = dfsSearch(neighbor,life);
			if (value > bestValue)
				bestValue = value;
		}
		return bestValue;	
	}
	
	boolean isValidMove(MOVE[] validMoves, MOVE move) {
		for (MOVE validMove : validMoves) {
			if (move == validMove) return true;
		}
		return false;
	}


	public MOVE getMove(Game game, long timeDue) 
	{
		this.ghostMoves.put(GHOST.BLINKY, game.getGhostLastMoveMade(GHOST.BLINKY));
		this.ghostMoves.put(GHOST.INKY, game.getGhostLastMoveMade(GHOST.INKY));
		this.ghostMoves.put(GHOST.PINKY, game.getGhostLastMoveMade(GHOST.PINKY));
		this.ghostMoves.put(GHOST.SUE, game.getGhostLastMoveMade(GHOST.SUE));
		tree.getHeadNode().setGameState(game);
		ArrayList<Node> neighbors = tree.getHeadNode().getNeighbors();
		
		int leftValue = dfsSearch(neighbors.get(0),game.getPacmanNumberOfLivesRemaining());
		int rightValue = dfsSearch(neighbors.get(1),game.getPacmanNumberOfLivesRemaining());
		int upValue = dfsSearch(neighbors.get(2),game.getPacmanNumberOfLivesRemaining());
		int downValue = dfsSearch(neighbors.get(3),game.getPacmanNumberOfLivesRemaining());

		return EvaluationHeuristic.getBestMove(leftValue, rightValue, upValue, downValue);
	}
	
	private int mean(LinkedList<Integer> l){
		int acc=0;
		for(Integer i : l){
			acc+=i;
		}
		System.out.println("masn");
		return acc/l.size();
		
	}
}
