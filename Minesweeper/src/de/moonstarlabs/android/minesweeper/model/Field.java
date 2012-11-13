package de.moonstarlabs.android.minesweeper.model;

public interface Field {

	public abstract Cell getCell(int position);

	public abstract int getCountCells();
	
	public abstract int calculateMinedNeighbours(int position);
	
	public abstract void openCell(int position);
	
	public abstract void toggleMarkCell(int position);
	
	public abstract void openAllMinedCells();

}