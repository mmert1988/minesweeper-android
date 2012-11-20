package de.moonstarlabs.android.minesweeper.model;

import android.os.Parcelable;

public interface Field extends Parcelable {

	public abstract Cell getCell(int position);

	public abstract int getCellsCount();
	
	public abstract int getMinedCellsCount();
	
	public abstract int getOpenedCellsCount();
	
	public abstract int getMarkedCellsCount();
	
	public abstract int calculateMinedNeighbours(int position);
	
	public abstract void openCell(int position);
	
	public abstract void markCell(int position);
	
	public abstract void unmarkCell(int position);
	
	public abstract void suspect(int position);
	
	public abstract void unsuspect(int position);
	
	public abstract void openAllMinedCells();
	
}