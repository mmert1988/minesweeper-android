package de.moonstarlabs.android.minesweeper.game;

import java.util.HashSet;
import java.util.Set;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;

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
    private long stopMillis;
    
    private final Set<GameListener> listeners = new HashSet<GameListener>();
    
    /**
     * Creates a new instance of {@link Game}.
     * 
     * @param level
     *            Schwierigkeits-Level des Spiels
     */
    public Game(final DifficultyLevel level) {
        initNewGame(level);
    }
    
    /**
     * Gibt den Status des Spiels zurück.
     * 
     * @return den Status des Spiels
     */
    public Status getStatus() {
        return status;
    }
    
    /**
     * Gibt die Anzahl der verbleibenden Minen zurück.
     * 
     * @return die Anzahl der verbleibenden Minen
     */
    public int getMinesLeft() {
        return computeMinesLeft();
    }
    
    /**
     * Gibt die Anfangszeit des Spiels in Millisekunden zurück.
     * 
     * @return die Anfangszeit des Spiels in Millisekunden
     */
    public long getStartMillis() {
        return startMillis;
    }
    
    /**
     * Gibt die Stopzeit des Spiels in Millisekunden zurück.
     * 
     * @return die Stopzeit des Spiels in Millisekunden zurück
     */
    public long getStopMillis() {
        return stopMillis;
    }
    
    /**
     * Gibt das Spielfeld zurück.
     * 
     * @return das Spielfeld
     */
    public Field getField() {
        return field;
    }
    
    /**
     * Öffnet eine Zelle. Kann unter Umständen den Status des Spiels ändern.
     * 
     * @param position
     *            die Position der Zelle
     */
    public void openCell(final int position) {
        if (status == Status.NEW) {
            initGameStart();
        }
        
        if (status != Status.RUNNING
                || (field.getCell(position).isMarked() && field.calculateMinedNeighbours(position) > 0)) {
            return;
        }
        
        if (field.getCell(position).isMined()) {
            initGameLost();
            return;
        }
        
        if (field.getCell(position).isOpen() && field.calculateMinedNeighbours(position) > 0) {
            if (!field.openUnmarkedNeighbours(position)) {
                initGameLost();
                return;
            }
        }
        else {
            field.openCell(position);
        }
        
        if (isGameWon()) {
            initGameWon();
        }
    }
    
    /**
     * Umschaltet die Markierung von einer Zelle.
     * 
     * @param position
     *            die Position der Zelle
     */
    public void toggleMarkCell(final int position) {
        if (status == Status.NEW) {
            initGameStart();
        }
        
        if (status != Status.RUNNING) {
            return;
        }
        
        if (field.getCell(position).isOpen() && field.calculateMinedNeighbours(position) > 0) {
            if (!field.openUnmarkedNeighbours(position)) {
                initGameLost();
                return;
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
            initGameWon();
        }
    }
    
    /**
     * Umschaltet den Verdacht zu einer Zelle.
     * 
     * @param position
     *            die Position der Zelle
     */
    public void toggleSuspectCell(final int position) {
        if (status == Status.NEW) {
            initGameStart();
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
     * Stopt das Spiel, indem sich die Zeit merkt und benachrichtigt auch die
     * Listeners.
     */
    public void stop() {
        if (status == Status.RUNNING) {
            stopMillis = SystemClock.elapsedRealtime();
            updateStatusAndListeners(Status.STOPPED);
        }
    }
    
    /**
     * Macht das Spiel wieder weiter, indem den Zeitunterschied berechnet und
     * die Listeners benachrichtigt.
     */
    public void resume() {
        if (status == Status.STOPPED) {
            startMillis = SystemClock.elapsedRealtime() - (stopMillis - startMillis);
            updateStatusAndListeners(Status.RUNNING);
        }
    }
    
    /**
     * Fügt neuen Listener hinzu.
     * 
     * @param listener
     *            Listener
     */
    public void addListener(final GameListener listener) {
        listeners.add(listener);
    }
    
    /**
     * Entfernt Listener.
     * 
     * @param listener
     *            Listener
     */
    public void removeListener(final GameListener listener) {
        listeners.remove(listener);
    }
    
    private void initNewGame(final DifficultyLevel level) {
        switch (level) {
            case EASY:
                field = RectangularField.random(9, 9, 10);
                break;
            case MEDIUM:
                field = RectangularField.random(16, 16, 40);
                break;
            case HARD:
                field = RectangularField.random(16, 30, 99);
                break;
            default:
                throw new IllegalArgumentException("Difficulty level is not supported: " + level);
        }
    }
    
    private void initGameStart() {
        startMillis = SystemClock.elapsedRealtime();
        updateStatusAndListeners(Status.RUNNING);
    }
    
    private void initGameLost() {
        field.openAllMinedCells();
        updateStatusAndListeners(Status.LOST);
    }
    
    private void initGameWon() {
        stopMillis = SystemClock.elapsedRealtime();
        updateStatusAndListeners(Status.WON);
    }
    
    private boolean isGameWon() {
        return field.getOpenedCellsCount() == field.getCellsCount() - field.getMinedCellsCount();
    }
    
    private int computeMinesLeft() {
        return field.getMinedCellsCount() - field.getMarkedCellsCount();
    }
    
    private void updateStatusAndListeners(final Status newStatus) {
        status = newStatus;
        for (GameListener listener : listeners) {
            listener.onGameStatusChanged(newStatus);
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
         * Spiel wurde gestoppt.
         */
        STOPPED,
        
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
        out.writeLong(stopMillis);
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
        stopMillis = in.readLong();
    }
    
}
