package de.moonstarlabs.android.minesweeper.model;

public class Cell {
	
	private boolean isMined;
	private int minedNeighbours;
	
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

}
