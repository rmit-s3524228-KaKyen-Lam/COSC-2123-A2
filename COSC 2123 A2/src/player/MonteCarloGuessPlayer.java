package player;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import player.GreedyGuessPlayer.OwnShip;
import ship.Ship;
import world.World;

/**
 * Monte Carlo guess player (task C).
 * Please implement this class.
 *
 * @author Youhan, Jeffrey
 */
public class MonteCarloGuessPlayer  implements Player{

	public class OwnShip {
		Ship ship = null;
		int[] rowCoord = { -1, -1, -1, -1, -1 };
		int[] colCoord = { -1, -1, -1, -1, -1 };
		boolean[] isdown = { true, true, true, true, true };

		private OwnShip() {
		}
	}

	private int rowSize = 0;
	private int colSize = 0;

	boolean[][] isGuessed;
	OwnShip[] ownShips = new OwnShip[5];

	private int rowTarget = 0;
	private int colTarget = 0;

	private int rowFirstHit = 0;
	private int colFirstHit = 0;

	private boolean isFound = false;
	private int huntPos = 0; // Hunting direction (0:up, 1:right, 2:down,
								// 3:left)

	private boolean isHunt = false; // Boolean to check if the four sides of the
									// initial hit has been checked
	private boolean isFirstHit = true;

	/* For testing */
	// private boolean resetLoop = false;

	@Override
	public void initialisePlayer(World world) {
		this.rowSize = world.numRow;
		this.colSize = world.numColumn;
		this.isGuessed = new boolean[this.rowSize][this.colSize];
		int i = 0;
		for (World.ShipLocation shiploc : world.shipLocations) {
			this.ownShips[i] = new OwnShip();
			this.ownShips[i].ship = shiploc.ship;
			for (int j = 0; j < this.ownShips[i].ship.len(); j++) {
				this.ownShips[i].rowCoord[j] = shiploc.coordinates.get(j).row;
				this.ownShips[i].colCoord[j] = shiploc.coordinates.get(j).column;
				this.ownShips[i].isdown[j] = false;
			}
			i++;
		}

	} // end of initialisePlayer()

	@Override
	public Answer getAnswer(Guess guess) {
		Answer answer = new Answer();
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < this.ownShips[i].ship.len(); j++) {
				if ((guess.row == this.ownShips[i].rowCoord[j]) && guess.column == this.ownShips[i].colCoord[j]) {
					answer.isHit = true;

					this.ownShips[i].isdown[j] = true;
					boolean isSunk = true;
					for (int k = 0; k < this.ownShips[i].ship.len(); k++) {
						if (this.ownShips[i].isdown[k] == false) {
							isSunk = false;
						}
					}

					if (isSunk) {
						answer.shipSunk = this.ownShips[i].ship;
					}
					return answer;
				}
			}

		}

		return answer;
	} // end of getAnswer()

	/**
	 * Create reference to grid above the initial hit target
	 */
	public void upInitial() {
		rowTarget = rowFirstHit + 1;
		colTarget = colFirstHit;
		huntPos = 0;
	}

	/**
	 * Create reference to grid right of the initial hit target
	 */
	public void rightInitial() {
		rowTarget = rowFirstHit;
		colTarget = colFirstHit + 1;
		huntPos = 1;
	}

	/**
	 * Create reference to grid below the initial hit target
	 */
	public void downInitial() {
		rowTarget = rowFirstHit - 1;
		colTarget = colFirstHit;
		huntPos = 2;
	}

	/**
	 * Create reference to grid left of the initial hit target
	 */
	public void leftInitial() {
		rowTarget = rowFirstHit;
		colTarget = colFirstHit - 1;
		huntPos = 3;
	}

	/**
	 * Check if guess exceeds boundary of the board
	 * 
	 * @param mode
	 *            Configuration for boundary check (0 for hunt mode, 1 for
	 *            target mode)
	 * 
	 */
	public void boundaryCheck(int mode) {

		if (mode == 0) {
			if (rowTarget >= rowSize) {
				rightInitial();
			}

			if (colTarget >= colSize) {
				downInitial();
			}
			if (rowTarget < 0) {
				leftInitial();
			}
			if (colTarget < 0) {
				upInitial();
			}
		} else {

			if (rowTarget >= rowSize) {
				downInitial();
			}

			if (colTarget >= colSize) {
				leftInitial();
			}
			if (rowTarget < 0) {
				upInitial();
			}
			if (colTarget < 0) {
				rightInitial();
			}
		}

	}

	

	@Override
	public Guess makeGuess() {

		Random rando = new Random();
		int rowIndex;
		int colIndex;
		int i = 0;
		int j = 0;
		int config;
		Guess randoGuess = new Guess();

		if (this.isFound != true) {
			// if ship has not been found
			
			do {
			
			//TODO Monte Carlo algorithm
			

			} while (this.isGuessed[i][j] != false);

			randoGuess.row = i;
			randoGuess.column = j;
			rowTarget = i;
			colTarget = j;
			this.isGuessed[i][j] = true;

			// if (resetLoop) /* Used for testing */ {
			// randoGuess.row = i;
			// randoGuess.column = j;
			// rowTarget = i;
			// colTarget = j;
			// } else {
			// // Test variables
			// randoGuess.row = 3;
			// randoGuess.column = 4;
			// }

			return randoGuess;
		} else {
			// if ship has been found

			randoGuess.row = rowTarget;
			randoGuess.column = colTarget;

			this.isGuessed[randoGuess.row][randoGuess.column] = true;
			return randoGuess;
		}

	}

	@Override
	public void update(Guess guess, Answer answer) {

		if (!this.isFound && answer.isHit) {
			// If ship has not been found earlier and is just being hit

			this.isFound = true;
			this.isHunt = true;
			huntPos = 0;
			colFirstHit = guess.column;
			rowFirstHit = guess.row;
			colTarget = colFirstHit;
			rowTarget = rowFirstHit;
			isFirstHit = true;
		}

		if (this.isFound) {
			// If ship has been found

			if (answer.isHit && !isFirstHit) {
				// If ship has been hit and is not the first hit

				isHunt = false;

				if (huntPos == 0) {
					rowTarget++;
				} else if (huntPos == 1) {
					colTarget++;
				} else if (huntPos == 2) {
					rowTarget--;
				} else if (huntPos == 3) {
					colTarget--;
				}
				boundaryCheck(1);

			}

			if (!answer.isHit) {
				// If ship is not hit

				if (isHunt) {
					// If in initial hunting mode

					huntPos++;

				} else {
					// If in targeting mode and direction has to be reversed

					if (huntPos == 0) {
						downInitial();
					} else if (huntPos == 1) {
						leftInitial();
					} else if (huntPos == 2) {
						upInitial();
					} else if (huntPos == 3) {
						rightInitial();
					}
				}
			}

			if (isHunt) {
				// If in initial hunting mode

				if (huntPos == 0) {
					upInitial();
				} else if (huntPos == 1) {
					rightInitial();
				} else if (huntPos == 2) {
					downInitial();
				} else if (huntPos == 3) {
					leftInitial();
				}
				boundaryCheck(0);
				isFirstHit = false;

			}
		}

		if (answer.shipSunk != null) {
			this.isFound = false;
			// resetLoop = true; //for testing

		}

	} // end of update()

	@Override
	public boolean noRemainingShips() {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < this.ownShips[i].ship.len(); j++) {
				if (this.ownShips[i].isdown[j] == false) {
					return false;
				}
			}
		}
		return true;
	} // end of noRemainingShips() 
} // end of class MonteCarloGuessPlayer
