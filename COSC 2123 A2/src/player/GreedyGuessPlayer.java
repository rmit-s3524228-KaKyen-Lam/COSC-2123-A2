package player;

import java.util.Random;
import java.util.Scanner;
import world.World;
import world.World.Coordinate;
import world.World.ShipLocation;

/**
 * Greedy guess player (task B). Please implement this class.
 *
 * @author Youhan, Jeffrey
 */
public class GreedyGuessPlayer implements Player {

	private World world;
	private Answer answer;
	private Guess guess = new Guess();
	private int rowGuess = 0;
	private int colGuess = 0;
	private int rowHunt = 0;
	private int colHunt = 0;
	private boolean isHunt = false;
	private int backDist = 3;
	private int pos = 0;

	@Override
	public void initialisePlayer(World world) {
		this.world = world;
		this.answer = new Answer();
	} // end of initialisePlayer()

	@Override
	public Answer getAnswer(Guess guess) {
		answer.isHit = false;
		for (ShipLocation shiploc : world.shipLocations) {
			for (Coordinate pos : shiploc.coordinates)
				if (guess.row == pos.row && guess.column == pos.column) {
					answer.isHit = true;
					isHunt = true;
					return answer;
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

	public void huntInit(int pos, Answer answer) {

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

	public void huntProgress(int pos, Answer answer) {

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
			backDist = 3;
		}

		if (answer.isHit && isbackTrack == true) {
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
		if (isHunt) {
			huntInit(pos , answer);
			//huntProgress(pos, answer);
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
		// dummy return
		return guess;
	} // end of makeGuess()

	@Override
	public void update(Guess guess, Answer answer) {

	} // end of update()

	@Override
	public boolean noRemainingShips() {

		return false;
	} // end of noRemainingShips()

} // end of class GreedyGuessPlayer
