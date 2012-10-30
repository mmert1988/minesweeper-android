package de.moonstarlabs.android.minesweeper.model;

public class Cell {
	
	private boolean isMined;
	private int minedNeighbours;
	private boolean isMarked = false;
	private boolean isOpen = false;
	
	Cell(boolean isMined, int minedNeighbours) {
		this.isMined = isMined;
		this.minedNeighbours = minedNeighbours;
	}
	
	void setMined(boolean isMined) {
		this.isMined = isMined;
	}
	
	void setMinedNeighbours(int minedNeighbours) {
		this.minedNeighbours = minedNeighbours;
	}
	
	public boolean isMined() {
		return isMined;
	}
	
	public int getMinedNeighbours() {
		return minedNeighbours;
	}
	
	public void mark() {
		isMarked = true;
	}
	
	public void unmark() {
		isMarked = false;
	}
	
	public boolean isMarked() {
		return isMarked;
	}
	
	public void open() {
		isOpen = true;
	}
	
	public boolean isOpen() {
		return isOpen;
	}

}
