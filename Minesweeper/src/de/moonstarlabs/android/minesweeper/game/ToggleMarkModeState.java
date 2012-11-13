package de.moonstarlabs.android.minesweeper.game;

import de.moonstarlabs.android.minesweeper.MainActivity;
import de.moonstarlabs.android.minesweeper.model.Field;

public class ToggleMarkModeState implements ClickModeState {

	@Override
	public void switchClickMode(MainActivity activity) {
		activity.setOpenCellMode();
	}

	@Override
	public void clickOn(Field field, int position) {
		field.toggleMarkCell(position);
	}

	@Override
	public void longClickOn(Field field, int position) {
		// TODO Auto-generated method stub

	}

}
