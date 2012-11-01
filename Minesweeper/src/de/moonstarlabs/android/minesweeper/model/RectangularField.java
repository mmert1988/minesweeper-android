package de.moonstarlabs.android.minesweeper.model;

import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

import android.util.Pair;

public class RectangularField extends Observable implements Field {

	public static Field random(int rows, int columns, int randomMines) {
		Set<Pair<Integer, Integer>> randomMineCoords = generateRandomMineCoords(rows, columns, randomMines);
		return new RectangularField(rows, columns, randomMineCoords);
	}

	private static Set<Pair<Integer, Integer>> generateRandomMineCoords(int rows, int columns, int randomMines) {
		Set<Pair<Integer, Integer>> result = new HashSet<Pair<Integer,Integer>>();
		int generatedCoords = 0;
		while (result.add(generateRandomCoord(rows, columns)) || generatedCoords < randomMines) {
			generatedCoords++;
		}
		return result;
	}
	
	private static Pair<Integer, Integer> generateRandomCoord(int rows, int columns) {
		int randomRow = (int)(Math.random()*17)%rows;//randomRows.nextInt(rows);
		int randomColumn = (int)(Math.random()*17)%columns;
		return new Pair<Integer, Integer>(randomRow, randomColumn);
	}

	public final int rows;
	public final int columns;
	private final Cell[][] cells;
//	private final int mines;
	private final Set<Pair<Integer, Integer>> mineCoords;

	private RectangularField(int rows, int columns, Set<Pair<Integer, Integer>> mineCoords) {
		if (rows * columns < mineCoords.size()) {
			throw new IllegalArgumentException();
		}
		this.rows = rows;
		this.columns = columns;
		this.cells = new Cell[rows][columns];
//		this.mines = mines.size();
		this.mineCoords = mineCoords;
		
		initField();
	}
	
//	private Field(int rows, int columns, int mines) {
//		if (rows * columns < mines) {
//			throw new IllegalArgumentException();
//		}
//		this.rows = rows;
//		this.columns = columns;
//		this.cells = new Cell[rows][columns];
//		this.mines = mines;
//		initField();
//	}

	private void initField() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				cells[i][j] = new Cell(false, 0);
			}
		}

//		int putMines = 0;
//		while (putMines < mines) {
//			if (putMine()) {
//				putMines++;
//			}
//		}
		for (Pair<Integer, Integer> mineCoord: mineCoords) {
			cells[mineCoord.first][mineCoord.second].setMined(true);
		}

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				computeMinedNeighbours(i, j);
			}
		}
	}

//	private boolean putMine() {
//		int randomRow = randomRows.nextInt(rows);
//		int randomColumn = randomColumns.nextInt(columns);
//
//		if (!cells[randomRow][randomColumn].isMined()) {
//			cells[randomRow][randomColumn].setMined(true);
//			return true;
//		}
//		return false;
//	}

	private void computeMinedNeighbours(int row, int column) {
		if (!cells[row][column].isMined()) {
			int minedNeighbours = 0;
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					try {
						if (cells[row + i - 1][column + j - 1].isMined()) {
							minedNeighbours++;
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						// Just ignore
					}
				}
			}
			cells[row][column].setMinedNeighbours(minedNeighbours);
		}
	}

	@Override
	public Cell getCell(int position) {
//		return cells[row][column];
		return null;
	}

	@Override
	public int getCountCells() {
		// TODO Auto-generated method stub
		return 0;
	}

}
