package recursion.assignment.NQueens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * The n-queens puzzle is the problem of placing n queens on an n×n chessboard
 * such that no two queens attack each other.
 * 
 * 
 * 
 * Given an integer n, return all distinct solutions to the n-queens puzzle.
 * 
 * Each solution contains a distinct board configuration of the n-queens'
 * placement, where 'Q' and '.' both indicate a queen and an empty space
 * respectively.
 * 
 * For example, There exist two distinct solutions to the 4-queens puzzle:
 * 
 * @author dwaraknathbs
 *
 */
public class NQueens {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		scanner.close();
		char[][] chessBoard = new char[n][n];
		for (int i = 0; i < chessBoard.length; i++) {
			chessBoard[i] = new char[chessBoard.length];
			Arrays.fill(chessBoard[i], 'X');
		}

		System.out.println("Solutions using back tracking");
		List<char[][]> solutions = solveNQueensUsingBackTracing(chessBoard, n);
		solutions.stream().forEach(sol -> printSolution(sol));
		System.out.println("Solutions using permutations");
		solutions = solveNQueensUsingPermutations(n);
		solutions.stream().forEach(sol -> printSolution(sol));

	}

	/**
	 * Key idea is to think in terms of output. We do not need a 2D array to
	 * denote the positions , instead it could be a 1D array with indices
	 * denoting the rows and the values in arrays denoting the columns. Once
	 * this insight is clear different positions of the queens in the baord is
	 * just the different permutations of the numbers 1..n
	 * 
	 * @param n
	 * @return
	 */
	private static List<char[][]> solveNQueensUsingPermutations(int n) {
		int[] rows = new int[n];
		for (int i = 0; i < n; i++) {
			rows[i] = i + 1;
		}
		List<char[][]> solutions = new ArrayList<>();
		nQueens(rows, 0, n, solutions);
		return solutions;
	}

	/**
	 * Contract: I get a position decided till the index, I will check for
	 * permutations from my index and revert it to the state I was given in
	 * 
	 * @param rows
	 * @param index
	 * @param n
	 * @param solutions
	 */
	private static void nQueens(int[] rows, int index, int n, List<char[][]> solutions) {

		if (index == rows.length) {

			char[][] sol = new char[n][n];
			for (int i = 0; i < rows.length; i++) {
				int colIndex = rows[i];
				sol[colIndex - 1] = new char[rows.length];
				sol[colIndex - 1][i] = 'Q';
			}

			solutions.add(sol);

		}

		for (int k = index; k < rows.length; k++) {
			swap(rows, index, k);
			if (isProperPosition(rows, index)) {
				nQueens(rows, index + 1, n, solutions);
			}
			swap(rows, index, k);
		}
	}

	/**
	 * Given a position as a 1D array determines if the queen is threatening
	 * each other.
	 * 
	 * @param rows
	 * @param index
	 * @return
	 */
	private static boolean isProperPosition(int[] rows, int index) {
		for (int i = 0; i < index; i++) {
			int col = rows[i];
			int col2 = rows[index];
			int r1 = index;
			int r2 = i;
			if (Math.abs(r1 - r2) == Math.abs(col - col2))
				return false;

		}
		return true;
	}

	private static void swap(int[] arr, int s, int k) {
		int temp = arr[s];
		arr[s] = arr[k];
		arr[k] = temp;

	}

	private static void printSolution(char[][] sol) {
		for (int i = 0; i < sol.length; i++) {
			for (int j = 0; j < sol.length; j++) {

				char c = sol[i][j] == 'Q' ? 'Q' : 'X';
				System.out.print(c + "|");
			}
			System.out.println();
		}
		System.out.println("=====================");

	}

	private static List<char[][]> solveNQueensUsingBackTracing(char[][] chessBoard, int n) {

		int row = 0;
		List<char[][]> solution = new ArrayList<char[][]>();
		placeQueens(chessBoard, row, n, solution);

		return solution;

	}

	/**
	 * This approach recusively places the Queen in the next available position
	 * and goes on to find if that position can find a solution. The contract is
	 * this method gets a chessboard which is properly placed till the row-1 and
	 * places the Queen in row row and marks all the threatening squares before
	 * calling the same function for row+1; When it comes out of recursion it
	 * should unmark all its markings including the queen so that other
	 * solutions can be tried
	 * 
	 * @param chessBoard
	 * @param row
	 * @param n
	 * @param solution
	 * @return
	 */
	private static void placeQueens(char[][] chessBoard, int row, int n, List<char[][]> solution) {

		if (n == 0) {
			solution.add(copy(chessBoard));
			return;
		}

		if (row == chessBoard.length) {
			System.out.println("No solution found");
			return;
		}

		for (int col = 0; col < chessBoard.length; col++) {
			if (chessBoard[row][col] == 'X') {
				chessBoard[row][col] = 'Q';
				markThreateningSquares(chessBoard, row, col, n);
				// System.out.println("After marking "+n);
				// printBoard(chessBoard);
				placeQueens(chessBoard, row + 1, n - 1, solution);
				unmarkThreateningSquares(chessBoard, row, col, n);
				// System.out.println("After unmarking "+n);
				// printBoard(chessBoard);
			}
		}

	}

	private static char[][] copy(char[][] chessBoard) {

		if (chessBoard == null)
			return null;
		char[][] result = new char[chessBoard.length][];
		for (int r = 0; r < chessBoard.length; r++) {
			result[r] = chessBoard[r].clone();
		}
		return result;

	}

	private static void printBoard(char[][] chessBoard) {
		for (int i = 0; i < chessBoard.length; i++) {
			for (int j = 0; j < chessBoard.length; j++) {
				System.out.print(chessBoard[i][j] + "|");
			}
			System.out.println();
		}
		System.out.println("=====================");

	}

	private static void unmarkThreateningSquares(char[][] chessBoard, int row, int col, int n) {
		char charToReplace = Character.forDigit(n, 10);

		char replacingChar = 'X';
		for (int i = row; i < chessBoard.length; i++) {
			if (chessBoard[i][col] == charToReplace || chessBoard[i][col] == 'Q') {
				chessBoard[i][col] = replacingChar;
			}
		}
		for (int i = col; i < chessBoard.length; i++) {
			if (chessBoard[row][i] == charToReplace || chessBoard[row][i] == 'Q') {
				chessBoard[row][i] = replacingChar;
			}
		}
		for (int i = row, j = col; i < chessBoard.length && j >= 0; i++, j--) {
			if (chessBoard[i][j] == charToReplace || chessBoard[i][j] == 'Q') {
				chessBoard[i][j] = replacingChar;
			}

		}
		for (int i = row, j = col; i < chessBoard.length && j < chessBoard.length; i++, j++) {
			if (chessBoard[i][j] == charToReplace || chessBoard[i][j] == 'Q') {
				chessBoard[i][j] = replacingChar;
			}

		}

	}

	private static void markThreateningSquares(char[][] chessBoard, int row, int col, int n) {

		markBoard(chessBoard, row, col, 'X', Character.forDigit(n, 10));

	}

	private static void markBoard(char[][] chessBoard, int row, int col, char charToReplace, char replacingChar) {
		for (int i = row; i < chessBoard.length; i++) {
			if (chessBoard[i][col] == charToReplace) {
				chessBoard[i][col] = replacingChar;
			}
		}
		for (int i = col; i < chessBoard.length; i++) {
			if (chessBoard[row][i] == charToReplace) {
				chessBoard[row][i] = replacingChar;
			}
		}
		for (int i = row, j = col; i < chessBoard.length && j >= 0; i++, j--) {
			if (chessBoard[i][j] == charToReplace) {
				chessBoard[i][j] = replacingChar;
			}

		}
		for (int i = row, j = col; i < chessBoard.length && j < chessBoard.length; i++, j++) {
			if (chessBoard[i][j] == charToReplace) {
				chessBoard[i][j] = replacingChar;
			}

		}
	}

}
