package de.moonstarlabs.android.minesweeper.model;

import android.os.Parcelable;

/**
 * Schnittstelle für Interaktionen mit einem Objekt für Feld (Field).
 */
public interface Field extends Parcelable {
    
    /**
     * Gibt die Zelle für die angegebene Position zurück.
     * @param position die Position der Zelle
     * @return die Zelle
     */
    Cell getCell(int position);
    
    /**
     * Gibt die Anzahl der Zellen zurück.
     * @return die Anzahl der Zellen
     */
    int getCellsCount();
    
    /**
     * Gibt die Anzahl der Zellen zurück, die Minen enthalten.
     * @return die Anzahl der Zellen, die Minen enthalten
     */
    int getMinedCellsCount();
    
    /**
     * Gibt die Anzahl der geöffneten Zellen zurück.
     * @return die Anzahl der geöffneten Zellen
     */
    int getOpenedCellsCount();
    
    /**
     * Gibt die Anzahl mit einer Fahne markierten Zellen zurück.
     * @return die Anzahl mit einer Fahne markierten Zellen
     */
    int getMarkedCellsCount();
    
    /**
     * Berechnet für eine Zelle zu gegebener Position die Anzahl der Nachbaren, die Minen enthalten.
     * @param position die Position der Zelle
     * @return die Anzahl der Nachbaren, die Minen enthalten
     */
    int calculateMinedNeighbours(int position);
    
    /**
     * Öffnet die Zelle zu gegebener Position.
     * @param position die Position der Zelle
     */
    void openCell(int position);
    
    /**
     * Markiert die Zelle zu gegebener Position mit einer Fahne.
     * @param position die Position der Zelle
     */
    void markCell(int position);
    
    /**
     * Entfernt die Markierung von der Zelle zu gegebener Position.
     * @param position die Position der Zelle
     */
    void unmarkCell(int position);
    
    /**
     * Setzt die Zelle zu gegebener Position unter Verdacht.
     * @param position die Position der Zelle
     */
    void suspect(int position);
    
    /**
     * Entfernt den Verdacht von der Zelle zu gegebener Position.
     * @param position die Position der Zelle
     */
    void unsuspect(int position);
    
    /**
     * Öffnet alle Zellen, die Minen enthalten.
     */
    void openAllMinedCells();
    
    /**
     * Öffnet alle nicht mit einer Fahne markierten Nachbaren einer Zelle.
     * @param position die Position der Zelle
     * @return ob eine oder mehrere von den geöffneten Nachbarn Minen enthielten
     */
    boolean openUnmarkedNeighbours(int position);
    
    void addListener(FieldListener listener);
    
    void removeListener(FieldListener listener);
    
}