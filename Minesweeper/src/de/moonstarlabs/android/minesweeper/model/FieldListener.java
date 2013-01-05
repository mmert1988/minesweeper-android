package de.moonstarlabs.android.minesweeper.model;

/**
 * Interface mit Callback-Methoden, die bei verschiedenen Ereignissen
 * bezüglich des jeweiligen Feld-Objekts aufgerufen werden sollen.
 */
public interface FieldListener {
    
    /**
     * Wird bei Änderungen auf dem Feld aufgerufen.
     */
    void onFieldChanged();
    
}

