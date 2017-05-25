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

	private int rowGuess = 0;
	private int colGuess = 0;

	private int huntCount = 0;
	private int rowInitHunt = 0;
	private int colInitHunt = 0;
	private int rowProgHunt = 0;
	private int colProgHunt = 0;

	private int huntTargetCol;
	private int huntTargetRow;

	private boolean isHunt = false;
	private int backDist = 2;
	private int huntPos = 0;

	int pos = 0;
	private boolean isInitialHunt = false;
	private boolean isBacktrack = false;

	public ArrayList<Coordinate> playerShots = new ArrayList<>();
	private int hitPos;
	private boolean isFirstGuess = true;
	private boolean resetLoop = false;

	// private ArrayList[][] checkShots = new ArrayList[int][int];

	// private static ArrayList<ArrayList<Integer>> checkShots = new
	// ArrayList<ArrayList<Integer>>();
	// private World.Coordinate test = new World.Coordinate();

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

	public void huntInit(int pos) { // Checks that hunting shot is not out of
									// bounds

		if ((huntTargetRow - rowSize) < 0) {
			pos = 2;
		} else if (huntTargetCol > colSize) {
			pos = 3;
		} else if (huntTargetRow > rowSize) {
			pos = 4;
		} else if ((huntTargetCol - colSize) < 0) {
			pos = 0;

		}
	}

	public void upInitial() {
		rowGuess = rowInitHunt + 1;
		colGuess = colInitHunt;
		pos = 0;
	}

	public void rightInitial() {
		rowGuess = rowInitHunt;
		colGuess = colInitHunt + 1;
		pos = 1;
	}

	public void downInitial() {
		rowGuess = rowInitHunt - 1;
		colGuess = colInitHunt;
		pos = 2;
	}

	public void leftInitial() {
		rowGuess = rowInitHunt;
		colGuess = colInitHunt - 1;
		pos = 3;
	}

	public void guessCheck(int mode) {
		boolean guessed = this.isGuessed[rowGuess][colGuess];
		if (guessed) {
			if (mode == 0) {
				if (pos == 0) {
					rightInitial();
					pos = 1;
				}

				if (pos == 1) {
					downInitial();
					pos = 2;
				}
				if (pos == 2) {
					leftInitial();
					pos = 3;
				}
				if (pos == 3) {
					upInitial();
					pos = 0;
				}
			} else {
				isBacktrack = true;
				if (pos == 0) {
					downInitial();
				}

				if (pos == 1) {
					leftInitial();
				}
				if (pos == 2) {
					upInitial();
					pos = 0;
				}
				if (pos == 3) {
					rightInitial();
					pos = 1;
				}
			}
		}
	}

	public void boundaryCheck(int mode) {

		if (mode == 0) {
			if (rowGuess >= rowSize) {
				rightInitial();
			}

			if (colGuess >= colSize) {
				downInitial();
			}
			if (rowGuess < 0) {
				leftInitial();
			}
			if (colGuess < 0) {
				upInitial();
			}
		} else {
			
			if (rowGuess >= rowSize) {
				downInitial();
				isBacktrack = true;
			}

			if (colGuess >= colSize) {
				leftInitial();
				isBacktrack = true;
			}
			if (rowGuess < 0) {
				upInitial();
				isBacktrack = true;
			}
			if (colGuess < 0) {
				rightInitial();
				isBacktrack = true;
			}
		}

	}

	//
	// } else if (pos == 1) {
	//
	// } else if (pos == 2) {
	//
	// } else if (pos == 3) {
	//
	// }

	// public boolean boundaryCheck() {
	// boolean isOut = true;
	// if (rowGuess >= rowSize) {
	// rowGuess = rowInitHunt;
	// pos = 1;
	// } else if (colGuess >= colSize) {
	// colGuess = colInitHunt;
	// pos = 2;
	// } else if (rowGuess < 0) {
	// rowGuess = rowInitHunt;
	// pos = 3;
	// } else if (colGuess < 0) {
	// colGuess = colInitHunt;
	// pos = 0;
	// } else {
	// isOut = false;
	// }
	// return isOut;
	// }

	// public void initialHunt() {
	// int i = 0;
	// int j = 0;
	//
	// do {
	// if (pos == 0) {
	// rowInitHunt = rowGuess;
	// colInitHunt = colGuess + 1;
	// } else if (pos == 1) {
	// rowInitHunt = rowGuess + 1;
	// colInitHunt = colGuess;
	// } else if (pos == 2) {
	// rowInitHunt = rowGuess;
	// colInitHunt = colGuess - 1;
	// } else if (pos == 3) {
	// rowInitHunt = rowGuess - 1;
	// colInitHunt = colGuess;
	// }
	//
	// } while (this.isGuessed[i][j] != false);
	// pos++;
	//
	// }

	public ArrayList<Integer> parityGen(int config, int line) {
		ArrayList<Integer> lineElem = new ArrayList<>();

		if (config == 0) {
			line--;
			while (line > -1) {
				lineElem.add(line);
				line -= 2;
			}
		}

		if (config == 1) {
			line -= 2;
			while (line > -1) {
				lineElem.add(line);
				line -= 2;
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
		Guess randoGuess = new Guess();

		if (this.isHunt != true) {

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

			if (resetLoop) /* Used for testing */ {

				randoGuess.row = i;
				randoGuess.column = j;
				rowGuess = i;
				colGuess = j;
			} else {
				// Test variables
				randoGuess.row = 9;
				randoGuess.column = 1;
			}

			this.isGuessed[i][j] = true;

			return randoGuess;
		} else {

			randoGuess.row = rowGuess;
			randoGuess.column = colGuess;

			this.isGuessed[randoGuess.row][randoGuess.column] = true;
			return randoGuess;
		}

	}

	@Override
	public void update(Guess guess, Answer answer) {

		if (!this.isHunt && answer.isHit) {
			this.isHunt = true;
			this.isInitialHunt = true;
			huntPos = 0; // Resets hunt stats to start hunting from last
							// hit
			colInitHunt = guess.column;
			rowInitHunt = guess.row;
			colGuess = colInitHunt;
			rowGuess = rowInitHunt;
			isFirstGuess = true;
		}

		if (this.isHunt) {
			if (answer.isHit && !isFirstGuess) {
				hitPos = pos;
				isInitialHunt = false;
				if (!isBacktrack) {
					if (pos == 0) {
						rowGuess++;
					} else if (pos == 1) {
						colGuess++;
					} else if (pos == 2) {
						rowGuess--;
					} else if (pos == 3) {
						colGuess--;
					}
					boundaryCheck(1);
				} else {
					if (pos == 0) {
						rowGuess--;
					} else if (pos == 1) {
						colGuess--;
					} else if (pos == 2) {
						rowGuess++;
					} else if (pos == 3) {
						colGuess++;
					}
				}
			}

			if (!answer.isHit) {
				if (isInitialHunt) {
					pos++;
				} else {
					if (pos == 0) {
						downInitial();
					} else if (pos == 1) {
						leftInitial();
					} else if (pos == 2) {
						upInitial();
					} else if (pos == 3) {
						rightInitial();
					}
					isBacktrack = true;
				}
			}

			if (isInitialHunt) {
				if (pos == 0) {
					upInitial();
				} else if (pos == 1) {
					rightInitial();
				} else if (pos == 2) {
					downInitial();
				} else if (pos == 3) {
					leftInitial();
				}
				boundaryCheck(0);
				isFirstGuess = false;
			}
		}

		if (answer.shipSunk != null) {
			this.isHunt = false;
			isBacktrack = false;
			resetLoop = true;
			pos = 0;
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