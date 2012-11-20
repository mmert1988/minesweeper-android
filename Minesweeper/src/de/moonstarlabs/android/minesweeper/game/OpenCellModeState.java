package de.moonstarlabs.android.minesweeper.game;

import de.moonstarlabs.android.minesweeper.MainActivity;

public class OpenCellModeState implements ClickModeState {

	@Override
	public void switchClickMode(MainActivity activity) {
		activity.setToggleMarkMode();
	}

	@Override
	public void clickOn(Game game, int position) {
		game.openCell(position);
	}

	@Override
	public void longClickOn(Game game, int position) {
		game.toggleSuspectCell(position);
	}

}
