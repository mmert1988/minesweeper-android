package de.moonstarlabs.android.minesweeper.model;

import java.util.HashSet;
import java.util.Set;

import android.database.ContentObservable;
import android.util.Pair;

public class RectangularField extends ContentObservable implements Field {

	public static Field random(int rows, int columns, int randomMines) {
		Set<Pair<Integer, Integer>> randomMineCoords = generateRandomMineCoords(rows, columns, randomMines);
		return new RectangularField(rows, columns, randomMineCoords);
	}

	private static Set<Pair<Integer, Integer>> generateRandomMineCoords(int rows, int columns, int randomMines) {
		Set<Pair<Integer, Integer>> result = new HashSet<Pair<Integer, Integer>>();
		int generatedCoords = 0;
		while (result.add(generateRandomCoord(rows, columns)) || generatedCoords < randomMines) {
			generatedCoords++;
		}
		return result;
	}

	private static Pair<Integer, Integer> generateRandomCoord(int rows, int columns) {
		int randomRow = (int) (Math.random() * 17) % rows;
		int randomColumn = (int) (Math.random() * 17) % columns;
		return new Pair<Integer, Integer>(randomRow, randomColumn);
	}

	public final int rows;
	public final int columns;
	private final Cell[][] cells;
	private final Set<Pair<Integer, Integer>> mineCoords;
	
	public RectangularField(int rows, int columns, Set<Pair<Integer, Integer>> mineCoords) {
		if (rows * columns < mineCoords.size()) {
			throw new IllegalArgumentException();
		}
		this.rows = rows;
		this.columns = columns;
		this.cells = new Cell[rows][columns];
		this.mineCoords = mineCoords;

		initField();
	}

	@Override
	public Cell getCell(int position) {
		int row = position / rows;
		int column = position % columns;
		return cells[row][column];
	}

	@Override
	public int getCountCells() {
		return rows * columns;
	}

	@Override
	public int calculateMinedNeighbours(int position) {
		int row = position / rows;
		int column = position % columns;
		return computeMinedNeighbours(row, column);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void openCell(int position) {
		Cell cell = getCell(position);
		if (cell.isMarked()) {
			return;
		}
		
		cell.open();
		notifyChange(false);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void toggleMarkCell(int position) {
		Cell cell = getCell(position);
		
		if (cell.isOpen()) {
			return;
		}
		
		if (cell.isMarked()) {
			cell.unmark();
		} else {
			cell.mark();
		}
		notifyChange(false);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void openAllMinedCells() {
		for (Pair<Integer, Integer> coord: mineCoords) {
			cells[coord.first][coord.second].open();
		}
		notifyChange(false);
	}
	
	private void initField() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				cells[i][j] = new Cell(mineCoords.contains(new Pair<Integer, Integer>(i, j)));
			}
		}
	}

	private int computeMinedNeighbours(int row, int column) {
		int minedNeighbours = 0;
		if (!cells[row][column].isMined()) {
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
		}
		return minedNeighbours;
	}
	
}
