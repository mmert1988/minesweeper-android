package de.moonstarlabs.android.minesweeper.model;

public interface Field {

	public abstract Cell getCell(int position);

	public abstract int getCountCells();

}