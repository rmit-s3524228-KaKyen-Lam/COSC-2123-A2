package player;

import ship.Ship;
import world.World;
import java.util.Random;

/**
 * Random guess player (task A).
 * Please implement this class.
 *
 * @author Youhan, Jeffrey
 */
public class RandomGuessPlayer implements Player{

	public class RandomShip {
		Ship ship = null;
		int[] rowCoord = { -1, -1, -1, -1, -1 };
		int[] colCoord = { -1, -1, -1, -1, -1 };
		boolean[] isdown = { true, true, true, true, true };

		private RandomShip() {
		}
	}

	private int rowSize = 0;
	private int colSize = 0;
	boolean[][] isGuessed;
	RandomShip[] playerShips = new RandomShip[5];


	@Override
	public void initialisePlayer(World world) {
		
		this.rowSize = world.numRow;
		this.colSize = world.numColumn;
		
		this.isGuessed = new boolean[this.rowSize][this.colSize];
		int i = 0;
		
		for (World.ShipLocation shiploc : world.shipLocations) {
			this.playerShips[i] = new RandomShip();
			this.playerShips[i].ship = shiploc.ship;
			for (int j = 0; j < this.playerShips[i].ship.len(); j++) {
				this.playerShips[i].rowCoord[j] = shiploc.coordinates.get(j).row;
				this.playerShips[i].colCoord[j] = shiploc.coordinates.get(j).column;
				this.playerShips[i].isdown[j] = false;
			}
			i++;
		}
		

	} // end of initialisePlayer()

    @Override
    public Answer getAnswer(Guess guess) {

    	Answer answer = new Answer();
		for (int i = 0; i < 5; i++) {
			
			for (int j = 0; j < this.playerShips[i].ship.len(); j++) {
			
				if ((guess.row == this.playerShips[i].rowCoord[j]) && guess.column == this.playerShips[i].colCoord[j]) {
					answer.isHit = true;
					this.playerShips[i].isdown[j] = true;
					boolean isSunk = true;
					for (int k = 0; k < this.playerShips[i].ship.len(); k++) {
						if (this.playerShips[i].isdown[k] == false) {
							isSunk = false;
						}
					}

					if (isSunk) {
						answer.shipSunk = this.playerShips[i].ship;
					}
					return answer;
				}
			}

		}

		return answer;
	} 
    @Override
    public Guess makeGuess() {
    	
        Random rando = new Random();
        int i;
        int j;
	        do
	        	{
	          i = rando.nextInt(this.rowSize);
	          j = rando.nextInt(this.colSize);
	          
	        	} while (this.isGuessed[i][j] != false);
	        
	        Guess randoGuess = new Guess();
	        
	        randoGuess.row = i;
	        
	        randoGuess.column = j;
	        
	        this.isGuessed[i][j] = true;
	        
        return randoGuess;
      }
       


    @Override
    public void update(Guess guess, Answer answer) {
    	
        // To be implemented.
    } // end of update()


    @Override
    public boolean noRemainingShips() {
    	{
    	    for (int i = 0; i < 5; i++) {
    	      for (int j = 0; j < this.playerShips[i].ship.len(); j++) {
    	        if (this.playerShips[i].isdown[j] == false) {
    	          return false;
    	        }
    	      }
    	    }
    	    return true;
    	  }
    }
} // end of class RandomGuessPlayer
