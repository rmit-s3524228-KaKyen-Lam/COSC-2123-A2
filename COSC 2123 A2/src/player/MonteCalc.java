package player;

public class MonteCalc {

	private static int maxCol = 10;
	private static int maxRow = 10;
	private String up = "up";
	private String down = "down";
	private String left = "left";
	private String right = "right";
	private static int[][] zeroScore = new int[10][10];

	public static void popZeroScore() {
		for (int i = 0; i < maxCol; i++) {
			for (int j = 0; j < maxRow; j++) {
				zeroScore[j][i] = -1;
			}
		}
	}

	public int[][] scoreGenerator(int length, int config) {

		int[][] score = new int[10][10];
		int endPoint = 0;
		int startPoint = 0;

		for (int i = 0; i < maxCol; i++) {
			startPoint = 0;
			endPoint = 0;
			while (length + endPoint <= maxRow) {
				for (int j = startPoint; j < length + endPoint; j++) {
					if (config == 0) {
						score[j][i] += 1;
					} else if (config == 1) {
						score[i][j] += 1;
					}
				}
				startPoint++;
				endPoint++;
			}

		}
		return score;
	}

	public int[][] scoreCombiner(int[][] score1, int[][] score2) {
		int[][] score = new int[10][10];
		for (int i = 0; i < maxCol; i++) {
			for (int j = 0; j < maxRow; j++) {
				score[j][i] = score1[j][i] + score2[j][i];
			}
		}

		return score;
	}

	public static int[][] currentHits(int[][] score) {
		for (int i = 0; i < maxCol; i++) {
			for (int j = 0; j < maxRow; j++) {
				if (zeroScore[j][i] == 0) {
					score[j][i] = 0;
				}
			}
		}

		return score;
	}

	public void decrementLimit(int[][] score, int column, int row) {
		if (score[column][row] <= 0 && zeroScore[column][row] != 0) {
			score[column][row] = 1;
		}
	}

	public void scoreLimit(int[][] score, int column, int row) {
		if (score[column][row] < 0) {
			score[column][row] = 0;
		}
	}

	public void decrement(int[][] score, int column, int row, int length, String direction) {

		if (direction == up) {

			if (row + length > maxRow) {
				length = maxRow - row;
			}
			for (int i = row; i < row + length; i++) {
				score[column][i]--;
				decrementLimit(score, column, i);
				scoreLimit(score, column, i);
			}
		}

		if (direction == right) {
			if (column + length > maxCol) {
				length = maxCol - column;
			}
			for (int i = column; i < column + length; i++) {
				score[i][row]--;
				decrementLimit(score, i, row);
				scoreLimit(score, i, row);
			}
		}

		if (direction == down) {
			if (row - length < 0) {
				length = row;
			}
			for (int i = row; i > row - length; i--) {
				score[column][i]--;
				decrementLimit(score, column, i);
				scoreLimit(score, column, i);
			}
		}

		if (direction == left) {
			if (column - length < 0) {
				length = column;
			}
			for (int i = column; i > column - length; i--) {
				score[i][row]--;
				decrementLimit(score, i, row);
				scoreLimit(score, i, row);
			}
		}
	}

	public int[][] scoreCalc(int[][] score, int maxScore, int length) {

		for (int i = 0; i < maxCol; i++) {
			for (int j = 0; j < maxRow; j++) {
				if (score[j][i] == 0) {
					if (i + 1 < maxRow) {
						if (score[j][i + 1] < maxScore) {
							decrement(score, j, i + 1, length, up);
						}
					}

					if (j + 1 < maxCol) {
						if (score[j + 1][i] < maxScore) {
							decrement(score, j + 1, i, length, right);
						}
					}

					if (i - 1 >= 0) {
						if (score[j][i - 1] < maxScore) {
							decrement(score, j, i - 1, length, down);
						}
					}
					if (j - 1 >= 0) {
						if (score[j - 1][i] < maxScore) {
							decrement(score, j - 1, i, length, left);
						}
					}
				}

				if (score[j][i] < 0) {
					score[j][i] = 0;
				}
			}
		}

		return score;
	}
	
	public class ShipCalc {
		
		
		MonteCalc mc = new MonteCalc();
		
		public ShipCalc(int length) {
			this.length = length;
			this.maxScore = length * 2;
			
		}
		private int length;
		private int maxScore;
		private int[][] hScore = mc.scoreGenerator(length, 0);
		private int[][] vScore = mc.scoreGenerator(length, 1);
		private int[][] score = mc.scoreCombiner(hScore, vScore);
		
		


		
	}

//	public class BattleShipCalc extends ShipCalc{
//
//		public BattleShipCalc() {
//			
//		}
//
//
//		public void reCalc() {
//			MonteCalc.currentHits(score);
//			scoreCalc(score, maxScore, length);
//		}
//
//		public void print() {
//			for (int j = 0; j < maxCol; j++) {
//				for (int i = 0; i < maxRow; i++) {
//					System.out.print("[" + score[i][j] + "]\t");
//				}
//				System.out.println();
//			}
//			System.out.println();
//		}
//
//	}
	
	public class CarrierCalc {

		MonteCalc mc = new MonteCalc();
		private int length = 5;
		private int maxScore = length * 2;
		private int[][] hScore = mc.scoreGenerator(length, 0);
		private int[][] vScore = mc.scoreGenerator(length, 1);
		private int[][] score = mc.scoreCombiner(hScore, vScore);


		public void reCalc() {
			MonteCalc.currentHits(score);
			scoreCalc(score, maxScore, length);
		}

		public void print() {
			for (int j = 0; j < maxCol; j++) {
				for (int i = 0; i < maxRow; i++) {
					System.out.print("[" + score[i][j] + "]\t");
				}
				System.out.println();
			}
			System.out.println();
		}

	}
	
	public class SubmarineCalc {

		MonteCalc mc = new MonteCalc();
		private int length = 3;
		private int maxScore = length * 2;
		private int[][] hScore = mc.scoreGenerator(length, 0);
		private int[][] vScore = mc.scoreGenerator(length, 1);
		private int[][] score = mc.scoreCombiner(hScore, vScore);


		public void reCalc() {
			MonteCalc.currentHits(score);
			scoreCalc(score, maxScore, length);
		}

		public void print() {
			for (int j = 0; j < maxCol; j++) {
				for (int i = 0; i < maxRow; i++) {
					System.out.print("[" + score[i][j] + "]\t");
				}
				System.out.println();
			}
			System.out.println();
		}

	}
	
	public class CruiserCalc {

		MonteCalc mc = new MonteCalc();
		private int length = 3;
		private int maxScore = length * 2;
		private int[][] hScore = mc.scoreGenerator(length, 0);
		private int[][] vScore = mc.scoreGenerator(length, 1);
		private int[][] score = mc.scoreCombiner(hScore, vScore);


		public void reCalc() {
			MonteCalc.currentHits(score);
			scoreCalc(score, maxScore, length);
		}

		public void print() {
			for (int j = 0; j < maxCol; j++) {
				for (int i = 0; i < maxRow; i++) {
					System.out.print("[" + score[i][j] + "]\t");
				}
				System.out.println();
			}
			System.out.println();
		}

	}
	
	public class DestroyerCalc {

		MonteCalc mc = new MonteCalc();
		private int length = 2;
		private int maxScore = length * 2;
		private int[][] hScore = mc.scoreGenerator(length, 0);
		private int[][] vScore = mc.scoreGenerator(length, 1);
		private int[][] score = mc.scoreCombiner(hScore, vScore);


		public void reCalc() {
			MonteCalc.currentHits(score);
			scoreCalc(score, maxScore, length);
		}

		public void print() {
			for (int j = 0; j < maxCol; j++) {
				for (int i = 0; i < maxRow; i++) {
					System.out.print("[" + score[i][j] + "]\t");
				}
				System.out.println();
			}
			System.out.println();
		}

	}
	

	public static void main(String[] args) {
		MonteCalc mc = new MonteCalc();
		MonteCalc.popZeroScore();
		MonteCalc.CarrierCalc car = mc.new CarrierCalc();
//		MonteCalc.BattleShipCalc battle = mc.new BattleShipCalc();
		MonteCalc.CruiserCalc cruise = mc.new CruiserCalc();
		MonteCalc.SubmarineCalc sub = mc.new SubmarineCalc();
		MonteCalc.DestroyerCalc destroy = mc.new DestroyerCalc();
	}
	
}