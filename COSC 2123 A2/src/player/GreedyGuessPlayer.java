package player;

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

	private int rowGuess = 0;
	private int colGuess = 0;
	private int rowHunt = 0;
	private int colHunt = 0;
	private boolean isHunt = false;
	private int backDist = 2;
	private int pos = 0;

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

	public void generateNextGuess() {
		colGuess += 2;
		if (colGuess == 10) {
			colGuess = 1;
			rowGuess++;
		} else if (colGuess == 11) {
			colGuess = 0;
			rowGuess++;
		}
	}

	public void huntInit(int pos) {

		// Initial
		// colGuess + 1;
		// colGuess - 1;
		// rowGuess + 1;
		// rowGuess - 1;

		if (pos == 0) {
			colHunt = colGuess - 2 + 1;
		}

		if (pos == 1) {
			colHunt = colGuess - 2 - 1;
		}

		if (pos == 2) {
			rowHunt = rowGuess - 2 + 1;
		}

		if (pos == 3) {
			rowHunt = rowGuess - 2 - 1;
		}
	}

	// Found one
	// backtrack dist = 3
	// Check if row or col
	// row/col + 1
	// backtrack dist + 1
	// if failed
	// row/col - backtrack

	public void huntProgress(int pos) {

		boolean isbackTrack = false;

		if (isbackTrack == false) {
			if (pos == 0) {
				colHunt++;
				backDist++;
			} else if (pos == 1) {
				colHunt--;
				backDist--;
			} else if (pos == 2) {
				rowHunt++;
				backDist++;
			} else if (pos == 3) {
				rowHunt--;
				backDist--;
			}
		} else {
			if (pos == 0) {
				colHunt -= backDist;
			} else if (pos == 1) {
				colHunt += backDist;
			} else if (pos == 2) {
				rowHunt -= backDist;
			} else if (pos == 3) {
				rowHunt += backDist;
			}
			isbackTrack = true;
			backDist = 2;
		}

		if (isbackTrack == true) {
			if (pos == 0) {
				colHunt--;
			} else if (pos == 1) {
				colHunt++;
			} else if (pos == 2) {
				rowHunt--;
			} else if (pos == 3) {
				rowHunt++;
			}
		}

	}

	@Override
	public Guess makeGuess() {
		Guess guess = new Guess();
		
		do {
		if (isHunt && this.isGuessed[guess.row][guess.column]) {
		 huntInit(pos);
		 //huntProgress(pos);
		 guess.row = rowHunt;
		 guess.column = colHunt;
		 if (pos < 4) {
		 pos++;
		 }
		 } else {
		 guess.row = rowGuess;
		 guess.column = colGuess;
		 generateNextGuess();
		 }
		 }while (this.isGuessed[guess.row][guess.column]);
		
		this.isGuessed[guess.row][guess.column] = true;
			
		 // dummy return
		 return guess;
	} // end of makeGuess()

	@Override
	public void update(Guess guess, Answer answer) {
		if (answer.isHit == true) {
			isHunt = true;
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
