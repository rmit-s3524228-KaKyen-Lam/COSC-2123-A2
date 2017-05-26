package player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MonteCalc {

	private static int maxCol = 10;
	private static int maxRow = 10;
	private String up = "up";
	private String down = "down";
	private String left = "left";
	private String right = "right";
	public static boolean[][] isGuessed = new boolean[10][10];
	public static int[][] zeroScore = new int[10][10];
	ArrayList<Integer> rowConfig0 = parityGen(0, maxRow);
	ArrayList<Integer> rowConfig1 = parityGen(1, maxRow);
	ArrayList<Integer> colConfig0 = parityGen(0, maxCol);
	ArrayList<Integer> colConfig1 = parityGen(1, maxCol);
	private boolean[][] parityGrid = new boolean[10][10];

	/**
	 * Aircraft Carrier Calculation class
	 * 
	 *
	 */
	public class AircraftCarrierCalc {

		MonteCalc mc = new MonteCalc();
		private int length = 5;
		private int[][] hScore = mc.scoreGenerator(length, 0);
		private int[][] vScore = mc.scoreGenerator(length, 1);
		private int[][] score = mc.scoreCombiner(hScore, vScore);
		private ArrayList<Integer> scoreList = new ArrayList<>();

		public void reCalc() {
			MonteCalc.currentHits(score);
			scoreCalc(score, scoreList, length);
		}

		public int[] targetCalc() {
			ArrayList<Integer> sortedList = new ArrayList<>();
			sortedList = heapInit(scoreList, sortedList);
			return acquireTarget(score, sortedList.get(0));
		}

		public void clearList() {
			scoreList.clear();
		}

		public void getScore() {
			for (int i = 0; i < maxCol; i++) {
				for (int j = 0; j < maxRow; j++) {
					System.out.print(score[j][i] + "\t");
				}
				System.out.println();
			}
			System.out.println();
		}

		public void resetScore(int column, int row) {
			score[column][row]++;
		}
	}

	/**
	 * Battleship Calculation class
	 * 
	 *
	 */
	public class BattleShipCalc {

		MonteCalc mc = new MonteCalc();
		private int length = 4;
		private int[][] hScore = mc.scoreGenerator(length, 0);
		private int[][] vScore = mc.scoreGenerator(length, 1);
		private int[][] score = mc.scoreCombiner(hScore, vScore);
		private ArrayList<Integer> scoreList = new ArrayList<>();

		public void reCalc() {
			MonteCalc.currentHits(score);
			scoreCalc(score, scoreList, length);
		}

		public int[] targetCalc() {
			ArrayList<Integer> sortedList = new ArrayList<>();
			sortedList = heapInit(scoreList, sortedList);
			return acquireTarget(score, sortedList.get(0));
		}

		public void clearList() {
			scoreList.clear();
		}

		public void getScore() {
			for (int i = 0; i < maxCol; i++) {
				for (int j = 0; j < maxRow; j++) {
					System.out.print(score[j][i] + "\t");
				}
				System.out.println();
			}
			System.out.println();
		}

		public void resetScore(int column, int row) {
			score[column][row]++;
		}

	}

	/**
	 * Submarine Calculation class
	 * 
	 */
	public class SubmarineCalc {

		MonteCalc mc = new MonteCalc();
		private int length = 3;
		private int[][] hScore = mc.scoreGenerator(length, 0);
		private int[][] vScore = mc.scoreGenerator(length, 1);
		private int[][] score = mc.scoreCombiner(hScore, vScore);
		private ArrayList<Integer> scoreList = new ArrayList<>();

		public void reCalc() {
			MonteCalc.currentHits(score);
			scoreCalc(score, scoreList, length);
		}

		public int[] targetCalc() {
			ArrayList<Integer> sortedList = new ArrayList<>();
			sortedList = heapInit(scoreList, sortedList);
			return acquireTarget(score, sortedList.get(0));
		}

		public void clearList() {
			scoreList.clear();
		}

		public void getScore() {
			for (int i = 0; i < maxCol; i++) {
				for (int j = 0; j < maxRow; j++) {
					System.out.print(score[j][i] + "\t");
				}
				System.out.println();
			}
			System.out.println();
		}

		public void resetScore(int column, int row) {
			score[column][row]++;
		}

	}

	/**
	 * Cruiser Calculation class
	 *
	 *
	 */
	public class CruiserCalc {

		MonteCalc mc = new MonteCalc();
		private int length = 3;
		private int[][] hScore = mc.scoreGenerator(length, 0);
		private int[][] vScore = mc.scoreGenerator(length, 1);
		private int[][] score = mc.scoreCombiner(hScore, vScore);
		private ArrayList<Integer> scoreList = new ArrayList<>();

		public void reCalc() {
			MonteCalc.currentHits(score);
			scoreCalc(score, scoreList, length);
		}

		public int[] targetCalc() {
			ArrayList<Integer> sortedList = new ArrayList<>();
			sortedList = heapInit(scoreList, sortedList);
			return acquireTarget(score, sortedList.get(0));
		}

		public void clearList() {
			scoreList.clear();
		}

		public void getScore() {
			for (int i = 0; i < maxCol; i++) {
				for (int j = 0; j < maxRow; j++) {
					System.out.print(score[j][i] + "\t");
				}
				System.out.println();
			}
			System.out.println();
		}

		public void resetScore(int column, int row) {
			score[column][row]++;
		}

	}

	/**
	 * Destroyer Calculation class
	 * 
	 *
	 */
	public class DestroyerCalc {

		MonteCalc mc = new MonteCalc();
		private int length = 2;
		private int[][] hScore = mc.scoreGenerator(length, 0);
		private int[][] vScore = mc.scoreGenerator(length, 1);
		private int[][] score = mc.scoreCombiner(hScore, vScore);
		private ArrayList<Integer> scoreList = new ArrayList<>();

		public void reCalc() {
			MonteCalc.currentHits(score);
			scoreCalc(score, scoreList, length);
		}

		public int[] targetCalc() {
			ArrayList<Integer> sortedList = new ArrayList<>();
			sortedList = heapInit(scoreList, sortedList);
			return acquireTarget(score, sortedList.get(0));
		}

		public void clearList() {
			scoreList.clear();
		}

		public void getScore() {
			for (int i = 0; i < maxCol; i++) {
				for (int j = 0; j < maxRow; j++) {
					System.out.print(score[j][i] + "\t");
				}
				System.out.println();
			}
			System.out.println();
		}

		public void resetScore(int column, int row) {
			score[column][row]++;
		}
	}

	/**
	 * Method to acquire target
	 * 
	 * @param grid
	 *            score frid to be analysed
	 * @param value
	 *            target value
	 * @return
	 */
	public int[] acquireTarget(int[][] grid, int value) {
		int[] target = new int[2];
		for (int i = 0; i < maxCol; i++) {
			for (int j = 0; j < maxRow; j++) {
				if (grid[j][i] == value) {
					target[0] = j;
					target[1] = i;
					return target;
				}
			}
		}
		return target;
	}

	/**
	 * Populate the zero grid
	 */
	public void popZeroScore() {
		for (int i = 0; i < maxCol; i++) {
			for (int j = 0; j < maxRow; j++) {
				zeroScore[j][i] = -1;
			}
		}
	}

	/**
	 * Generate the initial scores for the score grids
	 * 
	 * @param length
	 *            length of ship
	 * @param config
	 *            horizontal or vertical (0,1);
	 * @return
	 */
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
						score[j][i]++;
					} else if (config == 1) {
						score[i][j]++;
					}
				}
				startPoint++;
				endPoint++;
			}

		}
		return score;
	}

	/**
	 * Combine the score grids to form the final grid
	 * 
	 * @param score1
	 * @param score2
	 * @return
	 */
	public int[][] scoreCombiner(int[][] score1, int[][] score2) {
		int[][] score = new int[10][10];
		for (int i = 0; i < maxCol; i++) {
			for (int j = 0; j < maxRow; j++) {
				score[j][i] = score1[j][i] + score2[j][i];
			}
		}

		return score;
	}

	/**
	 * Track current hits on the board
	 * 
	 * @param score
	 *            score grid
	 * @return score on coordinate
	 */

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

	/**
	 * Setting limit for decrement
	 * 
	 * @param score
	 *            score grid
	 * @param column
	 *            column coordinate
	 * @param row
	 *            row coordinate
	 */
	public void decrementLimit(int[][] score, int column, int row) {
		if (score[column][row] <= 0 && zeroScore[column][row] != 0) {
			score[column][row] = 1;
		}
	}

	/**
	 * Setting score limit to ensure it does not go negative
	 * 
	 * @param score
	 *            score grid
	 * @param column
	 *            column coordinate
	 * @param row
	 *            row coordinate
	 */
	public void scoreLimit(int[][] score, int column, int row) {
		if (score[column][row] < 0) {
			score[column][row] = 0;
		}
	}

	/**
	 * Decrease the scores on the score grid
	 * 
	 * @param score
	 *            score grid
	 * @param column
	 *            column coordinate
	 * @param row
	 *            row coordinate
	 * @param length
	 *            ship length
	 * @param direction
	 *            direction of decrement
	 * 
	 */
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

	/**
	 * Calculate the scores after decrement
	 * 
	 * 
	 * @param score
	 *            score grid
	 * @param list
	 *            score array
	 * @param length
	 *            ship length
	 * @return
	 */
	public int[][] scoreCalc(int[][] score, ArrayList<Integer> list, int length) {

		for (int i = 0; i < maxCol; i++) {
			for (int j = 0; j < maxRow; j++) {
				if (score[j][i] == 0 && !isGuessed[j][i]) {

					if (i + 1 < maxRow) {
						decrement(score, j, i + 1, length, up);
					}

					if (j + 1 < maxCol) {
						decrement(score, j + 1, i, length, right);
					}

					if (i - 1 >= 0) {
						decrement(score, j, i - 1, length, down);
					}
					if (j - 1 >= 0) {
						decrement(score, j - 1, i, length, left);
					}

				}

				if (score[j][i] < 0) {
					score[j][i] = 0;
				}

				if (score[j][i] != 0 && parityGrid[j][i]) {
					list.add(score[j][i]);
				}

			}
		}

		return score;
	}

	/**
	 * Heapsort to sort data
	 * 
	 * @param inputArrayList
	 *            Unsorted arraylist
	 * @param outPutArrayList
	 *            Empty ArrayList
	 * @return Sorted arraylist
	 */
	public static ArrayList<Integer> heapInit(ArrayList<Integer> inputArrayList, ArrayList<Integer> outPutArrayList) {
		int[] sortArray = new int[inputArrayList.size()];// Gets arraylist size
															// for array[int]

		for (int j = 0; j < inputArrayList.size(); j++) {// Adds from arraylist
															// to array
			int i = inputArrayList.get(j);
			sortArray[j] = i;
		}

		sort(sortArray);

		for (int j = 0; j < sortArray.length; j++) {// Adds from arraylist to
													// array
			int i = sortArray[j];
			outPutArrayList.add(i);

		}
		Collections.reverse(outPutArrayList);
		return outPutArrayList;
	}

	/**
	 * Heapsort algorithm
	 * 
	 * @param inputArray
	 */
	public static void sort(int inputArray[]) {
		for (int i = inputArray.length / 2 - 1; i >= 0; i--) {// Havles the
																// array
			heapShift(inputArray, inputArray.length, i);
		}
		for (int i = inputArray.length - 1; i >= 0; i--)// Takes biggest element
														// to top
		{
			int temp = inputArray[0];
			inputArray[0] = inputArray[i];
			inputArray[i] = temp;
			heapShift(inputArray, i, 0);
		}
	}

	/**
	 * Heap Shift
	 * 
	 * @param arr
	 *            array input
	 * @param arrayLength
	 *            length of array
	 * @param root
	 *            root
	 */
	static void heapShift(int arr[], int arrayLength, int root) {

		int leftIndex = 2 * root + 1;
		int rightIndex = 2 * root + 2;
		int bigElem = root;

		if (leftIndex < arrayLength && arr[leftIndex] > arr[bigElem])
			bigElem = leftIndex;

		if (rightIndex < arrayLength && arr[rightIndex] > arr[bigElem])
			bigElem = rightIndex;

		if (bigElem != root) {

			int swap = arr[root];
			arr[root] = arr[bigElem];
			arr[bigElem] = swap;
			heapShift(arr, arrayLength, bigElem);
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

	public void parityCreate() {
		for (int i = 0; i < maxCol; i++) {
			for (int j = 0; j < maxRow; j++) {
				if (colConfig0.contains(i) && rowConfig1.contains(j)) {
					parityGrid[j][i] = true;
				} else if (colConfig1.contains(i) && rowConfig0.contains(j)) {
					parityGrid[j][i] = true;
				} else {
					parityGrid[j][i] = false;
				}

			}
		}
	}
}
