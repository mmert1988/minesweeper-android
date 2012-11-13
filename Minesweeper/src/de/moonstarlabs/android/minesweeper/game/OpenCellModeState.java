package de.moonstarlabs.android.minesweeper.game;

import de.moonstarlabs.android.minesweeper.MainActivity;
import de.moonstarlabs.android.minesweeper.model.Field;

public class OpenCellModeState implements ClickModeState {

	@Override
	public void switchClickMode(MainActivity activity) {
		activity.setToggleMarkMode();
	}

	@Override
	public void clickOn(Field field, int position) {
		field.openCell(position);
	}

	@Override
	public void longClickOn(Field field, int position) {
		// TODO Auto-generated method stub

	}

}
