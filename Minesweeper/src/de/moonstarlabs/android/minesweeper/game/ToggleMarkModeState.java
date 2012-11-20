package de.moonstarlabs.android.minesweeper.game;

import de.moonstarlabs.android.minesweeper.MainActivity;

public class ToggleMarkModeState implements ClickModeState {

	@Override
	public void switchClickMode(MainActivity activity) {
		activity.setOpenCellMode();
	}

	@Override
	public void clickOn(Game game, int position) {
		game.toggleMarkCell(position);
	}

	@Override
	public void longClickOn(Game game, int position) {
		game.toggleSuspectCell(position);
	}

}
