package de.moonstarlabs.android.minesweeper.model;

import java.util.HashSet;
import java.util.Set;

import android.database.ContentObservable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;

public class RectangularField extends ContentObservable implements Field {

	public static Field random(int rows, int columns, int randomMines) {
		Set<Pair<Integer, Integer>> randomMineCoords = generateRandomMineCoords(rows, columns, randomMines);
		return new RectangularField(rows, columns, randomMineCoords);
	}

	private static Set<Pair<Integer, Integer>> generateRandomMineCoords(int rows, int columns, int randomMines) {
		Set<Pair<Integer, Integer>> result = new HashSet<Pair<Integer, Integer>>();
		int generatedCoords = 0;
		while (generatedCoords < randomMines) {
			if (result.add(generateRandomCoord(rows, columns))) {
				generatedCoords++;
			}
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
	private int openedCells = 0;
	private int markedCells = 0;
	
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
	public int getCellsCount() {
		return rows * columns;
	}
	
	@Override
	public int getMinedCellsCount() {
		return mineCoords.size();
	}
	
	@Override
	public int getOpenedCellsCount() {
		return openedCells;
	}
	
	@Override
	public int getMarkedCellsCount() {
		return markedCells;
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
		openCellHelper(position);
		notifyChange(false);
	}
	
	private void openCellHelper(int position) {
		int row = position / rows;
		int column = position % columns;
		Cell cell = cells[row][column];
		cell.open();
		openedCells++;
		if (!cell.isMined() && calculateMinedNeighbours(position) == 0) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					try {
						int neighbourRow = row + i - 1;
						int neighbourColumn = column + j - 1;
						int neighbourPos = convertToPosition(neighbourRow, neighbourColumn);
						if (position != neighbourPos && !cells[neighbourRow][neighbourColumn].isOpen()) {
							openCellHelper(neighbourPos);
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						// Nothing
					}
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void markCell(int position) {
		getCell(position).mark();
		markedCells++;
		notifyChange(false);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void unmarkCell(int position) {
		getCell(position).unmark();
		markedCells--;
		notifyChange(false);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void suspect(int position) {
		getCell(position).suspect();
		notifyChange(false);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void unsuspect(int position) {
		getCell(position).unSuspect();
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
	
	@Override
	public void openUnmarkedNeighbours(int position) {
		// TODO Auto-generated method stub
		
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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(rows);
		out.writeInt(columns);
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				out.writeParcelable(cells[i][j], flags);
			}
		}
		
		out.writeInt(mineCoords.size());
		for (Pair<Integer, Integer> mineCoord: mineCoords) {
			out.writeInt(mineCoord.first);
			out.writeInt(mineCoord.second);
		}
		
		out.writeInt(openedCells);
		out.writeInt(markedCells);
	}
	
	public static final Parcelable.Creator<RectangularField> CREATOR = new Creator<RectangularField>() {
		@Override
		public RectangularField[] newArray(int size) {
			return new RectangularField[size];
		}
		
		@Override
		public RectangularField createFromParcel(Parcel in) {
			return new RectangularField(in);
		}
	};
	
	private RectangularField(Parcel in) {
		rows = in.readInt();
		columns = in.readInt();
		
		cells = new Cell[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				cells[i][j] = in.readParcelable(Cell.class.getClassLoader());
			}
		}
		
		mineCoords = new HashSet<Pair<Integer, Integer>>();
		int mineCoordsSize = in.readInt();
		for (int i = 0; i < mineCoordsSize; i++) {
			Pair<Integer, Integer> mineCoord = new Pair<Integer, Integer>(in.readInt(), in.readInt());
			mineCoords.add(mineCoord);
		}
		
		openedCells = in.readInt();
		markedCells = in.readInt();
	}
	
	private int convertToPosition(int row, int column) {
		return row * columns + column;
	}

}
