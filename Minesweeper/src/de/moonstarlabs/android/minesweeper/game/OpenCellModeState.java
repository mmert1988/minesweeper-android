package de.moonstarlabs.android.minesweeper.game;

import de.moonstarlabs.android.minesweeper.MainActivity;

/**
 * Implementierung den Zustand des State-Pattern für Öffnen von Zellen.
 */
public class OpenCellModeState implements ClickModeState {
    
    @Override
    public void switchClickMode(final MainActivity activity) {
        activity.setToggleMarkMode();
    }
    
    @Override
    public void clickOn(final Game game, final int position) {
        game.openCell(position);
    }
    
    @Override
    public void longClickOn(final Game game, final int position) {
        game.toggleSuspectCell(position);
    }
    
}
