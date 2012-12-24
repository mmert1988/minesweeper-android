package de.moonstarlabs.android.minesweeper.model;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import android.database.ContentObservable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;

/**
 * Implementierung für rechteckige Minenfelder.
 */
public class RectangularField extends ContentObservable implements Field {
    
    private static final Random RANDOM_GENERATOR = new Random();
    
    /**
     * Methode für Generierung von Feld mit gegebener Anzahl zufälliger Zellen.
     * @param rows Anzahl der Zeilen
     * @param columns Anzahl der Spalten
     * @param randomMines Anzahl der zu zufällig generierenden Minen
     * @return das generierte rechteckige Feld
     */
    public static Field random(final int rows, final int columns, final int randomMines) {
        Set<Pair<Integer, Integer>> randomMineCoords = generateRandomMineCoords(rows, columns, randomMines);
        return new RectangularField(rows, columns, randomMineCoords);
    }
    
    private static Set<Pair<Integer, Integer>> generateRandomMineCoords(final int rows, final int columns, final int randomMines) {
        Set<Pair<Integer, Integer>> result = new HashSet<Pair<Integer, Integer>>();
        int generatedCoords = 0;
        while (generatedCoords < randomMines) {
            if (result.add(generateRandomCoord(rows, columns))) {
                generatedCoords++;
            }
        }
        return result;
    }
    
    private static Pair<Integer, Integer> generateRandomCoord(final int rows, final int columns) {
        int randomRow = RANDOM_GENERATOR.nextInt(rows);
        int randomColumn = RANDOM_GENERATOR.nextInt(columns);
        return new Pair<Integer, Integer>(randomRow, randomColumn);
    }
    
    private final int rows;
    private final int columns;
    private final Cell[][] cells;
    private final Set<Pair<Integer, Integer>> mineCoords;
    private int openedCells;
    private int markedCells;
    
    /**
     * 
     * Creates a new instance of {@link RectangularField}.
     * @param rows Anzahl der Zeilen
     * @param columns Anzahlr der Spalten
     * @param mineCoords Koordinaten der Minen als Integere-Paare
     */
    public RectangularField(final int rows, final int columns, final Set<Pair<Integer, Integer>> mineCoords) {
        if (rows * columns < mineCoords.size()) {
            throw new IllegalArgumentException();
        }
        this.rows = rows;
        this.columns = columns;
        cells = new Cell[rows][columns];
        this.mineCoords = mineCoords;
        
        initField();
    }
    
    @Override
    public Cell getCell(final int position) {
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
    public int calculateMinedNeighbours(final int position) {
        int row = position / rows;
        int column = position % columns;
        return computeMinedNeighbours(row, column);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void openCell(final int position) {
        openCellHelper(position);
        notifyChange(false);
    }
    
    private void openCellHelper(final int position) {
        int row = position / rows;
        int column = position % columns;
        Cell cell = cells[row][column];
        
        if (!cell.isOpen()) {
            cell.open();
            openedCells++;
        }
        
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
                    }
                    catch (ArrayIndexOutOfBoundsException e) {
                        // Nothing
                    }
                }
            }
        }
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void markCell(final int position) {
        getCell(position).mark();
        markedCells++;
        notifyChange(false);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void unmarkCell(final int position) {
        getCell(position).unmark();
        markedCells--;
        notifyChange(false);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void suspect(final int position) {
        getCell(position).suspect();
        notifyChange(false);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void unsuspect(final int position) {
        getCell(position).unSuspect();
        notifyChange(false);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void openAllMinedCells() {
        for (Pair<Integer, Integer> coord: mineCoords) {
            openedCells++;
            cells[coord.first][coord.second].open();
        }
        notifyChange(false);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean openUnmarkedNeighbours(final int position) {
        int row = position / rows;
        int column = position % columns;
        
        int countMinedNeihbours = computeMinedNeighbours(row, column);
        int countMarkedNeighbours = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                try {
                    if (cells[row + i - 1][column + j - 1].isMarked()) {
                        countMarkedNeighbours++;
                    }
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    // Just ignore
                }
            }
        }
        
        if (countMarkedNeighbours < countMinedNeihbours) {
            return true;
        }
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                try {
                    Cell neighbour = cells[row + i - 1][column + j - 1];
                    int neighbourPos = convertToPosition(row + i - 1, column + j - 1);
                    if (!neighbour.isMarked() && position != neighbourPos) {
                        if (neighbour.isMined()) {
                            return false;
                        }
                        else {
                            openCellHelper(neighbourPos);
                        }
                    }
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    // Just ignore
                }
            }
        }
        
        notifyChange(false);
        return true;
    }
    
    private void initField() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                cells[i][j] = new Cell(mineCoords.contains(new Pair<Integer, Integer>(i, j)));
            }
        }
        openedCells = 0;
        markedCells = 0;
    }
    
    private int computeMinedNeighbours(final int row, final int column) {
        int minedNeighbours = 0;
        if (!cells[row][column].isMined()) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    try {
                        if (cells[row + i - 1][column + j - 1].isMined()) {
                            minedNeighbours++;
                        }
                    }
                    catch (ArrayIndexOutOfBoundsException e) {
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
    public void writeToParcel(final Parcel out, final int flags) {
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
    
    /**
     * Standard-Implementierung von Parcelable-Creator nach der Developer-Doku
     * von Android.
     */
    public static final Parcelable.Creator<RectangularField> CREATOR = new Creator<RectangularField>() {
        @Override
        public RectangularField[] newArray(final int size) {
            return new RectangularField[size];
        }
        @Override
        public RectangularField createFromParcel(final Parcel in) {
            return new RectangularField(in);
        }
    };
    
    private RectangularField(final Parcel in) {
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
    
    private int convertToPosition(final int row, final int column) {
        return row * columns + column;
    }
    
}
