package pacman.controllers;

import java.awt.event.KeyEvent;
import java.util.EnumMap;
import java.util.Random;

import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

import static pacman.game.Constants.*;

/*
 * The Class AggressiveGhosts.
 */
public final class HumanAggressiveGhosts extends Controller<EnumMap<GHOST,MOVE>>
{	
	private final static float CONSISTENCY=1.0f;	//carry out intended move with this probability
	private Random rnd=new Random();
	private EnumMap<GHOST,MOVE> myMoves=new EnumMap<GHOST,MOVE>(GHOST.class);
	private MOVE[] moves=MOVE.values();
	public KeyBoardInput input;
		
	/* (non-Javadoc)
	 * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
	 */
    
    public HumanAggressiveGhosts(KeyBoardInput input)
    {
    	this.input=input;
    }
    
    public KeyBoardInput getKeyboardInput()
    {
    	return input;
    }
    
	public EnumMap<GHOST,MOVE> getMove(Game game,long timeDue)
	{		
		myMoves.clear();		
		
		for(GHOST ghost : GHOST.values()){				//for each ghost
			if(ghost.name().equals("BLINKY")){
				switch(input.getKey()) // Tentativo di controllare un ghost
		    	{
			    	case KeyEvent.VK_UP: 	myMoves.put(GHOST.BLINKY,MOVE.UP); break;
			    	case KeyEvent.VK_RIGHT: myMoves.put(GHOST.BLINKY,MOVE.RIGHT); break;
			    	case KeyEvent.VK_DOWN: 	myMoves.put(GHOST.BLINKY,MOVE.DOWN); break;
			    	case KeyEvent.VK_LEFT: 	myMoves.put(GHOST.BLINKY,MOVE.LEFT); break;
			    	default: 				myMoves.put(GHOST.BLINKY,MOVE.NEUTRAL);
		    	}
			}else if(game.doesGhostRequireAction(ghost))		//if it requires an action
			{
					if(rnd.nextFloat()<CONSISTENCY)	//approach/retreat from the current node that Ms Pac-Man is at
					myMoves.put(ghost,game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost),
							game.getPacmanCurrentNodeIndex(),game.getGhostLastMoveMade(ghost),DM.PATH));
				else									//else take a random action
					myMoves.put(ghost,moves[rnd.nextInt(moves.length)]);
			}
		}
		return myMoves;
	}
}