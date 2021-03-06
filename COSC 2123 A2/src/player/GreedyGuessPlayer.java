package player;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.Scanner;

import ship.Ship;
import world.World;
import world.World.Coordinate;
import world.World.ShipLocation;

/**
 * Greedy guess player (task B). Please implement this class.
 *
 * @author Youhan, Jeffrey
 */
public class GreedyGuessPlayer implements Player {

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

	/**
	 * Generate parity selection of board by creating lines of alternating
	 * pattern based on line size
	 * 
	 * @param config
	 *            Configuration for alternate line selection (0,1)
	 * @param lineSize
	 *            Size of line
	 * @return
	 */
	public ArrayList<Integer> parityGen(int config, int lineSize) {
		ArrayList<Integer> lineElem = new ArrayList<>();

		if (config == 0) {
			lineSize--;
			while (lineSize > -1) {
				lineElem.add(lineSize);
				lineSize -= 2;
			}
		}

		if (config == 1) {
			lineSize -= 2;
			while (lineSize > -1) {
				lineElem.add(lineSize);
				lineSize -= 2;
			}
		}
		return lineElem;
	}

	@Override
	public Guess makeGuess() {

		Random rando = new Random();
		int rowIndex;
		int colIndex;
		int i = 0;
		int j = 0;
		int config;
		Guess guess = new Guess();

		if (this.isFound != true) {
			// if ship has not been found

			ArrayList<Integer> rowConfig0 = parityGen(0, this.rowSize);
			ArrayList<Integer> rowConfig1 = parityGen(1, this.rowSize);

			ArrayList<Integer> colConfig0 = parityGen(0, this.colSize);
			ArrayList<Integer> colConfig1 = parityGen(1, this.colSize);

			do {
				config = rando.nextInt(2);

				if (config == 0) {
					rowIndex = rando.nextInt(rowConfig0.size());
					colIndex = rando.nextInt(colConfig1.size());
					i = rowConfig0.get(rowIndex);
					j = colConfig1.get(colIndex);

				} else if (config == 1) {
					rowIndex = rando.nextInt(rowConfig1.size());
					colIndex = rando.nextInt(colConfig0.size());
					i = rowConfig1.get(rowIndex);
					j = colConfig0.get(colIndex);
				}

			} while (this.isGuessed[i][j] != false);

			guess.row = i;
			guess.column = j;
			rowTarget = i;
			colTarget = j;
			this.isGuessed[i][j] = true;

			// if (resetLoop) /* Used for testing */ {
			// guess.row = i;
			// guess.column = j;
			// rowTarget = i;
			// colTarget = j;
			// } else {
			// // Test variables
			// guess.row = 3;
			// guess.column = 4;
			// }

			return guess;
		} else {
			// if ship has been found

			guess.row = rowTarget;
			guess.column = colTarget;

			this.isGuessed[guess.row][guess.column] = true;
			return guess;
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

} // end of class GreedyGuessPlayer