package de.moonstarlabs.android.minesweeper.game;

import java.util.HashSet;
import java.util.Set;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Pair;

import de.moonstarlabs.android.minesweeper.model.Cell;
import de.moonstarlabs.android.minesweeper.model.Field;
import de.moonstarlabs.android.minesweeper.model.RectangularField;

/**
 * Klasse für Spiel, die verschiedene Methoden für die Spiel-Logik enthält.
 */
public class Game implements Parcelable {
    private Field field;
    private Status status = Status.NEW;
    private long startMillis;
    
    private final Set<GameListener> listeners = new HashSet<GameListener>();
    
    /**
     * Creates a new instance of {@link Game}.
     * @param level Schwierigkeits-Level des Spiels
     */
    public Game(final DifficultyLevel level) {
        initGame(level);
    }
    
    /**
     * Gibt den Status des Spiels zurück.
     * @return den Status des Spiels
     */
    public Status getStatus() {
        return status;
    }
    
    /**
     * Gibt die Anzahl der verbleibenden Minen zurück.
     * @return die Anzahl der verbleibenden Minen
     */
    public int getMinesLeft() {
        return computeMinesLeft();
    }
    
    /**
     * Gibt die Anfangszeit des Spiels in Millisekunden zurück.
     * @return die Anfangszeit des Spiels in Millisekunden
     */
    public long getStartMillis() {
        return startMillis;
    }
    
    /**
     * Gibt das Spielfeld zurück.
     * @return das Spielfeld
     */
    public Field getField() {
        return field;
    }
    
    /**
     * Öffnet eine Zelle. Kann unter Umständen den Status des Spiels ändern.
     * @param position die Position der Zelle
     */
    public void openCell(final int position) {
        if (status == Status.NEW) {
            start();
        }
        
        if (status != Status.RUNNING
                || (field.getCell(position).isMarked() && field.calculateMinedNeighbours(position) > 0)) {
            return;
        }
        
        if (field.getCell(position).isMined()) {
            field.openAllMinedCells();
            status = Status.LOST;
            
            for (GameListener listener : listeners) {
                listener.onGameStatusChanged(status);
            }
            return;
        }
        
        if (field.getCell(position).isOpen() && field.calculateMinedNeighbours(position) > 0) {
            if (!field.openUnmarkedNeighbours(position)) {
                field.openAllMinedCells();
                status = Status.LOST;
            }
        }
        else {
            field.openCell(position);
        }
        
        if (isGameWon()) {
            status = Status.WON;
            
            for (GameListener listener : listeners) {
                listener.onGameStatusChanged(status);
            }
        }
    }
    
    /**
     * Umschaltet die Markierung von einer Zelle.
     * @param position die Position der Zelle
     */
    public void toggleMarkCell(final int position) {
        if (status == Status.NEW) {
            start();
        }
        
        if (status != Status.RUNNING) {
            return;
        }
        
        if (field.getCell(position).isOpen() && field.calculateMinedNeighbours(position) > 0) {
            if (!field.openUnmarkedNeighbours(position)) {
                field.openAllMinedCells();
                status = Status.LOST;
            }
        }
        else {
            if (!field.getCell(position).isMarked()) {
                field.markCell(position);
            }
            else {
                field.unmarkCell(position);
            }
            
            for (GameListener listener : listeners) {
                listener.onMinesLeftChanged(computeMinesLeft());
            }
        }
        
        if (isGameWon()) {
            status = Status.WON;
            
            for (GameListener listener : listeners) {
                listener.onGameStatusChanged(status);
            }
        }
    }
    
    /**
     * Umschaltet den Verdacht zu einer Zelle.
     * @param position die Position der Zelle
     */
    public void toggleSuspectCell(final int position) {
        if (status == Status.NEW) {
            start();
        }
        
        Cell cell = field.getCell(position);
        if (status != Status.RUNNING || cell.isOpen() || cell.isMarked()) {
            return;
        }
        
        if (cell.isSuspect()) {
            field.unsuspect(position);
        }
        else {
            field.suspect(position);
        }
    }
    
    /**
     * Fügt neuen Listener hinzu.
     * @param listener Listener
     */
    public void addListener(final GameListener listener) {
        listeners.add(listener);
    }
    
    /**
     * Entfernt Listener.
     * @param listener Listener
     */
    public void removeListener(final GameListener listener) {
        listeners.remove(listener);
    }
    
    private void initGame(final DifficultyLevel level) {
        switch (level) {
            default:
                // TODO
                break;
        }
        Set<Pair<Integer, Integer>> mineCoords = new HashSet<Pair<Integer, Integer>>();
        mineCoords.add(new Pair<Integer, Integer>(0, 0));
        mineCoords.add(new Pair<Integer, Integer>(1, 1));
        mineCoords.add(new Pair<Integer, Integer>(2, 2));
        //        mineCoords.add(new Pair<Integer, Integer>(3, 3));
        //        mineCoords.add(new Pair<Integer, Integer>(4, 4));
        //        mineCoords.add(new Pair<Integer, Integer>(5, 5));
        //        mineCoords.add(new Pair<Integer, Integer>(6, 6));
        //        mineCoords.add(new Pair<Integer, Integer>(7, 7));
        //        mineCoords.add(new Pair<Integer, Integer>(8, 8));
        //        mineCoords.add(new Pair<Integer, Integer>(9, 9));
        //        mineCoords.add(new Pair<Integer, Integer>(10, 10));
        //        mineCoords.add(new Pair<Integer, Integer>(11, 11));
        field = new RectangularField(3, 3, mineCoords);
        // field = RectangularField.random(6, 6, 10);
    }
    
    private boolean isGameWon() {
        return field.getOpenedCellsCount() == field.getCellsCount() - field.getMinedCellsCount();
    }
    
    private int computeMinesLeft() {
        return field.getMinedCellsCount() - field.getMarkedCellsCount();
    }
    
    private void start() {
        startMillis = SystemClock.elapsedRealtime();
        status = Status.RUNNING;
        for (GameListener listener : listeners) {
            listener.onGameStatusChanged(status);
        }
    }
    
    /**
     * Aufzählung für die Schwierigkeit-Levels.
     */
    public static enum DifficultyLevel {
        /**
         * Leicht.
         */
        EASY,
        
        /**
         * Mittel.
         */
        MEDIUM,
        
        /**
         * Schwer.
         */
        HARD
    }
    
    /**
     * Aufzählung für die verschiedenen Stadien eines Spiels.
     */
    public static enum Status {
        /**
         * Neu: Spiel wurde neu gestartet und noch keine Aktion ausgeführt.
         */
        NEW,
        
        /**
         * Spiel läuft im Moment.
         */
        RUNNING,
        
        /**
         * Spiel wurde gewonnen.
         */
        WON,
        
        /**
         * Spiel wurde verloren.
         */
        LOST
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(final Parcel out, final int flags) {
        out.writeParcelable(field, flags);
        out.writeString(status.toString());
        out.writeLong(startMillis);
    }
    
    /**
     * Standard-Implementierung von Parcelable-Creator nach der Developer-Doku
     * von Android.
     */
    public static final Parcelable.Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game[] newArray(final int size) {
            return new Game[size];
        }
        @Override
        public Game createFromParcel(final Parcel in) {
            return new Game(in);
        }
    };
    
    private Game(final Parcel in) {
        field = in.readParcelable(Field.class.getClassLoader());
        status = Status.valueOf(in.readString());
        startMillis = in.readLong();
    }
    
}
