package player;

public class MonteCalc {

	private static int maxCol = 10;
	private static int maxRow = 10;
	private String up = "up";
	private String down = "down";
	private String left = "left";
	private String right = "right";
	private static int[][] zeroGrid = new int[10][10];

	public static void popZeroGrid() {
		for (int i = 0; i < maxCol; i++) {
			for (int j = 0; j < maxRow; j++) {
				zeroGrid[j][i] = -1;
			}
		}
	}

	public int[][] gridGenerator(int length, int config) {

		int[][] grid = new int[10][10];
		int endPoint = 0;
		int startPoint = 0;

		for (int i = 0; i < maxCol; i++) {
			startPoint = 0;
			endPoint = 0;
			while (length + endPoint <= maxRow) {
				for (int j = startPoint; j < length + endPoint; j++) {
					if (config == 0) {
						grid[j][i] += 1;
					} else if (config == 1) {
						grid[i][j] += 1;
					}
				}
				startPoint++;
				endPoint++;
			}

		}
		return grid;
	}

	public int[][] gridCombiner(int[][] grid1, int[][] grid2) {
		int[][] grid = new int[10][10];
		for (int i = 0; i < maxCol; i++) {
			for (int j = 0; j < maxRow; j++) {
				grid[j][i] = grid1[j][i] + grid2[j][i];
			}
		}

		return grid;
	}

	public static int[][] currentHits(int[][] grid) {
		for (int i = 0; i < maxCol; i++) {
			for (int j = 0; j < maxRow; j++) {
				if (zeroGrid[j][i] == 0) {
					grid[j][i] = 0;
				}
			}
		}

		return grid;
	}

	public void decrementLimit(int[][] grid, int column, int row) {
		if (grid[column][row] <= 0 && zeroGrid[column][row] != 0) {
			grid[column][row] = 1;
		}
	}

	public void scoreLimit(int[][] grid, int column, int row) {
		if (grid[column][row] < 0) {
			grid[column][row] = 0;
		}
	}

	public void decrement(int[][] grid, int column, int row, int length, String direction) {

		if (direction == up) {

			if (row + length > maxRow) {
				length = maxRow - row;
			}
			for (int i = row; i < row + length; i++) {
				grid[column][i]--;
				decrementLimit(grid, column, i);
				scoreLimit(grid, column, i);
			}
		}

		if (direction == right) {
			if (column + length > maxCol) {
				length = maxCol - column;
			}
			for (int i = column; i < column + length; i++) {
				grid[i][row]--;
				decrementLimit(grid, i, row);
				scoreLimit(grid, i, row);
			}
		}

		if (direction == down) {
			if (row - length < 0) {
				length = row;
			}
			for (int i = row; i > row - length; i--) {
				grid[column][i]--;
				decrementLimit(grid, column, i);
				scoreLimit(grid, column, i);
			}
		}

		if (direction == left) {
			if (column - length < 0) {
				length = column;
			}
			for (int i = column; i > column - length; i--) {
				grid[i][row]--;
				decrementLimit(grid, i, row);
				scoreLimit(grid, i, row);
			}
		}
	}

	public int[][] gridCalc(int[][] grid, int maxScore, int length) {

		for (int i = 0; i < maxCol; i++) {
			for (int j = 0; j < maxRow; j++) {
				if (grid[j][i] == 0) {
					if (i + 1 < maxRow) {
						if (grid[j][i + 1] < maxScore) {
							decrement(grid, j, i + 1, length, up);
						}
					}

					if (j + 1 < maxCol) {
						if (grid[j + 1][i] < maxScore) {
							decrement(grid, j + 1, i, length, right);
						}
					}

					if (i - 1 >= 0) {
						if (grid[j][i - 1] < maxScore) {
							decrement(grid, j, i - 1, length, down);
						}
					}
					if (j - 1 >= 0) {
						if (grid[j - 1][i] < maxScore) {
							decrement(grid, j - 1, i, length, left);
						}
					}
				}

				if (grid[j][i] < 0) {
					grid[j][i] = 0;
				}
			}
		}

		return grid;
	}

	public class BattleShipCalc {

		MonteCalc mc = new MonteCalc();
		private int length = 5;
		private int maxScore = 10;
		private int[][] hGrid = mc.gridGenerator(length, 0);
		private int[][] vGrid = mc.gridGenerator(length, 1);
		private int[][] grid = mc.gridCombiner(hGrid, vGrid);

		public void sampleHit() {
			MonteCalc.zeroGrid[5][5] = 0;
			MonteCalc.zeroGrid[4][5] = 0;
			MonteCalc.zeroGrid[5][4] = 0;
			MonteCalc.zeroGrid[4][4] = 0;
		}

		public void reCalc() {
			MonteCalc.currentHits(grid);
			gridCalc(grid, maxScore, length);
		}

		public void print() {
			for (int j = 0; j < maxCol; j++) {
				for (int i = 0; i < maxRow; i++) {
					System.out.print("[" + grid[i][j] + "]\t");
				}
				System.out.println();
			}

		}

	}

	public static void main(String[] args) {
		MonteCalc mc = new MonteCalc();
		MonteCalc.popZeroGrid();
		MonteCalc.BattleShipCalc bs = mc.new BattleShipCalc();
		bs.sampleHit();
		bs.reCalc();
		bs.print();
	}
}
