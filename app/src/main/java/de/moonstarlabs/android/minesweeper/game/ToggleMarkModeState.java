package de.moonstarlabs.android.minesweeper.game;

import de.moonstarlabs.android.minesweeper.MainActivity;

/**
 * Implementierung den Zustand des State-Pattern für Markieren von Zellen.
 */
public class ToggleMarkModeState implements ClickModeState {
    
    @Override
    public void switchClickMode(final MainActivity activity) {
        activity.setOpenCellMode();
    }
    
    @Override
    public void clickOn(final Game game, final int position) {
        game.toggleMarkCell(position);
    }
    
    @Override
    public void longClickOn(final Game game, final int position) {
        game.toggleSuspectCell(position);
    }
    
}
