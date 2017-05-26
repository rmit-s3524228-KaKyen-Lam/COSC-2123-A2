package player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import ship.Ship;
import world.World;

/**
 * Monte Carlo guess player (task C). Please implement this class.
 *
 * @author Youhan, Jeffrey
 */

/* http://www.datagenetics.com/blog/december32011/ */

public class MonteCarloGuessPlayer implements Player {

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

	private MonteCalc mc;

	private MonteCalc.AircraftCarrierCalc ac;
	private MonteCalc.BattleShipCalc battle;
	private MonteCalc.CruiserCalc cruise;
	private MonteCalc.SubmarineCalc sub;
	private MonteCalc.DestroyerCalc destroy;

	private ArrayList<Ship> remainingShips = new ArrayList<>();
	private ship.AircraftCarrier aircraftCarrier = new ship.AircraftCarrier();
	private ship.Battleship battleship = new ship.Battleship();
	private ship.Cruiser cruiser = new ship.Cruiser();
	private ship.Submarine submarine = new ship.Submarine();
	private ship.Destroyer destroyer = new ship.Destroyer();
	private Ship selectedShip;

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

		remainingShips.add(aircraftCarrier);
		remainingShips.add(battleship);
		remainingShips.add(cruiser);
		remainingShips.add(submarine);
		remainingShips.add(destroyer);

		this.mc = new MonteCalc();
		mc.parityCreate();
		mc.popZeroScore();
		ac = this.mc.new AircraftCarrierCalc();
		ac.reCalc();
		battle = this.mc.new BattleShipCalc();
		battle.reCalc();
		cruise = this.mc.new CruiserCalc();
		cruise.reCalc();
		sub = this.mc.new SubmarineCalc();
		sub.reCalc();
		destroy = this.mc.new DestroyerCalc();
		destroy.reCalc();

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
	 * Select ship
	 * 
	 * @return ship selected
	 */
	public Ship shipSelection() {

		if (remainingShips.contains(aircraftCarrier)) {
			return aircraftCarrier;
		} else if (remainingShips.contains(battleship)) {
			return battleship;
		} else if (remainingShips.contains(cruiser)) {
			return cruiser;
		} else if (remainingShips.contains(submarine)) {
			return submarine;
		} else {
			return destroyer;
		}

	}

	/**
	 * Update score
	 * 
	 * @param select
	 *            Ship selected
	 * @param column
	 *            column corrdinate
	 * @param row
	 *            row coordinate
	 */
	public void scoreUpdate(Ship select, int column, int row) {

		MonteCalc.zeroScore[column][row] = 0;

		if (select == aircraftCarrier) {
			ac.clearList();
			ac.reCalc();
		} else if (select == battleship) {
			battle.clearList();
			battle.reCalc();
		} else if (select == cruiser) {
			cruise.clearList();
			cruise.reCalc();
		} else if (select == submarine) {
			sub.clearList();
			sub.reCalc();
		} else if (select == destroyer) {
			destroy.clearList();
			destroy.reCalc();
		}

		MonteCalc.isGuessed[column][row] = true;

	}

	/**
	 * 
	 * @param select ship selection
	 * @param index
	 * @return
	 */
	public int[] monteSelect(Ship select) {
		if (select == aircraftCarrier) {
			return ac.targetCalc();
		} else if (select == battleship) {
			return battle.targetCalc();
		} else if (select == cruiser) {
			return cruise.targetCalc();
		} else if (select == submarine) {
			return sub.targetCalc();
		} else {
			return destroy.targetCalc();
		}

	}

	@Override
	public Guess makeGuess() {

		Random rando = new Random();
		int rowIndex;
		int colIndex;
		int config;
		int index = 0;
		Guess guess = new Guess();
		selectedShip = shipSelection();

		if (this.isFound != true) {
			// if ship has not been found

			do {

				int[] gridSelect = monteSelect(selectedShip);
				guess.column = gridSelect[0];
				guess.row = gridSelect[1];

				if (this.isGuessed[guess.column][guess.row]) {
					scoreUpdate(selectedShip, guess.column, guess.row);
					MonteCalc.isGuessed[guess.row][guess.column] = true;

				}

			} while (this.isGuessed[guess.column][guess.row] != false);

			this.isGuessed[guess.column][guess.row] = true;

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
			scoreUpdate(selectedShip, guess.column, guess.row);

			this.isGuessed[guess.row][guess.column] = true;
			MonteCalc.isGuessed[guess.row][guess.column] = true;

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
			if (answer.shipSunk.name().equals(aircraftCarrier.name())) {
				remainingShips.remove(aircraftCarrier);
			} else if (answer.shipSunk.name().equals(battleship.name())) {
				remainingShips.remove(battleship);
			} else if (answer.shipSunk.name().equals(cruiser.name())) {
				remainingShips.remove(cruiser);
			} else if (answer.shipSunk.name().equals(submarine.name())) {
				remainingShips.remove(submarine);
			} else if (answer.shipSunk.name().equals(destroyer.name())) {
				remainingShips.remove(destroyer);
			}
		}
		scoreUpdate(selectedShip, guess.column, guess.row);
		MonteCalc.isGuessed[guess.row][guess.column] = true;
		// resetLoop = true; //for testing

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
