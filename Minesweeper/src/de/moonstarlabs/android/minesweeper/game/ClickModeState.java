package de.moonstarlabs.android.minesweeper.game;

import de.moonstarlabs.android.minesweeper.MainActivity;

public interface ClickModeState {
	
	public void switchClickMode(MainActivity activity);

	public void clickOn(Game game, int position);

	public void longClickOn(Game game, int position);
	
}
