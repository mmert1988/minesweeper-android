package de.moonstarlabs.android.minesweeper.game;

/**
 * Schnittstelle für Listener, die im Lauf des Spiels verschiedene Informationen bekommen.
 */
public interface GameListener {
    
    /**
     * Wird aufgerufen, wenn Status des Spiels sich ändert.
     * @param status Spiel Status
     */
    void onGameStatusChanged(Game.Status status);
    
    /**
     * Wird aufgerufen, wenn Anzahl der verbleibenden Minen sich ändert.
     * @param minesLeft Anzahl der verbleibenden Minen.
     */
    void onMinesLeftChanged(int minesLeft);
    
}
