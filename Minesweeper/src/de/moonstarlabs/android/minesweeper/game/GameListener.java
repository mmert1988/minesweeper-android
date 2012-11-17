package de.moonstarlabs.android.minesweeper.game;

public interface GameListener {
	
	public void onGameStatusChanged(Game.Status status);
	
	public void onMinesLeftChanged(int minesLeft);

}
