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
	private int rowHunt = 0;
	private int colHunt = 0;
	private int huntTargetCol;
	private int huntTargetRow;
	
	private boolean isHunt = false;
	private int backDist = 2;
	private int huntPos = 0;
	
	 public ArrayList<Coordinate> playerShots = new ArrayList<>();
	 
	
//	private ArrayList[][] checkShots = new ArrayList[int][int];
	
//	private static ArrayList<ArrayList<Integer>> checkShots = new ArrayList<ArrayList<Integer>>();
//	private World.Coordinate test = new World.Coordinate();
	
	
	

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

	public void huntInit(int pos) { //Checks that hunting shot is not out of bounds

		// Initial
//		 colGuess + 1;
//		 colGuess - 1;
//		 rowGuess + 1;
//		 rowGuess - 1;

		if((huntTargetRow - rowSize) < 0) {
			pos =2; 
		} else if (huntTargetCol > colSize) {
			pos =3;
		} else if (huntTargetRow >rowSize) {
			pos =4;			
		} else if((huntTargetCol - colSize) < 0) {
			pos =0;
			
		}
		
//		if (pos == 0) {
//			colHunt = colGuess - 2 + 1;
//			
//		}
//
//		if (pos == 1) {
//			colHunt = colGuess - 2 - 1;
//		}
//
//		if (pos == 2) {
//			rowHunt = rowGuess - 2 + 1;
//		}
//
//		if (pos == 3) {
//			rowHunt = rowGuess - 2 - 1;
//		}
	}

	// Found one
	// backtrack dist = 3
	// Check if row or col
	// row/col + 1
	// backtrack dist + 1
	// if failed
	// row/col - backtrack

	public void huntProgress(int pos) {
		
		if(pos ==1) {
			huntTargetRow = (rowHunt --);
		} else if( pos ==2) {
			huntTargetCol = (colHunt ++);
		} else if (pos ==3) {
			huntTargetRow =(rowHunt ++);
		} else if (pos ==4) {
			huntTargetCol = (colHunt --);
		}
		
//		boolean isbackTrack = false;
//
//		if (isbackTrack == false) {
//			if (pos == 0) {
//				colHunt++;
//				backDist++;
//			} else if (pos == 1) {
//				colHunt--;
//				backDist--;
//			} else if (pos == 2) {
//				rowHunt++;
//				backDist++;
//			} else if (pos == 3) {
//				rowHunt--;
//				backDist--;
//			}
//		} else {
//			if (pos == 0) {
//				colHunt -= backDist;
//			} else if (pos == 1) {
//				colHunt += backDist;
//			} else if (pos == 2) {
//				rowHunt -= backDist;
//			} else if (pos == 3) {
//				rowHunt += backDist;
//			}
//			isbackTrack = true;
//			backDist = 2;
//		}
//
//		if (isbackTrack == true) {
//			if (pos == 0) {
//				colHunt--;
//			} else if (pos == 1) {
//				colHunt++;
//			} else if (pos == 2) {
//				rowHunt--;
//			} else if (pos == 3) {
//				rowHunt++;
//			}
//		}

	}

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
			line-=2;
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
		
		if(this.isHunt != true ) {
	
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
		

			randoGuess.row = i;
			randoGuess.column = j;

	
			this.isGuessed[i][j] = true;
		
			rowGuess = j;
			colGuess = i;
			
			
			
			return randoGuess;
		} else {
			
			if(this.huntCount ==3) { //Checks hunts have been done no more than 4 times
				this.isHunt = false;
				huntCount =0;
				
			} else {
				this.huntCount ++;
			}

				huntPos ++;
				
				
				huntProgress(huntPos);
				huntInit(huntPos); 
				
				randoGuess.row = huntTargetRow;
				randoGuess.column = huntTargetCol;
				
				
//				USE SOMETHING ELSE TO STORE CURRENT SHOT NOT HUNTCOL AND HUNTROW 
//				return randoGuess;
//					 do {
//					
//						 huntInit(pos);
//				
//					 	 huntProgress(pos);
//						 guess.row = rowHunt;
//						 guess.column = colHunt;
//						 if (pos < 4) {
//						 pos++;
//					 
//					
//					 if (pos == 4) {
//					
//					 	}
//					 } else {
//						 guess.row = rowGuess;
//						 guess.column = colGuess;
//						 generateNextGuess();
//					 }
//					
//					 if (guess.row < 0 || guess.column < 0 || guess.row > 9 || guess.column >9) {
//						 guess.row = rowGuess;
//						 guess.column = colGuess;
//						 generateNextGuess();
//					 }
//					 }while (this.isGuessed[guess.row][guess.column]);
//					
//						 this.isGuessed[guess.row][guess.column] = true;
//						
//						 if (guess.row < 0 || guess.column < 0 || guess.row > 9 || guess.column > 9) {
//					
//					 }

			}
		return randoGuess;
		}
		


	

	@Override
	public void update(Guess guess, Answer answer) {
		if (answer.isHit == true) {
		
			this.isHunt =true;
			huntPos = 0; //Resets hunt stats to start hunting from last hit
			colHunt =guess.column;
			rowHunt = guess.row;

		
			
			
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