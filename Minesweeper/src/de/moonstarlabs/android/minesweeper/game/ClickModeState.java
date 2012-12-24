package de.moonstarlabs.android.minesweeper.game;

import de.moonstarlabs.android.minesweeper.MainActivity;

/**
 * Schnittstelle für Realisierung von State-Pattern für Klick-Modi auf Zellen.
 */
public interface ClickModeState {
    
    /**
     * Wechselt zwischen beiden Modi.
     * @param activity Spiels MainActivity
     */
    void switchClickMode(MainActivity activity);
    
    /**
     * Methode für Click-Implementierung.
     * @param game Spiel-Objekt
     * @param position Position der gedrückten Zelle
     */
    void clickOn(Game game, int position);
    
    /**
     * Methode für LongClick-Implementierung.
     * @param game Spiel-Objekt
     * @param position Position der gedrückten Zelle
     */
    void longClickOn(Game game, int position);
    
}
