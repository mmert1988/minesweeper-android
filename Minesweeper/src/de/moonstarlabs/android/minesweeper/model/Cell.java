package de.moonstarlabs.android.minesweeper.model;

public class Cell {
	
	private boolean isMined;
	private boolean isMarked = false;
	private boolean isOpen = false;
	
	Cell(boolean isMined) {
		this.isMined = isMined;
	}
	
	void open() {
		isOpen = true;
	}
	
	void mark() {
		isMarked = true;
	}
	
	void unmark() {
		isMarked = false;
	}
	
	public boolean isMined() {
		return isMined;
	}
	
	public boolean isMarked() {
		return isMarked;
	}
	
	public boolean isOpen() {
		return isOpen;
	}

}
