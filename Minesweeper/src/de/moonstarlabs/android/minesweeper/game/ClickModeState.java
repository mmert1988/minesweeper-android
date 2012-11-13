package de.moonstarlabs.android.minesweeper.game;

import de.moonstarlabs.android.minesweeper.MainActivity;
import de.moonstarlabs.android.minesweeper.model.Field;

public interface ClickModeState {
	
	public void switchClickMode(MainActivity activity);

	public void clickOn(Field field, int position);

	public void longClickOn(Field field, int position);
	
}
